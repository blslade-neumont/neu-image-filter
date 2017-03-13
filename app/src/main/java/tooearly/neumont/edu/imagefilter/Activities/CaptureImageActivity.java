package tooearly.neumont.edu.imagefilter.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import tooearly.neumont.edu.imagefilter.R;
import tooearly.neumont.edu.imagefilter.Services.BitmapStorageService;

public class CaptureImageActivity extends AppCompatActivity {

    ImageView cameraView;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String IMAGE_EXTRA = "tooearly.neumont.edu.imagefilter.extra_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        cameraView = (ImageView)findViewById(R.id.cameraView);

        if (hasCamera()) {
            try {
                createImageFile();
            }
            catch (IOException e) {
                Toast.makeText(this, R.string.err_no_image_storage_location, Toast.LENGTH_LONG).show();
                return;
            }
            launchCamera(null);
        }
        else {
            Toast.makeText(this, R.string.err_no_camera, Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = null;
            try {
                String[] projection = { MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(imageFileUri, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                Log.v("tag", "here:" + capturedImageFilePath);
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(capturedImageFilePath)));
            }
            catch (IOException ignored) { }
            finally {
                if (imageBitmap == null) {
                    Toast.makeText(this, R.string.err_no_image_storage_location, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (imageBitmap.getHeight() < imageBitmap.getWidth()) {
                Matrix mat = new Matrix();
                mat.postRotate(90);
                imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), mat, true);
            }
            cameraView.setImageBitmap(imageBitmap);

            navigateToEditor(imageBitmap);
        }
    }
    private Uri imageFileUri;
    private void createImageFile() throws IOException {
        String fileName = "temp.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void navigateToEditor(Bitmap bmp) {
        Intent intent = new Intent(this, DrawerActivity.class);
        BitmapStorageService.storage.put(bmp.toString(), bmp);
        intent.putExtra(IMAGE_EXTRA, bmp.toString());
        startActivity(intent);
    }
}
