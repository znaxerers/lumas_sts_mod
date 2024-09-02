package TestMod.cards;

import TestMod.TestMod;
import TestMod.cards.AbstractLumoCard;
import TestMod.characters.TheLuma.Enums;
import TestMod.helper.GenericHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.Iterator;

import static TestMod.TestMod.makeCardPath;

public class AllIn extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(AllIn.class.getSimpleName());
    public static final String IMG = makeCardPath("AllIn_Attack.png");
    private static CardStrings cardStrings;

    public AllIn() {
        super(ID, IMG, cardStrings, 1, CardType.ATTACK, CardRarity.UNCOMMON, Enums.LUMO);
        this.setupDamage(4);
        this.misc = this.baseDamage;
        this.setupMagicNumber(7);
        //this.tags.add(Enums.CODE_CARD);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyChipPowerToLumo(this.damage);
        GenericHelper.addToBotAbstract(() -> {
            this.misc += this.magicNumber;
            this.baseDamage = this.misc;
            Iterator var2 = p.masterDeck.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c = (AbstractCard)var2.next();
                if (c.uuid.equals(this.uuid)) {
                    c.misc += this.magicNumber;
                    c.baseDamage = c.misc;
                }
            }

        });
    }

    public void applyPowers() {
        this.baseDamage = this.misc;
        super.applyPowers();
    }

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeDamage(2);
        this.upgradeMagicNumber(1);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
