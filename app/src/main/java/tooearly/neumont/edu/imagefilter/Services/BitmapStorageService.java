package tooearly.neumont.edu.imagefilter.Services;

import android.graphics.Bitmap;

import java.util.HashMap;

public class BitmapStorageService {
    public static HashMap<String, Bitmap> storage;

    static {
        storage = new HashMap<>();
    }
}
