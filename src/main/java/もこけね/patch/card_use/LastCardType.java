package もこけね.patch.card_use;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;


@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class LastCardType {
    public static AbstractCard.CardType type = AbstractCard.CardType.CURSE;

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "targetCard" }
    )
    public static void TrackLastUsed(UseCardAction __instance, AbstractCard targetCard) {
        type = targetCard.type;
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");// 37
            return LineFinder.findInOrder(ctBehavior, finalMatcher);// 38
        }
    }
}