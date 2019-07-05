package もこけね.fantasyEffects.generalBonus;
/*
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.FantasyEffect;
import もこけね.cards.colorless.FantasyCard;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.assetPath;
import static もこけね.もこけねは神の国.makeID;

public class Exhaust extends FantasyEffect {
    private static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(makeID("FantasyExhaust")).EXTENDED_DESCRIPTION;

    private static final float SCALE = 1.4f;

    public Exhaust()
    {}

    @Override
    public int register(int id) {
        FantasyEffect.bonusAttackEffects.add(this);
        FantasyEffect.bonusSkillEffects.add(this);
        FantasyEffect.effectIDs.put(this, id++);
        return id;
    }

    @Override
    public boolean isUsable(FantasyCard base) {
        return (base.baseDamage != -1 || base.baseBlock != -1);
    }

    @Override
    public TARGET_TYPE getTargetType() {
        return TARGET_TYPE.NONE;
    }

    @Override
    public String getDescription(FantasyCard c) {
        return TEXT[0];
    }

    @Override
    public Texture getTexture(FantasyCard c) {
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/exhaust.png"));
    }
    @Override
    public Texture getPortraitTexture(FantasyCard c) {
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/exhaust_p.png"));
    }

    @Override
    public void initialize(FantasyCard c) {
        c.setExhaust(true);

        if (c.baseBlock != -1)
        {
            c.block = c.baseBlock = MathUtils.floor(c.baseBlock * SCALE);
        }
        if (c.baseDamage != -1)
        {
            c.damage = c.baseDamage = MathUtils.floor(c.baseDamage * SCALE);
        }
    }

    @Override
    public void applyPowers(FantasyCard c) {
    }
    @Override
    public void calculateCardDamage(FantasyCard c, AbstractMonster m) {
    }

    @Override
    public void use(FantasyCard c, AbstractPlayer p, AbstractMonster m) {
    }
}*/