package TestMod.cards;

import TestMod.characters.Lumo;
import TestMod.characters.TheLuma.Enums;
import TestMod.TestMod;
import TestMod.cards.AbstractLumoCard;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TestMod.TestMod.makeCardPath;

public class ProductionUpgrade extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(ProductionUpgrade.class.getSimpleName());
    public static final String IMG = makeCardPath("ProductionUpgrade_Skill.png");
    private static CardStrings cardStrings;
    private static final int MEMBERSHIP_REQUIRED = 6;
    private static final int UPGRADE_MINUS_MEMBERSHIP = -2;

    public ProductionUpgrade() {
        super(ID, IMG, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, Enums.SELF_AND_LUMO);
        this.setupMagicNumber(MEMBERSHIP_REQUIRED);
        //this.tags.add(Enums.);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (canPayment()) {
            GenericHelper.addToBotAbstract(() -> {
                Lumo mo = LumoPatch.Inst();
                if (mo != null) {
                    mo.levelUp();
                    if (mo.lumoLevel == 1) this.applyChipPowerToLumo(2);
                    if (mo.lumoLevel == 2) this.applyChipPowerToLumo(5);
                    LumoPatch.ReduceReviveTime(5);
                }
            });
            if (p.hasPower("TestMod:ClubPreservationPower")) {
                p.getPower("TestMod:ClubPreservationPower").flash();
                return;
            }
            this.addToBot(new ReducePowerAction(p, p, "TestMod:MembershipPower", MEMBERSHIP_REQUIRED));
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

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeMagicNumber(UPGRADE_MINUS_MEMBERSHIP);
    }

    public boolean canPayment() {
        AbstractPlayer p = AbstractDungeon.player;
        if (MEMBERSHIP_REQUIRED <= 0) {
            return false;
        } else {
            return p.hasPower("TestMod:MembershipPower") && p.getPower("TestMod:MembershipPower").amount >= MEMBERSHIP_REQUIRED;
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
