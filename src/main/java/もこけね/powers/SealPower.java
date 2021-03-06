package もこけね.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.BasePower;

import static もこけね.もこけねは神の国.makeID;

public class SealPower extends BasePower {
    public static final String NAME = "Seal";
    public static final String POWER_ID = makeID(NAME);
    public static final PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public SealPower(final AbstractCreature owner, int amount)
    {
        super(NAME, TYPE, TURN_BASED, owner, null, amount);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.amount > 0 && damageAmount > 0 && info.owner == this.owner && info.type == DamageInfo.DamageType.HP_LOSS) {
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            return 0;
        }
        else if (this.amount <= 0)
        {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        return damageAmount;
    }

    public void updateDescription() {
        if (this.amount == 1)
        {
            this.description = descriptions()[0];
        }
        else
        {
            this.description = descriptions()[1] + amount + descriptions()[2];
        }
    }
}