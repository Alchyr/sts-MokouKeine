package もこけね.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReminisceAction extends AbstractGameAction {
    public ReminisceAction(int damage, AbstractPlayer source, AbstractMonster target)
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
        for (AbstractPower p : target.powers)
        {
            if (p.type == AbstractPower.PowerType.DEBUFF)
                finalDamage += this.amount;
        }
        AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(source, finalDamage, DamageInfo.DamageType.NORMAL), AttackEffect.SMASH));
        AbstractDungeon.actionManager.addToTop(new RemoveDebuffsAction(target));

        this.isDone = true;
    }
}