package septian.org.osmdroid.pulangkonvoi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import septian.org.osmdroid.pulangkonvoi.Kelas.GRoute;
import septian.org.osmdroid.pulangkonvoi.Kelas.Graph;
import septian.org.osmdroid.pulangkonvoi.LocalData.MyDatabase;
import septian.org.osmdroid.pulangkonvoi.Services.Rute.RuteCallback;
import septian.org.osmdroid.pulangkonvoi.Services.Rute.RuteService;
import septian.org.osmdroid.pulangkonvoi.Utility.GraphLogic;

public class MapPanduan extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MapListener, RuteCallback {
    private MapView map;
    private LocationManager locationManager;
    private String provider;
    DbHelper dbHelper;
    Location location_direction;
    GraphLogic graphLogic;
    List<GRoute> gRouteList;
    private RuteService ruteService;
    int total = 0;
    int sum = 0;


    private GoogleApiClient mGoogleApiClient;

    Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setTitle("Panduan Konvoi");
        gRouteList = new ArrayList<>();

        setContentView(R.layout.activity_map_panduan);
        setLocation_direction();

        buildGoogleApiClient();
        dbHelper = new DbHelper(this);
        Log.d("asdku", "onCreate: "+dbHelper.checkDataBase());

        Log.d("asd", "onCreate: lagi "+new MyDatabase(this).graphs().size());

        map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
//        map.getController().setZoom(18);
//        map.getController().setCenter(new GeoPoint(-7.78295, 110.36698));


        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


        @Override
    protected void onPause() {
        super.onPause();
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);*/
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setMyLocation();
        makeAGraph();
        Log.d(".asdku", "onConnected: "+graphLogic.getGraphs_astar().size());
        initMarker(graphLogic.getGraphs_astar());
        setMarker(myLocation, 0);
        setMarker(location_direction, 1);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("asd", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    public void setMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        this.myLocation = new Location("");
        this.myLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        Log.d("map", "setMyLocation: "+myLocation.getLatitude());
    }

    private void initRouteFromService(ArrayList<Graph> graphs){

        for(Graph graph:graphs){
            /*GRoute gr = new GRoute();
            gr.setLat(graph.getStartlat());
            gr.setLng(graph.getStartlong());
            gRouteList.add(gr);*/
            new RuteService(this, this, graph.getStartlat(), graph.getStartlong(), graph.getEndlat(), graph.getEndlong());
            /*if(graph.getStartlat()!=graph.getEndlat() && graph.getStartlong()!=graph.getEndlong() ){
            }*/
        }
    }

    private void initGRoutes(List<GRoute> gRoutes) {
        Polyline line = new Polyline();
        line.setTitle("Rute");
        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(10f);
        line.setColor(Color.GRAY);
        List<GeoPoint> pts = new ArrayList<>();

        for(GRoute gRoute : gRoutes){
            pts.add(new GeoPoint(gRoute.getLat(), gRoute.getLng()));
            Log.d(".asdruteku", "initGRoutes: "+gRoute.getLat()+", "+gRoute.getLng());
        }

        /*pts.add(new GeoPoint(40.796788, -73.949232));
        pts.add(new GeoPoint(40.796788, -73.981762));
        pts.add(new GeoPoint(40.768094, -73.981762));
        pts.add(new GeoPoint(40.768094, -73.949232));
        pts.add(new GeoPoint(40.796788, -73.949232));*/
        line.setPoints(pts);
        line.setGeodesic(true);
        map.getOverlayManager().add(line);
//        map.setMaxZoomLevel(22.0);
    }


    private void initRute(ArrayList<Graph> graphs) {
        Polyline line = new Polyline();
        line.setTitle("Rute");
        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(10f);
        line.setColor(Color.GRAY);
        List<GeoPoint> pts = new ArrayList<>();

        for(Graph graph:graphs){
            pts.add(new GeoPoint(graph.getStartlat(), graph.getStartlong()));
        }

        /*pts.add(new GeoPoint(40.796788, -73.949232));
        pts.add(new GeoPoint(40.796788, -73.981762));
        pts.add(new GeoPoint(40.768094, -73.981762));
        pts.add(new GeoPoint(40.768094, -73.949232));
        pts.add(new GeoPoint(40.796788, -73.949232));*/
        line.setPoints(pts);
        line.setGeodesic(true);
        map.getOverlayManager().add(line);
//        map.setMaxZoomLevel(22.0);
    }

