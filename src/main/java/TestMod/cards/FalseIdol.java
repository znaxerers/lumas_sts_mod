package TestMod.cards;

import TestMod.characters.Lumo;
import TestMod.characters.TheLuma.Enums;
import TestMod.TestMod;
import TestMod.cards.AbstractLumoCard;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TestMod.TestMod.makeCardPath;

public class FalseIdol extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(FalseIdol.class.getSimpleName());
    public static final String IMG = makeCardPath("FalseIdol_Skill.png");
    private static CardStrings cardStrings;

    public FalseIdol() {
        super(ID, IMG, cardStrings, 1, CardType.SKILL, CardRarity.UNCOMMON, Enums.SELF_AND_LUMO);
        this.setupMagicNumber(3);
        //this.tags.add(Enums.);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new BlurPower(p, this.magicNumber), this.magicNumber));

        GenericHelper.addToBotAbstract(() -> {
            Lumo mo = LumoPatch.Inst();
            if (mo != null) {
                this.applyLumoFirst((c) -> {
                    return new BlurPower(c, this.magicNumber);
                });
            }

        });
    }

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeMagicNumber(1);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
