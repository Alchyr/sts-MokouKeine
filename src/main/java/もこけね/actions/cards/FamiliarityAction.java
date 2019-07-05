package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.character.MokouKeine;

import java.util.ArrayList;

public class FamiliarityAction extends AbstractGameAction {
    public FamiliarityAction()
    {

    }

    @Override
    public void update() {
        ArrayList<AbstractCard> validCards = new ArrayList<>();
        ArrayList<AbstractCard> handCards = new ArrayList<>();
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            validCards.addAll(((MokouKeine) AbstractDungeon.player).otherPlayerDraw.group);
            validCards.addAll(((MokouKeine) AbstractDungeon.player).otherPlayerHand.group);
            validCards.addAll(((MokouKeine) AbstractDungeon.player).otherPlayerDiscard.group);

            handCards.addAll(((MokouKeine) AbstractDungeon.player).otherPlayerHand.group);
        }
        validCards.addAll(AbstractDungeon.player.drawPile.group);
        validCards.addAll(AbstractDungeon.player.hand.group);
        validCards.addAll(AbstractDungeon.player.discardPile.group);

        handCards.addAll(AbstractDungeon.player.hand.group);

        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat)
        {
            if (validCards.remove(c))
            {
                c.freeToPlayOnce = true;
                if (handCards.remove(c))
                {
                    c.superFlash();
                }
            }
        }

        this.isDone = true;
    }
}
