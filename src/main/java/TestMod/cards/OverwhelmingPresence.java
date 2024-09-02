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

public class OverwhelmingPresence extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(OverwhelmingPresence.class.getSimpleName());
    public static final String IMG = makeCardPath("OverwhelmingPresence_Attack.png");
    private static CardStrings cardStrings;

    public OverwhelmingPresence() {
        super(ID, IMG, cardStrings, 1, CardType.ATTACK, CardRarity.UNCOMMON, Enums.LUMO);
        this.setupDamage(10);
        this.misc = this.baseDamage;
        //this.tags.add(Enums.CODE_CARD);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyChipPowerToLumo(this.damage);
        GenericHelper.addToBotAbstract(() -> {
            Lumo mo = LumoPatch.Inst();
            if (mo != null) {
                mo.gainBlockNextTurn = false;
                mo.damageToLowestNextTurn = true;
            }

        });
    }

    public void applyPowers() {
        this.baseDamage = this.misc;
        super.applyPowers();
    }

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeDamage(4);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
