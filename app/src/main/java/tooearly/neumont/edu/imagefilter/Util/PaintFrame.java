package tooearly.neumont.edu.imagefilter.Util;

import android.graphics.Canvas;

import tooearly.neumont.edu.imagefilter.Views.PaintView;

public class PaintFrame {
    public PaintFrame(PaintView view, Canvas canvas) {
        this.paintView = view;
        this.canvas = canvas;
    }

    public final PaintView paintView;
    public final Canvas canvas;
}
