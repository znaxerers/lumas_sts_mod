package TestMod.relics;

import TestMod.TestMod;
import TestMod.powers.MembershipPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import util.TextureLoader;

import static TestMod.TestMod.makeRelicOutlinePath;
import static TestMod.TestMod.makeRelicPath;

public class LumaRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of combat, gain 5 Membership.
     */

    // ID, images, text.
    public static final String ID = TestMod.makeID("LumaRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("luma_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public LumaRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStart() {
        flash();

        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MembershipPower(AbstractDungeon.player, 5), 5));
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + 5 + DESCRIPTIONS[1];
    }

}