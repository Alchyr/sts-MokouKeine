package もこけね.patch.lobby;

import basemod.BaseMod;
import com.codedisaster.steamworks.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import もこけね.util.MultiplayerHelper;
import もこけね.util.SteamUserCallbacks;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static もこけね.util.MultiplayerHelper.CHARSET;
import static もこけね.もこけねは神の国.*;

public class HandleMatchmaking implements SteamMatchmakingCallback {
    private final ByteBuffer chatMessage = ByteBuffer.allocateDirect(4096);

    private static final int SEARCH_CAP = 7;

    private static final String lobbyModsKey = "mod_list";
    private static final String lobbyCharacterKey = "character";
    private static final String lobbyPublicKey = "is_public";
    private static final String metadataTrue = "true";
    private static final String metadataFalse = "false";

    public static SteamMatchmaking matchmaking;
    public static HandleMatchmaking handler;
    public static SteamUser currentUser;
    public static SteamID hostID;

    private static boolean searching;
    private static boolean joinorcreate;

    private static int searchAmt;

    public static boolean activeMultiplayer;
    private static SteamID currentLobbyID;

    public boolean isHost;

    public static void init()
    {
        dispose();

        handler = new HandleMatchmaking();

        matchmaking = new SteamMatchmaking(handler);
        currentUser = new SteamUser(new SteamUserCallbacks());

        searching = false;
        activeMultiplayer = false;
    }

    private void startNormalSearch()
    {
        matchmaking.addRequestLobbyListDistanceFilter(SteamMatchmaking.LobbyDistanceFilter.Far);
        matchmaking.addRequestLobbyListStringFilter(lobbyModsKey, generateModList(), SteamMatchmaking.LobbyComparison.Equal);
        matchmaking.addRequestLobbyListStringFilter(lobbyCharacterKey, CardCrawlGame.chosenCharacter.name(), SteamMatchmaking.LobbyComparison.Equal);
        matchmaking.addRequestLobbyListStringFilter(lobbyPublicKey, metadataTrue, SteamMatchmaking.LobbyComparison.Equal);
        SteamAPICall lobbySearch = matchmaking.requestLobbyList();
    }

    public static void startFindLobby()
    {
        if (matchmaking != null && !searching)
        {
            logger.info("Lobby filter: Only lobbies with this exact mod list: " + generateModList());
            logger.info("Only lobbies with this character: " + CardCrawlGame.chosenCharacter.name());
            handler.startNormalSearch();
            searchAmt = 0;
            searching = true;
            joinorcreate = false;
        }
        else
        {
            logger.error("ERROR: Attempting to find Steam Lobby while SteamMatchmaking has not been initialized.");
        }
    }

    public static void stop()
    {
        logger.info("Leaving queue.");
        if (searching)
        {
            searching = false;
        }
        if (joinorcreate)
        {
            joinorcreate = false;
        }
        activeMultiplayer = false;
        if (currentLobbyID != null)
        {
            logger.info("Left lobby " + currentLobbyID + ".");
            matchmaking.leaveLobby(currentLobbyID);
            currentLobbyID = null;
        }
    }
    public static void leave()
    {
        if (currentLobbyID != null)
        {
            logger.info("Left lobby " + currentLobbyID + ".");
            matchmaking.leaveLobby(currentLobbyID);
            currentLobbyID = null;
        }
    }

    public static void dispose()
    {
        if (matchmaking != null)
        {
            matchmaking.dispose();
            matchmaking = null;
        }
        if (currentUser != null)
        {
            currentUser.dispose();
            currentUser = null;
        }
    }

