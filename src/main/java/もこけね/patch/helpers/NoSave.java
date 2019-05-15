package もこけね.patch.helpers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import もこけね.character.MokouKeine;

@SpirePatch(
        clz = SaveHelper.class,
        method = "shouldSave"
)
public class NoSave {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> noYouShouldNotSave()
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }
}
