package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
import TestMod.patches.LumoPatch;
import TestMod.powers.MembershipPower;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static TestMod.TestMod.makeCardPath;
// "How come this card extends CustomCard and not DynamicCard like all the rest?"
// Skip this question until you start figuring out the AbstractDefaultCard/AbstractDynamicCard and just extend DynamicCard
// for your own ones like all the other cards.

// Well every card, at the end of the day, extends CustomCard.
// Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
// bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
// Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
// the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
// Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately to showcase custom cards/inheritance a bit more.

public class ManualLabour extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Manual Labour Gain 9 (13) Block. Payment 3: Summon Bootleg Fumo.
     */

    // TEXT DECLARATION

    public static final String ID = TestMod.makeID(ManualLabour.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ManualLabour_Skill.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name
    // and make sure it's in the correct folder. That's all.
    // There's makeCardPath, makeRelicPath, power, orb, event, etc..
    // The list of all of them can be found in the main DefaultMod.java file in the
    // ==INPUT TEXTURE LOCATION== section under ==MAKE IMAGE PATHS==


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;
    //public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 1;
    private static final int BLOCK = 9;
    private static final int MEMBERSHIP_REQUIRED = 3;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out DefaultAttackWithVariable and theDefault.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variables they personally have and create your own ones.

    // /STAT DECLARATION/

    // IMPORTANT NOTE: If you add parameters to your constructor, you'll crash the auto-add cards with a
    // `NoSuchMethodException` because it except a constructor with no params.
    // (If you don't know what a constructor or params are or what not pls google, java questions = java study)
    // You have two option:
    // 1. Create a new constructor with empty parameters call your custom one with default params in it
    // 2. Mark the card with @AutoAdd.NotSeen (https://github.com/daviscook477/BaseMod/wiki/AutoAdd) to prevent it from
    // being auto-add it, and then load it manually with
    // BaseMod.addCard(new DefaultCommonAttack());
    // UnlockTracker.unlockCard(DefaultCommonAttack.ID);
    // in your main class, in the receiveEditCards() method

    public ManualLabour() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        // Aside from baseDamage/MagicNumber/Block there's also a few more.
        // Just type this.base and let intelliJ auto complete for you, or, go read up AbstractCard

        // this changes card border art
        //this.setBackgroundTexture("img/custom_background_small.png", "img/custom_background_large.png");

        baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));

        if (canPayment()) {
            LumoPatch.ReduceReviveTime(5);
            if (p.hasPower("TestMod:ClubPreservationPower")) {
                p.getPower("TestMod:ClubPreservationPower").flash();
                return;
            }
            this.addToBot(new ReducePowerAction(p, p, "TestMod:MembershipPower", MEMBERSHIP_REQUIRED));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (canPayment()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void superUse(AbstractPlayer p, AbstractMonster m) {
        if (this.canPayment()) {
            //this.bloodUse(p, m);
        }
    }

    public void applyPowers() {
        super.applyPowers();
//        AbstractPlayer p = AbstractDungeon.player;
//        if (this.baseAttackTime > 0) {
//            this.attackTime = this.baseAttackTime;
//            if (p.hasRelic("SpecialGloves_Elena")) {
//                ++this.attackTime;
//            }
//        }

//        this.bloodMagic = this.canBloodMagic();
        this.triggerOnGlowCheck();

    }

//    public void calculateCardDamage(AbstractMonster mo) {
//        super.calculateCardDamage(mo);
//        AbstractPlayer p = AbstractDungeon.player;
//        if (this.baseAttackTime > 0) {
//            this.attackTime = this.baseAttackTime;
//            if (p.hasRelic("SpecialGloves_Elena")) {
//                ++this.attackTime;
//            }
//        }
//    }

    public boolean canPayment() {
        AbstractPlayer p = AbstractDungeon.player;
        if (MEMBERSHIP_REQUIRED <= 0) {
            return false;
        } else {
            return p.hasPower("TestMod:MembershipPower") && p.getPower("TestMod:MembershipPower").amount >= MEMBERSHIP_REQUIRED;
        }
    }

//    public void bloodUse(AbstractPlayer p, AbstractMonster m) {
//        if (!p.hasPower("BloodfiendFormPower_Elena")) {
//            this.addToBot(new ReducePowerAction(p, p, "BloodPower_Elena", this.bloodCost));
//        }
//    }

}