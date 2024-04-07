package TestMod.patches;

import TestMod.characters.Lumo;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch.LumoFields;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.HandDrill;
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
        AbstractCreature c = (AbstractCreature)LumoFields.Lumo.get(AbstractDungeon.player);
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

    @SpirePatch(
            clz = HandDrill.class,
            method = "onBlockBroken"
    )
    public static class HandDrillPatch {
        public HandDrillPatch() {
        }

        public static SpireReturn<Void> Prefix(HandDrill _inst, AbstractCreature c) {
            return c instanceof Lumo ? SpireReturn.Return(null) : SpireReturn.Continue(); // SpireReturn.Return((Object)null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = Burn.class,
            method = "use"
    )
    public static class BurnPatch {
        public BurnPatch() {
        }

        public static SpireReturn<Void> Prefix(Burn _inst, AbstractPlayer p, AbstractMonster m) {
            if (_inst.dontTriggerOnUseCard) {
                GenericHelper.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo((AbstractCreature)null, _inst.magicNumber, DamageType.THORNS), AttackEffect.FIRE));
            }

            return SpireReturn.Return(null); // (Object)null);
        }
    }

    @SpirePatch(
            clz = GainBlockAction.class,
            method = "update"
    )
    public static class GainBlockPatch {
        public GainBlockPatch() {
        }

        public static void Prefix(GainBlockAction _inst) {
            if (_inst.target == AbstractDungeon.player && GenericHelper.isAlive(_inst.target) && (Float)ReflectionHacks.getPrivate(_inst, AbstractGameAction.class, "duration") == (Float)ReflectionHacks.getPrivate(_inst, AbstractGameAction.class, "startDuration") && LumoTakeDamagePatch.getTarget()) {
                _inst.target = LumoTakeDamagePatch.target;
                LumoTakeDamagePatch.logger.info("获得格挡改变了目标。目前：" + _inst.target.name);
            }

        }
    }

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "<ctor>",
            paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
    )
    public static class ChangeApplyBuffTarget {
        public ChangeApplyBuffTarget() {
        }

        public static void Postfix(ApplyPowerAction _inst, AbstractCreature target, AbstractCreature source, AbstractPower power, int n, boolean b, AbstractGameAction.AttackEffect e) {
            if (_inst instanceof ApplyPowerAction && (source == null || !source.isPlayer) && target != null && target != source && (target == AbstractDungeon.player || power.owner == AbstractDungeon.player) && LumoTakeDamagePatch.acceptDebuffs.contains(power.ID) && LumoTakeDamagePatch.getTarget()) {
                _inst.target = LumoTakeDamagePatch.target;
                power.owner = _inst.target;
                LumoTakeDamagePatch.logger.info("本给予 " + target.name + " 的能力给予能力改变了目标。目前：target:" + _inst.target.name + ",owner:" + power.owner);
            }

        }
    }

    @SpirePatch(
            clz = AbstractGameAction.class,
            method = "setValues",
            paramtypez = {AbstractCreature.class, DamageInfo.class}
    )
    public static class ChangeDamageTarget {
        public ChangeDamageTarget() {
        }

        public static void Postfix(AbstractGameAction _inst, AbstractCreature target, DamageInfo info) {
            if (target != null && info.type != DamageType.HP_LOSS && (info.owner == null || !info.owner.isPlayer) && target == AbstractDungeon.player && LumoTakeDamagePatch.getTarget()) {
                _inst.target = LumoTakeDamagePatch.target;
                LumoTakeDamagePatch.logger.info(LumoTakeDamagePatch.target + "承受伤害");
            } else {
                LumoTakeDamagePatch.logger.info("你承受伤害");
            }

        }
    }
}

