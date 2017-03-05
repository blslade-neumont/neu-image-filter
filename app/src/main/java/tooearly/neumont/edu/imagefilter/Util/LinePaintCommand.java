package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.List;

public class LinePaintCommand extends PaintCommand {
    public LinePaintCommand(String name, Paint brush, List<PointF> pointsAry) {
        super(name);
        this.brush = brush;
        this.pointsAry = pointsAry;
    }

    public final Paint brush;
    public final List<PointF> pointsAry;

    @Override
    public void setColorFilter(ColorFilter filter) {
        brush.setColorFilter(filter);
    }

    @Override
    public void render(PaintFrame frame) {
        for (int i = 0; i < pointsAry.size() - 2; i++) {
            PointF from = pointsAry.get(i),
                   to = pointsAry.get(i + 1);
            frame.canvas.drawLine(from.x, from.y, to.x, to.y, brush);
        }
    }
}
