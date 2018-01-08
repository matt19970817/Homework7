package com.example.goroshigeno.lab10;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.view.Menu;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.widget.SearchView.*;

public class MainActivity extends AppCompatActivity {
    private SearchView SearchView;
    private GoogleMap map;
    private ListView  ListView;
    CustomAdapter mCustomAdapter;
    List my_list;
    List Search_name ;
    List Search_lang ;
    List Search_long ;

    int a = 12312312;
    int a = 12312312;
    int a = 12312312;
    int a = 12312312;
    Marker[] MarkerAll ;
    Marker[] MarkerCar = new Marker[999];
    Marker[] MarkerBike = new Marker[999];
    Marker[] MarkerMotorcycle = new Marker[999];
    Marker marker ;

    String[] name ;
    double[] longtitude ;
    double[] langtitude ;
    String[] motorcycle = new String[999];
    double[] motorcycle_lang = new double[999];
    double[] motorcycle_long = new double[999];
    String[] bike = new String[999];
    double[] bike_lang = new double[999];
    double[] bike_long = new double[999];
    String[] car = new String[999];
    double[] car_lang = new double[999];
    double[] car_long = new double[999];
    int motorcycle_count = 0 ;
    int bike_count = 0 ;
    int car_count = 0 ;


    class Data {
        Result result;

        class Result {
            Results[] results;

            class Results {
                String name; // = "停車場名稱";
                String lat; // = Double.parseDouble("經度(WGS84)");
                String lng; // = Double.parseDouble("緯度(WGS84)");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView show_all = (ImageView) findViewById(R.id.show_all);
        final ImageView show_car = (ImageView) findViewById(R.id.show_car);
        final ImageView show_bike = (ImageView) findViewById(R.id.show_bike);
        final ImageView show_motorcycle = (ImageView) findViewById(R.id.show_motorcycle);
        ListView = (ListView) findViewById(R.id.listview);
        my_list = new ArrayList<>();
        Search_name = new ArrayList<>();
        Search_lang = new ArrayList<>();
        Search_long = new ArrayList<>();
        mCustomAdapter = new CustomAdapter(MainActivity.this,my_list,Search_name,Search_lang,Search_long);
        SearchView = (SearchView) findViewById(R.id.searchview);
        SearchView.setIconifiedByDefault(false);// 關閉icon切換
        SearchView.setFocusable(false); // 不要進畫面就跳出輸入鍵盤
        setSearch_function();


        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] test = mCustomAdapter.getSearch_name(position);
                ListView.setAdapter(null);

