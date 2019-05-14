package もこけね.patch.ui;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import もこけね.patch.enums.CharacterEnums;

@SpirePatch(
        clz = EndTurnButton.class,
        method = SpirePatch.CONSTRUCTOR
)
public class EndTurnButtonPosition {
    private static final float ALT_Y = 250.0F * Settings.scale;
    public static void adjustPos(EndTurnButton __instance)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
        {
            ReflectionHacks.setPrivate(__instance, EndTurnButton.class, "current_y", ALT_Y);
        }
    }
}
