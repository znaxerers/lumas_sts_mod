package TestMod.patches;

import KaltsitMod.helper.GenericHelper;
import KaltsitMod.patches.MonsterPatch.Mon3trFields;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LumoTakeDamagePatch {
    private static final Logger logger = LogManager.getLogger(LumoTakeDamagePatch.class.getName());
    public static AbstractCreature target;
    private static final ArrayList<String> acceptDebuffs = new ArrayList();

    public LumoTakeDamagePatch() {
    }

    public static boolean getTarget() {
        AbstractCreature c = (AbstractCreature)Mon3trFields.Mon3tr.get(AbstractDungeon.player);
        if (c != null && GenericHelper.isAlive(c)) {
            target = c;
            logger.info(target.name + "承受伤害");
            return true;
        } else {
            target = null;
            logger.info("getTarget:你承受伤害");
            return false;
        }
    }

    static {
        acceptDebuffs.add("Weakened");
        acceptDebuffs.add("Vulnerable");
        acceptDebuffs.add("Poison");
    }
}

