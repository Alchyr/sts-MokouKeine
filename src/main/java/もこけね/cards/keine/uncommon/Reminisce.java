/*package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import もこけね.abstracts.KeineCard;
import もこけね.actions.cards.ReminisceAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Reminisce extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Reminisce",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 2;

    public Reminisce()
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
        int amt = 0;
        if (mo != null)
        {
            for (AbstractPower p : mo.powers)
            {
                if (p.type == AbstractPower.PowerType.DEBUFF)
                {
                    ++amt;
                }
            }

            this.baseMagicNumber = amt * this.baseDamage;
            this.magicNumber = amt * this.damage;
            this.isMagicNumberModified = this.isDamageModified;

            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ReminisceAction(this.damage, p, m));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Reminisce();
    }
}*/