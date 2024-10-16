//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.actions;

import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Iterator;
import java.util.Objects;

public class AspirationWishAction extends AbstractGameAction {
    AbstractCard card;

    public AspirationWishAction(AbstractCard callingCard) {
        this.card = callingCard;
        this.target = AbstractDungeon.player;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var1.next();
            Iterator var3 = mo.powers.iterator();

            while(var3.hasNext()) {
                AbstractPower p = (AbstractPower) var3.next();
                if (Objects.equals(p.ID, "TestMod:TalismanPower")) {
                    int count = p.amount;
                    int times = count;
                    for (int i = 0; i < times; i++) {
                        this.addToBot(new GainBlockAction(this.target, this.target, 1));
                    }
                }
            }
        }

        this.isDone = true;
    }
}
