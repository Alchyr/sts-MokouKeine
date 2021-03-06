package もこけね.patch.resting;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RecallOption;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.MultiplayerHelper;

import java.util.ArrayList;

public class TakeRedKey {
    @SpirePatch(
            clz = RecallOption.class,
            method = "useOption"
    )
    public static class SyncRecall
    {
        @SpirePostfixPatch
        public static void reportRecall(RecallOption __instance)
        {
            if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE && MultiplayerHelper.active)
            {
                MultiplayerHelper.sendP2PString("recall");
                MultiplayerHelper.sendP2PMessage(AbstractDungeon.player.name + AdjustRestAmount.TEXT[4]);
            }
        }
    }

    public static void obtainKey()
    {
        if (AbstractDungeon.getCurrRoom() instanceof RestRoom)
        {
            ArrayList<AbstractCampfireOption> buttons = (ArrayList)ReflectionHacks.getPrivate(((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI, CampfireUI.class, "buttons");
            AbstractCampfireOption toRemove = null;
            for (AbstractCampfireOption o : buttons)
            {
                if (o instanceof RecallOption)
                {
                    toRemove = o;
                    break;
                }
            }
            if (toRemove != null)
            {
                ((ArrayList)ReflectionHacks.getPrivate(((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI, CampfireUI.class, "buttons")).remove(toRemove);
            }
        }
        if (!Settings.hasRubyKey)
        {
            CardCrawlGame.sound.playA("KEY_OBTAIN", -0.2F);
            Settings.hasRubyKey = true;
        }
    }
}
