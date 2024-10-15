package TestMod.cards;

import TestMod.TestMod;
import TestMod.characters.Lumo;
import TestMod.characters.TheLuma.Enums;
import TestMod.helper.GenericHelper;
import TestMod.patches.LumoPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static TestMod.TestMod.makeCardPath;

public class Explode extends AbstractLumoCard {
    public static final String ID = TestMod.makeID(Explode.class.getSimpleName());
    public static final String IMG = makeCardPath("Explode_Attack.png");
    private static CardStrings cardStrings;

    public Explode() {
        super(ID, IMG, cardStrings, 1, CardType.ATTACK, CardRarity.UNCOMMON, Enums.LUMO);
        //this.tags.add(Enums.CODE_CARD);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        GenericHelper.addToBotAbstract(() -> {
            Lumo mo = LumoPatch.Inst();
            if (mo != null) {
                this.addToBot(new DamageAllEnemiesAction(p, mo.currentHealth, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
                this.addToBot(new VFXAction(new ExplosionSmallEffect(mo.hb.cX, mo.hb.cY), 0.1F));
                this.addToBot(new SuicideAction(mo));
            }
        });
    }

    public void applyPowers() {
        this.baseDamage = this.misc;
        super.applyPowers();
    }

    public void limitedUpgrade() {
        super.limitedUpgrade();
        this.upgradeBaseCost(0);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
