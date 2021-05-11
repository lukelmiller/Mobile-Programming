package edu.csce4623.ahnelson.uncleroyallaroundyou;

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

import edu.csce4623.ahnelson.uncleroyallaroundyou.data.PhotoItemDao;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;

    private Button myCameraButton;
    private FusedLocationProviderClient mFusedLocationClient;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    ImageView myImageView;
    private double latitude;
    private double longitude;
    PhotoItemDatabase database;
    PhotoItemDao photoItemDao;
    List<PhotoItem> photos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        database = Room.databaseBuilder(this, PhotoItemDatabase.class, "photo_db").allowMainThreadQueries().build();
        photoItemDao = database.getPhotoItemDao();
        photos = photoItemDao.getAll();

//        for(int i = 0; i< photos.size(); i++){
//            photoItemDao.delete(photos.get(i));
//        }


        myCameraButton = (Button) findViewById(R.id.btnGetLocation);
        myCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snapPic();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mapFragment != null) {
            transaction.add(R.id.cfFrameLayout, mapFragment);
            transaction.commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void snapPic() {
        if (mFusedLocationClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("MapsActivity", "No Location Permission");
                return;
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    if (location != null) {
                        dispatchTakePictureIntent();
                        Log.d("MapsActivity", "" + location.getLatitude() + ":" + location.getLongitude());

                    }
                }
            });
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("MainActivity",ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "edu.csce4623.ahnelson.cameratime2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                addPhotoToDatabase();
            }
        }
    }
    private void addPhotoToDatabase(){
        PhotoItem photo = new PhotoItem();
        photo.setLatitude(latitude);
        photo.setLongitude(longitude);
        photo.setUri(currentPhotoPath);
        Calendar c = Calendar.getInstance();
        photo.setDate(c.getTimeInMillis());
        photoItemDao.insert(photo);
        photos = photoItemDao.getAll();
        onMapReady(mMap);
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            enableMyLocation();
            if(!photos.isEmpty()) {
//                LatLng loc = new LatLng(photos.get(0).getLatitude(), photos.get(0).getLongitude());
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromAsset(photos.get(0).getUri());
//                Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Photo"));
                for (int i = 0; i < photos.size(); i++) {
                    LatLng loc = new LatLng(photos.get(i).getLatitude(), photos.get(i).getLongitude());
                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromAsset(photos.get(i).getUri());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(loc));
                    marker.setTag(i);
                    googleMap.setOnMarkerClickListener(this);
                }

            }
//            // Add a marker in Sydney and move the camera
//            LatLng sydney = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }
    @Override
    public boolean onMarkerClick(final Marker marker){

        Integer index = (Integer)marker.getTag();
        PhotoItem photo = photos.get(index);
        Intent showImage = new Intent(this, ImageViewsActivity.class);
        showImage.putExtra("photo", photo);
        startActivity(showImage);
        return false;
    }
}