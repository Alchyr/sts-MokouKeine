package もこけね.patch.rewards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import もこけね.character.MokouKeine;
import もこけね.patch.combat.PotionUse;
import もこけね.util.HandleMatchmaking;
import もこけね.util.MultiplayerHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static もこけね.もこけねは神の国.logger;

public class ObtainPotions {
    private static ArrayList<AbstractPotion> canLose = new ArrayList<>();

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "obtainPotion",
            paramtypez = { int.class, AbstractPotion.class }
    )
    public static class OnObtainPotionSimple
    {
        @SpirePostfixPatch
        public static void postObtain(AbstractPlayer __instance, int slot, AbstractPotion p)
        {
            if (__instance instanceof MokouKeine && MultiplayerHelper.active)
                PotionUse.updatePotionInfo();
        }
    }
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "obtainPotion",
            paramtypez = { AbstractPotion.class }
    )
    public static class OnObtainPotion
    {
        @SpirePostfixPatch
        public static void postObtain(AbstractPlayer __instance, AbstractPotion p)
        {
            if (__instance instanceof MokouKeine && MultiplayerHelper.active)
                PotionUse.updatePotionInfo();
        }
    }

    @SpirePatch(
            clz = RewardItem.class,
            method = "claimReward"
    )
    public static class IndividualTakePotion
    {
        @SpirePostfixPatch
        public static boolean onClaimReward(boolean result, RewardItem __instance)
        {
            if (__instance.type == RewardItem.RewardType.POTION && result)
            {
                if (AbstractDungeon.player instanceof MokouKeine && MultiplayerHelper.active)
                {
                    if (HandleMatchmaking.isHost)
                    {
                        MultiplayerHelper.sendP2PString("claim_potion" + __instance.potion.ID);
                        MultiplayerHelper.sendP2PMessage(CardCrawlGame.playerName + " claimed " + __instance.potion.name + "!");
                    }
                    else
                    {
                        MultiplayerHelper.sendP2PString("try_claim_potion" + __instance.potion.ID);
                        canLose.add(__instance.potion);
                    }
                }
            }
            return result;
        }
    }

    public static boolean removePotionReward(String id)
    {
        if (AbstractDungeon.getCurrRoom() != null && !AbstractDungeon.combatRewardScreen.rewards.isEmpty())
        {
            int index = -1;
            for (int i = 0; i < AbstractDungeon.combatRewardScreen.rewards.size(); i++)
            {
                if (AbstractDungeon.combatRewardScreen.rewards.get(i).type == RewardItem.RewardType.POTION && AbstractDungeon.combatRewardScreen.rewards.get(i).potion.ID.equals(id))
                {
                    index = i;
                    break;
                }
            }
            if (index >= 0)
            {
                AbstractDungeon.combatRewardScreen.rewards.remove(index);
                AbstractDungeon.combatRewardScreen.positionRewards();

                try
                {
                    Method m = CombatRewardScreen.class.getDeclaredMethod("setLabel");
                    m.setAccessible(true);
                    m.invoke(AbstractDungeon.combatRewardScreen);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage());
                }

                return true;
            }
        }
        return false;
    }

    public static void confirmClaimPotion(String id)
    {
        int index = -1;
        for (AbstractPotion p : canLose)
        {
            if (p.ID.equals(id))
            {
                index = canLose.indexOf(p);
            }
        }
        if (index != -1)
        {
            MultiplayerHelper.sendP2PMessage(CardCrawlGame.playerName + " claimed " + canLose.get(index).name + "!");
            canLose.remove(index);
        }
    }
    public static void confirmLosePotion(String id)
    {
        int index = -1;
        for (AbstractPotion p : canLose)
        {
            if (p.ID.equals(id))
            {
                index = canLose.indexOf(p);
            }
        }
        if (index != -1)
        {
            if (AbstractDungeon.player.hasPotion(canLose.get(index).ID))
            {
                int i = 0;
                for (; i < AbstractDungeon.player.potions.size(); ++i)
                {
                    if (AbstractDungeon.player.potions.get(i).ID.equals(id))
                    {
                        AbstractDungeon.player.removePotion(AbstractDungeon.player.potions.get(i));
                        break;
                    }
                }
            }
            canLose.remove(index);
        }
    }
}
