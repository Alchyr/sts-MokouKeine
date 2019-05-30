package もこけね.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Queue;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import もこけね.patch.input.InputHelper;
import もこけね.util.HandleMatchmaking;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.KEINE_COLOR;
import static もこけね.もこけねは神の国.assetPath;

public class ChatBox {
    public Queue<String> messages = new Queue<>();

    private static final float NORMAL_FADE_TIME = 6.0f;

    private static final float TYPE_X = 100.0f * Settings.scale;
    private static final float TYPE_Y = Settings.HEIGHT / 2.0f;

    private static final float MARKER_Y = TYPE_Y + 2.0f * Settings.scale;
    private static final float MARKER_WIDTH = FontHelper.getWidth(FontHelper.tipBodyFont, "_", 1);

    public static final float WIDTH = 600.0f * Settings.scale;
    public static final float HEIGHT = FontHelper.getHeight(FontHelper.tipBodyFont, " \n \n \n \n \n \n \n", 1);

    private static final float SEPARATOR_Y = TYPE_Y + FontHelper.getHeight(FontHelper.tipBodyFont, "X D", 1);
    private static final float SEPARATOR_HEIGHT = 3.0f * Settings.scale;

    private static final Color bgColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    private static final Texture white = TextureLoader.getTexture(assetPath("img/white.png"));

    private int maxLines;
    private String fullText;

    public boolean active;
    public boolean skipNextInput;
    public float fadeDelay;

    public ChatBox()
    {
        maxLines = 7;
        fullText = "";
        active = false;
        skipNextInput = false;
    }

    public void onPushEnter()
    {
        if (!active)
        {
            active = true;
        }
        else
        {
            if (!InputHelper.text.isEmpty())
            {
                HandleMatchmaking.sendMessage(CardCrawlGame.playerName + ": " + InputHelper.text);
                fadeDelay = NORMAL_FADE_TIME;
            }
            active = false;
            skipNextInput = true;
        }
    }

    public void receiveMessage(String msg)
    {
        if (!msg.isEmpty())
        {
            fadeDelay = NORMAL_FADE_TIME;

            messages.addLast(msg);
            if (messages.size > maxLines)
            {
                messages.removeFirst();
            }

            StringBuilder fullMessages = new StringBuilder();
            for (String s : messages)
            {
                fullMessages.append(s).append('\n');
            }
            fullText = fullMessages.toString();
        }
    }

    public void update()
    {
        skipNextInput = false;
        if (fadeDelay > 0)
            fadeDelay -= Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch sb)
    {
        if (fadeDelay > 0 || active)
        {
            sb.setColor(bgColor);
            sb.draw(white, TYPE_X, TYPE_Y, 0, 0, WIDTH, HEIGHT, 1.0f, 1.0f, 0, 0, 0, 1, 1, false, false);

            sb.setColor(Color.WHITE);
            sb.draw(white, TYPE_X, SEPARATOR_Y, 0, 0, WIDTH, SEPARATOR_HEIGHT, 1.0f, 1.0f, 0, 0, 0, 1, 1, false, false);

            if (active)
            {
                sb.draw(white, TYPE_X + FontHelper.getWidth(FontHelper.tipBodyFont, InputHelper.text, 1), MARKER_Y, 0, 0, MARKER_WIDTH, SEPARATOR_HEIGHT, 1.0f, 1.0f, 0, 0, 0, 1, 1, false, false);
                FontHelper.renderFontLeftDownAligned(sb, FontHelper.tipBodyFont, InputHelper.text, TYPE_X, TYPE_Y, Color.WHITE);
            }
            FontHelper.renderFontLeftDownAligned(sb, FontHelper.tipBodyFont, fullText, TYPE_X, TYPE_Y, Color.WHITE);
        }
    }
}
