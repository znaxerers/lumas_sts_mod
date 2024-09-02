package TestMod.actions;

import TestMod.powers.MembershipPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RibbonOfFateAction extends AbstractGameAction {
    private float startingDuration;
    private boolean isUpgraded;

    public RibbonOfFateAction(boolean upgraded) {
        this.target = AbstractDungeon.player;
        this.actionType = ActionType.WAIT;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
        this.isUpgraded = upgraded;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            int count = AbstractDungeon.player.hand.size();
            if (this.isUpgraded) {
                this.addToTop(new ApplyPowerAction(this.target, this.target, new MembershipPower(this.target, count*2), count*2));
                this.addToTop(new DrawCardAction(this.target, 1));
                this.addToTop(new MakeTempCardInDiscardAction(new Wound(), 1));
                this.addToTop(new DiscardAction(this.target, this.target, count, true));
            } else if (count != 0) {
                this.addToTop(new ApplyPowerAction(this.target, this.target, new MembershipPower(this.target, count*2), count*2));
                this.addToTop(new DrawCardAction(this.target, 1));
                this.addToTop(new MakeTempCardInDiscardAction(new Wound(), 1));
                this.addToTop(new DiscardAction(this.target, this.target, count, true));
            }

            this.isDone = true;
        }

    }
}

