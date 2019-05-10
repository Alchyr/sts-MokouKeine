package もこけね.patch.combat;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import もこけね.character.MokouKeine;
import もこけね.patch.lobby.HandleMatchmaking;
import もこけね.util.MultiplayerHelper;

import static もこけね.patch.lobby.HandleMatchmaking.isHost;
import static もこけね.もこけねは神の国.logger;
import static もこけね.もこけねは神の国.makeID;

public class RequireDoubleEndTurn {
    public static final UIStrings endTurnStrings = CardCrawlGame.languagePack.getUIString(makeID("EndTurn"));

    public static boolean ended = false;
    public static boolean otherPlayerEnded = false;

    private static boolean allowDisable = false;

    public static void reset()
    {
        ended = false;
        otherPlayerEnded = false;
        allowDisable = false;
    }

    @SpirePatch(
            clz = EndTurnButton.class,
            method = "disable",
            paramtypez = { boolean.class }
    )
    public static class onDisable
    {
        @SpirePrefixPatch
        public static SpireReturn preventInstantEnd(EndTurnButton __instance, boolean isEnemyTurn)
        {
            if (allowDisable)
            {
                reset();
                return SpireReturn.Continue();
            }

            if (AbstractDungeon.player instanceof MokouKeine && MultiplayerHelper.active)
            {
                if (!ended)
                {
                    endTurn();
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    private static void endTurn()
    {
        AbstractDungeon.player.releaseCard();
        ended = true;
        MultiplayerHelper.sendP2PMessage(CardCrawlGame.playerName + endTurnStrings.TEXT[1]);
        logger.info("Ended turn.");

        if (isHost && otherPlayerEnded)
        {
            allowDisable = true;
            MultiplayerHelper.sendP2PString("full_end_turn");
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
        }
        else
        {
            MultiplayerHelper.sendP2PString("end_turn");
        }
    }

    public static void otherPlayerEndTurn()
    {
        otherPlayerEnded = true;
        if (ended && isHost)
        {
            allowDisable = true;
            MultiplayerHelper.sendP2PString("full_end_turn");
            AbstractDungeon.overlayMenu.endTurnButton.disable(true);
        }
    }

    public static void fullEndTurn()
    {
        allowDisable = true;
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);
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
