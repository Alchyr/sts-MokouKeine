package もこけね.patch.hooks;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import もこけね.もこけねは神の国;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
)
public class PreMonsterTurn {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "m" }
    )
    public static void preMonsterTakeTurn(GameActionManager __instance, AbstractMonster m)
    {
        もこけねは神の国.preMonsterTurn(m);
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "takeTurn");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
