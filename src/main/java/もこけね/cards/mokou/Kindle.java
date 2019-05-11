package もこけね.cards.mokou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.ResetEnergyGainAction;
import もこけね.actions.character.SetEnergyGainAction;
import もこけね.character.MokouKeine;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Kindle extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Kindle",
            1,
            AbstractCard.CardType.SKILL,
            AbstractCard.CardTarget.SELF,
            AbstractCard.CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int ENERGY = 2; //energy descriptions aren't that flexible, so not using magic number.
    private static final int UPG_COST = 0;

    public Kindle()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p instanceof MokouKeine)
        {
            AbstractDungeon.actionManager.addToBottom(new SetEnergyGainAction(true));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY));
            AbstractDungeon.actionManager.addToBottom(new ResetEnergyGainAction());
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Kindle();
    }
}