package もこけね.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Queue;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import もこけね.patch.input.InputHelper;
import もこけね.patch.lobby.HandleMatchmaking;

import static もこけね.もこけねは神の国.KEINE_COLOR;

public class ChatBox {
    public Queue<String> messages = new Queue<>();

    private static final float NORMAL_FADE_TIME = 6.0f;

    private static final float TYPE_X = 100.0f * Settings.scale;
    private static final float TYPE_Y = Settings.HEIGHT / 2.0f;

    private int maxLines;
    private String fullText;

    public boolean active;
    public float fadeDelay;

    public ChatBox()
    {
        maxLines = 7;
        fullText = "";
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
        if (fadeDelay > 0)
            fadeDelay -= Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch sb)
    {
        if (fadeDelay > 0 || active)
        {
            if (active)
            {
                FontHelper.renderFontLeftDownAligned(sb, FontHelper.tipBodyFont, InputHelper.text, TYPE_X, TYPE_Y, Color.WHITE);
            }
            FontHelper.renderFontLeftDownAligned(sb, FontHelper.tipBodyFont, fullText, TYPE_X, TYPE_Y, KEINE_COLOR);
        }
    }
}
