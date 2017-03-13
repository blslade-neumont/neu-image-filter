package tooearly.neumont.edu.imagefilter.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.OutputStream;

import tooearly.neumont.edu.imagefilter.R;
import tooearly.neumont.edu.imagefilter.Services.BitmapStorageService;

import static tooearly.neumont.edu.imagefilter.Activities.CaptureImageActivity.IMAGE_EXTRA;

/**
 * Created by Daniel Kuhrmeyer on 3/10/2017.
 */

public class ShareActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


    }

    public void shareScreen(View v){
        Intent randomVariable = getIntent();
        String anotherVariable = randomVariable.getStringExtra(IMAGE_EXTRA);

        Bitmap icon = BitmapStorageService.storage.get(anotherVariable);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "description");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }
}
