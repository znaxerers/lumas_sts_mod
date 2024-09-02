package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.stances.AbstractStance;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

public class SapidInSpire extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Sapid In Spire Retain. If Retained, create a copy, spend 2 members to gain 6/9 block. Exhaust.
     * // new text
     */


    // TEXT DECLARATION

    public static final String ID = TestMod.makeID(SapidInSpire.class.getSimpleName());
    public static final String IMG = makeCardPath("SapidInSpire_Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int MEMBERSHIP_REQUIRED = 2;



    // /STAT DECLARATION/


    public SapidInSpire() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (canPayment()) {
            this.addToBot(new GainBlockAction(p, p, block));
            if (p.hasPower("TestMod:ClubPreservationPower")) {
                p.getPower("TestMod:ClubPreservationPower").flash();
                return;
            }
            this.addToBot(new ReducePowerAction(p, p, "TestMod:MembershipPower", MEMBERSHIP_REQUIRED));
        }
    }

    public void atTurnStartPreDraw() {
        this.addToBot(new DiscardToHandAction(this));
    }

    //Upgraded stats.
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
