package もこけね.patch.input;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtBehavior;
import もこけね.patch.enums.CharacterEnums;
import もこけね.patch.lobby.HandleMatchmaking;
import もこけね.util.MultiplayerHelper;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "playCard"
)
public class PlayCardCheck {
    @SpireInsertPatch (
            locator = Locator.class
    )
    public static SpireReturn playAlt(AbstractPlayer __instance)
    {
        if (__instance.chosenClass == CharacterEnums.MOKOUKEINE)
        {
            if (HandleMatchmaking.isHost)
            {
                if (__instance.hoveredCard.target != AbstractCard.CardTarget.ENEMY && __instance.hoveredCard.target != AbstractCard.CardTarget.SELF_AND_ENEMY)
                {
                    MultiplayerHelper.sendP2PString("other_play_card" + AbstractDungeon.player.hand.group.indexOf(__instance.hoveredCard) + " -1 " + __instance.hoveredCard.current_x + " " + __instance.hoveredCard.current_y);
                }
                else
                {
                    AbstractMonster target = (AbstractMonster)ReflectionHacks.getPrivate(__instance, AbstractPlayer.class, "hoveredMonster");
                    MultiplayerHelper.sendP2PString("other_play_card" + AbstractDungeon.player.hand.group.indexOf(__instance.hoveredCard) + " " + (target != null ? AbstractDungeon.getMonsters().monsters.indexOf(target) : "-1") + " " + __instance.hoveredCard.current_x + " " + __instance.hoveredCard.current_y);
                }
                //send play card message to other player
            }
            else //this isn't host - has to get permission in order to ensure everything occurs in order.
            {
                if (__instance.hoveredCard.target != AbstractCard.CardTarget.ENEMY && __instance.hoveredCard.target != AbstractCard.CardTarget.SELF_AND_ENEMY)
                {
                    MultiplayerHelper.sendP2PString("try_play_card" + AbstractDungeon.player.hand.group.indexOf(__instance.hoveredCard) + " -1 " + __instance.hoveredCard.current_x + " " + __instance.hoveredCard.current_y);
                }
                else
                {
                    AbstractMonster target = (AbstractMonster)ReflectionHacks.getPrivate(__instance, AbstractPlayer.class, "hoveredMonster");
                    MultiplayerHelper.sendP2PString("try_play_card" + AbstractDungeon.player.hand.group.indexOf(__instance.hoveredCard) + " " + (target != null ? AbstractDungeon.getMonsters().monsters.indexOf(target) : "-1") + " " + __instance.hoveredCard.current_x + " " + __instance.hoveredCard.current_y);
                }
                //send play card message to other player
                ReflectionHacks.setPrivate(__instance, AbstractPlayer.class, "isUsingClickDragControl", false); //handle rest of method
                __instance.hoveredCard = null;
                __instance.isDraggingCard = false;
                return SpireReturn.Return(null); //prevent playing of card
            }
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
            return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
