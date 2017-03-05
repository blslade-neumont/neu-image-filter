package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorMatrix;

public class InvertPaintCommand extends ColorMatrixCommand {
    public InvertPaintCommand() {
        super("Invert Colors", invertMatrix);
    }

    private static ColorMatrix invertMatrix;
    static {
        invertMatrix = new ColorMatrix(new float[] {
                -1.0f, 0.0f,  0.0f,  1.0f, 0.0f,
                0.0f,  -1.0f, 0.0f,  1.0f, 0.0f,
                0.0f,  0.0f,  -1.0f, 1.0f, 0.0f,
                1.0f,  1.0f,  1.0f,  1.0f, 0.0f
        });
    }
}
