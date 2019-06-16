package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.ReceiveSignalCardsAction;

import java.util.ArrayList;

public class ReceiveFantasyCardAction extends ReceiveSignalCardsAction {
    private AbstractCard sourceCard;

    public ReceiveFantasyCardAction(AbstractCard source)
    {
        this.sourceCard = source;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (signaledCards.size > 0)
        {
            if (!FantasyAction.createdCards.containsKey(sourceCard.uuid))
                FantasyAction.createdCards.put(sourceCard.uuid, new ArrayList<>());

            FantasyAction.createdCards.get(sourceCard.uuid).add(signaledCards.removeFirst());
            signaledGroups.removeFirst();
        }
        addGeneratedCards();
        this.isDone = true;
    }

    private void addGeneratedCards()
    {
        ArrayList<AbstractCard> created = FantasyAction.createdCards.get(sourceCard.uuid);

        if (created != null)
        {
            for (AbstractCard c : created)
            {
                AbstractCard toAdd = c.makeStatEquivalentCopy();
                if (sourceCard.upgraded)
                    toAdd.upgrade();
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(toAdd, 1, true, true));
            }
        }
    }
}
