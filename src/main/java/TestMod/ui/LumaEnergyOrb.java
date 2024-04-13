package TestMod.ui;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class LumaEnergyOrb extends CustomEnergyOrb {
    private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
    protected Texture bowLayer;

    public LumaEnergyOrb(String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        this.bowLayer = ImageMaster.loadImage("TestModResources/images/char/defaultCharacter/orb/layer0.png");
    }


    @Override
    public void updateOrb(int energyCount) {
        float[] var10000;
        if (energyCount == 0) {
            var10000 = this.angles;
            var10000[4] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[0] / 4.0F;
            var10000 = this.angles;
            var10000[3] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[1] / 4.0F;
            var10000 = this.angles;
            var10000[2] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[2] / 4.0F;
            var10000 = this.angles;
            var10000[1] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[3] / 4.0F;
            var10000 = this.angles;
            var10000[0] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[4] / 4.0F;
        } else {
            var10000 = this.angles;
            var10000[4] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[0];
            var10000 = this.angles;
            var10000[3] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[1];
            var10000 = this.angles;
            var10000[2] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[2];
            var10000 = this.angles;
            var10000[1] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[3];
            var10000 = this.angles;
            var10000[0] += Gdx.graphics.getDeltaTime() * this.layerSpeeds[4];
        }

    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.setColor(Color.WHITE);
        sb.draw(this.bowLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);

        int i;
        if (enabled) {
            for(i = 0; i < this.energyLayers.length; ++i) {
                sb.draw(this.energyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } else {
            for(i = 0; i < this.noEnergyLayers.length; ++i) {
                sb.draw(this.noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }

        sb.draw(this.baseLayer, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
    }
}
