package もこけね.abstracts;
/*
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtClass;
import org.clapper.util.classutil.*;
import もこけね.cards.colorless.FantasyCard;
import もこけね.util.FantasyEffectFilter;
import もこけね.もこけねは神の国;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static もこけね.もこけねは神の国.logger;

public abstract class FantasyEffect {
    public static ArrayList<FantasyEffect> baseAttackEffects = new ArrayList<>();
    public static ArrayList<FantasyEffect> baseSkillEffects = new ArrayList<>();
    public static ArrayList<FantasyEffect> bonusAttackEffects = new ArrayList<>();
    public static ArrayList<FantasyEffect> bonusSkillEffects = new ArrayList<>();

    public static HashMap<FantasyEffect, Integer> effectIDs = new HashMap<>(); //effect ID is used to build a unique image string for each card.

    public abstract boolean isUsable(FantasyCard base);
    public abstract void initialize(FantasyCard c);
    public abstract void applyPowers(FantasyCard c);
    public abstract void calculateCardDamage(FantasyCard c, AbstractMonster m);
    public abstract void use(FantasyCard c, AbstractPlayer p, AbstractMonster m);

    public abstract String getDescription(FantasyCard c);

    public abstract Texture getTexture(FantasyCard c);
    public abstract Texture getPortraitTexture(FantasyCard c);

    public abstract TARGET_TYPE getTargetType();

    public abstract int register(int id);

    //public abstract TARGET_TYPE getTargetType();

    public static ArrayList<FantasyEffect> getBaseOptions(FantasyCard base)
    {
        ArrayList<FantasyEffect> validOptions = new ArrayList<>();

        switch (base.type)
        {
            case ATTACK:
                for (FantasyEffect e : baseAttackEffects)
                {
                    if (e.isUsable(base))
                        validOptions.add(e);
                }
                break;
            case SKILL:
                for (FantasyEffect e : baseSkillEffects)
                {
                    if (e.isUsable(base))
                        validOptions.add(e);
                }
                break;
        }

        return validOptions;
    }
    public static ArrayList<FantasyEffect> getBonusOptions(FantasyCard base)
    {
        ArrayList<FantasyEffect> validOptions = new ArrayList<>();

        switch (base.type)
        {
            case ATTACK:
                for (FantasyEffect e : bonusAttackEffects)
                {
                    if (e.isUsable(base))
                        validOptions.add(e);
                }
                break;
            case SKILL:
                for (FantasyEffect e : bonusSkillEffects)
                {
                    if (e.isUsable(base))
                        validOptions.add(e);
                }
                break;
        }

        return validOptions;
    }





    public static void initializeEffectLists()
    {
        baseAttackEffects.clear();
        baseSkillEffects.clear();
        bonusAttackEffects.clear();
        bonusSkillEffects.clear();
        effectIDs.clear();

        try
        {
            int id = 0;


            ClassFinder finder = new ClassFinder();
            URL url = もこけねは神の国.class.getProtectionDomain().getCodeSource().getLocation();
            finder.add(new File(url.toURI()));

            ClassFilter filter =
                    new AndClassFilter(
                            new NotClassFilter(new InterfaceOnlyClassFilter()),
                            new NotClassFilter(new AbstractClassFilter()),
                            new ClassModifiersClassFilter(Modifier.PUBLIC),
                            new FantasyEffectFilter()
                    );
            Collection<ClassInfo> foundClasses = new ArrayList<>();
            finder.findClasses(foundClasses, filter);

            for (ClassInfo classInfo : foundClasses) {
                CtClass cls = Loader.getClassPool().get(classInfo.getClassName());

                boolean isEffect = false;

                CtClass superCls = cls;
                while (superCls != null) {
                    superCls = superCls.getSuperclass();
                    if (superCls == null) {
                        break;
                    }
                    if (superCls.getName().equals(FantasyEffect.class.getName())) {
                        isEffect = true;
                        break;
                    }
                }
                if (!isEffect) {
                    continue;
                }

                FantasyEffect effect = (FantasyEffect) Loader.getClassPool().toClass(cls).newInstance();
                id = effect.register(id);
            }
        }
        catch (Exception e)
        {
            logger.error("Error occurred while initializing FantasyEffects.");
            logger.error(e.getMessage());
        }
    }

    public enum TARGET_TYPE {
        SINGLE,
        RANDOM,
        ALL,
        NONE
    }
}*/