                for( int j = 0 ; j < MarkerAll.length ; j ++)
                {
                    if(!MarkerAll[j].equals(""))
                        MarkerAll[j].remove();
                    if( MarkerCar[j] != null )
                        MarkerCar[j].remove();
                    if(MarkerBike[j] != null )
                        MarkerBike[j].remove();
                    if(MarkerMotorcycle[j] != null )
                        MarkerMotorcycle[j].remove();
                }
                if(marker != null)
                    marker.remove();
                marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(test[2]), Double.valueOf(test[3])))
                        .title(test[1]).snippet( test[0] )
                        );
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(test[2]), Double.valueOf(test[3])),11));
                Log.d("listOnclick",test[0]+","+test[1]+","+test[2]+","+test[3]);
            }
        });

        show_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for( int j = 0 ; j < MarkerAll.length ; j ++)
                {
                    if(!MarkerAll[j].equals(""))
                        MarkerAll[j].remove();
                    if( MarkerCar[j] != null )
                        MarkerCar[j].remove();
                    if(MarkerBike[j] != null )
                        MarkerBike[j].remove();
                    if(MarkerMotorcycle[j] != null )
                        MarkerMotorcycle[j].remove();
                }
                for (int i = 0 ; i < name.length ; i ++) {
                    if(name[i].indexOf("機車") != -1) {
                        MarkerAll[i] = map.addMarker(new MarkerOptions()
                                .position(new LatLng(langtitude[i], longtitude[i]))
                                .title(name[i])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.motorcicly)));
                    }
                    else if(name[i].indexOf("自行車") != -1) {
                        MarkerAll[i] = map.addMarker(new MarkerOptions()
                                .position(new LatLng(langtitude[i], longtitude[i]))
                                .title(name[i])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)));
                    }
                    else{
                        MarkerAll[i] = map.addMarker(new MarkerOptions()
                                .position(new LatLng(langtitude[i], longtitude[i]))
                                .title(name[i])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                    }
                }
            }
        });

        show_bike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for( int j = 0 ; j < MarkerAll.length ; j ++)
                {
                    if(!MarkerAll[j].equals(""))
                        MarkerAll[j].remove();
                    if( MarkerCar[j] != null )
                        MarkerCar[j].remove();
                    if(MarkerBike[j] != null )
                        MarkerBike[j].remove();
                    if(MarkerMotorcycle[j] != null )
                        MarkerMotorcycle[j].remove();
                }
                for (int i = 0 ; i < bike_count ; i ++) {
                    MarkerBike[i] = map.addMarker(new MarkerOptions()
                            .position(new LatLng(bike_lang[i], bike_long[i]))
                            .title(bike[i])
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)));
                }
            }
        });

        show_car.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for( int j = 0 ; j < MarkerAll.length ; j ++)
                {
                    if(!MarkerAll[j].equals(""))
                        MarkerAll[j].remove();
                    if( MarkerCar[j] != null )
                        MarkerCar[j].remove();
                    if(MarkerBike[j] != null )
                        MarkerBike[j].remove();
                    if(MarkerMotorcycle[j] != null )
                        MarkerMotorcycle[j].remove();
                }
                for (int i = 0 ; i < car_count ; i ++) {
                    MarkerCar[i] = map.addMarker(new MarkerOptions()
                            .position(new LatLng(car_lang[i], car_long[i]))
                            .title(car[i])
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                }
            }
        });

        show_motorcycle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for( int j = 0 ; j < MarkerAll.length ; j ++)
                {
                    if(!MarkerAll[j].equals(""))
                        MarkerAll[j].remove();
//                    Log.d("DeBeg",""+MarkerCar[j].equals(""));
                    if( MarkerCar[j] != null )
                        MarkerCar[j].remove();
                    if(MarkerBike[j] != null )
                        MarkerBike[j].remove();
                    if(MarkerMotorcycle[j] != null )
                        MarkerMotorcycle[j].remove();
                }
                Log.d("debug",""+MarkerAll.length);
                Log.d("debug",""+motorcycle.length);
                for (int i = 0 ; i < motorcycle_count ; i ++) {
                    Log.d("debeg","name:"+motorcycle[i]+",lang"+motorcycle_lang[i]+",long"+motorcycle_long[i]);
                    MarkerMotorcycle[i] = map.addMarker(new MarkerOptions()
                            .position(new LatLng(motorcycle_lang[i], motorcycle_long[i]))
                            .title(motorcycle[i])
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.motorcicly)));
                }
                Log.d("debug","ok");
            }
        });

        BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String myJson = intent.getExtras().getString("json");
                myJson = myJson.replaceAll("停車場名稱","name");
                myJson = myJson.replaceAll("經度\\(WGS84\\)","lng");
                myJson = myJson.replaceAll("緯度\\(WGS84\\)","lat");
                Gson gson = new Gson();
                Data data = gson.fromJson(myJson, Data.class);
                name = new String[data.result.results.length];
                longtitude = new double[data.result.results.length];
                langtitude = new double[data.result.results.length];
                String AddressInfo = "";
                MarkerAll = new Marker[data.result.results.length];

                for (int i = 0; i < data.result.results.length; i++) {
                    name[i] = data.result.results[i].name;
                    langtitude[i] = Double.parseDouble(data.result.results[i].lat);
                    longtitude[i] = Double.parseDouble(data.result.results[i].lng);




                    if(name[i].indexOf("機車") != -1) {
                        Log.d("marker","motorcycle");
                        motorcycle[motorcycle_count] = name[i];
                        motorcycle_lang[motorcycle_count] = langtitude[i];
                        motorcycle_long[motorcycle_count] = longtitude[i];
                        //Log.d("marker:"+motorcycle_count,"motorcycle:"+motorcycle[motorcycle_count]+",lang:"+motorcycle_lang[motorcycle_count]+",long:"+motorcycle_long[motorcycle_count]);
                        motorcycle_count ++ ;
                        //Log.d("debeg","name:"+motorcycle[0]+",lang"+motorcycle_lang[0]+",long"+motorcycle_long[0]);

                        MarkerAll[i] = map.addMarker(new MarkerOptions()
                                .position(new LatLng(langtitude[i], longtitude[i]))
                                .title(name[i])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.motorcicly)));
                    }
                    else if(name[i].indexOf("自行車") != -1) {
                        Log.d("marker","bycycle");
                        bike[bike_count] = name[i];
                        bike_lang[bike_count] = langtitude[i];
                        bike_long[bike_count] = longtitude[i];
                        bike_count ++ ;
                        MarkerAll[i] = map.addMarker(new MarkerOptions()
                                .position(new LatLng(langtitude[i], longtitude[i]))
                                .title(name[i])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bike)));
                    }
                    else  {
                        Log.d("marker","car");
                        car[car_count] = name[i];
                        car_lang[car_count] = langtitude[i];
                        car_long[car_count] = longtitude[i];
                        car_count ++ ;
                        MarkerAll[i] = map.addMarker(new MarkerOptions()
                                .position(new LatLng(langtitude[i], longtitude[i]))
                                .title(name[i])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                    }
                    AddressInfo += getAddressByLocation(langtitude[i],longtitude[i]) + ",";
                    my_list.add(getAddressByLocation(langtitude[i],longtitude[i]));
                    Search_name.add(name[i]);
                    Search_lang.add(String.valueOf(langtitude[i]));
                    Search_long.add(String.valueOf(longtitude[i]));
                    //Set Custom InfoWindow Adapter

                }











                // TODO ListView set
                //ListView.setAdapter(mCustomAdapter);
                AddressInfo = AddressInfo.substring(0,AddressInfo.length()-1);
                String[] AddressArray = AddressInfo.split(",");
                //Log.d("addressArray",""+AddressArray);
                //setListView(AddressArray);

                Log.d("TAG", "onReceive: ");

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.033739,121.527886),11));
                Log.d("", " ");
            }
        };

        IntentFilter intentFilter = new IntentFilter("MyMessage");
        registerReceiver(myBroadcastReceiver, intentFilter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    //詢問權限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION },1
                    );

                    return;

                }
                googleMap.setMyLocationEnabled(true);

                GetData();
            }

        });


    }

    //處理使用者選擇後的結果
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetData();
                } else {
                    Toast.makeText(MainActivity.this,"不給權限不做事",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void GetData() {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("http://data.taipei/opendata/datalist/apiAccess" +
                        "?scope=resourceAquire&rid=a880adf3-d574-430a-8e29-3192a41897a5")
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("","");
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                Intent i = new Intent("MyMessage");
                i.putExtra("json", response.body().string());
                sendBroadcast(i);
            }
        });
    }


    public String getAddressByLocation(double lat, double lng )//將經緯度轉換成地址
    {
        String returnAddress="";
        try {
             if(returnAddress!=null) {

                 Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);
                List<Address> IstAddress = gc.getFromLocation(lat, lng, 1);

                if (!Geocoder.isPresent()) { //Since: API Level 9
                    returnAddress = "Sorry! Geocoder service not Present.";
                }
                returnAddress = IstAddress.get(0).getAddressLine(   0);

             }
        }
        catch (Exception e)
            {
                e.printStackTrace();

            }
            return  returnAddress;

    }



    // 設定searchView的文字輸入監聽
    private void setSearch_function() {
        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() != 0) {
                    ListView.setAdapter(mCustomAdapter);
                    mCustomAdapter.getFilter().filter(newText);
                }else ListView.setAdapter(null);

                return true;
            }
        });
    }
}








