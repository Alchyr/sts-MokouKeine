package もこけね.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import もこけね.abstracts.BasePower;

import static もこけね.もこけねは神の国.makeID;

public class WeightPower extends BasePower implements NonStackablePower {
    public static final String NAME = "Weight";
    public static final String POWER_ID = makeID(NAME);
    public static final PowerType TYPE = PowerType.DEBUFF;
    public static final boolean TURN_BASED = true;

    public WeightPower(final AbstractCreature owner)
    {
        super(NAME, TYPE, TURN_BASED, owner, null, 1);
    }

    //Patched into vulnerable.

    @Override
    public void onInitialApplication() {
        if (owner.hasPower(VulnerablePower.POWER_ID))
        {
            owner.getPower(VulnerablePower.POWER_ID).updateDescription();
        }
    }

    @Override
    public void onRemove() {
        if (owner.hasPower(VulnerablePower.POWER_ID))
        {
            owner.getPower(VulnerablePower.POWER_ID).updateDescription();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
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