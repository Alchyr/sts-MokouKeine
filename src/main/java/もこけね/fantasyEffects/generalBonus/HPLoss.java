package もこけね.fantasyEffects.generalBonus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.FantasyEffect;
import もこけね.cards.colorless.FantasyCard;
import もこけね.util.TextureLoader;

import static もこけね.もこけねは神の国.assetPath;
import static もこけね.もこけねは神の国.makeID;

public class HPLoss extends FantasyEffect {
    private static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(makeID("FantasyHPLoss")).EXTENDED_DESCRIPTION;

    private static final int MIN_COST = 3;
    private static final int MAX_COST = 9;

    private static final float MIN_SCALE = 1.15f;
    private static final float MAX_SCALE = 1.4f;

    private int amount;

    public HPLoss()
    {}

    public HPLoss(int amt)
    {
        this.amount = amt;
    }

    @Override
    public int register(int id) {
        for (int i = MIN_COST; i <= MAX_COST; ++i)
        {
            HPLoss loss = new HPLoss(i);
            FantasyEffect.bonusAttackEffects.add(loss);
            FantasyEffect.bonusSkillEffects.add(loss);
            FantasyEffect.effectIDs.put(loss, id); //all of them have same id because same image.
        }
        return id + 1;
    }

    @Override
    public boolean isUsable(FantasyCard base) {
        return base.baseMagicNumber == -1 && (base.baseDamage != -1 || base.baseBlock != -1);
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
        if (c.type == AbstractCard.CardType.ATTACK)
        {
            return TextureLoader.getTexture(assetPath("img/cards/fantasy/attackhploss.png"));
        }
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/skillhploss.png"));
    }
    @Override
    public Texture getPortraitTexture(FantasyCard c) {
        if (c.type == AbstractCard.CardType.ATTACK)
        {
            return TextureLoader.getTexture(assetPath("img/cards/fantasy/attackhploss_p.png"));
        }
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/skillhploss_p.png"));
    }

    @Override
    public void initialize(FantasyCard c) {
        c.setMagic(amount);
        float scale = MathUtils.lerp(MIN_SCALE, MAX_SCALE, (amount - MIN_COST) / (MAX_COST - MIN_COST));
        if (c.baseBlock != -1)
        {
            c.block = c.baseBlock = MathUtils.floor(c.baseBlock * scale);
        }
        if (c.baseDamage != -1)
        {
            c.damage = c.baseDamage = MathUtils.floor(c.baseDamage * scale);
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
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, c.magicNumber));
    }
}