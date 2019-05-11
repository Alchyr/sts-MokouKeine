package もこけね.patch.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;
import もこけね.patch.lobby.HandleMatchmaking;
import もこけね.util.MultiplayerHelper;
import もこけね.util.SmartTextHelper;
import もこけね.util.TextureLoader;

import java.util.ArrayList;

import static もこけね.patch.lobby.HandleMatchmaking.isHost;
import static もこけね.もこけねは神の国.*;
import static もこけね.もこけねは神の国.chat;

public class GenericEventVoting {
    private static final Texture mokouSelect = TextureLoader.getTexture(assetPath("img/ui/mokouSelect.png"));
    private static final Texture keineSelect = TextureLoader.getTexture(assetPath("img/ui/keineSelect.png"));

    public static int otherPlayerSelected = -1;
    private static LargeDialogOptionButton otherSelected = null;
    public static boolean choseOption = false;
    private static LargeDialogOptionButton mySelected = null;

    private static boolean voteComplete = false;

    private static ArrayList<LargeDialogOptionButton> getOptionList()
    {
        return AbstractDungeon.getCurrRoom().event.imageEventText.optionList;
    }

    public static void receiveVote(int index)
    {
        if (eventTest() && index >= 0 && index < getOptionList().size())
        {
            otherPlayerSelected = index;
            otherSelected = getOptionList().get(index);

            if (isHost && choseOption)
            {
                handleSelection();
            }
        }
        else
        {
            logger.error("Received invalid event selection index! !Possible! !desync! !alert!");
        }
    }

