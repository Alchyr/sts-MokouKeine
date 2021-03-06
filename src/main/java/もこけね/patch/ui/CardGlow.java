package もこけね.patch.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.combat.BurstActive;
import もこけね.patch.enums.CustomCardTags;

import java.lang.reflect.Field;

import static もこけね.もこけねは神の国.logger;

@SpirePatch(
        clz = CardGlowBorder.class,
        method = SpirePatch.CONSTRUCTOR
)
public class CardGlow {
    private static Field hoveredMonsterField;

    static {
        try {
            hoveredMonsterField = AbstractPlayer.class.getDeclaredField("hoveredMonster");
            hoveredMonsterField.setAccessible(true);
        }
        catch (Exception e) {
            logger.error("Failed to initialize Field hoveredMonster of AbstractPlayer.");
            logger.error(e.getMessage());
        }
    }

    @SpirePostfixPatch
    public static void PostFix(CardGlowBorder __instance, AbstractCard c)
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (c.hasTag(CustomCardTags.MK_ECHO_ATTACK) && LastCardType.type == AbstractCard.CardType.ATTACK) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.RED.cpy());
            }
            if (c.hasTag(CustomCardTags.MK_ECHO_SKILL) && LastCardType.type == AbstractCard.CardType.SKILL) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.GREEN.cpy());
            }
            if (c.hasTag(CustomCardTags.MK_ECHO_POWER) && LastCardType.type == AbstractCard.CardType.POWER) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.GOLD.cpy());
            }

            if ((c.target == AbstractCard.CardTarget.ENEMY || c.target == AbstractCard.CardTarget.SELF_AND_ENEMY) &&
                    c.equals(AbstractDungeon.player.hoveredCard) && //avoid possibility of null hovered card error
                    c.hasTag(CustomCardTags.MK_BURST))
            {
                try {
                    AbstractMonster target = (AbstractMonster) hoveredMonsterField.get(AbstractDungeon.player);
                    if (target != null)
                    {
                        if (BurstActive.active.get(target))
                        {
                            ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.RED.cpy());
                        }
                    }
                    else
                    {
                        int amt = 0;
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                        {
                            if (!m.isDeadOrEscaped())
                            {
                                target = m;
                                ++amt;
                            }
                        }

                        if (amt == 1)
                        {
                            if (BurstActive.active.get(target))
                            {
                                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.RED.cpy());
                            }
                        }
                    }
                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
