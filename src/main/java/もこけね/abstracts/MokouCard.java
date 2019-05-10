package もこけね.abstracts;

import もこけね.patch.enums.CharacterEnums;
import もこけね.util.CardInfo;

public abstract class MokouCard extends BaseCard {
    public MokouCard(CardInfo info, boolean upgradesDescription)
    {
        super(CharacterEnums.MOKOU, info, upgradesDescription);
    }
}
