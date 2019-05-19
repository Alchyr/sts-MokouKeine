package もこけね.relics;

import もこけね.abstracts.BaseRelic;

import static もこけね.もこけねは神の国.makeID;

public class DullShard extends BaseRelic {
    private static final String ID = makeID("DullShard");
    private static final String IMG = "prism";

    public DullShard()
    {
        super(ID, IMG, RelicTier.SPECIAL, LandingSound.CLINK);
    }
}
