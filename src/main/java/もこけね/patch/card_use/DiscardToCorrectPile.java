package もこけね.patch.card_use;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;
import もこけね.patch.combat.SoulAltDiscard;
import もこけね.patch.combat.SoulAltOnToDeck;
import もこけね.util.OtherPlayerCardQueueItem;

public class DiscardToCorrectPile {
    public static boolean useOtherDiscard = false;

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class onPlayerUseCard
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void setDiscardPile(GameActionManager __instance)
        {
            if (__instance.cardQueue.get(0) instanceof OtherPlayerCardQueueItem)
            {
                useOtherDiscard = true;
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "useCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class useCorrectPiles
    {
        @SpireInsertPatch(
                locator = DiscardLocator.class
        )
        public static void moveAltDiscard(UseCardAction __instance)
        {
            if (UseCardActionDestination.useAlternatePile.get(__instance) && AbstractDungeon.player instanceof MokouKeine)
            {
                SoulAltDiscard.altGroup = ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard;
                //SoulAltDiscard modifies Soul.discard, which is used by moveToDiscardPile.
            }
        }

        @SpireInsertPatch(
                locator = DrawLocator.class
        )
        public static void moveAltDraw(UseCardAction __instance)
        {
            if (UseCardActionDestination.useAlternatePile.get(__instance) && AbstractDungeon.player instanceof MokouKeine)
            {
                SoulAltOnToDeck.altGroup = ((MokouKeine) AbstractDungeon.player).otherPlayerDraw;
                //SoulAltDiscard modifies Soul.discard, which is used by moveToDiscardPile.
            }
        }

        @SpirePostfixPatch
        public static void resetVars(UseCardAction __instance)
        {
            //ensuring that if anything else prevents moveToDiscard or moveToDraw or whatever else from being called, the modified groups aren't used on next calls.
            SoulAltDiscard.altGroup = null;
            SoulAltOnToDeck.altGroup = null;
        }


        private static class DiscardLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
        private static class DrawLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDeck");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static void reset()
    {
        useOtherDiscard = false;
    }
}
