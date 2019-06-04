package もこけね.fantasyEffects.skillBase;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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

public class NormalBlock extends FantasyEffect {
    private static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(makeID("FantasyBlockNormal")).EXTENDED_DESCRIPTION;

    //first is X, then 0, 1, 2, 3
    private static final int[] BLOCK = new int[] {
            6,
            4,
            8,
            14,
            22
    };

    private static final int[] UPG_BLOCK = new int[] {
            3,
            3,
            3,
            4,
            4
    };

    public NormalBlock()
    {
    }

    @Override
    public int register(int id) {
        NormalBlock block = new NormalBlock();
        FantasyEffect.baseSkillEffects.add(block);
        FantasyEffect.effectIDs.put(block, id++);
        return id;
    }

    @Override
    public boolean isUsable(FantasyCard base) {
        return true;
    }

    @Override
    public TARGET_TYPE getTargetType() {
        return TARGET_TYPE.NONE;
    }

    @Override
    public String getDescription(FantasyCard c) {
        if (c.cost == -1)
        {
            return TEXT[0];
        }
        else
        {
            return TEXT[1];
        }
    }

    @Override
    public Texture getTexture(FantasyCard c) {
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/block.png"));
    }
    @Override
    public Texture getPortraitTexture(FantasyCard c) {
        return TextureLoader.getTexture(assetPath("img/cards/fantasy/block_p.png"));
    }

    @Override
    public void initialize(FantasyCard c) {
        int i = c.cost + 1;
        if (i < 0)
            i = 1;

        if (i > 4)
            i = 4;

        c.setBlock(BLOCK[i], UPG_BLOCK[i]);

        c.target = AbstractCard.CardTarget.NONE;
    }

    @Override
    public void applyPowers(FantasyCard c) {
    }
    @Override
    public void calculateCardDamage(FantasyCard c, AbstractMonster m) {
    }

    @Override
    public void use(FantasyCard c, AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, c.block));
    }
}
