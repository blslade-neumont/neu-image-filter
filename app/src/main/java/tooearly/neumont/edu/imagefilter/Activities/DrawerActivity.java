package tooearly.neumont.edu.imagefilter.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import tooearly.neumont.edu.imagefilter.Util.InvertPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.MatrixPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.PaintCommand;
import tooearly.neumont.edu.imagefilter.Util.SepiaPaintCommand;
import tooearly.neumont.edu.imagefilter.Views.PaintView;

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

    private void init() {
        Intent intent = getIntent();
        Bitmap bmp = BitmapStorageService.storage.get(intent.getStringExtra(CaptureImageActivity.IMAGE_EXTRA));
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

        finalizeDrawer();
    }
    private void convolute(String name, float[][] kernel) {
        Bitmap bmp = Bitmap.createBitmap(paintView.getBaseImage().getWidth(), paintView.getBaseImage().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        paintView.stack.render(paintView, canvas, false);
        bmp = ConvolutionService.convolute(bmp, kernel);
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

    }

    public void onShareClicked(View view) {
        share();
    }
    private void share() {

    }

}
