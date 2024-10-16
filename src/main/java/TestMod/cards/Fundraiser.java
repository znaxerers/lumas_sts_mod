package TestMod.cards;

import TestMod.TestMod;
import TestMod.actions.DoubleMembershipAction;
import TestMod.actions.DoubleTalismanAction;
import TestMod.characters.TheLuma;
import TestMod.powers.TalismanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

// public class ${NAME} extends AbstractDynamicCard
// Remove this line when you make a template. Refer to https://github.com/daviscook477/BaseMod/wiki/AutoAdd if you want to know what it does.
public class Fundraiser extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Fundraiser Double your Membership.
     */

    // TEXT DECLARATION

    // public static final String ID = DefaultMod.makeID(${NAME}.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String ID = TestMod.makeID("Fundraiser"); // DELETE THIS ONE.
    public static final String IMG = makeCardPath("Fundraiser_Skill.png");// "public static final String IMG = makeCardPath("${NAME}.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 1;  // COST = ${COST}

    private static final int UPGRADE_PLUS_COST = 0;

    // /STAT DECLARATION/


    public Fundraiser() { // public ${NAME}() - This one and the one right under the imports are the most important ones, don't forget them
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DoubleMembershipAction(p, p));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_PLUS_COST);
            initializeDescription();
        }
    }
}
