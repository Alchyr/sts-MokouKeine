package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class TragedyAction extends AbstractGameAction {
    public TragedyAction(int damage, AbstractPlayer source, AbstractMonster target)
    {
        setValues(target, source, damage);
    }

    @Override
    public void update() {
        if (target == null)
        {
            this.isDone = true;
            return;
        }
        int finalDamage = 0;
        if (target.hasPower(WeakPower.POWER_ID))
        {
            finalDamage += target.getPower(WeakPower.POWER_ID).amount * this.amount;
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, WeakPower.POWER_ID));
        }
        if (target.hasPower(VulnerablePower.POWER_ID))
        {
            finalDamage += target.getPower(VulnerablePower.POWER_ID).amount * this.amount;
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, VulnerablePower.POWER_ID));
        }

        if (finalDamage > 0)
        {
            AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(source, finalDamage, DamageInfo.DamageType.NORMAL), AttackEffect.POISON));
        }

        this.isDone = true;
    }
}
