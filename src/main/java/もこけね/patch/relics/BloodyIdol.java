package もこけね.patch.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.patch.enums.CharacterEnums;

public class BloodyIdol {
    private static final int ALT_HEAL = 2;

    @SpirePatch(
            clz = com.megacrit.cardcrawl.relics.BloodyIdol.class,
            method = "getUpdatedDescription"
    )
    public static class OtherValueDescription
    {
        @SpirePrefixPatch
        public static SpireReturn<String> useAltValue(com.megacrit.cardcrawl.relics.BloodyIdol __instance)
        {
            if (AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
            {
                return SpireReturn.Return(__instance.DESCRIPTIONS[0] + ALT_HEAL + __instance.DESCRIPTIONS[1]);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = com.megacrit.cardcrawl.relics.BloodyIdol.class,
            method = "onGainGold"
    )
    public static class OtherValueHeal
    {
        @SpirePrefixPatch
        public static SpireReturn useAltValue(com.megacrit.cardcrawl.relics.BloodyIdol __instance)
        {
            if (AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
            {
                __instance.flash();
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));
                AbstractDungeon.player.heal(ALT_HEAL, true);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
