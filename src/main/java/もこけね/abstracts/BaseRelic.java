package もこけね.abstracts;

import basemod.abstracts.CustomRelic;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.assetPath;

public abstract class BaseRelic extends CustomRelic {
    public BaseRelic(String setId, String textureID, RelicTier tier, LandingSound sfx) {
        super(setId, TextureLoader.getTexture(assetPath("img/relics/") + textureID + ".png"), tier, sfx);
        outlineImg = TextureLoader.getTexture(assetPath("img/relics/outline/") + textureID + ".png");
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}