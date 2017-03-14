package tooearly.neumont.edu.imagefilter.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import tooearly.neumont.edu.imagefilter.R;
import tooearly.neumont.edu.imagefilter.Services.BitmapStorageService;

import static tooearly.neumont.edu.imagefilter.Activities.CaptureImageActivity.IMAGE_EXTRA;

public class ShareActivity extends AppCompatActivity {

    public static String TAG = "ShareActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        bmp = BitmapStorageService.storage.get(intent.getStringExtra(IMAGE_EXTRA));

        ImageView preview = (ImageView)findViewById(R.id.img_preview);
        preview.setImageBitmap(bmp);

        txt_name = (EditText)findViewById(R.id.txt_name);
        txt_description = (EditText)findViewById(R.id.photoDescription);
    }

    private Bitmap bmp;
    private EditText txt_name, txt_description;

    public void onShareScreenClicked(View v) {
        shareScreen();
    }
    public void shareScreen() {
        String title = txt_name.getText().toString();
        String description = txt_description.getText().toString();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) throw new IllegalStateException();

        OutputStream outstream = null;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            if (outstream == null) throw new IllegalStateException();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
        }
        catch (FileNotFoundException e) {
            Log.wtf(TAG, e);
            Toast.makeText(this, R.string.msg_failed_to_share_image, Toast.LENGTH_LONG).show();
            return;
        }
        finally {
            if (outstream != null) {
                try {outstream.close();}
                catch (IOException e) { Log.wtf(TAG, e); }
            }
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        //noinspection deprecation
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TITLE, title);
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, description);
        startActivity(Intent.createChooser(share, "Share Image"));
    }
}
