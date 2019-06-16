package もこけね.cards.mokou.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.MokouCard;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.util.CardInfo;
import もこけね.util.OtherPlayerCardQueueItem;

import static もこけね.もこけねは神の国.makeID;

public class SoaringKick extends MokouCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SoaringKick",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 2;

    private boolean isEcho;

    public SoaringKick()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
        isEcho = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null && !m.isDeadOrEscaped())
        {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));

            if (!isEcho)
            {
                AbstractCard copy = this.makeSameInstanceOf();
                if (copy instanceof SoaringKick)
                {
                    ((SoaringKick)copy).isEcho = true;
                }

                p.limbo.addToBottom(copy);

                copy.current_x = this.current_x;
                copy.target_x = this.target_x;
                copy.current_y = this.current_y;
                copy.target_y = this.target_y;

                copy.freeToPlayOnce = true;
                copy.purgeOnUse = true;

                copy.calculateCardDamage(m);

                if (TrackCardSource.useOtherEnergy)
                {
                    AbstractDungeon.actionManager.cardQueue.add(new OtherPlayerCardQueueItem(copy, m, this.energyOnUse));
                }
                else
                {
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(copy, m, this.energyOnUse));
                }
            }
        }

    }

    @Override
    public AbstractCard makeCopy() {
        return new SoaringKick();
    }
}