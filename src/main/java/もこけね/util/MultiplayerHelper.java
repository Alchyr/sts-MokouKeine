package もこけね.util;

import com.codedisaster.steamworks.*;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.actions.character.WaitForSignalAction;
import もこけね.character.MokouKeine;
import もこけね.patch.combat.RequireDoubleEndTurn;
import もこけね.patch.deck_changes.ReportObtainCard;
import もこけね.patch.events.GenericEventVoting;
import もこけね.patch.events.RoomEventVoting;
import もこけね.patch.lobby.HandleMatchmaking;
import もこけね.patch.map.MapRoomVoting;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static もこけね.patch.combat.PotionUse.*;
import static もこけね.patch.rewards.ObtainPotions.*;
import static もこけね.もこけねは神の国.*;

//Handles all multiplayer post-game start. For setting up multiplayer, see HandleMatchmaking.java as well as UseMultiplayerQueue.java
public class MultiplayerHelper implements SteamNetworkingCallback {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    private static final int defaultChannel = 1;

    public static SteamNetworking communication;
    public static MultiplayerHelper callback;
    public static SteamID currentPartner;

    public static boolean active;
    //When second player joins, send signal to start game start timer
    //Upon game start, host then sends necessary information (seed, which player is which character) to second player.

    private static ByteBuffer packetSendBuffer = ByteBuffer.allocateDirect(4096);
    private static ByteBuffer packetReceiveBuffer = ByteBuffer.allocateDirect(4096);


    public static void init()
    {
        dispose();

        callback = new MultiplayerHelper();
        communication = new SteamNetworking(callback, SteamNetworking.API.Client);
    }


    public static void dispose()
    {
        if (communication != null)
        {
            communication.dispose();
        }
        callback = null;
    }

