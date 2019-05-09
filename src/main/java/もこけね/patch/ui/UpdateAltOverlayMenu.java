package もこけね.patch.ui;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import javassist.CtBehavior;
import もこけね.ui.AltOverlayMenu;

@SpirePatch(
        clz = OverlayMenu.class,
        method = "update"
)
public class UpdateAltOverlayMenu {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void changeGroup(OverlayMenu __instance)
    {
        if (__instance instanceof AltOverlayMenu)
        {
            ((AltOverlayMenu) __instance).otherPlayerDrawPile.updatePositions(((AltOverlayMenu) __instance).wasOpen);
            ((AltOverlayMenu) __instance).otherDiscardPilePanel.updatePositions();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(DrawPilePanel.class, "updatePositions");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
