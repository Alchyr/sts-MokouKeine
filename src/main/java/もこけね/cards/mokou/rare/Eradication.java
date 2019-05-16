package もこけね.cards.mokou.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.general.SuperFastDamageAction;
import もこけね.effects.EradicationEffect;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Eradication extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Eradication",
            4,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);
    //4 cost: Deal 3 [4] damage 12 times.
    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 1;
    private static final int HIT_COUNT = 12;

    public Eradication()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(HIT_COUNT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
        {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new EradicationEffect(m.hb.cX, m.hb.cY, this.magicNumber), Settings.FAST_MODE ? 0.3f : 0.9f));
        }
        for (int i = 0; i < this.magicNumber; i++)
        {
            AbstractDungeon.actionManager.addToBottom(new SuperFastDamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Eradication();
    }
}