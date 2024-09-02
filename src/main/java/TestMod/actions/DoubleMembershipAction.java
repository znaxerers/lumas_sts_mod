package TestMod.actions;

import TestMod.powers.MembershipPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DoubleMembershipAction extends AbstractGameAction {

    public DoubleMembershipAction(AbstractCreature target, AbstractCreature source) {
        this.target = target;
        this.source = source;
        this.actionType = ActionType.DEBUFF;
        this.attackEffect = AttackEffect.FIRE;
    }

    public void update() {
        if (this.target != null && this.target.hasPower("TestMod:MembershipPower")) {
            this.addToTop(new ApplyPowerAction(this.target, this.source, new MembershipPower(this.target, this.target.getPower("TestMod:MembershipPower").amount), this.target.getPower("TestMod:MembershipPower").amount));
        }

        this.isDone = true;
    }

}
