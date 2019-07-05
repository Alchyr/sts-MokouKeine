package もこけね.patch.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.StasisPower;

public class Stasis {
    @SpirePatch(
            clz = StasisPower.class,
            method = SpirePatch.CLASS
    )
    public static class StasisFields
    {
        public static SpireField<Boolean> stoleOther = new SpireField<>(()->false);
    }


}
