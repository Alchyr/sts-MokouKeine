package もこけね.patch.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.relics.PandorasBox;
import javassist.CtBehavior;
import もこけね.patch.enums.CharacterEnums;
import もこけね.patch.events.VampiresRemoval;
import もこけね.util.MultiplayerHelper;

import java.util.Iterator;

@SpirePatch(
        clz = PandorasBox.class,
        method = "onEquip"
)
public class PandoraRemoval {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "e" }
    )
    public static void reportRemoval(PandorasBox __instance, AbstractCard e)
    {
        if (MultiplayerHelper.active && AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
        {
            MultiplayerHelper.sendP2PString("other_remove_card" + AbstractDungeon.player.masterDeck.group.indexOf(e));
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Iterator.class, "remove");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
