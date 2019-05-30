package もこけね.cards.keine.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.TragedyAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Tragedy extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Tragedy",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 2;

    public Tragedy()
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
        this.baseMagicNumber = 0;
        this.magicNumber = 0;
        if (mo != null)
        {
            if (mo.hasPower(WeakPower.POWER_ID))
            {
                this.baseMagicNumber += mo.getPower(WeakPower.POWER_ID).amount * this.baseDamage;
                this.magicNumber += mo.getPower(WeakPower.POWER_ID).amount * this.damage;
            }
            if (mo.hasPower(VulnerablePower.POWER_ID))
            {
                this.baseMagicNumber += mo.getPower(VulnerablePower.POWER_ID).amount * this.baseDamage;
                this.magicNumber += mo.getPower(VulnerablePower.POWER_ID).amount * this.damage;
            }
            this.isMagicNumberModified = this.isDamageModified;

            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new TragedyAction(this.damage, p, m));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Tragedy();
    }
}