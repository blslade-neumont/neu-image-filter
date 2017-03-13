package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.Canvas;

import tooearly.neumont.edu.imagefilter.Views.PaintView;

public class PaintFrame {
    public PaintFrame(PaintView view, Canvas canvas, boolean shift) {
        this.paintView = view;
        this.canvas = canvas;
        this.shift = shift;
    }

    public final PaintView paintView;
    public final Canvas canvas;
    public final boolean shift;
}
