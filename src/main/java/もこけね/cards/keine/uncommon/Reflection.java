package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.character.OtherPlayerDiscardAction;
import もこけね.actions.character.ResetEnergyGainAction;
import もこけね.actions.character.SetEnergyGainAction;
import もこけね.character.MokouKeine;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Reflection extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Reflection",
            2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 1;

    public Reflection()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amt = p.hand.size();
        if (p instanceof MokouKeine)
        {
            if (TrackCardSource.useOtherEnergy)
            {
                amt = ((MokouKeine) p).otherPlayerHand.size();
                AbstractDungeon.actionManager.addToBottom(new OtherPlayerDiscardAction((MokouKeine)p, p, amt, false));
            }
            else
            {
                AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, amt, false));
            }
            AbstractDungeon.actionManager.addToBottom(new SetEnergyGainAction(true));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amt));
            AbstractDungeon.actionManager.addToBottom(new ResetEnergyGainAction());
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amt));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Reflection();
    }
}