package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.ReceiveSignalCardsAction;
import もこけね.actions.character.MakeTempCardInOtherHandAction;
import もこけね.character.MokouKeine;

public class ReceiveCounterfeitCardAction extends ReceiveSignalCardsAction {
    public ReceiveCounterfeitCardAction(int amount)
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
        this.duration = 10.0f;
    }

    @Override
    public void update() {
        if (signals.size > 0)
        {
            processCardStrings();
        }

        if (signaledCards.size > 0)
        {
            AbstractCard c = signaledCards.removeFirst();
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInOtherHandAction(c.makeCopy(), this.amount));
            if (AbstractDungeon.player instanceof MokouKeine)
            {
                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, ((MokouKeine) AbstractDungeon.player).otherPlayerHand));
            }
            signaledGroups.removeFirst();
            this.isDone = true;
        }
        tickDuration();
    }
}