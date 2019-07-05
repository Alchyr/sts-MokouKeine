package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import もこけね.actions.character.WaitForSignalAction;
import もこけね.character.MokouKeine;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.util.MultiplayerHelper;

import java.util.HashMap;

import static もこけね.util.MultiplayerHelper.partnerName;
import static もこけね.もこけねは神の国.makeID;

public class EraseAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Erase"));
    public static final String[] TEXT = uiStrings.TEXT;

    private HashMap<AbstractCard, Boolean> canExhaust = new HashMap<>();

    public EraseAction()
    {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        if (TrackCardSource.useOtherEnergy && AbstractDungeon.player instanceof MokouKeine) //played by other player.
        {
            MokouKeine p = (MokouKeine)AbstractDungeon.player;
            AbstractDungeon.actionManager.addToTop(new WaitForSignalAction(TEXT[1] + partnerName + TEXT[2]));
            this.isDone = true;
        }
        else
        {
            if (this.duration == Settings.ACTION_DUR_FASTER) {
                canExhaust.clear();

                if (AbstractDungeon.player instanceof MokouKeine) {
                    for (AbstractCard c : ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.group) {
                        canExhaust.put(c, true);
                    }
                }
                for (AbstractCard c : AbstractDungeon.player.drawPile.group)
                {
                    canExhaust.put(c, false);
                }

                if (canExhaust.isEmpty()) {
                    this.isDone = true;
                    MultiplayerHelper.sendP2PString("signal");
                    return;
                }
                else if (canExhaust.size() == 1) {
                    for (AbstractCard c : canExhaust.keySet()) //getting a key from a hashmap is kind of annoying.
                    {
                        String signal = "signal";
                        int index = -1;
                        if (canExhaust.get(c))
                        {
                            index = ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.group.indexOf(c);
                            if (index != -1)
                            {
                                signal += "exhaustdraw " + index;
                                ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.moveToExhaustPile(c);
                            }
                        }
                        else
                        {
                            index = AbstractDungeon.player.drawPile.group.indexOf(c);
                                if (index != -1)
                            {
                                signal += "exhaustother_draw " + index;
                                AbstractDungeon.player.drawPile.moveToExhaustPile(c);
                            }
                        }

                        this.isDone = true;
                        MultiplayerHelper.sendP2PString(signal);
                        return;
                    }
                }
                else {
                    CardGroup options = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    options.group.addAll(canExhaust.keySet());
                    AbstractDungeon.gridSelectScreen.open(options, 1, TEXT[0], false, false, false, false);
                    this.tickDuration();
                    return;
                }
            }

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

                String signal = "signal";
                int index = -1;
                if (canExhaust.get(c))
                {
                    index = ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.group.indexOf(c);
                    if (index != -1)
                    {
                        signal += "exhaustdraw " + index;
                        ((MokouKeine) AbstractDungeon.player).otherPlayerDraw.moveToExhaustPile(c);
                    }
                }
                else
                {
                    index = AbstractDungeon.player.drawPile.group.indexOf(c);
                    if (index != -1)
                    {
                        signal += "exhaustother_draw " + index;
                        AbstractDungeon.player.drawPile.moveToExhaustPile(c);
                    }
                }
                this.isDone = true;
                MultiplayerHelper.sendP2PString(signal);

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
        }
    }
}
