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
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TestMod.TestMod.makeCardPath;

public class AngerStimulation extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(AngerStimulation.class.getSimpleName());
    public static final String IMG = makeCardPath("AngerStimulation_Skill.png");
    private static CardStrings cardStrings;

    public AngerStimulation() {
        super(ID, IMG, cardStrings, 1, CardType.SKILL, CardRarity.COMMON, Enums.SELF_AND_LUMO);
        this.setupMagicNumber(2);
        //this.tags.add(Enums.);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyLumoFirst((c) -> {
            return new StrengthPower(c, this.magicNumber);
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
