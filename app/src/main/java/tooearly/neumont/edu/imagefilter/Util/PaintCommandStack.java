package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Stack;

import tooearly.neumont.edu.imagefilter.Views.PaintView;

public class PaintCommandStack {
    public PaintCommandStack() {
        bmpCmd = new BitmapPaintCommand(null);
        push(bmpCmd);
        setMinimumUndoStackSize(undoStack.size());
    }

    private BitmapPaintCommand bmpCmd;

    private Stack<PaintCommand> undoStack = new Stack<>(),
                                redoStack = new Stack<>();
    private float[] matrixValues = new float[9];

    public void render(PaintView view, Canvas canvas) {
        PaintFrame frame = new PaintFrame(view, canvas);
        Bitmap baseImg = view.getBaseImage();
        bmpCmd.setBitmap(baseImg);

        float gutterLeft = 0, gutterRight = 0, gutterTop = 0, gutterBottom = 0;
        float scale = 1;
        Matrix previousMatrix = new Matrix();
        float canvasWidth = canvas.getWidth(), canvasHeight = canvas.getHeight();
        float aspectRatio = baseImg.getWidth() / baseImg.getHeight();
        float viewAspectRatio = canvasWidth / (float)canvasHeight;
        if (viewAspectRatio > aspectRatio) {
            scale = canvasHeight / baseImg.getHeight();
            gutterLeft = gutterRight = (canvasWidth - baseImg.getWidth() * scale) / 2;
        }
        else {
            scale = canvasWidth / baseImg.getWidth();
            gutterTop = gutterBottom = (canvasHeight - baseImg.getHeight() * scale) / 2;
        }
        previousMatrix.postTranslate(gutterLeft, gutterTop);
        previousMatrix.postScale(scale, scale);

        ColorMatrix previousColorMatrix = null;
        ColorFilter previousColorFilter = null;

        int startCommand = 0;
        for (int q = undoStack.size() - 1; q >= 0; q--) {
            PaintCommand cmd = undoStack.get(q);
            Matrix newMatrix = new Matrix();
            previousMatrix.getValues(matrixValues);
            newMatrix.setValues(matrixValues);

            if (cmd instanceof ColorMatrixCommand) {
                ColorMatrix newColorMatrix = new ColorMatrix();
                float[] values = ((ColorMatrixCommand)cmd).colorMatrix.getArray();
                newColorMatrix.set(values);
                if (previousColorMatrix != null) newColorMatrix.postConcat(previousColorMatrix);
                previousColorMatrix = newColorMatrix;
                previousColorFilter = new ColorMatrixColorFilter(previousColorMatrix);
            }

            cmd.setColorFilter(previousColorFilter);
            previousMatrix = cmd.calculateMatrix(newMatrix, frame);

            if (cmd instanceof BitmapPaintCommand) {
                startCommand = q;
                break;
            }
        }

        Paint clearBrush = new Paint(Color.WHITE);
        clearBrush.setColorFilter(previousColorFilter);
        canvas.drawPaint(clearBrush);

        for (int q = startCommand; q < undoStack.size(); q++) {
            PaintCommand cmd = undoStack.get(q);
            cmd.renderFull(frame);
        }
    }

    private int minUndoStack = 0;
    private void setMinimumUndoStackSize(int size) {
        minUndoStack = Math.max(0, size);
    }
    public boolean canUndo() {
        return undoStack.size() > minUndoStack;
    }
    public PaintCommand undo() {
        if (!canUndo()) throw new IndexOutOfBoundsException();
        PaintCommand cmd = undoStack.pop();
        redoStack.push(cmd);
        return cmd;
    }

    public boolean canRedo() {
        return redoStack.size() > 0;
    }
    public PaintCommand redo() {
        if (!canRedo()) throw new IndexOutOfBoundsException();
        PaintCommand cmd = redoStack.pop();
        undoStack.push(cmd);
        return cmd;
    }

    public void push(PaintCommand cmd) {
        push(cmd, true);
    }
    public void push(PaintCommand cmd, boolean clearRedoStack) {
        if (clearRedoStack) redoStack.clear();
        undoStack.push(cmd);
    }
}
