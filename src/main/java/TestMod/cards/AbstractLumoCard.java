package TestMod.cards;

import TestMod.characters.Lumo;
import TestMod.characters.TheLuma.Enums;
//import KaltsitMod.helper.EventHelper;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
//import KaltsitMod.powers.ChipPower;
//import KaltsitMod.powers.DrusePower;
import TestMod.powers.ChipPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.function.Function;

public abstract class AbstractLumoCard extends CustomCard { // implements EventHelper.CustomSubscriber {
    public int membershipCost = -1;
    public boolean paymentBool;

    public int baseSecondaryMagicNum;
    public int SecondaryMagicNum;
    public boolean upgradedSecondaryMagicNum;
    public boolean isSecondaryMagicNumModified;
    public CardStrings strings;

    public AbstractLumoCard(String ID, String IMG, CardStrings strings, int COST, AbstractCard.CardType TYPE, AbstractCard.CardRarity RARITY, AbstractCard.CardTarget TARGET) {
        super(ID, strings.NAME, IMG, COST, strings.DESCRIPTION, TYPE, Enums.COLOR_GRAY, RARITY, TARGET);
        this.strings = strings;
    }

    protected void setupDamage(int amt) {
        this.baseDamage = amt;
        this.damage = amt;
    }

    protected void setupBlock(int amt) {
        this.baseBlock = amt;
        this.block = amt;
    }

    protected void setupMagicNumber(int amt) {
        this.baseMagicNumber = amt;
        this.magicNumber = amt;
    }

    protected void setupSecondMagicNumber(int amt) {
        this.baseSecondaryMagicNum = amt;
        this.SecondaryMagicNum = amt;
    }

    protected void upgradeSecondMagicNumber(int amount) {
        this.baseSecondaryMagicNum += amount;
        this.SecondaryMagicNum = this.baseSecondaryMagicNum;
        this.upgradedSecondaryMagicNum = true;
    }

    public void applyPowers() {
        super.applyPowers();
    }

    public void receiveBattleStart() {
        //EventHelper.Subscribe(this);
    }

//    public void applyDruse(int amt, GenericHelper.VoidSupplier ifTrigger, GenericHelper.VoidSupplier notTrigger) {
//        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrusePower(AbstractDungeon.player, amt)));
//        if (ifTrigger != null) {
//            DrusePower.IfTriggerList.add(ifTrigger);
//        }
//
//        if (notTrigger != null) {
//            DrusePower.NotTriggerList.add(notTrigger);
//        }
//
//    }

//    public boolean ifTriggerDruse(int amt) {
//        return this.ifTriggerDruse(amt, true);
//    }
//
//    public boolean ifTriggerDruse(int amt, boolean flag) {
//        int amount = 0;
//        int max = 4;
//        if (AbstractDungeon.player.hasPower(DrusePower.id)) {
//            DrusePower p = (DrusePower)AbstractDungeon.player.getPower(DrusePower.id);
//            amount = p.amount;
//            max = (Integer)p.counters.get(0);
//        }
//
//        if (flag) {
//            return amount + amt >= max;
//        } else {
//            return amount + amt < max;
//        }
//    }

    public void applyLumoFirst(Function<AbstractCreature, AbstractPower> func) {
        this.applyLumoFirst(func, true);
    }

    public void applyLumoFirst(Function<AbstractCreature, AbstractPower> func, boolean code) {
        GenericHelper.addToBotAbstract(() -> {
            if (code) {
                //LumoPatch.ReduceReviveTime(1);
            }

            Lumo m = LumoPatch.Inst();
            if (m != null) {
                GenericHelper.addToNext(new ApplyPowerAction(m, m, (AbstractPower)func.apply(m)));
            } else {
                GenericHelper.addToNext(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, (AbstractPower)func.apply(AbstractDungeon.player)));
            }

        });
    }

    public void applyChipPowerToLumo(int amt) {
        this.applyChipPowerToLumo(amt, true);
    }

    public void applyChipPowerToLumo(int amt, boolean code) {
        GenericHelper.addToBotAbstract(() -> {
            if (code) {
                //LumoPatch.ReduceReviveTime(1);
            }

            Lumo m = LumoPatch.Inst();
            if (m != null) {
                GenericHelper.addToNext(new ApplyPowerAction(m, m, new ChipPower(m, amt)));
            }

        });
    }

    public void upgradeLumo() {

    }

    public void limitedUpgrade() {
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.limitedUpgrade();
            this.initializeDescription();
        }
    }
}

