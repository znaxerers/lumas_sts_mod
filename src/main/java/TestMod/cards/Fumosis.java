//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.Lumo;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
import TestMod.characters.TheLuma;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TestMod.TestMod.makeCardPath;

public class Fumosis extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(Fumosis.class.getSimpleName());
    public static final String IMG = makeCardPath("Fumosis_Attack.png");
    private static CardStrings cardStrings;

    public Fumosis() {
        super(ID, IMG, cardStrings, 1, CardType.ATTACK, CardRarity.COMMON, TheLuma.Enums.LUMO);
        this.setupMagicNumber(5);
        //this.tags.add(Enums.CODE_CARD);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        this.applyLumoFirst((c) -> {
            return new StrengthPower(c, -this.magicNumber);
        });
        this.applyLumoFirst((c) -> {
            return new GainStrengthPower(c, this.magicNumber);
        });

        GenericHelper.addToBotAbstract(() -> {
            Lumo mo = LumoPatch.Inst();
            if (mo != null) {
                mo.damageTimes += 1;
            }

        });
    }

    public void applyPowers() {
        this.baseDamage = this.misc;
        super.applyPowers();
    }

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeMagicNumber(-2);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
