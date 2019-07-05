package もこけね.cards.keine.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import もこけね.abstracts.KeineCard;
import もこけね.actions.character.DontUseSpecificEnergyAction;
import もこけね.patch.enums.CharacterEnums;
import もこけね.util.CardInfo;

import static もこけね.もこけねは神の国.makeID;

public class SagaPeace extends KeineCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SagaPeace",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 3;

    public SagaPeace()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void triggerWhenCopied() {
        this.superFlash(Color.VIOLET);
        if (AbstractDungeon.player.chosenClass == CharacterEnums.MOKOUKEINE)
        {
            AbstractDungeon.actionManager.addToBottom(new DontUseSpecificEnergyAction());
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 2));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SagaPeace();
    }
}