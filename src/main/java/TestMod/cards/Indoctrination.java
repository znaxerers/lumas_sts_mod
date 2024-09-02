package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.stances.AbstractStance;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

public class Indoctrination extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Sapid In Spire At the start of the turn, put this in your hand, spend 2 members to gain 6/9 block
     */


    // TEXT DECLARATION

    public static final String ID = TestMod.makeID(Indoctrination.class.getSimpleName());
    public static final String IMG = makeCardPath("Indoctrination_Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int STRENGTH = 2;
    private static final int UPGRADE_PLUS_STRENGTH = 1;
    private static final int MEMBERSHIP_REQUIRED = 4;



    // /STAT DECLARATION/


    public Indoctrination() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = STRENGTH;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        if (canPayment()) {
            this.addToBot(new MakeTempCardInHandAction(this.makeStatEquivalentCopy()));
            if (p.hasPower("TestMod:ClubPreservationPower")) {
                p.getPower("TestMod:ClubPreservationPower").flash();
                return;
            }
            this.addToBot(new ReducePowerAction(p, p, "TestMod:MembershipPower", MEMBERSHIP_REQUIRED));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH);
            initializeDescription();
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (canPayment()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void applyPowers() {
        super.applyPowers();
        this.triggerOnGlowCheck();
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
