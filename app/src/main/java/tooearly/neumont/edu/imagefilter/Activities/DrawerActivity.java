package tooearly.neumont.edu.imagefilter.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tooearly.neumont.edu.imagefilter.R;
import tooearly.neumont.edu.imagefilter.Services.BitmapStorageService;
import tooearly.neumont.edu.imagefilter.Services.ConvolutionService;
import tooearly.neumont.edu.imagefilter.Util.BWPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.BitmapPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.ColorMatrixCommand;
import tooearly.neumont.edu.imagefilter.Util.HSVPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.HSVPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.InvertPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.MatrixPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.PaintCommand;
import tooearly.neumont.edu.imagefilter.Util.SepiaPaintCommand;
import tooearly.neumont.edu.imagefilter.Views.PaintView;

import static tooearly.neumont.edu.imagefilter.Activities.CaptureImageActivity.IMAGE_EXTRA;

public class DrawerActivity extends AppCompatActivity {

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        init();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, CaptureImageActivity.class));
    }

    private void init() {
        Intent intent = getIntent();
        Bitmap bmp = BitmapStorageService.storage.get(intent.getStringExtra(IMAGE_EXTRA));
        paintView = (PaintView)this.findViewById(R.id.paintView);
        paintView.setBaseImage(bmp);

        initDrawer();
    }
    private void initDrawer() {
        addDrawerItem("Invert", new Runnable() {
            @Override
            public void run() {
                addCommand(new InvertPaintCommand());
            }
        });
        addDrawerItem("Black and White", new Runnable() {
            @Override
            public void run() {
                addCommand(new BWPaintCommand());
            }
        });
        addDrawerItem("Sepia", new Runnable() {
            @Override
            public void run() {
                addCommand(new SepiaPaintCommand());
            }
        });
        addDrawerItem("Flip Horizontally", new Runnable() {
            @Override
            public void run() {
                addCommand(new MatrixPaintCommand("Flip Vertically", -1, 1));
            }
        });
        addDrawerItem("Flip Vertically", new Runnable() {
            @Override
            public void run() {
                addCommand(new MatrixPaintCommand("Flip Vertically", 1, -1));
            }
        });
        addDrawerItem("Gaussian Blur", new Runnable() {
            @Override
            public void run() {
                convolute("Gaussian Blur", ConvolutionService.gaussianBlur5x5);
            }
        });
        addDrawerItem("Box Blur", new Runnable() {
            @Override
            public void run() {
                convolute("Box Blur", ConvolutionService.boxBlur5x5);
            }
        });
        addDrawerItem("Sharpen", new Runnable() {
            @Override
            public void run() {
                convolute("Sharpen", ConvolutionService.sharpen3x3);
            }
        });
        addDrawerItem("Lower Saturation", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(0, .5f, 0));
            }
        });
        addDrawerItem("Increase Saturation", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(0, 1.5f, 0));
            }
        });
        addDrawerItem("Red Shift", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(-((3.14159f)/(3.0f)), 0, 0));
            }
        });
        addDrawerItem("Blue Shift", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(((3.14159f)/(3.0f)), 0, 0));
            }
        });
        addDrawerItem("Darken", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(0, 0, -32));
            }
        });
        addDrawerItem("Lighten", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(0, 0, 32));
            }
        });
        addDrawerItem("Redless", new Runnable() {
            @Override
            public void run() {
                addCommand(new ColorMatrixCommand("Redless", new ColorMatrix(new float[] {
                        0, 0.5f, 0.5f, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                })));
            }
        });
        addDrawerItem("Blueless", new Runnable() {
            @Override
            public void run() {
                addCommand(new ColorMatrixCommand("Blueless", new ColorMatrix(new float[] {
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0.5f, 0.5f, 0, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                })));
            }
        });
        addDrawerItem("Greenless", new Runnable() {
            @Override
            public void run() {
                addCommand(new ColorMatrixCommand("Greenless", new ColorMatrix(new float[] {
                        1, 0, 0, 0, 0,
                        0.5f, 0, 0.5f, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                })));
            }
        });
        addDrawerItem("Golden Edge", new Runnable() {
            @Override
            public void run() {
                convolute("Golden Edge", ConvolutionService.goldenEdge3x3);
                addCommand(new ColorMatrixCommand("Golden Edge", new ColorMatrix(new float[] {
                        1, 1, 0, 0, 10,
                        1, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 1
                })));
                flatten("Golden Edge", 2);
            }
        });

        addDrawerItem("Willy Wonky", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(-5, -5, 0));
            }
        });
        addDrawerItem("Petri Dish Mode", new Runnable() {
            @Override
            public void run() {
                addCommand(new HSVPaintCommand(255, 205, 0));
            }
        });




        finalizeDrawer();
    }
    private void convolute(String name, float[][] kernel) {
        Bitmap bmp = Bitmap.createBitmap(paintView.getBaseImage().getWidth(), paintView.getBaseImage().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        paintView.stack.render(paintView, canvas, false);
        bmp = ConvolutionService.convolute(bmp, kernel);
        addCommand(new BitmapPaintCommand(name, bmp));
    }
    private void flatten(String name, int count) {
        Bitmap bmp = Bitmap.createBitmap(paintView.getBaseImage().getWidth(), paintView.getBaseImage().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        paintView.stack.render(paintView, canvas, false);
        for (int q = 0; q < count; q++)
            paintView.stack.undo();
        addCommand(new BitmapPaintCommand(name, bmp));
    }
    private void addDrawerItem(String name, Runnable action) {
        drawerNames.add(name);
        drawerActions.add(action);
    }
    private void finalizeDrawer() {
        drawerList = (ListView)findViewById(R.id.drawerList);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerNames));
        ((ArrayAdapter<String>)drawerList.getAdapter()).notifyDataSetChanged();

        drawerList.setOnItemClickListener(this.new DrawerItemClickListener());
    }
    private List<String> drawerNames = new ArrayList<>();
    private List<Runnable> drawerActions = new ArrayList<>();

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerActions.get(position).run();
            drawerLayout.closeDrawer(drawerList);
        }
    }

    public void onUndoClicked(View view) {
        undo();
    }
    public void onRedoClicked(View view) {
        redo();
    }
    private void undo() {
        if (paintView.stack.canUndo()) paintView.stack.undo();
        paintView.invalidate();
    }
    private void redo() {
        if (paintView.stack.canRedo()) paintView.stack.redo();
        paintView.invalidate();
    }

    private void addCommand(PaintCommand cmd) {
        paintView.stack.push(cmd);
        paintView.invalidate();
    }

    public void onSaveClicked(View view) {
        save();
    }
    private void save() {
        Bitmap bmp = Bitmap.createBitmap(paintView.getBaseImage().getWidth(), paintView.getBaseImage().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        paintView.stack.render(paintView, canvas, false);
    }

    public void onShareClicked(View view) {
        share();
    }
    private void share() {
        Bitmap bmp = Bitmap.createBitmap(paintView.getBaseImage().getWidth(), paintView.getBaseImage().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        paintView.stack.render(paintView, canvas, false);
        Intent intent = new Intent(this, ShareActivity.class);
        BitmapStorageService.storage.put(bmp.toString(), bmp);
        intent.putExtra(IMAGE_EXTRA, bmp.toString());
        startActivity(intent);
    }

}
