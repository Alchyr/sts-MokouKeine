package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import もこけね.abstracts.ReceiveSignalCardsAction;
import もこけね.actions.character.WaitForSignalAction;
import もこけね.character.MokouKeine;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.util.MultiplayerHelper;

import java.util.HashMap;

import static もこけね.util.MultiplayerHelper.partnerName;
import static もこけね.もこけねは神の国.makeID;

public class CounterfeitAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Counterfeit"));
    public static final String[] TEXT = uiStrings.TEXT;

    private String exhaustString;

    private HashMap<AbstractCard, Integer> indexes = new HashMap<>();

    public CounterfeitAction(int amount)
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;
        this.amount = amount;
        exhaustString = "";
    }

    @Override
    public void update() {
        if (TrackCardSource.useOtherEnergy && AbstractDungeon.player instanceof MokouKeine) //played by other player.
        {
            AbstractDungeon.actionManager.addToTop(new ReceiveCounterfeitCardAction(this.amount));
            AbstractDungeon.actionManager.addToTop(new WaitForSignalAction(TEXT[1] + partnerName + TEXT[2]));
            this.isDone = true;
            return;
        } else if (AbstractDungeon.player instanceof MokouKeine) {
            if (this.duration == Settings.ACTION_DUR_FASTER) {
                if (AbstractDungeon.player.hand.isEmpty()) {
                    this.isDone = true;
                    MultiplayerHelper.sendP2PString("signal");
                    return;
                }

                for (int i = 0; i < AbstractDungeon.player.hand.group.size(); ++i)
                {
                    indexes.put(AbstractDungeon.player.hand.group.get(i), i);
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
                this.tickDuration();
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                boolean first = true;
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
                {
                    if (first)
                    {
                        AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c.makeCopy(), this.amount));
                        AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
                        exhaustString = "exhaustother_hand " + indexes.get(c);
                        MultiplayerHelper.sendP2PString(ReceiveSignalCardsAction.signalCardString(indexes.get(c), AbstractDungeon.player.hand, true));
                        first = false;
                    }
                    AbstractDungeon.player.hand.addToTop(c);
                }
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
        }

        this.tickDuration();

        if (this.isDone)
            MultiplayerHelper.sendP2PString("signal" + exhaustString); //this will add exhaust action to top, which will resolve before the queued ReceiveCounterfeitCardAction
    }
}
