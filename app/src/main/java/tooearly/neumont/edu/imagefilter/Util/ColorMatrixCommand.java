package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;

@SuppressWarnings("UnnecessarySemicolon")
public class ColorMatrixCommand extends PaintCommand {
    public ColorMatrixCommand(String name, ColorMatrix colorMatrix) {
        super(name);
        this.colorMatrix = colorMatrix;
    }

    public final ColorMatrix colorMatrix;

    @Override
    public void setColorFilter(ColorFilter filter) {
        ;
    }

    @Override
    public void render(PaintFrame frame) {
        ;
    }
}