    public static void sendP2PString(SteamID dest, String msg)
    {
        if (communication != null && dest != null && dest.isValid())
        {
        try
        {
            packetSendBuffer.clear();

            packetSendBuffer.put(msg.getBytes(CHARSET));

            packetSendBuffer.flip();

            logger.info("Sending P2P message: " + msg);
            communication.sendP2PPacket(dest, packetSendBuffer, SteamNetworking.P2PSend.Reliable, defaultChannel);
            currentPartner = dest;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
        }
    }
    public static void sendP2PString(String msg)
    {
        sendP2PString(currentPartner, msg);
    }
    public static void sendP2PMessage(String msg)
    {
        if (chat != null)
        {
            chat.receiveMessage(msg);
        }
        String finalMessage = "chat_message" + msg;

        sendP2PString(currentPartner, finalMessage);
    }

    public static void readPostUpdate()
    {
        try
        {
            if (communication != null)
            {
                if (communication.isP2PPacketAvailable(defaultChannel) > 0)
                {
                    packetReceiveBuffer.clear();

                    SteamID sender = new SteamID();

                    if (communication.readP2PPacket(sender, packetReceiveBuffer, defaultChannel) > 0)
                    {
                        int received = packetReceiveBuffer.limit();
                        logger.info("Received " + received + " bytes from " + sender.getAccountID());

                        String msg = CHARSET.decode(packetReceiveBuffer).toString();
                        logger.info("Message: " + msg);

                        processMessage(sender, msg);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    private static void processMessage(SteamID sender, String msg)
    {
        //Note to future self: Use 3 character code as first three characters of message so that you can just use a switch.
        if (msg.startsWith("chat_message"))
        {
            msg = msg.substring(12);
            chat.receiveMessage(msg);
        }
        else if (msg.startsWith("other_play_card")) //Host played a card.
        {
            tryOtherPlayCard(msg.substring(15));
        }
        else if (msg.startsWith("try_play_card")) //Player that isn't host played a card.
        {
            String args = msg.substring(13);
            tryOtherPlayCard(args);
            //Send confirmation to play the card.
            sendP2PString("confirm_play_card" + args);
        }
        else if (msg.startsWith("confirm_play_card"))
        {
            tryPlayCard(msg.substring(17));
        }
        else if (msg.startsWith("use_potion")) //Host used a potion.
        {
            usePotion(msg.substring(10));
        }
        else if (msg.startsWith("try_use_potion"))
        {
            String info = msg.substring(14);
            if (otherCanUsePotion(info))
            {
                usePotion(info);
                sendP2PString("confirm_use_potion" + info);
            }
        }
        else if (msg.startsWith("confirm_use_potion"))
        {
            confirmUsePotion(msg.substring(18));
        }
        else if (msg.startsWith("update_potions"))
        {
            receivePotionUpdate(msg.substring(14));
        }
        else if (msg.startsWith("signal"))
        {
            WaitForSignalAction.signal += 1;
            processMessage(sender, msg.substring(6));
        }
        else if (msg.equals("end_turn"))
        {
            RequireDoubleEndTurn.otherPlayerEndTurn();
        }
        else if (msg.equals("full_end_turn"))
        {
            RequireDoubleEndTurn.fullEndTurn();
        }
        else if (msg.startsWith("exhaust"))
        {
            String[] args = msg.substring(7).split(" ");
            if (args.length == 2)
            {
                int index = Integer.valueOf(args[1]);
                if (index >= 0)
                {
                    switch (args[0])
                    {
                        case "discard":
                            if (index <= AbstractDungeon.player.discardPile.group.size())
                            {
                                AbstractCard toExhaust = AbstractDungeon.player.discardPile.group.get(index);
                                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(toExhaust, AbstractDungeon.player.discardPile, true));
                            }
                            break;
                        case "other_discard":
                            if (AbstractDungeon.player instanceof MokouKeine && index <= ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard.group.size())
                            {
                                AbstractCard toExhaust = ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard.group.get(index);
                                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(toExhaust, ((MokouKeine) AbstractDungeon.player).otherPlayerDiscard, true));
                            }
                            break;
                    }
                }
            }
        }
        else if (msg.startsWith("other_obtain_card"))
        {
            ReportObtainCard.receiveOtherObtainCard(msg.substring(17));
        }
        else if (msg.startsWith("room_option_choose"))
        {
            RoomEventVoting.selectOption(Integer.valueOf(msg.substring(18)));
        }
        else if (msg.startsWith("room_option"))
        {
            RoomEventVoting.receiveVote(Integer.valueOf(msg.substring(11)));
        }
        else if (msg.startsWith("generic_option_choose"))
        {
            GenericEventVoting.selectOption(Integer.valueOf(msg.substring(21)));
        }
        else if (msg.startsWith("generic_option"))
        {
            GenericEventVoting.receiveVote(Integer.valueOf(msg.substring(14)));
        }
        else if (msg.startsWith("vote_node"))
        {
            String[] params = msg.substring(9).split(" ");

            if (params.length == 2)
            {
                MapRoomVoting.receiveVote(params);
            }
            else
            {
                logger.error("ERROR: Invalid map node vote.");
            }
        }
        else if (msg.startsWith("choose_node"))
        {
            String[] params = msg.substring(11).split(" ");

            if (params.length == 2)
            {
                MapRoomVoting.setNode(params);
            }
            else
            {
                logger.error("ERROR: Invalid map node vote.");
            }
        }
        else if (msg.startsWith("claim_potion"))
        {
            removePotionReward(msg.substring(12));
        }
        else if (msg.startsWith("try_claim_potion"))
        {
            String id = msg.substring(16);
            if (removePotionReward(id))
            {
                sendP2PString("confirm_claim_potion" + id);
            }
            else
            {
                sendP2PString("confirm_lose_potion" + id);
            }
        }
        else if (msg.startsWith("confirm_claim_potion"))
        {
            confirmClaimPotion(msg.substring(20));
        }
        else if (msg.startsWith("confirm_lose_potion"))
        {
            confirmLosePotion(msg.substring(19));
        }
        else if (msg.equals("start_game"))
        {
            //Host has send start game message, and both parties are properly connected.
            sendP2PString("leave");
            startSetupGame();
        }
        else if (msg.equals("leave"))
        {
            HandleMatchmaking.leave();
        }
        else if (msg.equals("stop"))
        {
            active = false;
            stopGameStart();
            chat.receiveMessage("Other player left.");
            logger.info("Other player left.");
            currentPartner = null;
        }
        else if (msg.startsWith("trial"))
        {
            msg = msg.substring(5);
            Settings.specialSeed = Long.valueOf(msg);
            Settings.isTrial = true;
        }
        else if (msg.startsWith("ascension"))
        {
            int level = Integer.valueOf(msg.substring(9));
            if (level != 0)
            {
                AbstractDungeon.isAscensionMode = true;
            }
            AbstractDungeon.ascensionLevel = level;
        }
        else if (msg.startsWith("seedset"))
        {
            msg = msg.substring(7);
            Settings.seed = Long.valueOf(msg);
            Settings.seedSet = true;
        }
        else if (msg.startsWith("seed"))
        {
            msg = msg.substring(4);
            Settings.seed = Long.valueOf(msg);
        }
        else if (msg.equals("success"))
        {
            active = true;
            currentPartner = sender;
            if (HandleMatchmaking.isHost)
            {
                chat.receiveMessage("Another player has joined the lobby.");
                logger.info("Connection established.");
                HandleMatchmaking.leave();
                sendP2PString("leave");
                beginGameStartTimer();
            }
            else
            {
                sendP2PString("success");
            }
        }
        else if (msg.equals("connect"))
        {
            active = true;
            currentPartner = sender;
            sendP2PString("success");
        }
    }


    @Override
    public void onP2PSessionConnectFail(SteamID steamID, SteamNetworking.P2PSessionError p2pSessionError) {
        logger.error("Failed to connect to lobby partner.");
        logger.error(p2pSessionError);
        //currentPartner = null;
        sendP2PString(steamID, "connect");
    }

    @Override
    public void onP2PSessionRequest(SteamID steamID) {
        logger.info("Received session request from " + steamID.getAccountID());
        if (HandleMatchmaking.inLobby(steamID) || steamID.equals(currentPartner))
        {
            logger.info("Accepted session request.");
            currentPartner = steamID;
            communication.acceptP2PSessionWithUser(steamID);
        }
    }



    private static void tryOtherPlayCard(String args)
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            MokouKeine p = (MokouKeine)AbstractDungeon.player;
            String[] params = args.split(" ");

            if (params.length == 4)
            {
                logger.info("Other player played a card.");
                int cardIndex = Integer.valueOf(params[0]);
                int targetIndex = Integer.valueOf(params[1]);
                float x = Float.valueOf(params[2]);
                float y = Float.valueOf(params[3]);

                if (cardIndex >= 0 && cardIndex < p.otherPlayerHand.size())
                {
                    AbstractCard toPlay = p.otherPlayerHand.group.get(cardIndex);
                    p.otherPlayerHand.removeCard(toPlay);

                    AbstractMonster target = null;
                    if (targetIndex >= 0 && targetIndex < AbstractDungeon.getMonsters().monsters.size()) {
                        target = AbstractDungeon.getMonsters().monsters.get(targetIndex);
                        if (target != null) {
                            toPlay.calculateCardDamage(target);
                        }
                    }

                    //AbstractDungeon.player.limbo.addToBottom(toPlay);
                    toPlay.target_x = toPlay.current_x = x;
                    toPlay.target_y = toPlay.current_y = y;

                    AbstractDungeon.actionManager.cardQueue.add(new OtherPlayerCardQueueItem(toPlay, target));
                }
                else
                {
                    logger.error("ERROR: Attempted to play card with invalid index.");
                }
            }
        }
        else
        {
            logger.info("ERROR: Received invalid attempt to play a card.");
        }
    }

    private static void tryPlayCard(String args)
    {
        String[] params = args.split(" ");

        if (params.length == 4)
        {
            logger.info("Received confirmation to play a card.");
            int cardIndex = Integer.valueOf(params[0]);
            int targetIndex = Integer.valueOf(params[1]);
            float x = Float.valueOf(params[2]);
            float y = Float.valueOf(params[3]);

            if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.hand.size())
            {
                AbstractCard toPlay = AbstractDungeon.player.hand.group.get(cardIndex);

                AbstractMonster target = null;
                if (targetIndex >= 0 && targetIndex < AbstractDungeon.getMonsters().monsters.size()) {
                    target = AbstractDungeon.getMonsters().monsters.get(targetIndex);
                    if (target != null) {
                        toPlay.calculateCardDamage(target);
                    }
                }

                //AbstractDungeon.player.limbo.addToBottom(toPlay);
                toPlay.target_x = toPlay.current_x = x;
                toPlay.target_y = toPlay.current_y = y;

                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(toPlay, target));
            }
            else
            {
                logger.error("ERROR: Attempted to play card with invalid index.");
            }
        }
    }
}