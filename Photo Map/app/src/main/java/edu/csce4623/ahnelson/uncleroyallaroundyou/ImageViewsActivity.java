package edu.csce4623.ahnelson.uncleroyallaroundyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItem;
import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItemDao;
import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItemDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.List;

import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItem;
import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItemDao;
import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItemDatabase;

public class ImageViewsActivity extends AppCompatActivity {

    PhotoItem photo;
    ImageView iv;
    String currentPhotoPath;
    TextView dateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_views);

        dateView = findViewById(R.id.dateView);
        iv = findViewById(R.id.imageView);
        Intent callingIntent = getIntent();
        if(callingIntent.hasExtra("photo")) {
            photo = (PhotoItem) callingIntent.getSerializableExtra("photo");
            currentPhotoPath = photo.getUri();
            long date = photo.getDate();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(date);

            SimpleDateFormat timeStamp = new SimpleDateFormat("MM-dd-yy");
            dateView.setText(timeStamp.format(c.getTime()));


            Log.d("currentPhotoPath", "currentphotopath: "+ currentPhotoPath);
            setPic();
        }
        else{
            finish();
        }

    }

    private void setPic() {
        // Get the dimensions of the View

        int targetW = iv.getHeight();
        int targetH = iv.getWidth();

        BitmapFactory.Options bounds = new BitmapFactory.Options();

        bounds.inJustDecodeBounds = true;
        int photoW = bounds.outWidth;
        int photoH = bounds.outHeight;

        int scaleFactor;
        // Determine how much to scale down the image
        if(iv.getHeight() != 0 || iv.getWidth() !=0 ) {
            scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
        }
        else{
            scaleFactor = 1;
        }


        // Decode the image file into a Bitmap sized to fill the View
        bounds.inSampleSize = scaleFactor;
        bounds.inPurgeable = true;
        BitmapFactory.decodeFile(currentPhotoPath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath, opts);

        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        iv.setImageBitmap(rotatedBitmap);
    }

}