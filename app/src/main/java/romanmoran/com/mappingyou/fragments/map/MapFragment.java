package romanmoran.com.mappingyou.fragments.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.api_utility.ApiClient;
import romanmoran.com.mappingyou.cluster_renderer.PersonRenderer;
import romanmoran.com.mappingyou.data.Item;
import romanmoran.com.mappingyou.data.PreferencesData;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.fragments.BaseFragment;
import romanmoran.com.mappingyou.utility.Constants;
import romanmoran.com.mappingyou.utility.Utility;

/**
 * Created by roman on 25.07.2017.
 */

public class MapFragment extends BaseFragment
        implements OnMapReadyCallback ,
        MapMvp.View {

    public static final String TAG = MapFragment.class.getName();

    private Spinner spinner;
    private GoogleMap mMap;
    private ClusterManager<Item> mClusterManager;
    private User user;
    private List<Item> itemsList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MapPresenter mPresenter;


    private int categoryPosition ;

    private LatLng lastLatLng;

    private float lastZoom;
    private int traffic = 1;
    private ApiClient apiClient = ApiClient.getInstance();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_map;
    }

    public static MapFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.USER_DATA_EXTRA, user);

        MapFragment fragment = new MapFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"MapFragment onCreate");
        mPresenter = new MapPresenter(this);
        if (getArguments()!=null){
            user = getArguments().getParcelable(Constants.USER_DATA_EXTRA);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"MapFragment onViewCreated");
        spinner = ButterKnife.findById(activity,R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mPresenter.addAllFriends(user);
                        break;
                    case 1:
                        mPresenter.addAllUsers(user);
                        break;
                    case 2:
                        mPresenter.addAllEvents(user);

                        break;
                }
                Log.d(TAG,"spinner = "+position);
                categoryPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onResume() {
        Log.d(TAG,"MapFragment onResume");
        if (mMap!=null){
            loadData();
            getCurrentPosition();
        }
        setUpMap();
        spinner.setVisibility(View.VISIBLE);
        super.onResume();
    }

    private void loadData() {
        lastLatLng = PreferencesData.getLatLng();

        lastZoom = PreferencesData.getZoom();
        categoryPosition = PreferencesData.getCategoryPosition();
        Log.d(TAG,"LOAD DATA X = "+lastLatLng.latitude+" Y = "+lastLatLng.longitude+" zoom"+lastZoom+" catPos = "+categoryPosition);

    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        uploadMarkers();

        Log.d(TAG,"setUpMap");
    }


    @Override
    public void onPause() {
        Log.d(TAG,"MapFragment Fragment onPause");
        saveData();
        spinner.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG,"MapFragment Fragment onStop");
        super.onStop();
    }

    private void saveData() {
        if (lastLatLng!=null) {
            PreferencesData.saveLastLocation(lastLatLng, lastZoom, categoryPosition);
            Log.d(TAG, "SAVE DATA X = " + lastLatLng.latitude + " Y = " + lastLatLng.longitude + " zoom" + lastZoom);
        }else {
            Log.d(TAG,"failed save data");
        }
    }

    private void saveDataMain(User userData) {
        PreferencesData.saveJson(Utility.getStringJsonWithData(userData));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                uploadMarkers();
                break;
            case R.id.menu_traffic_lights_actionbar:
                traffic++;
                mMap.setTrafficEnabled(traffic%2==0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;
        startDemo();
        loadData();

        getCurrentPosition();

    }

    protected void startDemo() {
        mClusterManager = new ClusterManager<Item>(activity, mMap);
        mClusterManager.setRenderer(new PersonRenderer(activity,mMap,mClusterManager));
        mMap.setOnCameraIdleListener(() -> {
            new DynamicallyAddMarkerTask().execute(mMap.getProjection().getVisibleRegion().latLngBounds);

            lastLatLng = mMap.getCameraPosition().target;
            lastZoom = mMap.getCameraPosition().zoom;
        });
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

    }

    @OnClick(R.id.imgZoomIn)
    void zoomIn() {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    @OnClick(R.id.imgZoomOut)
    void zoomOut() {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    @OnClick(R.id.imgMyLocation)
    void myLoc() {
        //statusCheck();
        FusedLocationProviderClient mFusedLocationClient = new FusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.LOCATION_PERMISSIONS_CODE);
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, location -> {
            if (location != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12);
                mMap.animateCamera(cameraUpdate);
                user.setCoordinates(location);

                apiClient.setCoord(user);
                saveDataMain(user);
            }
        });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utility.buildAlertMessageNoGps(activity);
        }
    }

    public void getCurrentPosition() {
        if (user.getLatLang()!=null) {
            LatLng latLng = user.getLatLang();

            View viewMaker = activity.getLayoutInflater().inflate(R.layout.marker, null);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(Utility.getBitmap(viewMaker));

            float zoom = 12;

            mMap.addMarker(markerOptions.position(latLng));
            Log.d(TAG, "curPos lastX = " + lastLatLng.latitude + " lastY = " + lastLatLng.longitude);
            if (lastLatLng.latitude != 0.0 && latLng.longitude != 0.0) {
                latLng = new LatLng(lastLatLng.latitude, lastLatLng.longitude);
                zoom = lastZoom;
            }
            Log.d(TAG, "latLng X = " + latLng.latitude + " Y = " + latLng.longitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }//if
    }


    private void uploadMarkers() {
        switch (categoryPosition){
            case 0:
                mPresenter.addAllFriends(user);
                break;
            case 1:
                mPresenter.addAllUsers(user);
                break;
            case 2:
                mPresenter.addAllEvents(user);
                break;
        }
        spinner.setSelection(categoryPosition);
        Log.d(TAG,"categoryPosition = "+categoryPosition);

    }

    @Override
    public void showToast(String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressIndicator(boolean active) {
        activity.runOnUiThread(() ->  {
            progressBar.setVisibility(active?View.VISIBLE:View.GONE);
            progressBar.setEnabled(active);
        });

        Log.d(TAG,"progressbar = "+progressBar.getVisibility());
    }

    @Override
    public void startUpdateMap(List<Item>itemsList) {
        this.itemsList = itemsList;
        if (mMap!=null)
            new DynamicallyAddMarkerTask().execute(mMap.getProjection().getVisibleRegion().latLngBounds);
    }


    class DynamicallyAddMarkerTask extends AsyncTask<LatLngBounds, Void, Void> {
        protected Void doInBackground(LatLngBounds... bounds) {
            if (mClusterManager!=null) mClusterManager.clearItems();
            if (itemsList != null) {
                if(itemsList.size()>0) {
                    for (Item currentUser : itemsList) {
                        if (bounds[0].contains(currentUser.getPosition())) {
                            mClusterManager.addItem(currentUser);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (itemsList !=null && progressBar.isEnabled()) {
                setProgressIndicator(false);
            Log.d(TAG,"onPostExecute itemList="+itemsList.size());}
            mClusterManager.cluster();
        }
    }



}
