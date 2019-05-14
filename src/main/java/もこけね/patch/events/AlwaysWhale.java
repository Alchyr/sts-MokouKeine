package もこけね.patch.events;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.neow.NeowEvent;
import javassist.CtBehavior;
import もこけね.patch.enums.CharacterEnums;

@SpirePatch(
        clz = NeowEvent.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = { boolean.class }
)
public class AlwaysWhale {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "bossCount" }
    )
    public static void ALWAYS_WHALE(NeowEvent __instance, boolean isDone, @ByRef int[] bossCount)
    {
        if (!Settings.isEndless || AbstractDungeon.floorNum <= 1)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
                bossCount[0] = 1;
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractEvent.class, "body");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
