package com.heyzqt.googletraingdemo3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "MainActivity";

    AudioManager audioManager;

    private static final int INTENT_FULL_PICTURE = 100;

    private static final int INTENT_IMAGE_PICTURE = 101;

    private Button mFullPicBtn;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        ComponentName mbCN = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
        Log.i(TAG, "onCreate: name = " + RemoteControlReceiver.class.getName());
        Log.i(TAG, "onCreate: getCanonicalName = " + RemoteControlReceiver.class.getCanonicalName());
        Log.i(TAG, "onCreate: simple name = " + RemoteControlReceiver.class.getSimpleName());
        audioManager.registerMediaButtonEventReceiver(mbCN);

        mFullPicBtn = (Button) findViewById(R.id.button1);
        mFullPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFullSizePicture();
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageview1);
        setPic();

        //use Camera to take a picture
        //takePicture();

        //galleryAddPic();
    }

    private void setPic() {
        // get picture width and height
//        int imgWidth = mImageView.getWidth();
//        int imgHeight = mImageView.getHeight();

        // get bitmap width and height
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/imgg.jpg";
        File f = new File(path);
        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int bitmapWidth = options.outWidth;
            int bitmapHeight = options.outHeight;

            //find a flaxible scale size
            //int scalesize = Math.min(bitmapWidth / imgWidth, bitmapHeight / imgHeight);

            //set options
            options.inJustDecodeBounds = false;
            options.inSampleSize = 4;
            options.inPurgeable = true;

            //set image view
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            mImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //add the pictures to the gallery
    public void galleryAddPic() throws NullPointerException {
        //Uri uri = Uri.parse("file:///storage/emulated/0/Pictures/JPEG_171130_102123_-234018204.jpg");
        Intent scanintent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        for (File f : directory.listFiles()) {
            Uri contentUri = Uri.fromFile(f);
            Log.i(TAG, "galleryAddPic: path = " + Uri.fromFile(f));
            scanintent.setData(contentUri);
            this.sendBroadcast(scanintent);
        }
    }

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, INTENT_IMAGE_PICTURE);
        }
    }

    public void saveFullSizePicture() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File picture = null;
            try {
                picture = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (picture != null) {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                Log.i(TAG, "saveFullSizePicture: uri = " + Uri.fromFile(picture));
                startActivityForResult(pictureIntent, INTENT_FULL_PICTURE);
            }
        }
    }

    public File createImageFile() throws IOException {
        File image = null;
        String tempfile = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        String imgname = "JPEG_" + tempfile + "_";
        File storageFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(imgname, ".jpg", storageFile);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_FULL_PICTURE && resultCode == RESULT_OK) {
            Log.i(TAG, "onActivityResult: full picture");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: ");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioManager.unregisterMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
