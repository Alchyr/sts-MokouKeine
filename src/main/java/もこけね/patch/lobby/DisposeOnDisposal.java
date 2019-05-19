package もこけね.patch.lobby;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import もこけね.util.HandleMatchmaking;
import もこけね.util.MultiplayerHelper;

@SpirePatch(
        clz = CardCrawlGame.class,
        method = "dispose"
)
public class DisposeOnDisposal {
    @SpirePrefixPatch
    public static void cleanup(CardCrawlGame __instance)
    {
        HandleMatchmaking.dispose();
        MultiplayerHelper.dispose();
    }
}
