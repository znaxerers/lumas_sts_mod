package TestMod.patches;

import TestMod.characters.Lumo;
import TestMod.characters.TheLuma.Enums;
import TestMod.helper.GenericHelper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.util.ArrayList;
import java.util.Objects;
import javassist.CtBehavior;

public class LumoPatch {
    public static final int REVIVE_TURNS = 5;
    public static int ReviveTimer = 0;

    public LumoPatch() {
    }

    public static Lumo Inst() {
        return AbstractDungeon.player == null ? null : (Lumo)TestMod.patches.LumoPatch.LumoFields.Lumo.get(AbstractDungeon.player);
    }

    public static void ReduceReviveTime(int amt) {
        if (AbstractDungeon.player.chosenClass == Enums.THE_LUMA || AbstractDungeon.player.hasRelic("PrismaticShard")) {
            ReviveTimer -= amt;
            if (Inst() == null) {
                if (ReviveTimer <= 0) {
                    ReviveTimer = 0;
                    TestMod.patches.LumoPatch.LumoFields.Lumo.set(AbstractDungeon.player, new Lumo());
                    ((Lumo)Objects.requireNonNull(Inst())).spawn();
                }

                int roll = MathUtils.random(0, 2);
                if (roll == 0) {
                    CardCrawlGame.sound.play("BUFF_1");
                } else if (roll == 1) {
                    CardCrawlGame.sound.play("BUFF_2");
                } else {
                    CardCrawlGame.sound.play("BUFF_3");
                }
            }
        }

    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "<class>"
    )
    public static class LumoFields {
        public static SpireField<Lumo> Lumo = new SpireField(() -> {
            return null;
        });

        public LumoFields() {
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderHoverReticle"
    )
    public static class ReticlePatch {
        public ReticlePatch() {
        }

        public static void Postfix(AbstractPlayer _inst, SpriteBatch sb) {
            Lumo m;
            if (_inst.hoveredCard.target == Enums.SELF_AND_LUMO) {
                _inst.renderReticle(sb);
                m = LumoPatch.Inst();
                if (m != null) {
                    m.renderReticle(sb);
                }
            } else if (_inst.hoveredCard.target == Enums.LUMO) {
                m = LumoPatch.Inst();
                if (m != null) {
                    m.renderReticle(sb);
                }
            }

        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "renderTip"
    )
    public static class RenderTipPatch {
        public RenderTipPatch() {
        }

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractMonster _inst, SpriteBatch sb) {
            if (_inst instanceof Lumo) {
                ((Lumo)_inst).addTip();
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[0]};
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "onModifyPower"
    )
    public static class OnModifyPowerPatch {
        public OnModifyPowerPatch() {
        }

        public static void Postfix() {
            if (AbstractDungeon.player != null && AbstractDungeon.getMonsters() != null) {
                Lumo m = LumoPatch.Inst();
                if (m != null) {
                    m.applyPowers();
                }
            }

        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "callEndOfTurnActions"
    )
    public static class EndOfTurnPatch {
        public EndOfTurnPatch() {
        }

        public static void Postfix(GameActionManager _inst) {
            Lumo m = LumoPatch.Inst();
            if (m != null) {
                m.takeTurn();
                m.applyEndOfTurnTriggers();
            }

        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class StartOfTurnPatch {
        public StartOfTurnPatch() {
        }

        @SpireInsertPatch(
                rloc = 240
        )
        public static void Insert(GameActionManager _inst) {
            Lumo m = LumoPatch.Inst();
            if (m != null) {
                if (!m.hasPower("Barricade") && !m.hasPower("Blur")) {
                    m.loseBlock();
                }

                m.applyStartOfTurnPowers();
                m.applyStartOfTurnPostDrawPowers();
                GenericHelper.addToBotAbstract(() -> {
                    m.gainBlockNextTurn = false;
                    m.damageToAllNextTurn = false;
                    m.damageToLowestNextTurn = false;
                    m.meltdownNextTurn = false;
                    m.damageTimes = 1;
                    m.applyPowers();
                });
            } else {
                //LumoPatch.ReduceReviveTime(1);
                //dont want the lumo to be automatically summoned
            }

        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyPreCombatLogic"
    )
    public static class MonsterStartOfBattlePatch {
        public MonsterStartOfBattlePatch() {
        }

        public static void Postfix(AbstractPlayer _inst) {
            if (AbstractDungeon.player.chosenClass != Enums.THE_LUMA && !AbstractDungeon.player.hasRelic("PrismaticShard")) {
                LumoPatch.LumoFields.Lumo.set(AbstractDungeon.player, (Lumo)null); //(Object)null);
                //MonsterPatch.Mon3trFields.Calcite.set(AbstractDungeon.player, (Object)null);
            } else {
                LumoPatch.LumoFields.Lumo.set(AbstractDungeon.player, (Lumo)null); //(Object)null);
                //MonsterPatch.Mon3trFields.Calcite.set(AbstractDungeon.player, new CalciteModel());
//                if (_inst.hasRelic(Calcite.ID)) {
                LumoPatch.ReviveTimer = 2;
//                } else {
//                    LumoPatch.ReviveTimer = 5;
//                }
            }

        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class MonsterRenderPatch {
        public MonsterRenderPatch() {
        }

        @SpireInsertPatch(
                rloc = 12
        )
        public static void Insert(AbstractRoom _inst, SpriteBatch sb) {
            if (_inst.monsters != null) {
                Lumo m = LumoPatch.Inst();
                if (m != null) {
                    m.render(sb);
                } //else {
//                    CalciteModel mod = (CalciteModel)MonsterPatch.Mon3trFields.Calcite.get(AbstractDungeon.player);
//                    if (mod != null) {
//                        mod.render(sb);
//                    }
//                }
            }

        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "update"
    )
    public static class MonsterUpdatePatch {
        public MonsterUpdatePatch() {
        }

        public static void Postfix(AbstractRoom _inst) {
            Lumo m = LumoPatch.Inst();
            if (m != null) {
                m.update();
                m.updateAnimations();
                if (m.isDead) {
                    LumoPatch.LumoFields.Lumo.set(AbstractDungeon.player, (Lumo)null); //(Object)null);
                }
            } //else {
//                CalciteModel mod = (CalciteModel)MonsterPatch.Mon3trFields.Calcite.get(AbstractDungeon.player);
//                if (mod != null) {
//                    mod.update();
//                }
//            }

        }
    }
}

