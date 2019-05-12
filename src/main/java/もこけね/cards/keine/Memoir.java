package もこけね.cards.keine;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import もこけね.abstracts.KeineCard;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Memoir extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Memoir",
            1,
            CardType.SKILL,
            CardTarget.ENEMY,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DEBUFF = 2;
    private static final int UPG_DEBUFF = 1;

    private static final int DRAW = 2;

    public Memoir()
    {
        super(cardInfo, false);

        setMagic(DEBUFF, UPG_DEBUFF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));

        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, DRAW));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Memoir();
    }
}