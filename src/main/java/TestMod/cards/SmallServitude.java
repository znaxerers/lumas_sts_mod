package TestMod.cards;

import TestMod.characters.TheLuma.Enums;
import TestMod.TestMod;
import TestMod.cards.AbstractLumoCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TestMod.TestMod.makeCardPath;

public class SmallServitude extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(SmallServitude.class.getSimpleName());
    public static final String IMG = makeCardPath("SmallServitude_Skill.png");
    private static CardStrings cardStrings;

    public SmallServitude() {
        super(ID, IMG, cardStrings, 1, CardType.SKILL, CardRarity.RARE, Enums.SELF_AND_LUMO);
        this.setupMagicNumber(3);
        //this.tags.add(Enums.);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyLumoFirst((c) -> {
            return new RegenPower(c, this.magicNumber);
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
