package com.csce4623.ahnelson.restclientexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class UserView extends FragmentActivity  implements Callback<List<Post>>, OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Post> myPostList;
    ArrayList<User> myUserList;
    UserView.PostAdapter myPostAdapter;
    ListView lvUserPosts;
    int id;
    User user;
    LatLng userLoc;//= new LatLng(1, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        lvUserPosts = (ListView)findViewById(R.id.lvUserPosts);
        id = this.getIntent().getIntExtra("userId", 0);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(mapFragment != null){
            transaction.add(R.id.flUserLocation, mapFragment);
            transaction.commit();
        }
        mapFragment.getMapAsync(this);

        startQuery();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
        if(userLoc!= null){
            mMap.addMarker(new MarkerOptions().position(userLoc).title("User")); //+ userLoc.latitude +userLoc.longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
        }
        //onMapReady(mMap);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public void startQuery() {

        Debug.startMethodTracing("test");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserAPI userAPI = retrofit.create(UserAPI.class);
        Call<List<User>> userCall = userAPI.loadUserById(id);
        userCall.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    List<User> users = new ArrayList<User>(response.body());
                    user = users.get(0);
                    TextView tvUserName = (TextView)findViewById(R.id.tvUserName);
                    TextView tvUserUsername = (TextView)findViewById(R.id.tvUserUsername);
                    TextView tvUserEmail = (TextView)findViewById(R.id.tvUserEmail);
                    TextView tvUserPhone = (TextView)findViewById(R.id.tvUserPhone);
                    TextView tvUserWebsite = (TextView)findViewById(R.id.tvUserWebsite);
                    tvUserName.setText(user.getName());
                    tvUserEmail.setText(user.getEmail());
                    tvUserPhone.setText(user.getPhone());
                    tvUserUsername.setText(user.getUsername());
                    tvUserWebsite.setText(user.getWebsite());

                    Double lat = Double.parseDouble(users.get(0).getAddress().getGeo().getLat());
                    Double lang = Double.parseDouble(users.get(0).getAddress().getGeo().getLng());
                    userLoc = new LatLng(lat, lang);
                    if(mMap != null){
                        onMapReady(mMap);
                    }

                } else {
                    System.out.println(response.errorBody());
                }
                Debug.stopMethodTracing();

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        PostAPI postAPI = retrofit.create(PostAPI.class);
        Call<List<Post>> call = postAPI.loadPosts();
        call.enqueue(this);




    }



    @Override
    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
        if(response.isSuccessful()) {
            myPostList = new ArrayList<Post>(response.body());
            ArrayList<Post> finalPostList = new ArrayList<Post>();
            for (int i=0; i<myPostList.size(); i++) {
                if(myPostList.get(i).getUserId() == id){
                    finalPostList.add(myPostList.get(i));
                }
            }
            myPostList = finalPostList;

            myPostAdapter = new UserView.PostAdapter(this,myPostList);
            lvUserPosts.setAdapter(myPostAdapter);
            for (Post post:myPostList) {
                Log.d("UserViewActivity","ID: " + post.getId());
            }
        } else {
            System.out.println(response.errorBody());
        }
        Debug.stopMethodTracing();
        if(mMap != null){
            onMapReady(mMap);
        }
    }

    @Override
    public void onFailure(Call<List<Post>> call, Throwable t) {
        t.printStackTrace();
    }

    protected class PostAdapter extends ArrayAdapter<Post> {
        public PostAdapter(Context context, ArrayList<Post> posts) {
            super(context, 0, posts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Post post = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout, parent, false);
            }

            // Lookup view for data population
            TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            // Populate the data into the template view using the data object

            tvTitle.setText(post.getTitle());
            tvId.setText(Integer.toString(post.getId()));
            tvUsername.setText(Integer.toString(post.getUserId()));
            return convertView;

            // Return the completed view to render on screen

        }
    }
}