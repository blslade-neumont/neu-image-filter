package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;

public class ShapePaintCommand extends PaintCommand {
    public ShapePaintCommand(String name, Paint brush, ShapeType type, RectF dimens) {
        super(name);
        this.brush = brush;
        this.type = type;
        this.dimens = dimens;
    }

    public final Paint brush;
    public final ShapeType type;
    public final RectF dimens;

    @Override
    public void setColorFilter(ColorFilter filter) {
        brush.setColorFilter(filter);
    }

    @Override
    public void render(PaintFrame frame) {
        switch (type) {
        case Rect:
            frame.canvas.drawRect(dimens, brush);
            break;

        case Oval:
            frame.canvas.drawOval(dimens, brush);
            break;

        case Line:
            frame.canvas.drawLine(dimens.left, dimens.top, dimens.right, dimens.bottom, brush);
            break;

        default:
            throw new IllegalStateException();
        }
    }
}
