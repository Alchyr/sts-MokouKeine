package もこけね.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.codedisaster.steamworks.SteamID;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import もこけね.util.HandleMatchmaking;
import もこけね.util.LobbyData;
import もこけね.util.TextureLoader;

import java.util.ArrayList;

import static もこけね.character.MokouKeine.characterStrings;
import static もこけね.util.HandleMatchmaking.metadataTrue;
import static もこけね.もこけねは神の国.assetPath;
import static もこけね.もこけねは神の国.makeID;

public class LobbyMenu {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("LobbyPanel"));
    private static final String[] TEXT = uiStrings.TEXT;

    private static final Texture panelBack = TextureLoader.getTexture(assetPath("img/ui/lobbyPanel.png"));
    private static final Texture refreshButton = TextureLoader.getTexture(assetPath("img/ui/refresh.png"));

    private static final float LINE_HEIGHT = FontHelper.getHeight(FontHelper.buttonLabelFont, "X", 1);

    private static final int PANEL_WIDTH = 840;
    private static final int PANEL_HEIGHT = 600;

    private static final float PANEL_X = Settings.WIDTH / 2.0f;
    private static final float PANEL_Y = Settings.HEIGHT / 2.0f;

    private static final int REFRESH_SIZE = 56;
    private static final float REFRESH_OFFSET = REFRESH_SIZE / 2.0f;

    private static final float REFRESH_X = PANEL_X + (PANEL_WIDTH * Settings.scale) - 45.0f * Settings.scale;
    private static final float REFRESH_Y = PANEL_Y + PANEL_HEIGHT + 10.0f * Settings.scale;

    private static final float PANEL_CENTER_X = PANEL_X + (PANEL_WIDTH / 2.0f) * Settings.scale;
    private static final float PANEL_CENTER_Y = PANEL_Y + (PANEL_HEIGHT / 2.0f) * Settings.scale;

    private static final float LABEL_Y = PANEL_Y + 525.f * Settings.scale;
    private static final float LOBBY_START_Y = LABEL_Y - (LINE_HEIGHT + 15.0f * Settings.scale);

    private static final float NAME_LABEL_X = PANEL_X + 75.0f * Settings.scale;
    private static final float PUBLIC_LABEL_X = PANEL_X + 450.0f * Settings.scale;
    private static final float CHARACTER_LABEL_X = PANEL_X + 660.0f * Settings.scale;

    private static final int LOBBIES_PER_PAGE = 12;

    public boolean visible = false;

    private ArrayList<LobbyData> lobbies = new ArrayList<>();
    private int page = 0;
    private int maxPage = 0;

    private Color refreshButtonColor = Color.GRAY.cpy();
    private float refreshButtonScale = 1.0f;
    private float refreshButtonHoverTime = 0.0f;

    public boolean searching = false;

    public void show(boolean searching)
    {
        this.lobbies.clear();
        this.searching = searching;
    }
    public void show(ArrayList<SteamID> lobbies)
    {
        setLobbies(lobbies);
        this.visible = true;
    }

    public void setLobbies(ArrayList<SteamID> lobbies)
    {
        this.lobbies.clear();
        searching = false;
        if (!lobbies.isEmpty())
        {
            for (SteamID lobby : lobbies)
            {
                LobbyData data = new LobbyData();

                data.id = lobby;
                data.name = HandleMatchmaking.matchmaking.getLobbyData(lobby, HandleMatchmaking.lobbyNameKey);
                data.hostIsMokou = HandleMatchmaking.matchmaking.getLobbyData(lobby, HandleMatchmaking.hostIsMokouKey).equals(metadataTrue);
                data.isPublic = HandleMatchmaking.matchmaking.getLobbyData(lobby, HandleMatchmaking.lobbyPublicKey).equals(metadataTrue);

                this.lobbies.add(data);
            }

            //debug code for viewing layout
            while (this.lobbies.size() < LOBBIES_PER_PAGE * 2.5)
            {
                LobbyData testData = new LobbyData();
                testData.id = null;
                testData.isPublic = MathUtils.randomBoolean();
                testData.hostIsMokou = MathUtils.randomBoolean();
                testData.name = "Lobby " + lobbies.size() + 1;
                this.lobbies.add(testData);
            }

            this.lobbies.sort(new LobbyData.LobbyDataComparer());
            page = 0;
            maxPage = lobbies.size() / LOBBIES_PER_PAGE;
        }
    }

    public void hide()
    {
        this.visible = false;
    }

    public void update()
    {
        if (visible)
        {

        }
    }

    public void render(SpriteBatch sb)
    {
        if (visible)
        {
            sb.setColor(Color.WHITE);
            sb.draw(panelBack, PANEL_X, PANEL_Y, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, Settings.scale, Settings.scale, 0, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, false, false);

            sb.setColor(refreshButtonColor);
            sb.draw(refreshButton, REFRESH_X, REFRESH_Y, REFRESH_OFFSET, REFRESH_OFFSET, REFRESH_SIZE, REFRESH_SIZE, refreshButtonScale, refreshButtonScale, 0, 0, 0, REFRESH_SIZE, REFRESH_SIZE, false, false);

            sb.setColor(Color.WHITE);

            if (searching)
            {
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[6], PANEL_CENTER_X, PANEL_CENTER_Y, Color.WHITE);
            }
            else if (lobbies.isEmpty())
            {
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[3], PANEL_CENTER_X, PANEL_CENTER_Y, Color.WHITE);
            }
            else
            {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.buttonLabelFont, TEXT[0], NAME_LABEL_X, LABEL_Y, Color.GOLD);
                FontHelper.renderFontCenteredTopAligned(sb, FontHelper.buttonLabelFont, TEXT[1], PUBLIC_LABEL_X, LABEL_Y, Color.GOLD);
                FontHelper.renderFontCenteredTopAligned(sb, FontHelper.buttonLabelFont, TEXT[2], CHARACTER_LABEL_X, LABEL_Y, Color.GOLD);

                int max = Math.min(lobbies.size(), (page * LOBBIES_PER_PAGE + LOBBIES_PER_PAGE));
                float y = LOBBY_START_Y;
                for (int i = page * LOBBIES_PER_PAGE; i < max; ++i)
                {
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.buttonLabelFont, lobbies.get(i).name, NAME_LABEL_X, y, Color.WHITE);
                    FontHelper.renderFontCenteredTopAligned(sb, FontHelper.buttonLabelFont, lobbies.get(i).isPublic ? TEXT[4] : TEXT[5], PUBLIC_LABEL_X, y, Color.WHITE);
                    FontHelper.renderFontCenteredTopAligned(sb, FontHelper.buttonLabelFont, lobbies.get(i).hostIsMokou ? characterStrings.NAMES[1] : characterStrings.NAMES[2], CHARACTER_LABEL_X, y, Color.WHITE);
                    y -= LINE_HEIGHT;
                }
            }
        }
    }
}
