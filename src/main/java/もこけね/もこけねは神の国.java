package もこけね;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.helpers.TrialHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.*;
import もこけね.character.MokouKeine;
import もこけね.patch.card_use.DiscardToCorrectPile;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.combat.MixEnemyTempCards;
import もこけね.patch.combat.PotionUse;
import もこけね.patch.combat.RequireDoubleEndTurn;
import もこけね.patch.energy_division.TrackCardSource;
import もこけね.patch.enums.CharacterEnums;
import もこけね.patch.events.GenericEventVoting;
import もこけね.patch.events.RoomEventVoting;
import もこけね.ui.LobbyMenu;
import もこけね.util.HandleMatchmaking;
import もこけね.patch.map.BossRoomVoting;
import もこけね.patch.map.MapRoomVoting;
import もこけね.patch.relics.VoteBossRelic;
import もこけね.ui.ChatBox;
import もこけね.util.CardFilter;
import もこけね.util.KeywordWithProper;
import もこけね.util.MultiplayerHelper;
import もこけね.util.TextureLoader;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/*TODO LIST:
+Upon entering next act and generating map, if other player has already voted, re-check voted node so that it is rendered/works properly.

EVENTS:
Living Wall - Augumenter - Transmogrifier - Does transform work correctly?
Ancient Writing - Upgrading does not upgrade other deck
Council of Ghosts - Doubled apparitions is kind of op... Maybe remove from pool
+Removal of bottles - Unbottle other player's bottled card?
Vampires - Probably strike removal won't work, since pandora's doesn't.
Falling - Should be fine, but check.
Mind bloom -
    I Am Awake - Upgrades do not sync
    Other options should be fine.
A Note For Yourself - Remove from pool
Bonfire Spirits - report hp changes
Designer In-Spire - ensure transform/upgrades are reported properly
The Divine Fountain - Make sure it works
We Meet Again/Ranwid - Gold option: If other player cannot afford it, cannot take
                        Potion option: Also can take a potion in list used to track other player potions
                        Card option: Fine as is

Powers:
+Hex - Sync generation based on who played card
Automaton orbs - Steal from both decks? Return to appropriate deck when death?

Relics:
Pandora's Box - Card additions are synced, but card removals are not
+Bottle Relics - canSpawn will desync if one player can bottle but other cannot
Ninja Scroll? Silent only, so it should be fine, but... I don't trust things.
+Mummified hand
Test how sundial interacts
Dead Branch - Ensure card goes to the correct hand
+Du-vu doll - Count curses in both decks
Gambling Chip - test - should be functional.
+Girya - Patch campsite option
Shovel - Test. It probably will need adjustment.
Unceasing Top - If one player's hand is empty, they draw? This one will be a pain.
Astrolabe - Make sure transformation works properly
Calling Bell - Ensure both players obtain the relics if one of them chooses to take one. This should work, since it's a combat reward screen, but not 100% sure.
+Eternal Feather - Count both decks
Bloody Idol - Heal for 2, triggers on both players' gold gain?
Necronomicon - Test if it works on cards played by ally. Should be fine?
Nilry's - Test
Warped Tongs - Upgrade a random card in each player's hand?
Strange Spoon - Should work fine, but test
Toolbox - 1 colorless for each player? Or just 1 colorless in one player's hand?
Toy Ornithopter/onPotionUse ensure it works when other player uses potion
Choker - Increase limit to 8?

+Fairy Potion - If one player has fairy potion, should work for both players (check tracked other player potions on death)

Colorless cards:
Mind Blast
Secret Weapon/Tool thing
Jack Of All Trades/Transmutation

extra features:
Resync command - when sent by host, resyncs other player forcefully

 */

