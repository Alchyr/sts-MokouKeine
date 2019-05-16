package もこけね.actions.character;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import もこけね.effects.ShowCardAndAddToOtherDiscardEffect;
import もこけね.effects.ShowCardAndAddToOtherHandEffect;

public class MakeTempCardInOtherHandAction extends AbstractGameAction {
    private static final float DURATION_PER_CARD = 0.35F;
    private AbstractCard c;
    private static final float PADDING;
    private boolean isOtherCardInCenter;

    public MakeTempCardInOtherHandAction(AbstractCard card, boolean isOtherCardInCenter) {
        this.isOtherCardInCenter = true;
        UnlockTracker.markCardAsSeen(card.cardID);// 20
        this.amount = 1;// 21
        this.actionType = ActionType.CARD_MANIPULATION;// 22
        this.duration = DURATION_PER_CARD;// 23
        this.c = card;// 24
        this.isOtherCardInCenter = isOtherCardInCenter;// 25
    }// 26

    public MakeTempCardInOtherHandAction(AbstractCard card) {
        this(card, 1);// 29
    }// 30

    public MakeTempCardInOtherHandAction(AbstractCard card, int amount) {
        this.isOtherCardInCenter = true;// 17
        UnlockTracker.markCardAsSeen(card.cardID);// 33
        this.amount = amount;// 34
        this.actionType = ActionType.CARD_MANIPULATION;// 35
        this.duration = DURATION_PER_CARD;// 36
        this.c = card;// 37
    }// 38

    public MakeTempCardInOtherHandAction(AbstractCard card, int amount, boolean isOtherCardInCenter) {
        this(card, amount);// 41
        this.isOtherCardInCenter = isOtherCardInCenter;// 42
    }// 43

    public void update() {
        if (this.amount == 0) {// 47
            this.isDone = true;// 48
        } else {
            int discardAmount = 0;// 52
            int handAmount = this.amount;// 53
            if (this.amount + AbstractDungeon.player.hand.size() > 10) {// 56
                AbstractDungeon.player.createHandIsFullDialog();// 57
                discardAmount = this.amount + AbstractDungeon.player.hand.size() - 10;// 58
                handAmount -= discardAmount;// 59
            }

            this.addToHand(handAmount);// 62
            this.addToDiscard(discardAmount);// 63
            if (this.amount > 0) {// 65
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.8F));// 66
            }

            this.isDone = true;// 69
        }
    }// 49 70

    private void addToHand(int handAmt) {
        int i;
        switch(this.amount) {// 73
            case 0:
                break;
            case 1:
                if (handAmt == 1) {// 77
                    if (this.isOtherCardInCenter) {// 78
                        AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));// 79 81
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy()));// 85
                    }
                }
                break;
            case 2:
                if (handAmt == 1) {// 90
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT / 2.0F));// 91 93
                } else if (handAmt == 2) {// 96
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F));// 97 99
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));// 102 104
                }
                break;
            case 3:
                if (handAmt == 1) {// 110
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));// 111 113
                } else if (handAmt == 2) {// 116
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F));// 117 119
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F));// 122 124
                } else if (handAmt == 3) {// 127
                    for(i = 0; i < this.amount; ++i) {// 130
                        AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy()));// 131
                    }
                }
                break;
            default:
                for(i = 0; i < handAmt; ++i) {// 136
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherHandEffect(this.c.makeStatEquivalentCopy(), MathUtils.random((float)Settings.WIDTH * 0.2F, (float)Settings.WIDTH * 0.8F), MathUtils.random((float)Settings.HEIGHT * 0.3F, (float)Settings.HEIGHT * 0.7F)));// 137 139 140 141
                }
        }

    }// 145

    private void addToDiscard(int discardAmt) {
        switch(this.amount) {// 148
            case 0:
                break;
            case 1:
                if (discardAmt == 1) {// 152
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F));// 153 155
                }
                break;
            case 2:
                if (discardAmt == 1) {// 161
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT * 0.5F));// 162 164
                } else if (discardAmt == 2) {// 167
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT * 0.5F));// 168 170
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH * 0.5F, (float)Settings.HEIGHT * 0.5F));// 173 175
                }
                break;
            case 3:
                if (discardAmt == 1) {// 181
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT * 0.5F));// 182 184
                } else if (discardAmt == 2) {// 187
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT * 0.5F));// 188 190
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT * 0.5F));// 193 195
                } else if (discardAmt == 3) {// 198
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F, (float)Settings.HEIGHT * 0.5F));// 199 201
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT * 0.5F));// 204 206
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT * 0.5F));// 209 211
                }
                break;
            default:
                for(int i = 0; i < discardAmt; ++i) {// 217
                    AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(this.c.makeStatEquivalentCopy(), MathUtils.random((float)Settings.WIDTH * 0.2F, (float)Settings.WIDTH * 0.8F), MathUtils.random((float)Settings.HEIGHT * 0.3F, (float)Settings.HEIGHT * 0.7F)));// 218 220 221 222
                }
        }

    }// 226

    static {
        PADDING = 25.0F * Settings.scale;// 16
    }
}