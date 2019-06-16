package もこけね.actions.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.ReceiveSignalCardsAction;
import もこけね.actions.character.MakeTempCardInOtherHandAction;

public class ReceiveOriginateCardAction extends ReceiveSignalCardsAction {
    public ReceiveOriginateCardAction()
    {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (signaledCards.size > 0)
        {
            AbstractCard c = signaledCards.removeFirst().makeStatEquivalentCopy();
            signaledGroups.removeFirst();
            c.modifyCostForCombat(-1);
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInOtherHandAction(c));
        }
        this.isDone = true;
    }
}