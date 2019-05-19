package もこけね.patch.events;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.city.BackToBasics;

@SpirePatch(
        clz = BackToBasics.class,
        method = "upgradeStrikeAndDefends"
)
public class AncientWriting {
}
