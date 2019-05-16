package もこけね.cards.keine.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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

public class Shatter extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Shatter",
            1,
            CardType.SKILL,
            CardTarget.ENEMY,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DEBUFF = 1;
    private static final int UPG_DEBUFF = 1;

    public Shatter()
    {
        super(cardInfo, false);

        setDamage(DEBUFF, UPG_DEBUFF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amt = 0;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
        {
            if (!mo.isDeadOrEscaped())
                amt += this.magicNumber;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, amt, false), amt));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Shatter();
    }
}