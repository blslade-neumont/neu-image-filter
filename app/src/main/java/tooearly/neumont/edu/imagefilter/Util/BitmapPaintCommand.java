package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Paint;

public class BitmapPaintCommand extends PaintCommand {
    public BitmapPaintCommand(Bitmap bmp, Paint brush) {
        super("Bitmap");
        this.bmp = bmp;
        this.brush = brush;
    }

    private Bitmap bmp;
    private Paint brush;

    @Override
    public void setColorFilter(ColorFilter filter) {
        this.brush.setColorFilter(filter);
    }

    @Override
    public void render(PaintFrame frame) {
        frame.canvas.drawBitmap(bmp, 0, 0, brush);
    }
}
