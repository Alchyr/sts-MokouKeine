package もこけね.patch.combat;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import もこけね.character.MokouKeine;
import もこけね.patch.lobby.HandleMatchmaking;

import static もこけね.もこけねは神の国.makeID;

public class RequireDoubleEndTurn {
    public static final UIStrings endTurnStrings = CardCrawlGame.languagePack.getUIString(makeID("EndTurn"));

    public static boolean ended = false;
    public static boolean otherPlayerEnded = false;

    public static void otherPlayerEndTurn()
    {
        otherPlayerEnded = true;
        if (ended)
        {
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
        }
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "disable",
            paramtypez = { boolean.class }
    )
    public static class onDisable
    {
        @SpirePrefixPatch
        public static void preventInstantEnd(EndTurnButton __instance, @ByRef boolean[] isEnemyTurn)
        {
            if (/*otherPlayerEnded && */isEnemyTurn[0])
            {
                ended = false;
                otherPlayerEnded = false;
                return;
            }
            if (isEnemyTurn[0] && HandleMatchmaking.activeMultiplayer && AbstractDungeon.player instanceof MokouKeine)
            {
                isEnemyTurn[0] = false;

                AbstractDungeon.player.releaseCard();
                ended = true;
                //Send turn end to other player
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "playCard"
    )
    public static class noPlay
    {
        @SpirePrefixPatch
        public static void noPlayAfterEnd(AbstractPlayer __instance)
        {
            if (ended && HandleMatchmaking.activeMultiplayer && __instance instanceof MokouKeine)
            {
                AbstractDungeon.effectList.add(new ThoughtBubble(__instance.dialogX, __instance.dialogY, 3.0F, endTurnStrings.TEXT[0], true));
                __instance.releaseCard();
            }
        }
    }
}
