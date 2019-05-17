package もこけね.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.abstracts.BasePower;
import もこけね.actions.character.MakeTempCardInOtherDrawAction;
import もこけね.character.MokouKeine;

import static もこけね.もこけねは神の国.makeID;

public class RecordPower extends BasePower implements NonStackablePower {
    public static final String NAME = "Record";
    public static final String POWER_ID = makeID(NAME);
    public static final PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = true;

    private boolean other;

    public RecordPower(final AbstractCreature owner, int amount, boolean other)
    {
        super(NAME, TYPE, TURN_BASED, owner, null, amount);
        this.other = other;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0)
        {
            this.amount--;
            this.updateDescription();

            if (other)
            {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInOtherDrawAction(card, 1, true, true));
            }
            else
            {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(card, 1, true, true));
            }
        }

        if (this.amount <= 0)
        {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    public void updateDescription() {
        String name = descriptions()[4];
        if (other && AbstractDungeon.player instanceof MokouKeine)
        {
            name = ((MokouKeine) AbstractDungeon.player).getOtherPlayerName() + descriptions()[5];
        }

        if (this.amount == 1)
        {
            this.description = descriptions()[0] + name + descriptions()[3];
        }
        else
        {
            this.description = descriptions()[1] + amount + descriptions()[2] + name + descriptions()[3];
        }
    }
}