package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorMatrix;

public class BWPaintCommand extends ColorMatrixCommand {
    public BWPaintCommand() {
        super("Black and White", blackWhiteMatrix);
    }

    public static ColorMatrix blackWhiteMatrix;
    static {
        blackWhiteMatrix = new ColorMatrix();
        blackWhiteMatrix.setSaturation(0);
    }
}
