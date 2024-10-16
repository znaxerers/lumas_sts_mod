//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.powers;

import TestMod.TestMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import util.TextureLoader;

import static TestMod.TestMod.makePowerPath;

public class DomainExpansionPower extends AbstractPower implements CloneablePowerInterface {    public AbstractCreature source;

    public static final String POWER_ID = TestMod.makeID("DomainExpansionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("domainexpansion_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("domainexpansion_power32.png"));

    public DomainExpansionPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CommonPower(owner, source, amount);
    }

    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.ID.equals("TestMod:Talisman") && source == this.owner && target != this.owner && !target.hasPower("Artifact")) {
            this.flash();
            this.addToBot(new DamageAction(target, new DamageInfo(this.owner, this.amount * target.getPower("TestMod:Talisman").amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
        }

    }
}
