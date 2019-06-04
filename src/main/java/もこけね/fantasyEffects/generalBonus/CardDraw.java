package もこけね.fantasyEffects.generalBonus;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.FantasyEffect;
import もこけね.cards.colorless.FantasyCard;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.assetPath;
import static もこけね.もこけねは神の国.makeID;

public class CardDraw extends FantasyEffect {
    private static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(makeID("FantasyCardDraw")).EXTENDED_DESCRIPTION;

    //first is X, then 0, 1, 2, 3
    private static final int[] DRAW = new int[] {
            0, //x + 0
            1,
            1,
            2,
            2
    };

    private static final int[] UPG_DRAW = new int[] {
            1,
            0,
            1,
            1,
            1
    };

    public CardDraw()
    {}

    @Override
    public int register(int id) {
        CardDraw draw = new CardDraw();
        FantasyEffect.bonusAttackEffects.add(draw);
        FantasyEffect.bonusSkillEffects.add(draw);
        FantasyEffect.effectIDs.put(draw, id++);
        return id;
    }

    @Override
    public boolean isUsable(FantasyCard base) {
        return base.baseMagicNumber == -1;
    }

    @Override
    public TARGET_TYPE getTargetType() {
        return TARGET_TYPE.NONE;
    }

    @Override
    public String getDescription(FantasyCard c) {
        if (c.cost == -1)
        {
            if (c.magicNumber == 0)
            {
                return TEXT[2];
            }
            return TEXT[3];
        }
        else
        {
            if (c.magicNumber == 1)
            {
                return TEXT[0];
            }
            return TEXT[1];
        }
    }

    @Override
    public Texture getTexture(FantasyCard c) {
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/draw.png"));
    }
    @Override
    public Texture getPortraitTexture(FantasyCard c) {
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/draw_p.png"));
    }

    @Override
    public void initialize(FantasyCard c) {
        int i = c.cost + 1;
        if (i < 0)
            i = 1;

        if (i > 4)
            i = 4;

        c.setMagic(DRAW[i], UPG_DRAW[i]);
    }

    @Override
    public void applyPowers(FantasyCard c) {
    }
    @Override
    public void calculateCardDamage(FantasyCard c, AbstractMonster m) {
    }

    @Override
    public void use(FantasyCard c, AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, c.magicNumber));
    }
}