    public static boolean inLobby(SteamID member)
    {
        if (currentLobbyID != null && currentLobbyID.isValid())
        {
            int max = matchmaking.getNumLobbyMembers(currentLobbyID);
            for (int i = 0; i < max; ++i)
            {
                if (member.equals(matchmaking.getLobbyMemberByIndex(currentLobbyID, i)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onFavoritesListChanged(int i, int i1, int i2, int i3, int i4, boolean b, int i5) {

    }

    @Override
    public void onLobbyInvite(SteamID steamID, SteamID steamID1, long l) {

    }

    @Override
    public void onLobbyEnter(SteamID steamIDLobby, int chatPermissions, boolean blocked, SteamMatchmaking.ChatRoomEnterResponse response) {
        if (joinorcreate)
        {
            chat.receiveMessage("Entered lobby: " + steamIDLobby);
            logger.info("Lobby entered: " + steamIDLobby);
            logger.info("  - response: " + response);

            int numMembers = matchmaking.getNumLobbyMembers(steamIDLobby);
            logger.info("  - " + numMembers + " members in lobby");
            for (int i = 0; i < numMembers; i++) {
                SteamID member = matchmaking.getLobbyMemberByIndex(steamIDLobby, i);
                logger.info("    - " + i + ": accountID=" + member.getAccountID());
            }

            currentLobbyID = steamIDLobby;
            joinorcreate = false;
            activeMultiplayer = true;

            hostID = matchmaking.getLobbyOwner(steamIDLobby);

            if (numMembers == 2)
            {
                //start game
                //MultiplayerHelper.sendP2PString(hostID, "start");
                BaseMod.setRichPresence("Playing as the Immortal and Guardian - Starting game...");
            }
            else
            {
                BaseMod.setRichPresence("Playing as the Immortal and Guardian - In Lobby (" + numMembers + " / 2)");
            }
        }
        else
        {
            logger.info("Lobby entered. As queue has been exited, immediately leaving.");
            matchmaking.leaveLobby(steamIDLobby);
        }
    }

    @Override
    public void onLobbyDataUpdate(SteamID lobbyID, SteamID steamIDMember, boolean success) {
        logger.info("Lobby data update for " + lobbyID);
        logger.info("  - member: " + steamIDMember.getAccountID());
        logger.info("  - success: " + success);
    }

    @Override
    public void onLobbyChatUpdate(SteamID lobbyID, SteamID IDChanged, SteamID IDSource, SteamMatchmaking.ChatMemberStateChange chatMemberStateChange) {
        logger.info("Lobby chat update for " + lobbyID);
        logger.info("  - user changed: " + IDChanged.getAccountID());
        logger.info("  - made by user: " + IDSource.getAccountID());
        logger.info("  - state changed: " + chatMemberStateChange.name());

        if (matchmaking.getNumLobbyMembers(lobbyID) == 2)
        {
            logger.info("Lobby is full!");
            if (isHost)
            {
                logger.info("This is host:");
                logger.info("  - Establishing P2P connection with " + IDChanged.getAccountID());
                MultiplayerHelper.sendP2PString(IDChanged, "connect");
            }
        }
        else if (matchmaking.getNumLobbyMembers(lobbyID) != 2)
        {
            logger.info("Lobby does not have number of members needed to start.");
            if (isHost)
            {
                stopGameStart();
            }
            MultiplayerHelper.currentPartner = null;
        }
    }

    @Override
    public void onLobbyChatMessage(SteamID lobbyID, SteamID userID, SteamMatchmaking.ChatEntryType chatEntryType, int msgIndex) {
        logger.info("Lobby chat message for " + lobbyID);
        logger.info("  - from user: " + userID.getAccountID());
        logger.info("  - chat entry type: " + chatEntryType);
        logger.info("  - chat id: #" + msgIndex);
        
        try {
            matchmaking.getLobbyChatEntry(lobbyID, msgIndex, new SteamMatchmaking.ChatEntry(), chatMessage);

            /*byte[] bytes = new byte[size];//chatMessage.remaining()];
            chatMessage.get(bytes);
            chatMessage.clear();*/

            chat.receiveMessage(CHARSET.decode(chatMessage).toString());
            chatMessage.clear();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onLobbyGameCreated(SteamID steamID, SteamID steamID1, int i, short i1) {

    }

    public static void sendMessage(String msg)
    {
        if (activeMultiplayer && currentLobbyID != null && currentLobbyID.isValid())
        {
            logger.info("Sending message: " + msg);
            if (matchmaking.sendLobbyChatMsg(currentLobbyID, msg))
            {
                logger.info("Message sent successfully.");
            }
            else
            {
                logger.error("Message failed to send.");
            }
        }
        else if (MultiplayerHelper.currentPartner != null)
        {
            MultiplayerHelper.sendP2PMessage(msg);
        }
        else
        {
            logger.info("No current lobby or p2p connection to send message to!");
        }
    }

    @Override
    public void onLobbyMatchList(int resultCount) {
        if (searching)
        {
            logger.info("Found " + resultCount + " matching lobbies.");
            chat.receiveMessage("Found " + resultCount + " valid lobbies.");

            if (resultCount == 0)
            {
                searching = false;
                joinorcreate = true;
                logger.info("Attempting to create a new lobby.");
                logger.info("Creating public lobby for 2 players.");
                chat.receiveMessage("Creating new lobby.");
                matchmaking.createLobby(SteamMatchmaking.LobbyType.Public,2);
            }
            else if (resultCount > 0)
            {
                ArrayList<SteamID> lobbies = new ArrayList<>();
                for (int i = 0; i < resultCount; i++) {
                    SteamID lobby = matchmaking.getLobbyByIndex(i);
                    lobbies.add(lobby);
                }
                listLobbies(lobbies);


                int lastJoinAttempt = 0;

                logger.info("Joining the first lobby.");
                while (lastJoinAttempt < lobbies.size() && !lobbies.get(lastJoinAttempt).isValid())
                {
                    logger.info("Lobby " + (++lastJoinAttempt) + " is invalid, testing next lobby.");
                }

                if (lastJoinAttempt >= lobbies.size())
                {
                    if (searchAmt < SEARCH_CAP)
                    {
                        logger.info("All lobbies are invalid. Searching again.");

                        handler.startNormalSearch();
                        searchAmt++;
                        searching = true;
                    }
                    else
                    {
                        logger.info("Failed to join too many times. Creating new lobby.");

                        searchAmt = 0;
                        searching = false;
                        logger.info("Attempting to create a new lobby.");
                        logger.info("Creating public lobby for 2 players.");
                        matchmaking.createLobby(SteamMatchmaking.LobbyType.Public,2);
                        joinorcreate = true;
                    }
                }
                else
                {
                    logger.info("Joining...");
                    matchmaking.joinLobby(lobbies.get(lastJoinAttempt));
                    searching = false;
                    joinorcreate = true;
                }
            }
        }
    }

    @Override
    public void onLobbyKicked(SteamID lobbyID, SteamID user, boolean dc) {
        System.out.println("Kicked from lobby: " + lobbyID);
        System.out.println("  - by user: " + user.getAccountID());
        System.out.println("  - kicked due to disconnect: " + (dc ? "yes" : "no"));

        activeMultiplayer = false;
    }

    @Override
    public void onLobbyCreated(SteamResult result, SteamID steamIDLobby) {
        if (joinorcreate)
        {
            logger.info("Lobby created: " + steamIDLobby);
            logger.info("  - result: " + result.name());
            if (result == SteamResult.OK) {
                String modList = generateModList();
                logger.info("  - lobby modlist: " + modList);
                matchmaking.setLobbyData(steamIDLobby, lobbyModsKey, modList);
                matchmaking.setLobbyData(steamIDLobby, lobbyCharacterKey, CardCrawlGame.chosenCharacter.name());
                matchmaking.setLobbyData(steamIDLobby, lobbyPublicKey, metadataTrue);

                this.isHost = true;

                currentLobbyID = steamIDLobby;
                joinorcreate = false;
                activeMultiplayer = true;
                //matchmaking.joinLobby(steamIDLobby);
                BaseMod.setRichPresence("Playing as the Immortal and Guardian - In Lobby (1 / 2)");
                logger.info(currentUser.getSteamID());
            }
        }
        else
        {
            if (result == SteamResult.OK)
            {
                logger.info("Lobby created successfully. However, queue has been exited, so immediately leaving lobby.");
                matchmaking.leaveLobby(steamIDLobby);
            }
        }
    }

    @Override
    public void onFavoritesListAccountsUpdated(SteamResult steamResult) {

    }



    private void listLobbies(ArrayList<SteamID> lobbies) {
        int index = 1;
        for (SteamID lobby : lobbies) {
            logger.info("   Match " + index++ + ":");
            if (lobby.isValid()) {
                int members = matchmaking.getNumLobbyMembers(lobby);
                logger.info(members + " members");
            } else {
                logger.error("invalid SteamID!");
            }
        }
    }

    public static String generateModList()
    {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> modData = new ArrayList<>();
        for (ModInfo m : Loader.MODINFOS)
        {
            modData.add(m.ID + ":" + m.ModVersion.toString());
        }
        modData.sort(String::compareTo);
        for (String s : modData)
        {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }
}
