package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

// public class ${NAME} extends AbstractDynamicCard
// Remove this line when you make a template. Refer to https://github.com/daviscook477/BaseMod/wiki/AutoAdd if you want to know what it does.
public class LostChild extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Cutting Words Deal 8 (11) damage to ALL enemies. NL Payment 4: Apply 2 (3) Vulnerable to ALL enemies.
     */

    // TEXT DECLARATION

    // public static final String ID = DefaultMod.makeID(${NAME}.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String ID = TestMod.makeID("LostChild"); // DELETE THIS ONE.
    public static final String IMG = makeCardPath("LostChild_Attack.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 1;  // COST = ${COST}

    private static final int DAMAGE = 9;    // DAMAGE = ${DAMAGE}
    private static final int WEAK = 2;
    private static final int UPGRADE_PLUS_DMG = 3;  // UPGRADE_PLUS_DMG = ${UPGRADED_DAMAGE_INCREASE}
    private static final int UPGRADE_PLUS_WEAK = 1;

    // /STAT DECLARATION/


    public LostChild() { // public ${NAME}() - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = WEAK;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        if (m.hasPower("TestMod:TalismanPower")) {
            this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            initializeDescription();
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if (!m.isDeadOrEscaped() && m.hasPower("TestMod:TalismanPower")) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
    }

    public void applyPowers() {
        super.applyPowers();
        this.triggerOnGlowCheck();
    }

}
