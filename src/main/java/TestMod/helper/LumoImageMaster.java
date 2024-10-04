//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package TestMod.helper;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class LumoImageMaster {
    public static Texture[] LUMO_LEVEL = new Texture[3];

    public LumoImageMaster() {
    }

    public static Texture updateLumo(int level) {
        switch (level) {
            case 0:
                ImageMaster.loadImage("TestModResources/images/char/bootlegLumo.png");
            case 1:
                ImageMaster.loadImage("TestModResources/images/char/normalLumo.png");
            case 2:
                ImageMaster.loadImage("TestModResources/images/char/roidedLumo.png");;
        }
         return null; //this should never happen
    }

    public static void initialize() {
        LUMO_LEVEL[0] = ImageMaster.loadImage("TestModResources/images/char/bootlegLumo.png");
        LUMO_LEVEL[1] = ImageMaster.loadImage("TestModResources/images/char/normalLumo.png");
        LUMO_LEVEL[2] = ImageMaster.loadImage("TestModResources/images/char/roidedLumo.png");
    }
}
