package もこけね.cards.mokou.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.SummonSparkAction;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Warmup extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Warmup",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int COST_UPG = 0;

    private static final int DAMAGE = 5;

    public Warmup()
    {
        super(cardInfo, false);

        setCostUpgrade(COST_UPG);
        setDamage(DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(new SummonSparkAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Warmup();
    }
}