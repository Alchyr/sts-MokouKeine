package もこけね.patch.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import javassist.CtBehavior;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.MultiplayerHelper;

import java.util.ArrayList;

public class ReportPurchase {
    private static StoreRelic forcePurchase = null;

    @SpirePatch(
            clz = StoreRelic.class,
            method = "update"
    )
    public static class ReportOnPurchase
    {
        @SpireInsertPatch(
                locator = ForcePurchaseLocator.class
        )
        public static void forcePurchase(StoreRelic __instance, float pos)
        {
            if (__instance.equals(forcePurchase))
            {
                __instance.relic.hb.clicked = true;
            }
        }

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void onPurchase(StoreRelic __instance, float pos)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                if (__instance != forcePurchase)
                {
                    MultiplayerHelper.sendP2PMessage(AbstractDungeon.player.name + " bought " + __instance.relic.name + ".");
                    MultiplayerHelper.sendP2PString("purchase_relic" + __instance.relic.relicId);
                }
                else //if (__instance == forcePurchase)
                {
                    forcePurchase = null;
                }
            }
        }

        private static class ForcePurchaseLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Hitbox.class, "clicked");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "loseGold");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static void forcePurchase(String id)
    {
        if (AbstractDungeon.shopScreen != null)
        {
            ArrayList<StoreRelic> shopRelics = (ArrayList<StoreRelic>)ReflectionHacks.getPrivate(AbstractDungeon.shopScreen, ShopScreen.class, "relics");
            for (StoreRelic r : shopRelics)
            {
                if (r.relic != null && r.relic.relicId.equals(id))
                {
                    forcePurchase = r;
                }
            }
        }

        if (forcePurchase != null)
        {
            StoreRelic force = forcePurchase;
            force.price = 0;
            force.update(0); //sets forcePurchase to null

            if (force.isPurchased)
                ((ArrayList<StoreRelic>)ReflectionHacks.getPrivate(AbstractDungeon.shopScreen, ShopScreen.class, "relics")).remove(force);
        }
    }
}
