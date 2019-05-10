package もこけね.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.RefreshEnergyEffect;

public class OtherEnergyPanel extends EnergyPanel {
    private static float x_pos = 0;

    public OtherEnergyPanel()
    {
        super();
        this.show_x = x_pos = Settings.WIDTH - show_x;
        this.hide_x = Settings.WIDTH - hide_x;
        this.current_x = hide_x;
    }

    public static void setEnergy(int energy) {
        totalCount = energy;
        RefreshEnergyEffect e = new RefreshEnergyEffect();
        TextureAtlas.AtlasRegion img = (TextureAtlas.AtlasRegion)ReflectionHacks.getPrivate(e, RefreshEnergyEffect.class, "img");
        ReflectionHacks.setPrivate(e, RefreshEnergyEffect.class, "x", x_pos - (float)img.packedWidth / 2.0F);
        AbstractDungeon.effectsQueue.add(e);

        energyVfxTimer = 2.0F;
        fontScale = 2.0F;
    }
}
