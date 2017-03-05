package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorFilter;
import android.graphics.Matrix;

public abstract class PaintCommand {
    public PaintCommand(String name) {
        this.name = name;
    }

    public final String name;

    private Matrix matrix;
    public Matrix calculateMatrix(Matrix matrix, PaintFrame frame) {
        return this.matrix = matrix;
    }
    public abstract void setColorFilter(ColorFilter filter);

    public final void renderFull(PaintFrame frame) {
        frame.canvas.setMatrix(this.matrix);
        render(frame);
    }
    public abstract void render(PaintFrame frame);
}
