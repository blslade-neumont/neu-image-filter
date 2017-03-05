package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;

public class PathPaintCommand extends PaintCommand {
    public PathPaintCommand(String name, Paint brush, Path path) {
        super(name);
        this.brush = brush;
        this.path = path;
    }

    public final Paint brush;
    public final Path path;

    @Override
    public void setColorFilter(ColorFilter filter) {
        brush.setColorFilter(filter);
    }

    @Override
    public void render(PaintFrame frame) {
        frame.canvas.drawPath(path, brush);
    }
}
