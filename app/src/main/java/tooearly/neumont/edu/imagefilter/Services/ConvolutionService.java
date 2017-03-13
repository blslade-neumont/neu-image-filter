package tooearly.neumont.edu.imagefilter.Services;

import android.graphics.Bitmap;

public class ConvolutionService {
    public static Bitmap convolute(Bitmap original, float[][] kernel) {
        if (original.getConfig() != Bitmap.Config.ARGB_8888) throw new IllegalStateException("Cannot convolute bitmap - invalid format");

        int width = original.getWidth(),
            height = original.getHeight();
        int stride = width;
        int[] pixels = new int[width * height];
        original.getPixels(pixels, 0, stride, 0, 0, width, height);

        int kernelDist = kernel.length / 2;
//        int kernelElementCount = kernel.length * kernel.length;

        int[] newpixels = new int[width * height];
        for (int q = 0; q < width; q++) {
            for (int w = 0; w < height; w++) {
                float a = 0, r = 0, g = 0, b = 0;

                for (int i = -kernelDist; i <= kernelDist; i++) {
                    for (int j = -kernelDist; j <= kernelDist; j++) {
                        int xx = Math.min(Math.max(q + i, 0), width - 1);
                        int yy = Math.min(Math.max(w + j, 0), height - 1);
                        int color = pixels[(yy * width) + xx];
                        a += ((color >> 24) & 0xFF) * kernel[i + kernelDist][j + kernelDist] / 255.f;
                        r += ((color >> 16) & 0xFF) * kernel[i + kernelDist][j + kernelDist] / 255.f;
                        g += ((color >> 8)  & 0xFF) * kernel[i + kernelDist][j + kernelDist] / 255.f;
                        b += ((color)       & 0xFF) * kernel[i + kernelDist][j + kernelDist] / 255.f;
                    }
                }

                newpixels[(w * width) + q] =
                        ((int)Math.floor(a * 255) << 24) |
                        ((int)Math.floor(r * 255) << 16) |
                        ((int)Math.floor(g * 255) << 8) |
                        ((int)Math.floor(b * 255));
            }
        }

        return Bitmap.createBitmap(newpixels, 0, stride, original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
    }

    public static float[][] gaussianBlur5x5;
    public static float[][] boxBlur5x5;
    public static float[][] sharpen3x3;
    public static float[][] goldenEdge3x3;

    static {
        gaussianBlur5x5 = new float[][] {
                { 0.003765f, 0.015019f, 0.023792f, 0.015019f, 0.003765f },
                { 0.015019f, 0.059912f, 0.094907f, 0.059912f, 0.015019f },
                { 0.023792f, 0.094907f, 0.150342f, 0.094907f, 0.023792f },
                { 0.015019f, 0.059912f, 0.094907f, 0.059912f, 0.015019f },
                { 0.003765f, 0.015019f, 0.023792f, 0.015019f, 0.003765f }
        };
        boxBlur5x5 = new float[][] {
                { 1/25.f, 1/25.f, 1/25.f, 1/25.f, 1/25.f },
                { 1/25.f, 1/25.f, 1/25.f, 1/25.f, 1/25.f },
                { 1/25.f, 1/25.f, 1/25.f, 1/25.f, 1/25.f },
                { 1/25.f, 1/25.f, 1/25.f, 1/25.f, 1/25.f },
                { 1/25.f, 1/25.f, 1/25.f, 1/25.f, 1/25.f }
        };
        final float SHARPEN_AMOUNT = 2;
        sharpen3x3 = new float[][] {
                { -SHARPEN_AMOUNT/9.f, -SHARPEN_AMOUNT/9.f,      -SHARPEN_AMOUNT/9.f },
                { -SHARPEN_AMOUNT/9.f, 1+(8*SHARPEN_AMOUNT/9.f), -SHARPEN_AMOUNT/9.f },
                { -SHARPEN_AMOUNT/9.f, -SHARPEN_AMOUNT/9.f,      -SHARPEN_AMOUNT/9.f },
        };
        goldenEdge3x3 = new float[][] {
                { 2f, 3f, -3f },
                { 1f, -1f, 1f },
                { -2f, 1f, -2f }
        };

    }
}
