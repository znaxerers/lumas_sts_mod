package TestMod.cards;

import TestMod.TestMod;
import TestMod.actions.CookoutAction;
import TestMod.characters.TheLuma;
import TestMod.powers.MembershipPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static TestMod.TestMod.makeCardPath;

// public class ${NAME} extends AbstractDynamicCard
// Remove this line when you make a template. Refer to https://github.com/daviscook477/BaseMod/wiki/AutoAdd if you want to know what it does.
public class Cookout extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Pin Scatter Deal 13 (17) damage and gain 8 (10) Membership.
     */

    // TEXT DECLARATION

    // public static final String ID = DefaultMod.makeID(${NAME}.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String ID = TestMod.makeID("Cookout");
    public static final String IMG = makeCardPath("Cookout_Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = -1;  // COST = ${COST}
    private static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    private static final int MEMBERSHIP_REQUIRED = 2;

    // /STAT DECLARATION/


    public Cookout() { // public ${NAME}() - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CookoutAction(p, this.freeToPlayOnce, this.energyOnUse, this.magicNumber));
        if (canPayment()) {
            this.addToBot(new GainBlockAction(p, p, 10));
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
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (canPayment()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public boolean canPayment() {
        AbstractPlayer p = AbstractDungeon.player;
        if (MEMBERSHIP_REQUIRED <= 0) {
            return false;
        } else {
            return p.hasPower("TestMod:MembershipPower") && p.getPower("TestMod:MembershipPower").amount >= MEMBERSHIP_REQUIRED;
        }
    }
}
