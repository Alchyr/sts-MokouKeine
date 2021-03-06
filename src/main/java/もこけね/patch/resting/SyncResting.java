package もこけね.patch.resting;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.MultiplayerHelper;

public class SyncResting {
    public static boolean otherPlayerRest = false;

    @SpirePatch(
            clz = CampfireSleepEffect.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class SyncHPLoss
    {
        @SpirePostfixPatch
        public static void sendReportMessage(CampfireSleepEffect __instance)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                if (otherPlayerRest)
                {
                    otherPlayerRest = false;
                    return;
                }
                MultiplayerHelper.sendP2PMessage(AbstractDungeon.player.name + AdjustRestAmount.TEXT[3]);
                MultiplayerHelper.sendP2PString("rest");
            }
        }
        /*
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void reportNightTerrorHPLoss(CampfireSleepEffect __instance)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                MultiplayerHelper.sendP2PString("lose_max_hp5");
            }
        }
        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "decreaseMaxHealth");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }*/
    }
}
