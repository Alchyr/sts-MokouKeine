package もこけね.patch.powers;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StasisPower;
import もこけね.actions.character.MakeTempCardInOtherDiscardAction;
import もこけね.actions.character.MakeTempCardInOtherHandAction;
import もこけね.character.MokouKeine;

public class Stasis {
    @SpirePatch(
            clz = StasisPower.class,
            method = SpirePatch.CLASS
    )
    public static class StasisFields
    {
        public static SpireField<Boolean> stoleOther = new SpireField<>(()->false);
    }

    @SpirePatch(
            clz = StasisPower.class,
            method = "onDeath"
    )
    public static class ReturnToCorrectHand
    {
        @SpirePrefixPatch
        public static SpireReturn checkDestination(StasisPower __instance)
        {
            if (StasisFields.stoleOther.get(__instance) && AbstractDungeon.player instanceof MokouKeine)
            {
                AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, StasisPower.class, "card");
                if (((MokouKeine) AbstractDungeon.player).otherPlayerHand.size() < BaseMod.MAX_HAND_SIZE) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInOtherHandAction(c, false, true));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInOtherDiscardAction(c, true));
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
