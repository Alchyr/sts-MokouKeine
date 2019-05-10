package もこけね.patch.ui;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import javassist.CtBehavior;
import もこけね.patch.enums.CharacterEnums;

@SpirePatch(
        clz = ColorTabBarFix.Render.class,
        method = "Insert"
)
public class NoCharacterNameInLibrary {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "playerClass" }
    )
    public static void changeTabName(ColorTabBar __instance, SpriteBatch sb, float y, ColorTabBar.CurrentTab tab, @ByRef AbstractPlayer.PlayerClass[] playerClass)
    {
        if (playerClass[0] != null && playerClass[0] == CharacterEnums.MOKOUKEINE)
        {
            playerClass[0] = null;
        }
        //Later, move insert patch to next line, and if tab name is Mokou or Keine, get localized name
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "getLocalizedCharacterName");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
