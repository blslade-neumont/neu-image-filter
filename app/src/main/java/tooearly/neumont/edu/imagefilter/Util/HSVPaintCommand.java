package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorMatrix;

public class HSVPaintCommand extends ColorMatrixCommand {
    public HSVPaintCommand(float hue, float saturation, float value) {
        super("Hue, Saturation, Value", createColorMatrix(hue, saturation, value));
    }

    private static ColorMatrix createColorMatrix(float hue, float saturation, float value) {
        ColorMatrix matrix = new ColorMatrix(new float[] {
                1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0,
                0, 0, 0, 0, 1
        });
        if (saturation != 0) {
            ColorMatrix saturationMatrix = new ColorMatrix();
            saturationMatrix.setSaturation(saturation);
            matrix.postConcat(saturationMatrix);
        }
        if (hue != 0) {
            //Mathematical formula to create a "shift-hue" color matrix adapted from http://stackoverflow.com/a/17490633/768597
            float cosVal = (float)Math.cos(value);
            float sinVal = (float)Math.sin(value);
            float lumR = 0.213f;
            float lumG = 0.715f;
            float lumB = 0.072f;
            ColorMatrix hueMatrix = new ColorMatrix(new float[] {
                    lumR + cosVal * (1 - lumR) + sinVal * (-lumR),    lumG + cosVal * (-lumG) + sinVal * (-lumG),     lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                    lumR + cosVal * (-lumR) + sinVal * (0.143f),      lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f),  0, 0,
                    lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG),      lumB + cosVal * (1 - lumB) + sinVal * (lumB),  0, 0,
                    0f, 0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 0f, 1f
            });
            matrix.postConcat(hueMatrix);
        }
        return matrix;
    }
}
