package TestMod.cards;

import TestMod.TestMod;
import TestMod.cards.AbstractLumoCard;
import TestMod.characters.Lumo;
import TestMod.characters.TheLuma.Enums;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

public class Flatline extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(Flatline.class.getSimpleName());
    public static final String IMG = makeCardPath("Flatline_Attack.png");
    private static CardStrings cardStrings;

    public Flatline() {
        super(ID, IMG, cardStrings, 2, CardType.ATTACK, CardRarity.RARE, Enums.LUMO);
        this.setupMagicNumber(3);
        //this.tags.add(Enums.CODE_CARD);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        GenericHelper.addToBotAbstract(() -> {
            Lumo mo = LumoPatch.Inst();
            if (mo != null) {
                mo.takeTurn();
                mo.takeTurn();
            }

            GenericHelper.addToBotAbstract(() -> {
                GenericHelper.addToBotAbstract(() -> {
                    Lumo mo2 = LumoPatch.Inst();
                    if (mo2 != null) {
                        mo2.die();
                    }

                });
            });
        });
    }

    public void applyPowers() {
        this.baseDamage = this.misc;
        super.applyPowers();
    }

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeMagicNumber(1);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
