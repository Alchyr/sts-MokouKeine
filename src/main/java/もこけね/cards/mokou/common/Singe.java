package もこけね.cards.mokou.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.MakeTempCardInOtherHandAction;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class Singe extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Singe",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 4;

    public Singe()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        if (TrackCardSource.useOtherEnergy)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInOtherHandAction(new Burn()));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Burn()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Singe();
    }
}