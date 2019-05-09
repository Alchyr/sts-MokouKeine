package もこけね.patch.multiplayerQueue;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CtBehavior;
import もこけね.patch.enums.CharacterEnums;
import もこけね.patch.lobby.HandleMatchmaking;

import static もこけね.もこけねは神の国.chat;

@SpirePatch(
        clz = CharacterSelectScreen.class,
        method = "updateButtons"
)
public class UseMultiplayerQueue {
    public static boolean inQueue;

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn useQueue(CharacterSelectScreen __instance)
    {
        if (CardCrawlGame.chosenCharacter != null && CardCrawlGame.chosenCharacter == CharacterEnums.MOKOUKEINE)
        {
            __instance.confirmButton.hb.clicked = false;
            inQueue = true;
            __instance.confirmButton.hide();
            __instance.cancelButton.show("Exit Queue");

            BaseMod.setRichPresence("Playing as the Immortal and Guardian - In Queue");


            HandleMatchmaking.startFindLobby();



            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "seed");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    @SpireInsertPatch(
            locator = SecondLocator.class
    )
    public static void leaveMultiQueue(CharacterSelectScreen __instance)
    {
        if (inQueue && (__instance.cancelButton.hb.clicked || InputHelper.pressedEscape))
        {
            leaveQueue();
            __instance.confirmButton.isDisabled = false;
            __instance.confirmButton.show();
        }
    }

    private static class SecondLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "pressedEscape");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    public static void leaveQueue()
    {
        InputHelper.pressedEscape = false;
        CardCrawlGame.mainMenuScreen.charSelectScreen.cancelButton.hb.clicked = false;
        CardCrawlGame.mainMenuScreen.charSelectScreen.cancelButton.show(CharacterSelectScreen.TEXT[5]);
        inQueue = false;
        HandleMatchmaking.stop();
        CardCrawlGame.publisherIntegration.setRichPresenceDisplayInMenu();
        if (chat != null)
        {
            chat.receiveMessage("Left queue.");
        }
    }
}
