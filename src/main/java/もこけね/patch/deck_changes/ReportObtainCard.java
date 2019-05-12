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

    private static String cardInfoString(AbstractCard c)
    {
        //Format:
        //cardID upgraded(1/0) timesUpgraded prefix suffix cost basedamage baseblock basemagic
        StringBuilder sb = new StringBuilder(c.cardID).append("|||");

        sb.append(c.upgraded ? 1 : 0).append("|||");
        sb.append(c.timesUpgraded).append("|||");

        if (c.name.equals(c.originalName))
        {
            sb.append("!!!|||!!!|||");
        }
        else if (c.name.contains(c.originalName))
        {
            String[] parts = c.name.split(c.originalName);
            if (parts.length == 2)
            {
                if (parts[0].isEmpty())
                    sb.append("!!!|||");
                else
                    sb.append(parts[0]).append("|||");

                if (parts[1].isEmpty())
                    sb.append("!!!|||");
                else
                    sb.append(parts[1]).append("|||");
            }
            else //it contains original name multiple times??? or somehow, not at all? No idea how to handle that, so just use the original name.
            {
                sb.append("!!!|||!!!|||");
            }
        }
        else
        {
            sb.append("!!!|||!!!|||");
        }

        sb.append(c.cost).append("|||");
        sb.append(c.baseDamage).append("|||");
        sb.append(c.baseBlock).append("|||");
        sb.append(c.baseMagicNumber);

        return sb.toString();
    }
}
