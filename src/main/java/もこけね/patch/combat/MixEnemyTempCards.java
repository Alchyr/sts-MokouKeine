package もこけね.patch.combat;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import もこけね.character.MokouKeine;
import もこけね.effects.ShowCardAndAddToOtherDiscardEffect;

public class MixEnemyTempCards {
    public static boolean toMokou = true;

    private static AbstractGameAction lastAction = null;

    //Check if isPlayerTurn
    @SpirePatch(
            clz = MakeTempCardInDiscardAction.class,
            method = "update"
    )
    public static class changeDestination
    {
        @SpirePrefixPatch
        public static SpireReturn adjust(MakeTempCardInDiscardAction __instance)
        {
            if (AbstractDungeon.player instanceof MokouKeine && AbstractDungeon.actionManager.turnHasEnded && __instance != lastAction)
            {
                //This action occurred during enemy turn
                toMokou = !toMokou; //swap target
                lastAction = __instance; //don't check more than once
                if (toMokou ^ ((MokouKeine) AbstractDungeon.player).isMokou) //The target is other player
                {
                    float d = (float)ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
                    if (d == Settings.ACTION_DUR_FAST)
                    {
                        //do the stuff, but for other discard pile.
                        int amt = (int)ReflectionHacks.getPrivate(__instance, MakeTempCardInDiscardAction.class, "numCards");
                        AbstractCard base = (AbstractCard)ReflectionHacks.getPrivate(__instance, MakeTempCardInDiscardAction.class, "cardToMake");
                        AbstractCard c;

                        for(int i = 0; i < amt; ++i) {
                            c = base.makeStatEquivalentCopy();
                            AbstractDungeon.effectList.add(new ShowCardAndAddToOtherDiscardEffect(c));
                        }

                        ReflectionHacks.setPrivate(__instance, AbstractGameAction.class, "duration", d - Gdx.graphics.getDeltaTime());
                    }
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
