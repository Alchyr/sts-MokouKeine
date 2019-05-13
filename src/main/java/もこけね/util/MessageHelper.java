package もこけね.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class MessageHelper {
    public static String cardInfoString(AbstractCard c)
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
