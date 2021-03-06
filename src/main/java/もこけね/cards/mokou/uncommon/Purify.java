package もこけね.cards.mokou.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import もこけね.abstracts.MokouCard;
import もこけね.actions.cards.PurifyAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Purify extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Purify",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 3;

    public Purify()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (mo != null)
        {
            int amt = 0;

            for (AbstractPower p : mo.powers)
            {
                if (p.type == AbstractPower.PowerType.DEBUFF)
                {
                    ++amt;
                }
            }

            this.magicNumber = this.baseMagicNumber = amt;

            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new PurifyAction(this.damage, p, m));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Purify();
    }
}