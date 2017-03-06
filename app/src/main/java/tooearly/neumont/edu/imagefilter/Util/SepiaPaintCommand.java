package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorMatrix;

public class SepiaPaintCommand extends ColorMatrixCommand {
    public SepiaPaintCommand() {
        super("Sepia", sepiaMatrix);
    }

    private static ColorMatrix sepiaMatrix;
    static {
        sepiaMatrix = new ColorMatrix();
        sepiaMatrix.setScale(1.0f, 0.95f, 0.82f, 1.0f);
        sepiaMatrix.preConcat(BWPaintCommand.blackWhiteMatrix);
    }
}
