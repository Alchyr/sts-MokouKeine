package もこけね.abstracts;

import もこけね.patch.enums.CharacterEnums;
import もこけね.util.CardInfo;

public abstract class KeineCard extends BaseCard {
    public KeineCard(CardInfo info, boolean upgradesDescription)
    {
        super(CharacterEnums.KEINE, info, upgradesDescription);
    }
}