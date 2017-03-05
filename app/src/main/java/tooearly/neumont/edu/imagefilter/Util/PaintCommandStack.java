package tooearly.neumont.edu.imagefilter.Util;

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

    }

    private Stack<PaintCommand> undoStack = new Stack<>(),
                                redoStack = new Stack<>();
    private float[] matrixValues = new float[9];

    public void render(PaintView view, Canvas canvas) {
        PaintFrame frame = new PaintFrame(view, canvas);
        Matrix previousMatrix = new Matrix();
        ColorMatrix previousColorMatrix = null;
        ColorFilter previousColorFilter = null;

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
        }

        Paint clearBrush = new Paint(Color.WHITE);
        clearBrush.setColorFilter(previousColorFilter);
        canvas.drawPaint(clearBrush);

        for (int q = 0; q < undoStack.size(); q++) {
            PaintCommand cmd = undoStack.get(q);
            cmd.renderFull(frame);
        }
    }

    public boolean canUndo() {
        return undoStack.size() > 0;
    }
    public PaintCommand undo() {
        PaintCommand cmd = undoStack.pop();
        redoStack.push(cmd);
        return cmd;
    }

    public boolean canRedo() {
        return redoStack.size() > 0;
    }
    public PaintCommand redo() {
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
