package もこけね.actions.cards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import もこけね.abstracts.ReceiveSignalCardsAction;
import もこけね.effects.AltShowCardAndAddToHandEffect;


public class ReceiveRevisionCardsAction extends ReceiveSignalCardsAction {
    public ReceiveRevisionCardsAction()
    {
        this.actionType = ActionType.CARD_MANIPULATION;
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
            int amt = signaledCards.size;
            int handSpace = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();

            for (int i = 0; i < amt; ++i)
            {
                CardGroup source = signaledGroups.removeFirst();
                AbstractCard c = signaledCards.removeFirst();

                source.removeCard(c);
                if (handSpace > 0)
                {
                    AbstractDungeon.effectList.add(new AltShowCardAndAddToHandEffect(c, false));
                    --handSpace;
                }
                else
                {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
                }
            }
            this.isDone = true;
        }
        tickDuration();
    }
}