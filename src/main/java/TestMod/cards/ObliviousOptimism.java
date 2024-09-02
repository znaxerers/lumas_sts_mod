package TestMod.cards;

import TestMod.powers.ObliviousPower;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import TestMod.TestMod;
import TestMod.characters.TheLuma;
import TestMod.powers.DrivePower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static TestMod.TestMod.makeCardPath;

public class ObliviousOptimism extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Membership Drive gain 2/3 intangible, when taking damage shuffle a wound into your discard pile.
     */

    // TEXT DECLARATION

    public static final String ID = TestMod.makeID(ObliviousOptimism.class.getSimpleName());
    public static final String IMG = makeCardPath("ObliviousOptimism_Power.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    private static final int MAGIC = 2;

    // /STAT DECLARATION/


    public ObliviousOptimism() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        //Tag your strike, defend and form cards so that they work correctly.
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ObliviousPower(p, 1), magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