    private void initMarker(ArrayList<Graph> graphs){
        for(Graph graph:graphs){
            Location l = new Location("");
            l.setLatitude(graph.getStartlat());
            l.setLongitude(graph.getStartlong());
            setMarker(l, -1);
//            pts.add(new GeoPoint(graph.getStartlat(), graph.getStartlong()));
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void setLocation_direction(){
        location_direction = new Location("");
        location_direction.setLatitude(Double.valueOf(getIntent().getStringExtra("lat")));
        location_direction.setLongitude(Double.valueOf(getIntent().getStringExtra("lng")));
        Log.d("asd", "setLocation_direction: "+location_direction.getLatitude()+" "+location_direction.getLongitude());
    }

    private void setMarker(final Location location, int a){
//        LatLng lokasi_tujuan = new LatLng(location.getLatitude(), location.getLongitude());
        GeoPoint gp = new GeoPoint(location.getLatitude(), location.getLongitude());
        if(a==0){
            IGeoPoint iGeoPoint = new IGeoPoint() {
                @Override
                public int getLatitudeE6() {
                    return 0;
                }

                @Override
                public int getLongitudeE6() {
                    return 0;
                }

                @Override
                public double getLatitude() {
                    return location.getLatitude();
                }

                @Override
                public double getLongitude() {
                    return location.getLongitude();
                }
            };
            map.getController().setZoom(13);
            map.getController().setCenter(iGeoPoint);

            Marker marker = new Marker(map);
            marker.setDraggable(false);
            marker.setTitle("Lokasi Anda");
            marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(R.drawable.marker_default));
            marker.setDraggable(true);
            map.getOverlays().add(marker);

        }else if(a==-1) {
            Marker marker = new Marker(map);
            marker.setDraggable(false);
            marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_mylocation));
            marker.setTitle("titik");
            marker.setDraggable(true);
            map.getOverlays().add(marker);
        }else{
                Marker marker = new Marker(map);
                marker.setDraggable(false);
                marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setIcon(getResources().getDrawable(R.drawable.marker));
                marker.setTitle("Lokasi Kumpul Konvoi");
                marker.setDraggable(true);
                map.getOverlays().add(marker);

            /*final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.marker);
            ArrayList<OverlayItem> overlayArray = new ArrayList<>();
            OverlayItem mapItem = new OverlayItem("Lokasi Kumpul Konvoi", "", gp);
            mapItem.setMarker(marker);
            overlayArray.add(mapItem);*/

        }
    }

    private void makeAGraph(){
        graphLogic = new GraphLogic(myLocation, location_direction, new MyDatabase(this).graphs());
        graphLogic.makeAGraph();

        for(Graph g : graphLogic.getGraphs_astar()){
            Log.d(".asd", "setMarker: "+g.getUrutan_first()+" - "+g.getUrutan_last());
            Log.d(".asd", "setFirstLocation: "+g.getStartlat()+" - "+g.getStartlong());
            Log.d(".asd", "setLastLocation: "+g.getEndlat()+" - "+g.getEndlong());
        }

        sum = graphLogic.getGraphs_astar().size();
        initRouteFromService(graphLogic.getGraphs_astar());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }

    @Override
    public void setMarker(String jarak, List<GRoute> routes, Location l, int status) {
        if(status==1){
            ++total;
            for(GRoute g:routes){
                gRouteList.add(g);
            }
            /*GRoute gr = new GRoute();
            gr.setLat(l.getLatitude());
            gr.setLng(l.getLongitude());
            gRouteList.add(gr);*/
            if(total==sum){
                initGRoutes(this.gRouteList);
            }
        }
    }
}
