package もこけね.actions.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.ReceiveSignalCardsAction;
import もこけね.effects.AltShowCardAndAddToHandEffect;

import java.util.Map;

public class ReceiveRevisionCardsAction extends ReceiveSignalCardsAction {
    public ReceiveRevisionCardsAction()
    {
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (!signaledCards.isEmpty())
        {
            for (Map.Entry<AbstractCard, CardGroup> e : signaledCards.entrySet())
            {
                e.getValue().removeCard(e.getKey());
                AbstractDungeon.effectList.add(new AltShowCardAndAddToHandEffect(e.getKey(), false));
            }
            signaledCards.clear();
        }
        this.isDone = true;
    }
}