package tooearly.neumont.edu.imagefilter.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tooearly.neumont.edu.imagefilter.R;
import tooearly.neumont.edu.imagefilter.Services.BitmapStorageService;
import tooearly.neumont.edu.imagefilter.Util.BWPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.InvertPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.MatrixPaintCommand;
import tooearly.neumont.edu.imagefilter.Util.PaintCommand;

public class DrawerActivity extends AppCompatActivity {

    private ListView drawerList;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        init();
    }

    private void init() {
        Intent intent = getIntent();
        Bitmap bmp = BitmapStorageService.storage.get(intent.getStringExtra(CaptureImageActivity.IMAGE_EXTRA));
        ImageView imageView = (ImageView)this.findViewById(R.id.imageView);
        imageView.setImageBitmap(bmp);

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

        finalizeDrawer();
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
//            drawerList.setItemChecked(position, true);
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

    }
    private void redo() {

    }

    private void addCommand(PaintCommand cmd) {

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
