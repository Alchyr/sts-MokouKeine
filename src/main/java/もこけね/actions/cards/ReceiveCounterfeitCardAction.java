package もこけね.actions.cards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.ReceiveSignalCardsAction;
import もこけね.actions.character.MakeTempCardInOtherHandAction;

public class ReceiveCounterfeitCardAction extends ReceiveSignalCardsAction {
    public ReceiveCounterfeitCardAction(int amount)
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (signaledCards.size > 0)
        {
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInOtherHandAction(signaledCards.removeFirst().makeCopy(), this.amount));
        }
        this.isDone = true;
    }
}