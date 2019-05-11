package もこけね.patch.ui;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;
import もこけね.patch.enums.CharacterEnums;

import java.util.ArrayList;

@SpirePatch(
        clz = ColorTabBarFix.Render.class,
        method = "Insert"
)
public class LocalizedColorNamesInLibrary {
    private static ArrayList<ColorTabBarFix.ModColorTab> modTabs = null;
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "i", "tabName" }
    )
    public static void changeTabName(ColorTabBar __instance, SpriteBatch sb, float y, ColorTabBar.CurrentTab tab, int i, @ByRef String[] tabName)
    {
        if (modTabs == null)
        {
            modTabs = (ArrayList<ColorTabBarFix.ModColorTab>)ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabs");
        }
        if (modTabs.get(i).color == CharacterEnums.MOKOU)
        {
            tabName[0] = MokouKeine.characterStrings.NAMES[1];
        }
        else if (modTabs.get(i).color == CharacterEnums.KEINE)
        {
            tabName[0] = MokouKeine.characterStrings.NAMES[2];
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
