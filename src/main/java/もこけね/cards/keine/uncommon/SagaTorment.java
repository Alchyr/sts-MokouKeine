package もこけね.cards.keine.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Shockwave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import もこけね.abstracts.KeineCard;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class SagaTorment extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SagaTorment",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DEBUFF = 1;
    private static final int UPG_DEBUFF = 1;

    public SagaTorment()
    {
        super(cardInfo, false);

        setMagic(DEBUFF, UPG_DEBUFF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
    }

    @Override
    public void triggerWhenCopied() {
        this.superFlash(Color.VIOLET);
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShockWaveEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.BLACK.cpy(), ShockWaveEffect.ShockWaveType.NORMAL)));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, AbstractDungeon.player, new VulnerablePower(mo, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SagaTorment();
    }
}