package TestMod.patches;

import KaltsitMod.character.CalciteModel;
import KaltsitMod.character.Mon3tr;
import KaltsitMod.character.Kaltsit.Enums;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.Objects;

public class LumoPatch {
    public static final int REVIVE_TURNS = 5;
    public static int ReviveTimer = 0;

    public LumoPatch() {
    }

    public static Lumo Inst() {
        return AbstractDungeon.player == null ? null : (Mon3tr)KaltsitMod.patches.MonsterPatch.Mon3trFields.Mon3tr.get(AbstractDungeon.player);
    }

    public static void ReduceReviveTime(int amt) {
        if (AbstractDungeon.player.chosenClass == Enums.KALTSIT || AbstractDungeon.player.hasRelic("PrismaticShard")) {
            ReviveTimer -= amt;
            if (Inst() == null) {
                if (ReviveTimer <= 0) {
                    ReviveTimer = 0;
                    KaltsitMod.patches.MonsterPatch.Mon3trFields.Mon3tr.set(AbstractDungeon.player, new Mon3tr());
                    ((Mon3tr)Objects.requireNonNull(Inst())).spawn();
                }

                CalciteModel mod = (CalciteModel)KaltsitMod.patches.MonsterPatch.Mon3trFields.Calcite.get(AbstractDungeon.player);
                if (mod != null) {
                    mod.reduce();
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
}

