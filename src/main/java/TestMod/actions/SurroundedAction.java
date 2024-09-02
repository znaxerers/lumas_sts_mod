//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.actions;

import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

import java.util.Iterator;

public class SurroundedAction extends AbstractGameAction {
    private DamageInfo info;

    public SurroundedAction(AbstractCreature source) {
        this.actionType = ActionType.BLOCK;
    }

    public void update() {
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();
            if (mo != null && mo.hasPower("TestMod:TalismanPower")) {
                int count = mo.getPower("TestMod:TalismanPower").amount;

                this.addToTop(new ApplyPowerAction(mo, source, new ConstrictedPower(mo, source, count), count));
                this.addToTop(new RemoveSpecificPowerAction(mo, source, "TestMod:TalismanPower"));
            }
        }

        this.isDone = true;
    }
}