    public static void reset()
    {
        choseOption = false;
        otherPlayerSelected = -1;
        otherSelected = null;
        mySelected = null;
        voteComplete = false;
    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "clear"
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "show",
            paramtypez = {}
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "clearRemainingOptions"
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "clearAllDialogs"
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "removeDialogOption"
    )
    public static class ResetOnModify
    {
        @SpirePrefixPatch
        public static void onChange(GenericEventDialog __instance)
        {
            reset();
        }
    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "setDialogOption",
            paramtypez = { String.class }
    )
    public static class ResetOnModifySingleParam
    {
        @SpirePrefixPatch
        public static void onChange(GenericEventDialog __instance, String a)
        {
            reset();
        }
    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "show",
            paramtypez = { String.class, String.class }
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "updateDialogOption",
            paramtypez = { int.class, String.class }
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "setDialogOption",
            paramtypez = { String.class, AbstractCard.class}
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "setDialogOption",
            paramtypez = { String.class, boolean.class}
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "updateDialogOption",
            paramtypez = { int.class, String.class, boolean.class }
    )
    public static class ResetOnModifyTwoParam
    {
        @SpirePrefixPatch
        public static void onChange(GenericEventDialog __instance, Object a, Object b)
        {
            reset();
        }
    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "updateDialogOption",
            paramtypez = { int.class, String.class, boolean.class }
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "updateDialogOption",
            paramtypez = { int.class, String.class, AbstractCard.class }
    )
    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "setDialogOption",
            paramtypez = { String.class, boolean.class, AbstractCard.class}
    )
    public static class ResetOnModifyThreeParam
    {
        @SpirePrefixPatch
        public static void onChange(GenericEventDialog __instance, Object a, Object b, Object c)
        {
            reset();
        }
    }

    @SpirePatch(
            clz = GenericEventDialog.class,
            method = "update"
    )
    public static class OnChooseOption
    {
        @SpirePostfixPatch
        public static void changeSelectionToVote(GenericEventDialog __instance)
        {
            if (AbstractDungeon.player instanceof MokouKeine && MultiplayerHelper.active)
            {
                if (!GenericEventDialog.waitForInput && !voteComplete) //this means an option has been chosen
                {
                    MultiplayerHelper.sendP2PString("generic_option" + GenericEventDialog.selectedOption);
                    if (!choseOption || (mySelected != null && !mySelected.equals(getOptionList().get(GenericEventDialog.selectedOption))))
                        HandleMatchmaking.sendMessage(CardCrawlGame.playerName + " chose \"" + SmartTextHelper.clearSmartText(getOptionList().get(GenericEventDialog.selectedOption).msg) + "\"");
                    choseOption = true;
                    mySelected = getOptionList().get(GenericEventDialog.selectedOption);
                    GenericEventDialog.waitForInput = true; //prevent option input

                    if (otherPlayerSelected >= 0 && isHost)
                    {
                        handleSelection();
                    }
                }
                else if (voteComplete)
                {
                    reset();
                }
            }
        }
    }


    @SpirePatch(
            clz = LargeDialogOptionButton.class,
            method = "render"
    )
    public static class RenderSelectedOptions
    {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "scale", "x", "y" }
        )
        public static void onRender(LargeDialogOptionButton __instance, SpriteBatch sb, float scale, float x, float y)
        {
            if (AbstractDungeon.player instanceof MokouKeine) {
                //This should be safe even if they're null.
                if (__instance.equals(mySelected)) {
                    if (((MokouKeine) AbstractDungeon.player).isMokou)
                    {
                        sb.setColor(Color.WHITE.cpy());
                        sb.draw(mokouSelect, x - 445.0F, y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, scale, scale, 0.0F, 0, 0, 890, 77, false, false);
                    }
                    else
                    {
                        sb.setColor(Color.WHITE.cpy());
                        sb.draw(keineSelect, x - 445.0F, y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, scale, scale, 0.0F, 0, 0, 890, 77, false, false);
                    }
                }
                if (__instance.equals(otherSelected)) {
                    if (((MokouKeine) AbstractDungeon.player).isMokou)
                    {
                        sb.setColor(Color.WHITE.cpy());
                        sb.draw(keineSelect, x - 445.0F, y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, scale, scale, 0.0F, 0, 0, 890, 77, false, false);
                    }
                    else
                    {
                        sb.setColor(Color.WHITE.cpy());
                        sb.draw(mokouSelect, x - 445.0F, y - 38.5F, 445.0F, 38.5F, 890.0F, 77.0F, scale, scale, 0.0F, 0, 0, 890, 77, false, false);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    public static void resolveConflict()
    {
        if (isHost)
        {
            MultiplayerHelper.sendP2PMessage("Resolving...");

            if (MathUtils.randomBoolean() && otherPlayerSelected >= 0)
            {
                selectOption(otherPlayerSelected);
                MultiplayerHelper.sendP2PString("generic_option_choose" + otherPlayerSelected);
            }
            else
            {
                selectOption(GenericEventDialog.selectedOption);
                MultiplayerHelper.sendP2PString("generic_option_choose" + GenericEventDialog.selectedOption);
            }
        }
    }

    private static void handleSelection()
    {
        if (otherPlayerSelected >= 0 && choseOption)
        {
            if (GenericEventDialog.selectedOption != otherPlayerSelected)
            {
                MultiplayerHelper.sendP2PMessage("Players disagree. Conflict will be automatically resolved in " + 10 + " seconds.");
                startEventChooseTimer(10.0f);
            }
            else
            {
                chat.receiveMessage("Option " + GenericEventDialog.selectedOption + ", \"" + SmartTextHelper.clearSmartText(getOptionList().get(GenericEventDialog.selectedOption).msg) + "\" chosen!");
                GenericEventDialog.waitForInput = false;
                stopEventChooseTimer();
                voteComplete = true;
                MultiplayerHelper.sendP2PString("generic_option_choose" + GenericEventDialog.selectedOption);
            }
        }
    }
    public static void selectOption(int index)
    {
        GenericEventDialog.selectedOption = index;
        chat.receiveMessage("Option " + GenericEventDialog.selectedOption + ", \"" + SmartTextHelper.clearSmartText(getOptionList().get(GenericEventDialog.selectedOption).msg) + "\" chosen!");
        GenericEventDialog.waitForInput = false;
        voteComplete = true;
    }



    private static boolean eventTest()
    {
        return (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.EVENT && AbstractDungeon.getCurrRoom().event != null);
    }
}