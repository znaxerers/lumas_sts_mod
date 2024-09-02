//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.powers;

import TestMod.TestMod;
import TestMod.characters.Lumo;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import util.TextureLoader;

import java.util.ArrayList;

import static TestMod.TestMod.makePowerPath;

public class ChipPower extends AbstractPower implements CloneablePowerInterface {
    private static final String POWER_ID = TestMod.makeID("ChipPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("chip_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("chip_power32.png"));
    public ArrayList<Integer> counters = new ArrayList();


    public ChipPower(AbstractCreature owner, int amt) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = amt;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.canGoNegative = true;
    }

    public void applyChipPowerToLumo(int amt) {
        this.applyChipPowerToLumo(amt, true);
    }

    public void applyChipPowerToLumo(int amt, boolean code) {
        GenericHelper.addToBotAbstract(() -> {
            Lumo m = LumoPatch.Inst();
            if (m != null) {
                GenericHelper.addToNext(new ApplyPowerAction(m, m, new ChipPower(m, amt)));
            } else if (code) {
                //LumoPatch.ReduceReviveTime(1);
            }

        });
    }

    public String getDescription() {
        this.type = this.amount >= 0 ? PowerType.BUFF : PowerType.DEBUFF;
        if (this.type == PowerType.BUFF) {
            return DESCRIPTIONS[0];
        }
        return DESCRIPTIONS[1];
    }

    @Override
    public void updateDescription() {
        String des = this.getDescription();

        for(int i = 0; i < this.counters.size(); ++i) {
            des = des.replace(String.format("M%d", i), String.valueOf(this.counters.get(i)));
        }

        des = des.replace("M", String.valueOf(this.amount));
        this.description = des;
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type != DamageType.NORMAL && type != DamageType.HP_LOSS ? damage : damage + (float)this.amount;
    }

    public void atStartOfTurn() {
        Lumo mo = LumoPatch.Inst();
        if (mo != null) {
            if (mo.lumoLevel == 1) {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this, this.amount - 2));
                return;
            }

            if (mo.lumoLevel == 2) {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this, this.amount - 7));
                return;
            }
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        this.flash();
        this.updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}
