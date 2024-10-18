package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
//import TestMod.powers.StepIntoTheScenePower;
import TestMod.powers.MembershipPower;
import TestMod.powers.TalismanPower;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnvenomPower;

import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

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
    private static final int BLOCK = 3;
    private static final int MEMBERSHIP = 2;
    private static final int UPGRADE_GAIN = 1;
    private static final int UPGRADE_UPGRADE_GAIN = 1;



    // /STAT DECLARATION/


    public StepIntoTheScene() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = this.block = BLOCK + this.misc;
        this.baseMagicNumber = this.magicNumber = MEMBERSHIP + this.misc;
        this.defaultBaseSecondMagicNumber = this.defaultSecondMagicNumber = UPGRADE_GAIN;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new ApplyPowerAction(p, p, new StepIntoTheScenePower(p, 1), 1));
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new ApplyPowerAction(p, p, new MembershipPower(p,magicNumber)));
        this.misc += this.defaultSecondMagicNumber;
        this.baseBlock = BLOCK + this.misc;;
        this.baseMagicNumber = MEMBERSHIP + this.misc;
        Iterator var2 = p.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.uuid.equals(this.uuid)) {
                c.misc += this.defaultSecondMagicNumber;
                c.baseBlock = BLOCK + this.misc;;
                c.baseMagicNumber = MEMBERSHIP + this.misc;
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDefaultSecondMagicNumber(UPGRADE_UPGRADE_GAIN);
            initializeDescription();
        }
    }
}
