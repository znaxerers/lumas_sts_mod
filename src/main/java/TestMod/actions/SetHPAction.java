package TestMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class SetHPAction extends AbstractGameAction {
    public SetHPAction(AbstractCreature target) {
        this.source = null;
        this.target = target;
    }

    public void update() {
        this.target.currentHealth = 15;
        this.target.healthBarUpdatedEvent();
        this.target.damage(new DamageInfo((AbstractCreature)null, 0, DamageInfo.DamageType.HP_LOSS)); //don't know what base does
        this.isDone = true;
    }
}
