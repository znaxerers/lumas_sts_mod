package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
import TestMod.powers.TalismanPower;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

// public class ${NAME} extends AbstractDynamicCard
// Remove this line when you make a template. Refer to https://github.com/daviscook477/BaseMod/wiki/AutoAdd if you want to know what it does.
public class CuttingWords extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Cutting Words Deal 8 (11) damage to ALL enemies. NL Payment 4: Apply 2 (3) Vulnerable to ALL enemies.
     */

    // TEXT DECLARATION

    // public static final String ID = DefaultMod.makeID(${NAME}.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String ID = TestMod.makeID("CuttingWords"); // DELETE THIS ONE.
    public static final String IMG = makeCardPath("CuttingWords_Attack.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 1;  // COST = ${COST}

    private static final int DAMAGE = 8;    // DAMAGE = ${DAMAGE}
    private static final int VULNERABLE = 2;
    private static final int UPGRADE_PLUS_DMG = 3;  // UPGRADE_PLUS_DMG = ${UPGRADED_DAMAGE_INCREASE}
    private static final int UPGRADE_PLUS_VULNERABLE = 1;
    private static final int MEMBERSHIP_REQUIRED = 4;

    // /STAT DECLARATION/


    public CuttingWords() { // public ${NAME}() - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isMultiDamage = true;
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = VULNERABLE;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));

        if (canPayment()) {
            for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p,
                        new VulnerablePower(mo, magicNumber, false), magicNumber));
            }
            this.addToBot(new ReducePowerAction(p, p, "TestMod:MembershipPower", MEMBERSHIP_REQUIRED));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
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
