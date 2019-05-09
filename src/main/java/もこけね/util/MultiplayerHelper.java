package もこけね.util;

import com.codedisaster.steamworks.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.random.Random;
import もこけね.patch.lobby.HandleMatchmaking;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static もこけね.もこけねは神の国.*;

//Handles all multiplayer post-game start. For setting up multiplayer, see HandleMatchmaking.java as well as UseMultiplayerQueue.java
public class MultiplayerHelper implements SteamNetworkingCallback {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    private static final int defaultChannel = 1;

    public static SteamNetworking communication;
    public static MultiplayerHelper callback;
    public static SteamID currentPartner;
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
        try
        {
            if (communication != null && dest != null && dest.isValid())
            {
                packetSendBuffer.clear();

                packetSendBuffer.put(msg.getBytes(CHARSET));

                communication.sendP2PPacket(dest, packetSendBuffer, SteamNetworking.P2PSend.Unreliable, defaultChannel);
                currentPartner = dest;
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }
    public static void sendP2PString(String msg)
    {
        if (currentPartner != null && currentPartner.isValid() && communication != null)
        try
        {
            packetSendBuffer.clear();

            packetSendBuffer.put(msg.getBytes(CHARSET));

            communication.sendP2PPacket(currentPartner, packetSendBuffer, SteamNetworking.P2PSend.Unreliable, defaultChannel);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }
    public static void sendP2PMessage(String msg)
    {
        if (currentPartner != null && currentPartner.isValid() && communication != null)
        {
            try
            {
                if (chat != null)
                {
                    chat.receiveMessage(msg);
                }
                String finalMessage = "chat_message" + msg;
                packetSendBuffer.clear();

                packetSendBuffer.put(finalMessage.getBytes(CHARSET));

                communication.sendP2PPacket(currentPartner, packetSendBuffer, SteamNetworking.P2PSend.Unreliable, defaultChannel);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage());
            }
        }
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

                        processMessage(msg);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    private static void processMessage(String msg)
    {
        if (msg.startsWith("chat_message"))
        {
            msg = msg.substring(12);
            chat.receiveMessage(msg);
        }
        else if (msg.equals("start"))
        {
            beginGameStartTimer();
        }
        else if (msg.equals("start_game"))
        {

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
    }


    @Override
    public void onP2PSessionConnectFail(SteamID steamID, SteamNetworking.P2PSessionError p2pSessionError) {
            logger.error("Failed to connect to lobby partner.");
    }

    @Override
    public void onP2PSessionRequest(SteamID steamID) {
        if (HandleMatchmaking.inLobby(steamID) || steamID.equals(currentPartner))
        {
            currentPartner = steamID;
            communication.acceptP2PSessionWithUser(steamID);
        }
    }
}