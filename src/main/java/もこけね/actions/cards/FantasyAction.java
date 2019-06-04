package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import もこけね.abstracts.FantasyEffect;
import もこけね.actions.character.WaitForSignalAction;
import もこけね.cards.colorless.FantasyCard;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.CardChoiceBuilder;
import もこけね.util.MultiplayerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static もこけね.util.MultiplayerHelper.partnerName;
import static もこけね.もこけねは神の国.makeID;

public class FantasyAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Fantasy"));

    private static HashMap<UUID, ArrayList<FantasyCard>> createdCards = new HashMap<>();


    private AbstractCard sourceCard;
    private boolean upgraded;

    private int stage;

    public FantasyAction(AbstractCard source)
    {
        this.sourceCard = source;
        this.upgraded = source.upgraded;
        this.stage = 0;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        CardChoiceBuilder b;
        FantasyCard baseCard;
        switch (stage)
        {
            case 0:
                if (MultiplayerHelper.active && TrackCardSource.useOtherEnergy && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
                {
                    AbstractDungeon.actionManager.addToTop(new WaitForSignalAction(uiStrings.TEXT[0] + partnerName + uiStrings.TEXT[1]));
                    this.isDone = true;

                    break;
                }

                ++stage;

                new CardChoiceBuilder()
                        .addOption(new FantasyCard(AbstractCard.CardType.ATTACK))
                        .addOption(new FantasyCard(AbstractCard.CardType.SKILL)).open();
                break;
            case 1:
                if (AbstractDungeon.cardRewardScreen.discoveryCard instanceof FantasyCard)
                {
                    baseCard = (FantasyCard)AbstractDungeon.cardRewardScreen.discoveryCard;
                }
                else
                {
                    this.stage = 99;
                    break;
                }

                ArrayList<Integer> possibleCosts = new ArrayList<>();
                possibleCosts.add(-1);
                possibleCosts.add(0);
                possibleCosts.add(1);
                possibleCosts.add(2);
                possibleCosts.add(3);

                possibleCosts.remove(AbstractDungeon.cardRandomRng.random(4));
                possibleCosts.remove(AbstractDungeon.cardRandomRng.random(3));

                b = new CardChoiceBuilder();
                for (int i : possibleCosts)
                {
                    b.addOption(new FantasyCard(baseCard.type, i));
                }

                ++stage;

                b.open();
                break;
            case 2:
                if (AbstractDungeon.cardRewardScreen.discoveryCard instanceof FantasyCard)
                {
                    baseCard = (FantasyCard)AbstractDungeon.cardRewardScreen.discoveryCard;
                }
                else
                {
                    this.stage = 99;
                    break;
                }

                ArrayList<FantasyEffect> possibleBaseOptions = FantasyEffect.getBaseOptions(baseCard);

                ArrayList<FantasyEffect> chosenOptions = new ArrayList<>();
                for (int i = 0; i < 3; ++i)
                {
                    if (possibleBaseOptions.isEmpty())
                        break;

                    chosenOptions.add(possibleBaseOptions.remove(AbstractDungeon.cardRandomRng.random(possibleBaseOptions.size() - 1)));
                }

                b = new CardChoiceBuilder();

                for (FantasyEffect e : chosenOptions)
                {
                    b.addOption(new FantasyCard(baseCard.type, baseCard.cost).setBaseEffect(e));
                }

                ++stage;

                b.open();
                break;
            case 3:
                if (AbstractDungeon.cardRewardScreen.discoveryCard instanceof FantasyCard)
                {
                    baseCard = (FantasyCard)AbstractDungeon.cardRewardScreen.discoveryCard;
                }
                else
                {
                    this.stage = 99;
                    break;
                }

                ArrayList<FantasyEffect> possibleBonusOptions = FantasyEffect.getBonusOptions(baseCard);

                ArrayList<FantasyEffect> chosenBonusOptions = new ArrayList<>();
                for (int i = 0; i < 3; ++i)
                {
                    if (possibleBonusOptions.isEmpty())
                        break;

                    chosenBonusOptions.add(possibleBonusOptions.remove(AbstractDungeon.cardRandomRng.random(possibleBonusOptions.size() - 1)));
                }

                b = new CardChoiceBuilder();

                for (FantasyEffect e : chosenBonusOptions)
                {
                    b.addOption(new FantasyCard(baseCard.type, baseCard.cost).setBaseEffect(baseCard.baseEffect).setBonusEffect(e));
                }

                ++stage;

                b.open();
                break;
            case 4:
                if (AbstractDungeon.cardRewardScreen.discoveryCard instanceof FantasyCard)
                {
                    baseCard = (FantasyCard)AbstractDungeon.cardRewardScreen.discoveryCard;
                }
                else
                {
                    this.stage = 99;
                    break;
                }

                //card construction should be done (for now). Later, this should be the stage to choose a name/additional image to go on the card.
                //will determine attack effect and maybe some other vfx?
                FantasyCard completeCard = new FantasyCard(baseCard.type, baseCard.cost).setBaseEffect(baseCard.baseEffect).setBonusEffect(baseCard.bonusEffect);

                if (!createdCards.containsKey(sourceCard.uuid))
                    createdCards.put(sourceCard.uuid, new ArrayList<>());

                createdCards.get(sourceCard.uuid).add(completeCard);

                ++stage;

                break;
            default:
                addGeneratedCards();

                if (MultiplayerHelper.active && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
                    MultiplayerHelper.sendP2PString("signalcrrng" + AbstractDungeon.cardRandomRng.counter);
                this.isDone = true;
        }
    }

    private void addGeneratedCards()
    {
        ArrayList<FantasyCard> created = createdCards.get(sourceCard.uuid);

        if (created != null)
        {
            for (FantasyCard c : created)
            {
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), 1, true, true));
            }
        }
    }
}
