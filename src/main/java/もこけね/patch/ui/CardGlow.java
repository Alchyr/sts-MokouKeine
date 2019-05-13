package もこけね.patch.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import もこけね.patch.card_use.LastCardType;
import もこけね.patch.enums.CustomCardTags;

@SpirePatch(
        clz = CardGlowBorder.class,
        method = SpirePatch.CONSTRUCTOR
)
public class CardGlow {
    @SpirePostfixPatch
    public static void PostFix(CardGlowBorder __instance, AbstractCard c)
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (c.hasTag(CustomCardTags.ECHO_ATTACK) && LastCardType.type == AbstractCard.CardType.ATTACK) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.RED.cpy());
            }
            if (c.hasTag(CustomCardTags.ECHO_SKILL) && LastCardType.type == AbstractCard.CardType.SKILL) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.GREEN.cpy());
            }
            if (c.hasTag(CustomCardTags.ECHO_POWER) && LastCardType.type == AbstractCard.CardType.POWER) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", Color.GOLD.cpy());
            }
        }
    }
}
