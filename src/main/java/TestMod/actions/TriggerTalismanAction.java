//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.actions;

import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.Iterator;
import java.util.Objects;

public class TriggerTalismanAction extends AbstractGameAction {
    AbstractCard card;

    public TriggerTalismanAction(AbstractCard callingCard) {
        this.card = callingCard;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var1.next();
            Iterator var3 = mo.powers.iterator();

            while(var3.hasNext()) {
                AbstractPower p = (AbstractPower) var3.next();
                if (Objects.equals(p.ID, "TestMod:TalismanPower")) {
                    TalismanPower p1 = (TalismanPower) p;
                    p1.triggerTalisman(this.card);
                }
            }
        }

        this.isDone = true;
    }
}
