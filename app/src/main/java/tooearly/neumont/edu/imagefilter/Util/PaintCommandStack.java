package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;

import java.util.Stack;

import tooearly.neumont.edu.imagefilter.Views.PaintView;

public class PaintCommandStack {
    public PaintCommandStack() {

    }

    private Stack<PaintCommand> undoStack = new Stack<>(),
                                redoStack = new Stack<>();
    private float[] matrixValues = new float[9];

    public static ColorFilter invertFilter;
    public static ColorFilter normalFilter;
    static {
        invertFilter = new ColorMatrixColorFilter(new ColorMatrix(new float[] {
            -1.0f, 0.0f,  0.0f,  1.0f, 0.0f,
            0.0f,  -1.0f, 0.0f,  1.0f, 0.0f,
            0.0f,  0.0f,  -1.0f, 1.0f, 0.0f,
            1.0f,  1.0f,  1.0f,  1.0f, 0.0f
        }));
        normalFilter = null;
    }

    public void render(PaintView view, Canvas canvas) {
        PaintFrame frame = new PaintFrame(view, canvas);
        Matrix previousMatrix = new Matrix();
        boolean isInverted = false;
        for (int q = undoStack.size() - 1; q >= 0; q--) {
            PaintCommand cmd = undoStack.get(q);
            Matrix newMatrix = new Matrix();
            previousMatrix.getValues(matrixValues);
            newMatrix.setValues(matrixValues);
            cmd.setColorFilter(isInverted ? invertFilter : normalFilter);
            previousMatrix = cmd.calculateMatrix(newMatrix, frame);
            if (cmd instanceof InvertPaintCommand) isInverted = !isInverted;
        }

        if (isInverted) canvas.drawColor(Color.BLACK);
        else canvas.drawColor(Color.WHITE);
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
