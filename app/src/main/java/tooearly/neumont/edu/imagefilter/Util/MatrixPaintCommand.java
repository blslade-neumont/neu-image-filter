package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.ColorFilter;
import android.graphics.Matrix;

public class MatrixPaintCommand extends PaintCommand {
    public MatrixPaintCommand(String name, float scaleX, float scaleY) {
        this(name, scaleX, scaleY, 0, 0);
    }
    public MatrixPaintCommand(String name, float scaleX, float scaleY, float translateX, float translateY) {
        this(name, scaleX, scaleY, translateX, translateY, 0, 0, 0);
    }
    public MatrixPaintCommand(String name, float scaleX, float scaleY, float translateX, float translateY, float rotate, float rotateOriginX, float rotateOriginY) {
        super(name);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.translateX = translateX;
        this.translateY = translateY;
        this.rotate = rotate;
        this.rotateOriginX = rotateOriginX;
        this.rotateOriginY = rotateOriginY;
    }

    public final float scaleX, scaleY, translateX, translateY, rotate, rotateOriginX, rotateOriginY;

    @Override
    public Matrix calculateMatrix(Matrix matrix, PaintFrame frame) {
        if (scaleX != 1 || scaleY != 1) {
            int padLeft = frame.paintView.getPaddingLeft();
            int padRight = frame.paintView.getPaddingRight();
            int drawWidth = frame.paintView.getWidth() - (padLeft + padRight);
            int padTop = frame.paintView.getPaddingTop();
            int padBottom = frame.paintView.getPaddingBottom();
            int drawHeight = frame.paintView.getHeight() - (padTop + padBottom);
            matrix.postTranslate(-(padLeft + (drawWidth / 2)), -(padTop + (drawHeight / 2)));
            matrix.postScale(scaleX, scaleY);
            matrix.postTranslate(padLeft + (drawWidth / 2), padTop + (drawHeight / 2));
        }
        matrix.postTranslate(translateX, translateY);
        matrix.postRotate(rotate, rotateOriginX, rotateOriginY);
        frame.canvas.setMatrix(matrix);
        return super.calculateMatrix(matrix, frame);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        ;
    }

    @Override
    public void render(PaintFrame frame) {
        ;
    }
}
