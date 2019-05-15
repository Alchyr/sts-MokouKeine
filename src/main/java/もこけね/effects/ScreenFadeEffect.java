package もこけね.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ScreenFadeEffect extends AbstractGameEffect {
    public boolean finishing;
    private String message;
    private float progress;
    private Color textColor;

    public ScreenFadeEffect(String msg)
    {
        this.message = msg;
        finishing = false;
        progress = 0;
        this.color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        this.textColor = new Color(1.0f, 1.0f, 1.0f, 0.0f);
    }

    public void update()
    {
        if (!finishing)
        {
            if (progress < 1)
            {
                progress += Gdx.graphics.getDeltaTime() * 2;
                if (progress > 1)
                    progress = 1;
            }
        }
        else
        {
            if (progress > 0)
            {
                progress -= Gdx.graphics.getDeltaTime() * 4;
                if (progress < 0)
                {
                    progress = 0;
                    this.isDone = true;
                }
            }
        }
        color.a = MathUtils.lerp(0, 1, progress);
        textColor.a = color.a;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);

        FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.message, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), textColor);
    }

    @Override
    public void dispose() {

    }
}
