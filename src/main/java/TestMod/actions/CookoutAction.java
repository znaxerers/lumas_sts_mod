//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.actions;

import TestMod.powers.MembershipPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

import java.util.Iterator;
import java.util.Objects;

public class CookoutAction extends AbstractGameAction {
    private boolean freeToPlayOnce = false;
    private int energyOnUse = -1;
    private int multiplier = -1;
    private AbstractPlayer p;

    public CookoutAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse, int multiplier) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.multiplier = multiplier;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        effect *= multiplier;

        if (effect > 0) {
            for(int i = 0; i < effect; ++i) {
                this.addToBot(new VFXAction(p, new InflameEffect(p), 0.01F));
                this.addToBot(new ApplyPowerAction(p, p, new MembershipPower(p, 1), 1));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
