package もこけね.patch.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CtBehavior;
import もこけね.character.MokouKeine;
import もこけね.util.MultiplayerHelper;

import static もこけね.patch.lobby.HandleMatchmaking.isHost;
import static もこけね.patch.lobby.HandleMatchmaking.isMokou;
import static もこけね.もこけねは神の国.*;

public class MapRoomVoting {
    private static final float IMG_WIDTH;
    private static final float OFFSET_X;
    private static final float OFFSET_Y;
    private static final float SPACING_X;

    private static boolean resetOnPost = false;

    private static MapRoomNode otherSelected = null;
    private static MapRoomNode mySelected = null;
    private static MapRoomNode chosenNode = null;

    public static void reset()
    {
        otherSelected = null;
        mySelected = null;
        chosenNode = null;
        resetOnPost = false;
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class VoteTarget
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn preventEntry(MapRoomNode __instance)
        {
            if (AbstractDungeon.player instanceof MokouKeine && MultiplayerHelper.active && !__instance.equals(chosenNode))
            {
                __instance.highlighted = true;
                if (chosenNode != null)
                {
                    return SpireReturn.Return(null); //no voting once destination is chosen.
                }

                String indexInfo = getNodeIndex(__instance);

                if (!indexInfo.equals(""))
                {
                    mySelected = __instance;
                    MultiplayerHelper.sendP2PString("vote_node" + indexInfo);

                    chat.receiveMessage("Voted for " + indexInfo);

                    if (isHost && otherSelected != null)
                    {
                        handleSelection();
                    }

                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
        @SpireInsertPatch(
                locator = SecondLocator.class
        )
        public static SpireReturn preventFirstFloorEntry(MapRoomNode __instance)
        {
            if (AbstractDungeon.player instanceof MokouKeine && MultiplayerHelper.active && !__instance.equals(chosenNode))
            {
                __instance.highlighted = true;
                if (chosenNode != null)
                {
                    return SpireReturn.Return(null); //no voting once destination is chosen.
                }

                String indexInfo = getNodeIndex(__instance);

                if (!indexInfo.equals(""))
                {
                    mySelected = __instance;
                    MultiplayerHelper.sendP2PString("vote_node" + indexInfo);

                    chat.receiveMessage("Voted for " + indexInfo);

                    if (isHost && otherSelected != null)
                    {
                        handleSelection();
                    }

                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }

        @SpireInsertPatch(
                locator = ChosenLocator.class
        )
        public static void enableChosen(MapRoomNode __instance)
        {
            if (__instance.equals(chosenNode))
            {
                __instance.hb.hovered = true;
                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP)
                    AbstractDungeon.dungeonMapScreen.open(false);
                AbstractDungeon.dungeonMapScreen.clicked = true;

                resetOnPost = true;
            }
        }

        @SpirePostfixPatch
        public static void resetAfter(MapRoomNode __instance)
        {
            if (resetOnPost)
                reset();
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
        private static class SecondLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(DungeonMapScreen.class, "dismissable");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
        private static class ChosenLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "render"
    )
    public static class ShowVotes
    {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {
                        "scale",
                        "angle"
                }
        )
        public static void renderCircles(MapRoomNode __instance, SpriteBatch sb, float scale, float angle)
        {
            if (__instance.equals(otherSelected))
            {
                sb.setColor(isMokou ? KEINE_COLOR : MOKOU_COLOR);
                sb.draw(ImageMaster.MAP_CIRCLE_1, (float)__instance.x * SPACING_X + OFFSET_X - 96.0F + __instance.offsetX, (float)__instance.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 96.0F + __instance.offsetY, 96.0F, 96.0F, 192.0F, 192.0F, (scale * 0.95F + 0.2F) * Settings.scale, (scale * 0.95F + 0.2F) * Settings.scale, angle, 0, 0, 192, 192, false, false);
            }
            else if (__instance.equals(mySelected))
            {
                sb.setColor(isMokou ? MOKOU_COLOR : KEINE_COLOR);
                sb.draw(ImageMaster.MAP_CIRCLE_1, (float)__instance.x * SPACING_X + OFFSET_X - 96.0F + __instance.offsetX, (float)__instance.y * Settings.MAP_DST_Y + OFFSET_Y + DungeonMapScreen.offsetY - 96.0F + __instance.offsetY, 96.0F, 96.0F, 192.0F, 192.0F, (scale * 0.95F + 0.2F) * Settings.scale, (scale * 0.95F + 0.2F) * Settings.scale, angle, 0, 0, 192, 192, false, false);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "getMapImg");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static void receiveVote(String[] params)
    {
        if (params.length == 2)
        {
            int a = Integer.valueOf(params[0]);
            int b = Integer.valueOf(params[1]);

            otherSelected = AbstractDungeon.map.get(a).get(b);

            chat.receiveMessage("Other player voted for " + a + " " + b);

            if (isHost && mySelected != null)
            {
                handleSelection();
            }
        }
    }
    public static void setNode(String[] params)
    {
        if (params.length == 2)
        {
            int a = Integer.valueOf(params[0]);
            int b = Integer.valueOf(params[1]);

            chosenNode = AbstractDungeon.map.get(a).get(b);
            chat.receiveMessage("Destination chosen!");
        }
    }

    public static void resolveConflict()
    {
        if (isHost)
        {
            MultiplayerHelper.sendP2PMessage("Resolving...");

            if (MathUtils.randomBoolean() && otherSelected != null)
            {
                chosenNode = otherSelected;
            }
            else
            {
                chosenNode = mySelected;
            }
            MultiplayerHelper.sendP2PString("choose_node" + getNodeIndex(chosenNode));
            chat.receiveMessage("Destination chosen!");
        }
    }

    private static void handleSelection()
    {
        if (otherSelected != null && mySelected != null)
        {
            if (otherSelected.equals(mySelected)) {
                stopMapChooseTimer();
                chosenNode = mySelected;
                MultiplayerHelper.sendP2PString("choose_node" + getNodeIndex(chosenNode));
                chat.receiveMessage("Destination chosen!");
            }
            else {
                MultiplayerHelper.sendP2PMessage("Players disagree. Conflict will be automatically resolved in " + 15 + " seconds.");
                startMapChooseTimer(15);
            }
        }
    }

    private static String getNodeIndex(MapRoomNode node)
    {
        for (int a = 0; a < AbstractDungeon.map.size(); ++a)
        {
            for (int b = 0; b < AbstractDungeon.map.get(0).size(); ++b)
            {
                if (node.equals(AbstractDungeon.map.get(a).get(b)))
                {
                    return a + " " + b;
                }
            }
        }
        return "";
    }


    static {
        IMG_WIDTH = (int) (Settings.scale * 64.0F);
        OFFSET_X = 560.0F * Settings.scale;
        OFFSET_Y = 180.0F * Settings.scale;
        SPACING_X = (float) IMG_WIDTH * 2.0F;
    }
}
