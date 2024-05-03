package TestMod.actions;

import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DoubleTalismanAction extends AbstractGameAction {

    public DoubleTalismanAction(AbstractCreature target, AbstractCreature source) {
        this.target = target;
        this.source = source;
        this.actionType = ActionType.DEBUFF;
        this.attackEffect = AttackEffect.FIRE;
    }

    public void update() {
        if (this.target != null && this.target.hasPower("TestMod:TalismanPower")) {
            this.addToTop(new ApplyPowerAction(this.target, this.source, new TalismanPower(this.target, this.source, this.target.getPower("TestMod:TalismanPower").amount), this.target.getPower("TestMod:TalismanPower").amount));
        }

        this.isDone = true;
    }

}
