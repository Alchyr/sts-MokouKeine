package もこけね.patch.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import もこけね.patch.enums.CharacterEnums;

@SpirePatch(
        clz = EnergyPanel.class,
        method = SpirePatch.CONSTRUCTOR
)
public class EnergyPanelPos {
    public static final float ENERGY_PANEL_ADJUST = 190.0F * Settings.scale;

    @SpirePostfixPatch
    public static void adjustPos(EnergyPanel __instance)
    {
        if (CardCrawlGame.chosenCharacter == CharacterEnums.MOKOUKEINE)
        {
            __instance.show_y = Settings.WIDTH / 2.0f;
            __instance.hide_y = Settings.WIDTH / 2.0f;
        }
    }
}
