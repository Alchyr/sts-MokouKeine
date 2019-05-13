package もこけね.patch.deck_changes;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.daily.mods.Hoarder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import もこけね.character.MokouKeine;
import もこけね.util.MultiplayerHelper;

import java.io.Serializable;

import static もこけね.util.MessageHelper.cardInfoString;
import static もこけね.もこけねは神の国.chat;
import static もこけね.もこけねは神の国.logger;

public class ReportObtainCard implements Serializable {
    @SpirePatch(
            clz = Soul.class,
            method = "obtain"
    )
    public static class OnObtainCard
    {
        @SpirePrefixPatch
        public static void ReportObtain(Soul __instance, AbstractCard c)
        {
            String msg = "other_obtain_card" + cardInfoString(c);

            MultiplayerHelper.sendP2PString(msg);
            if (ModHelper.isModEnabled(Hoarder.ID)) {
                MultiplayerHelper.sendP2PString(msg);
                MultiplayerHelper.sendP2PString(msg);
            }
        }
    }

    public static void receiveOtherObtainCard(String cardInfo)
    {
        if (AbstractDungeon.player instanceof MokouKeine)
        {
            try
            {
                String[] args = cardInfo.split("\\|\\|\\|");
                AbstractCard c = CardLibrary.getCopy(args[0]);

                if (args[1].equals("1"))
                {
                    for (int i = 0; i < Integer.valueOf(args[2]); ++i)
                    {
                        c.upgrade();
                    }
                }

                String name = c.originalName;
                if (!args[3].equals("!!!"))
                    name = args[3] + name;
                if (!args[4].equals("!!!"))
                    name += args[4];

                c.name = name;
                c.costForTurn = c.cost = Integer.valueOf(args[5]);
                c.damage = c.baseDamage = Integer.valueOf(args[6]);
                c.block = c.baseBlock = Integer.valueOf(args[7]);
                c.magicNumber = c.baseMagicNumber = Integer.valueOf(args[8]);

                ((MokouKeine) AbstractDungeon.player).otherPlayerMasterDeck.addToTop(c);
                chat.receiveMessage("Other player obtained " + c.name);
            }
            catch (Exception e)
            {
                logger.info("Received invalid card information! !Desync! !warning!");
            }
        }
    }

}
