package もこけね.patch.hooks;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import もこけね.interfaces.OnEnemyDeathPower;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = { boolean.class }
)
public class OnEnemyDeath {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void triggerOnDeathPowers(AbstractMonster __instance, boolean triggerRelics)
    {
        if (triggerRelics)
        {
            for (AbstractPower p : AbstractDungeon.player.powers)
            {
                if (p instanceof OnEnemyDeathPower)
                {
                    ((OnEnemyDeathPower) p).onEnemyDeath(__instance);
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "areMonstersBasicallyDead");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
