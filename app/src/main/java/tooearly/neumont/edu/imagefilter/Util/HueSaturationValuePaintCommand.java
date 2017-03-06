package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorMatrix;

public class HueSaturationValuePaintCommand extends ColorMatrixCommand {
    public HueSaturationValuePaintCommand(float hue, float saturation, float value) {
        super("Hue, Saturation, Value", createColorMatrix(hue, saturation, value));
    }

    private static ColorMatrix createColorMatrix(float hue, float saturation, float value) {
        if (hue != 0) throw new IllegalStateException("Can't shift hue! Not implemented!");
        ColorMatrix matrix = new ColorMatrix(new float[] {
                1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0
        });
        if (saturation != 0) {
            ColorMatrix saturationMatrix = new ColorMatrix();
            saturationMatrix.setSaturation(saturation);
            matrix.preConcat(saturationMatrix);
        }
        return matrix;
    }
}
