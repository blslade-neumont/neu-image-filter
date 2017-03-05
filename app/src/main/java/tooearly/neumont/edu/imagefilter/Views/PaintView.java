package tooearly.neumont.edu.imagefilter.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import tooearly.neumont.edu.imagefilter.Util.LinePaintCommand;
import tooearly.neumont.edu.imagefilter.Util.PaintCommandStack;
import tooearly.neumont.edu.imagefilter.Util.PathPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.ShapePaintCommand;
import tooearly.neumont.edu.imagefilter.Util.ShapeType;

public class PaintView extends View {

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.stack = new PaintCommandStack();
        setWillNotDraw(false);

        init();
    }

    private void init() {
        curX = 0;
        curY = 0;
        lineCoordsAry = new ArrayList<>();

        myBrush = new Paint();
        myBrush.setColor(Color.BLUE);
        myBrush.setStyle(Paint.Style.FILL_AND_STROKE);
        myBrush.setStrokeCap(Paint.Cap.ROUND);
        myBrush.setStrokeWidth(3);

        setDrawingCacheEnabled(true);

        setShapeType(shapeType);
        setBrushColor(brushColor);
        setBrushSize(brushSize);
    }

    public final PaintCommandStack stack;

    private float startX, startY, curX, curY;
    private boolean isDrawingShape = false;
    private Paint myBrush;
    private ArrayList<PointF> lineCoordsAry;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (shapeType == ShapeType.Brush) return onBrushTouchEvent(event);
        else return onShapeTouchEvent(event);
    }
    private boolean onBrushTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                curX = event.getRawX();
                curY = event.getRawY();
                stack.push(new LinePaintCommand("Brush", cloneBrush(myBrush), lineCoordsAry));
                return true;
            case (MotionEvent.ACTION_MOVE):
                curX = event.getRawX();
                curY = event.getRawY();
                lineCoordsAry.add(new PointF(curX, curY));
                invalidate();
                return true;

            case (MotionEvent.ACTION_UP):
                curX = event.getRawX();
                curY = event.getRawY();
                lineCoordsAry.add(new PointF(curX, curY));
                invalidate();

                lineCoordsAry = new ArrayList<>();
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
    private boolean onShapeTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                startX = curX = event.getRawX();
                startY = curY = event.getRawY();
                isDrawingShape = true;
                return true;

            case (MotionEvent.ACTION_MOVE):
                curX = event.getRawX();
                curY = event.getRawY();
                invalidate();
                return true;

            case (MotionEvent.ACTION_UP):
                curX = event.getRawX();
                curY = event.getRawY();
                if (shapeType == ShapeType.Tri) stack.push(new PathPaintCommand("Triangle", cloneBrush(myBrush), getTriPath()));
                else stack.push(new ShapePaintCommand(shapeType.name(), cloneBrush(myBrush), shapeType, getShapeRect()));
                isDrawingShape = false;
                invalidate();
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    private Paint cloneBrush(Paint old) {
        Paint brush = new Paint();

        brush.setColor(old.getColor());
        brush.setStyle(old.getStyle());
        brush.setAlpha(old.getAlpha());
        brush.setAntiAlias(old.isAntiAlias());
        brush.setDither(old.isDither());
        brush.setFakeBoldText(old.isFakeBoldText());
        brush.setFlags(old.getFlags());
        brush.setColorFilter(old.getColorFilter());
        brush.setFilterBitmap(old.isFilterBitmap());
        brush.setHinting(old.getHinting());
        brush.setLinearText(old.isLinearText());
        brush.setMaskFilter(old.getMaskFilter());
        brush.setPathEffect(old.getPathEffect());
        brush.setShader(old.getShader());
        brush.setStrikeThruText(old.isStrikeThruText());
        brush.setStrokeCap(old.getStrokeCap());
        brush.setStrokeJoin(old.getStrokeJoin());
        brush.setStrokeMiter(old.getStrokeMiter());
        brush.setStrokeWidth(old.getStrokeWidth());
        brush.setSubpixelText(old.isSubpixelText());
        brush.setTextAlign(old.getTextAlign());
        brush.setTextSize(old.getTextSize());
        brush.setTextScaleX(old.getTextScaleX());
        brush.setTextSkewX(old.getTextSkewX());
        brush.setUnderlineText(old.isUnderlineText());
        brush.setXfermode(old.getXfermode());
        brush.setStrikeThruText(old.isStrikeThruText());
        brush.setTypeface(old.getTypeface());

        if (Build.VERSION.SDK_INT >= 21) {
            brush.setElegantTextHeight(old.isElegantTextHeight());
            brush.setFontFeatureSettings(old.getFontFeatureSettings());
            brush.setLetterSpacing(old.getLetterSpacing());
        }
        if (Build.VERSION.SDK_INT >= 24) {
            brush.setTextLocales(old.getTextLocales());
        }

        return brush;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.stack.render(this, canvas);

        if (isDrawingShape) {
            switch (shapeType) {
                case Rect:
                    canvas.drawRect(getShapeRect(), myBrush);
                    break;

                case Oval:
                    canvas.drawOval(getShapeRect(), myBrush);
                    break;

                case Line:
                    canvas.drawLine(startX, startY, curX, curY, myBrush);
                    break;

                case Tri:
                    Path path = getTriPath();
                    canvas.drawPath(path, myBrush);
            }
        }
    }
    private RectF getShapeRect() {
        if (shapeType == ShapeType.Line) return new RectF(startX, startY, curX, curY);
        else return new RectF(Math.min(startX, curX), Math.min(startY, curY), Math.max(startX, curX), Math.max(startY, curY));
    }
    private Path getTriPath() {
        Path path = new Path();
        RectF rect = getShapeRect();
        path.moveTo(rect.centerX(), rect.top);
        path.lineTo(rect.left, rect.bottom);
        path.lineTo(rect.right, rect.bottom);
        path.close();
        return path;
    }

    private ShapeType shapeType = ShapeType.Brush;
    public void setShapeType(ShapeType type) {
        this.shapeType = type;
        if (type == ShapeType.Brush || type == ShapeType.Line) myBrush.setStrokeWidth(this.brushSize);
        else myBrush.setStrokeWidth(0);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private int brushColor, brushSize = 15;
    public void setBrushColor(int color) {
        this.brushColor = color;
        myBrush.setColor(this.brushColor);
    }
    public void setBrushSize(int size) {
        this.brushSize = size;
        setShapeType(ShapeType.Brush);
    }

}
