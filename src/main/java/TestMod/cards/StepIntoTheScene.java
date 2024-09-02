package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
//import TestMod.powers.StepIntoTheScenePower;
import TestMod.powers.TalismanPower;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnvenomPower;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

@AutoAdd.Ignore
public class StepIntoTheScene extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Bad Cosplay Gain 6 (9) Block. Apply 4 (6) talisman to ALL enemies. (exhaust)
     */


    // TEXT DECLARATION

    public static final String ID = TestMod.makeID(StepIntoTheScene.class.getSimpleName());
    public static final String IMG = makeCardPath("StepIntoTheScene_Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;



    // /STAT DECLARATION/


    public StepIntoTheScene() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new ApplyPowerAction(p, p, new StepIntoTheScenePower(p, 1), 1));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
