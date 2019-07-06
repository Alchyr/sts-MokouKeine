package もこけね.cards.mokou.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.actions.character.MakeTempCardInOtherHandAction;
import もこけね.effects.FlameBurstEffect;
import もこけね.patch.combat.BurstActive;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CustomCardTags;
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

    private static final int DAMAGE = 11;
    private static final int UPG_DAMAGE = 3;

    public Singe()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);

        tags.add(CustomCardTags.MK_BURST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));

        if (m != null)
        {
            if (BurstActive.active.get(m))
            {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Singe();
    }
}