@SpireInitializer
public class もこけねは神の国 implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber,
        EditCharactersSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, PreStartGameSubscriber,
        RenderSubscriber, PostUpdateSubscriber, OnStartBattleSubscriber, StartGameSubscriber, PostBattleSubscriber
{
    public static final String modID = "もこけね";

    public static final Logger logger = LogManager.getLogger(modID);

    // Mod panel stuff
    private static final String BADGE_IMAGE = "img/Badge.png";
    private static final String MODNAME = "妹紅 慧音";
    private static final String AUTHOR = "Alchyr";
    private static final String DESCRIPTION = " . . . ";

    // Card backgrounds/basic images
        //mokou
    private static final String MOKOU_ATTACK_BACK = "img/cards/mokou/attack_normal.png";
    private static final String MOKOU_POWER_BACK = "img/cards/mokou/power_normal.png";
    private static final String MOKOU_SKILL_BACK = "img/cards/mokou/skill_normal.png";
    private static final String MOKOU_CARD_ENERGY_ORB = "img/cards/mokou/card_small_orb.png";

    private static final String MOKOU_ATTACK_PORTRAIT = "img/cards/mokou/attack_portrait.png";
    private static final String MOKOU_POWER_PORTRAIT = "img/cards/mokou/power_portrait.png";
    private static final String MOKOU_SKILL_PORTRAIT = "img/cards/mokou/skill_portrait.png";
    private static final String MOKOU_CARD_ENERGY_ORB_PORTRAIT = "img/cards/mokou/card_large_orb.png";

    private static final String MOKOU_ENERGY_ORB = "img/cards/mokou/card_orb.png";

        //keine
    private static final String KEINE_ATTACK_BACK = "img/cards/mokou/attack_normal.png";
    private static final String KEINE_POWER_BACK = "img/cards/keine/power_normal.png";
    private static final String KEINE_SKILL_BACK = "img/cards/keine/skill_normal.png";
    private static final String KEINE_CARD_ENERGY_ORB = "img/cards/keine/card_small_orb.png";

    private static final String KEINE_ATTACK_PORTRAIT = "img/cards/keine/attack_portrait.png";
    private static final String KEINE_POWER_PORTRAIT = "img/cards/keine/power_portrait.png";
    private static final String KEINE_SKILL_PORTRAIT = "img/cards/keine/skill_portrait.png";
    private static final String KEINE_CARD_ENERGY_ORB_PORTRAIT = "img/cards/keine/card_large_orb.png";

    private static final String KEINE_ENERGY_ORB = "img/cards/keine/card_orb.png";

    // Character images
    private static final String BUTTON = "img/Character/CharacterButton.png";
    private static final String PORTRAIT = "img/Character/CharacterPortrait.png";

    // Colors
    public static final Color MOKOU_COLOR = new Color(0.74901960784f, 0.08235294117f, 0.08235294117f, 1.0f);
    public static final Color KEINE_COLOR = new Color(0.05490196078f, 0.2431372549f, 0.80784313725f, 1.0f);



    public static String makeID(String partialID)
    {
        return modID + ":" + partialID;
    }
    public static String assetPath(String partialPath)
    {
        return modID + "/" + partialPath;
    }

    public static LobbyMenu lobbyMenu;
    public static ChatBox chat;

    private static boolean startingGame = false;
    public static boolean gameStarted = false;
    private static float gameStartTimer = 0.0f;
    private static float eventChooseTimer = 0.0f;
    private static float mapChooseTimer = 0.0f;
    private static float bossRelicChooseTimer = 0.0f;

    private static int lastMilestone = 0;


    public static void beginGameStartTimer()
    {
        if (!startingGame && !gameStarted && HandleMatchmaking.activeMultiplayer)
        {
            startingGame = true;
            gameStartTimer = 5.0f;
            lastMilestone = 4;
            HandleMatchmaking.sendMessage("Starting game in 5");
        }
    }
    public static void stopGameStart()
    {
        if (startingGame)
        {
            startingGame = false;
            gameStartTimer = 0.0f;
            lastMilestone = 0;
        }
    }

    public static void startEventChooseTimer(float startTime)
    {
        if (eventChooseTimer <= 0.0f)
        {
            eventChooseTimer = startTime;
            lastMilestone = MathUtils.floor(startTime - 0.5f);
        }
    }
    public static void stopEventChooseTimer()
    {
        eventChooseTimer = 0.0f;
        lastMilestone = 0;
    }
    public static void startBossRelicChooseTimer(float startTime)
    {
        if (bossRelicChooseTimer <= 0.0f)
        {
            bossRelicChooseTimer = startTime;
            lastMilestone = MathUtils.floor(startTime - 0.5f);
        }
    }
    public static void stopBossRelicChooseTimer()
    {
        bossRelicChooseTimer = 0.0f;
        lastMilestone = 0;
    }
    public static void startMapChooseTimer(float startTime)
    {
        if (mapChooseTimer <= 0.0f)
        {
            MultiplayerHelper.sendP2PMessage("Players disagree. Conflict will be automatically resolved in " + 15 + " seconds.");
            mapChooseTimer = startTime;
            lastMilestone = MathUtils.floor(startTime - 0.5f);
        }
    }
    public static void stopMapChooseTimer()
    {
        mapChooseTimer = 0.0f;
        lastMilestone = 0;
    }

    @Override
    public void receiveStartGame() {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            MapRoomVoting.reset();
            BossRoomVoting.waitingForBoss = false;
            BossRoomVoting.otherWaitingForBoss = false;
            VoteBossRelic.votedRelic = null;
            VoteBossRelic.otherVotedRelic = null;
            VoteBossRelic.chosenRelic = null;
        }
    }

    @Override
    public void receivePostUpdate() {
        if (chat != null)
        {
            chat.update();
            if (startingGame)
            {
                if (gameStartTimer > 0)
                {
                    gameStartTimer -= Gdx.graphics.getDeltaTime();
                    if (lastMilestone > gameStartTimer)
                    {
                        HandleMatchmaking.sendMessage(String.valueOf(lastMilestone));
                        updateMilestone();
                    }
                }
                else
                {
                    startingGame = false;
                    gameStarted = true;

                    startGame();
                }
            }

            if (eventChooseTimer > 0.0f)
            {
                eventChooseTimer -= Gdx.graphics.getDeltaTime();
                if (eventChooseTimer <= 0.0f)
                {
                    if (RoomEventVoting.choseOption)
                        RoomEventVoting.resolveConflict();
                    if (GenericEventVoting.choseOption)
                        GenericEventVoting.resolveConflict();
                }
                else if (lastMilestone > eventChooseTimer)
                {
                    HandleMatchmaking.sendMessage(String.valueOf(lastMilestone));
                    updateMilestone();
                }
            }

            if (mapChooseTimer > 0.0f)
            {
                mapChooseTimer -= Gdx.graphics.getDeltaTime();
                if (mapChooseTimer <= 0.0f)
                {
                    MapRoomVoting.resolveConflict();
                }
                else if (lastMilestone > mapChooseTimer)
                {
                    HandleMatchmaking.sendMessage(String.valueOf(lastMilestone));
                    updateMilestone();
                }
            }

            if (bossRelicChooseTimer > 0.0f)
            {
                bossRelicChooseTimer -= Gdx.graphics.getDeltaTime();
                if (bossRelicChooseTimer <= 0.0f)
                {
                    VoteBossRelic.resolveConflict();
                }
                else if (lastMilestone > bossRelicChooseTimer)
                {
                    HandleMatchmaking.sendMessage(String.valueOf(lastMilestone));
                    updateMilestone();
                }
            }
        }

        if (lobbyMenu != null)
        {
            lobbyMenu.update();
        }
        MultiplayerHelper.readPostUpdate();
    }

    private static void updateMilestone()
    {
        if (lastMilestone > 30)
        {
            lastMilestone = (MathUtils.ceil(lastMilestone / 10.0f) - 1) * 10;
        }
        else if (lastMilestone > 5)
        {
            lastMilestone = (MathUtils.ceil(lastMilestone / 5.0f) - 1) * 5;
        }
        else
        {
            --lastMilestone;
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        DiscardToCorrectPile.reset();
        RequireDoubleEndTurn.reset();
        TrackCardSource.useOtherEnergy = false;
        TrackCardSource.useMyEnergy = false;
        MixEnemyTempCards.toMokou = true;
        LastCardType.type = AbstractCard.CardType.CURSE; //to avoid any null issues. Nothing will trigger off of playing curses.
        LastCardType.lastCardCopy = null;
        PotionUse.queuedPotionUse.clear();
    }
    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        TrackCardSource.useOtherEnergy = false;
        TrackCardSource.useMyEnergy = false;
        MixEnemyTempCards.toMokou = true;
        LastCardType.type = AbstractCard.CardType.CURSE;
        LastCardType.lastCardCopy = null;
        PotionUse.queuedPotionUse.clear();
    }

    public void startGame()
    {
        if (CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT)
        {
            TipTracker.neverShowAgain("NEOW_SKIP");

            if (Settings.seed == null) {
                long sourceTime = System.nanoTime();
                Random rng = new Random(sourceTime);
                Settings.seedSourceTimestamp = sourceTime;
                Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
            } else {
                Settings.seedSet = true;
            }

            CardCrawlGame.mainMenuScreen.isFadingOut = true;
            CardCrawlGame.mainMenuScreen.fadeOutMusic();
            Settings.isDailyRun = false;
            boolean isTrialSeed = TrialHelper.isTrialSeed(SeedHelper.getString(Settings.seed));
            if (isTrialSeed) {
                Settings.specialSeed = Settings.seed;
                long sourceTime = System.nanoTime();
                Random rng = new Random(sourceTime);
                Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
                Settings.isTrial = true;
            }

            if (Settings.seedSet)
                MultiplayerHelper.sendP2PString("seedset" + Settings.seed.toString());
            else
                MultiplayerHelper.sendP2PString("seed" + Settings.seed.toString());

            if (Settings.isTrial)
            {
                MultiplayerHelper.sendP2PString("trial" + Settings.specialSeed);
            }

            ModHelper.setModsFalse();
            AbstractDungeon.generateSeeds();
            AbstractDungeon.isAscensionMode = CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode;
            if (AbstractDungeon.isAscensionMode) {
                AbstractDungeon.ascensionLevel = CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel;
            } else {
                AbstractDungeon.ascensionLevel = 0;
            }
            MultiplayerHelper.sendP2PString("ascension" + AbstractDungeon.ascensionLevel);

            MultiplayerHelper.sendP2PString("start_game");

            MultiplayerHelper.sendP2PMessage("Starting game...");
        }
    }
    public static void startSetupGame() //start game where settings have already been set
    {
        TipTracker.neverShowAgain("NEOW_SKIP");
        CardCrawlGame.mainMenuScreen.isFadingOut = true;
        CardCrawlGame.mainMenuScreen.fadeOutMusic();
        Settings.isDailyRun = false;
        AbstractDungeon.generateSeeds();

        HandleMatchmaking.leave();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (chat != null)
            chat.render(sb);

        if (lobbyMenu != null)
            lobbyMenu.render(sb);
    }

    public もこけねは神の国()
    {
        BaseMod.subscribe(this);

        BaseMod.addColor(CharacterEnums.MOKOU, MOKOU_COLOR,
                assetPath(MOKOU_ATTACK_BACK), assetPath(MOKOU_SKILL_BACK), assetPath(MOKOU_POWER_BACK),
                assetPath(MOKOU_ENERGY_ORB),
                assetPath(MOKOU_ATTACK_PORTRAIT), assetPath(MOKOU_SKILL_PORTRAIT), assetPath(MOKOU_POWER_PORTRAIT),
                assetPath(MOKOU_CARD_ENERGY_ORB_PORTRAIT), assetPath(MOKOU_CARD_ENERGY_ORB));

        BaseMod.addColor(CharacterEnums.KEINE, KEINE_COLOR,
                assetPath(KEINE_ATTACK_BACK), assetPath(KEINE_SKILL_BACK), assetPath(KEINE_POWER_BACK),
                assetPath(KEINE_ENERGY_ORB),
                assetPath(KEINE_ATTACK_PORTRAIT), assetPath(KEINE_SKILL_PORTRAIT), assetPath(KEINE_POWER_PORTRAIT),
                assetPath(KEINE_CARD_ENERGY_ORB_PORTRAIT), assetPath(KEINE_CARD_ENERGY_ORB));
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new MokouKeine(MathUtils.randomBoolean()),
                assetPath(BUTTON), assetPath(PORTRAIT), CharacterEnums.MOKOUKEINE);
    }

    @Override
    public void receiveEditCards() {
        try {
            autoAddCards();
        } catch (URISyntaxException | IllegalAccessException | InstantiationException | NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveEditRelics() {
        //BaseMod.addRelicToCustomPool(new SkyMirror(), CardColorEnum.ASTROLOGER);
    }

    @Override
    public void receiveEditStrings()
    {
        String lang = getLangString();

        try
        {
            BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("localization/" + lang + "/RelicStrings.json"));
            BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/" + lang + "/CardStrings.json"));
            BaseMod.loadCustomStringsFile(CharacterStrings.class, assetPath("localization/" + lang + "/CharacterStrings.json"));
            BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("localization/" + lang + "/PowerStrings.json"));
            BaseMod.loadCustomStringsFile(UIStrings.class, assetPath("localization/" + lang + "/UIStrings.json"));
            BaseMod.loadCustomStringsFile(MonsterStrings.class, assetPath("localization/" + lang + "/EnemyStrings.json"));
        }
        catch (Exception e)
        {
            lang = "eng";
            BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath("localization/" + lang + "/RelicStrings.json"));
            BaseMod.loadCustomStringsFile(CardStrings.class, assetPath("localization/" + lang + "/CardStrings.json"));
            BaseMod.loadCustomStringsFile(CharacterStrings.class, assetPath("localization/" + lang + "/CharacterStrings.json"));
            BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath("localization/" + lang + "/PowerStrings.json"));
            BaseMod.loadCustomStringsFile(UIStrings.class, assetPath("localization/" + lang + "/UIStrings.json"));
            BaseMod.loadCustomStringsFile(MonsterStrings.class, assetPath("localization/" + lang + "/EnemyStrings.json"));
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        String lang = getLangString();

        try
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(assetPath("localization/" + lang + "/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

            if (keywords != null) {
                for (KeywordWithProper keyword : keywords) {
                    BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
        catch (Exception e)
        {
            Gson gson = new Gson();
            String json = Gdx.files.internal(assetPath("localization/eng/Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordWithProper[] keywords = gson.fromJson(json, KeywordWithProper[].class);

            if (keywords != null) {
                for (KeywordWithProper keyword : keywords) {
                    BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }

    @Override
    public void receivePreStartGame() {
        //Max hand size is set to 10 immediately before this is called.
        if (CardCrawlGame.chosenCharacter == CharacterEnums.MOKOUKEINE)
            BaseMod.MAX_HAND_SIZE = 6;
    }

    @Override
    public void receivePostInitialize() {
        //Setup mod menu info stuff
        Texture badgeTexture = TextureLoader.getTexture(assetPath(BADGE_IMAGE));

        if (badgeTexture != null)
        {
            //ModPanel panel = new ModPanel();

            BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, null);
        }

        //Initialize multiplayer stuff
        HandleMatchmaking.init();
        MultiplayerHelper.init();

        //Initialize UI
        chat = new ChatBox();
        lobbyMenu = new LobbyMenu();
    }





    //I totally didn't copy this from Hubris, made by kiooeht.
    private static void autoAddCards() throws URISyntaxException, IllegalAccessException, InstantiationException, NotFoundException, CannotCompileException
    {
        ClassFinder finder = new ClassFinder();
        URL url = もこけねは神の国.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter =
                new AndClassFilter(
                        new NotClassFilter(new InterfaceOnlyClassFilter()),
                        new NotClassFilter(new AbstractClassFilter()),
                        new ClassModifiersClassFilter(Modifier.PUBLIC),
                        new CardFilter()
                );
        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        for (ClassInfo classInfo : foundClasses) {
            CtClass cls = Loader.getClassPool().get(classInfo.getClassName());

            boolean isCard = false;
            CtClass superCls = cls;
            while (superCls != null) {
                superCls = superCls.getSuperclass();
                if (superCls == null) {
                    break;
                }
                if (superCls.getName().equals(AbstractCard.class.getName())) {
                    isCard = true;
                    break;
                }
            }
            if (!isCard) {
                continue;
            }

            AbstractCard card = (AbstractCard) Loader.getClassPool().toClass(cls).newInstance();

            BaseMod.addCard(card);
        }
    }

    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }

    @SuppressWarnings("unused") public static void initialize() {
        new もこけねは神の国();
    }
}