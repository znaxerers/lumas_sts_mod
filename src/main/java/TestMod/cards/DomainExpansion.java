package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.TheLuma;
import TestMod.powers.DomainExpansionPower;
import TestMod.powers.DrivePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static TestMod.TestMod.makeCardPath;

public class DomainExpansion extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Membership Drive At the start of your turn, gain 3/4 Membership.
     */

    // TEXT DECLARATION

    public static final String ID = TestMod.makeID(DomainExpansion.class.getSimpleName());
    public static final String IMG = makeCardPath("DomainExpansion_Power.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheLuma.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    private static final int MAGIC = 2;

    // /STAT DECLARATION/


    public DomainExpansion() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        //Tag your strike, defend and form cards so that they work correctly.
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DomainExpansionPower(p, magicNumber), magicNumber));
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
