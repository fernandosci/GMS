package uk.ac.gla.dcs.gms.lms;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.kml.KmlLayer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import uk.ac.gla.dcs.gms.api.GMS;
import uk.ac.gla.dcs.gms.api.GMSException;
import uk.ac.gla.dcs.gms.api.http.HTTPProgressStatus;
import uk.ac.gla.dcs.gms.api.http.HTTPResponseListener;
import uk.ac.gla.dcs.gms.api.lms.LMSSession;
import uk.ac.gla.dcs.gms.api.lms.LMSTrailsRequestParamBuilder;
import uk.ac.gla.dcs.gms.main.GMSMainFragment;

public class LMSMapFragment extends GMSMainFragment {
    public static final String ARG_LAT = "Lat";
    public static final String ARG_LON = "Lon";
    public static final String ARG_MODE = "MODE";
    public static final int MODE_TRAILS = 0x01;
    public static final int MODE_HEATMAP = 0x02;

    private MapView mapView;
    private GoogleMap map;
    private int mode;
    private LMSSession lmsSession;
    private LMSMapFragment.localHttpListener localHttpListener;
    private Context context;


    public static LMSMapFragment newInstance(String section, int mode ) {
        LMSMapFragment lmsMapFragment = new LMSMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        args.putInt(ARG_MODE, mode);
        lmsMapFragment.setArguments(args);
        return lmsMapFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mainf_maps, container, false);

        context = inflater.getContext();

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());



        try {
            lmsSession = GMS.getInstance().getLMSSession();
            localHttpListener = new localHttpListener();
        } catch (GMSException e) {
            e.printStackTrace();
            //TODO handle this error
            lmsSession = null;
        }

        Bundle bundle = getArguments();
        LatLng latLng;
        // Updates the initial location and zoom of the MapView
        if (bundle.containsKey(ARG_LAT) && bundle.containsKey(ARG_LON)) {
            latLng = new LatLng(bundle.getDouble(ARG_LAT), bundle.getDouble(ARG_LON));
        }else{
            //default position
            latLng = new LatLng(55.864237, -4.251806);  //fixme move to resources
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        map.animateCamera(cameraUpdate);
        map.setOnCameraChangeListener(new MapZoomListener());

        if (bundle.containsKey(ARG_MODE)){
            mode = bundle.getInt(ARG_MODE);
        }else
            mode = MODE_TRAILS; //default
        onModeChanged();

        return v;
    }

    public void changeMode(int mode){
        int oldmode = this.mode;
        this.mode = mode;
        if (oldmode != this.mode)
            onModeChanged();
    }


    private void onModeChanged(){
        if (mode == MODE_TRAILS){
            initializeTrails();

        }else if (mode == MODE_HEATMAP){
            //default mode
            initializeHeatMap();
        }
    }


    private void initializeTrails(){
        if (lmsSession != null) {
            //todo get better from and to interval
            Calendar from = Calendar.getInstance();
            from.setTimeInMillis(0);
            Calendar to = Calendar.getInstance();
            lmsSession.getTrails(localHttpListener,MODE_TRAILS, new LMSTrailsRequestParamBuilder().setIsPersonal(true).setIsHeatmap(false).setInterval(from,to).toString());
        }
    }

    private void initializeHeatMap(){
        if (lmsSession != null) {
            //todo get better from and to interval
            Calendar from = Calendar.getInstance();
            from.setTimeInMillis(0);
            Calendar to = Calendar.getInstance();
            lmsSession.getTrails(localHttpListener,MODE_HEATMAP, new LMSTrailsRequestParamBuilder().setIsPersonal(true).setIsHeatmap(true).setInterval(from,to).toString());
        }
    }

    private void addTrails(String kml){

        try {
            InputStream stream = new ByteArrayInputStream(kml.getBytes("UTF-8"));
            KmlLayer layer = new KmlLayer(map, stream, context);
            layer.addLayerToMap();

        } catch (Exception e) {
            e.printStackTrace();
            //todo handle exception
        }


    }

    private void addHeatMap(List<LatLng> list) {

//        int[] colors = {
//                Color.rgb(0, 225, 255),
//                Color.rgb(0, 225, 255),
//                Color.rgb(0, 191, 255),
//                Color.rgb(0, 127, 255),
//                Color.rgb(0, 63, 255),
//                Color.rgb(0, 0, 255),
//                Color.rgb(0, 0, 223),
//                Color.rgb(0, 0, 191),
//                Color.rgb(0, 0, 159),
//                Color.rgb(0, 0, 127),
//                Color.rgb(63, 0, 91),
//                Color.rgb(127, 0, 63),
//                Color.rgb(191, 0, 31),
//                Color.rgb(255, 0, 0),
//        };
//        float start, end;
//        start = 0.5f;
//        end = 1f;
//        float[] startPoints = new float[colors.length];
//        float step = (end - start) / (float)colors.length;
//        for (int c = 0; c < colors.length; c++) {
//            start+=step;
//            startPoints[c] = start;
//        }

        int[] colors = {
               /* Color.rgb(102, 225, 0), // green*/
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                /*0.2f, */1f
        };



        Gradient gradient = new Gradient(colors, startPoints, list.size()/100);
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .opacity(1.0)
                .radius(50)
                .gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private class localHttpListener implements HTTPResponseListener {
        @Override
        public void onResponse(int requestCode, boolean successful, HashMap<String, Object> data, Exception exception) {

            if (requestCode == MODE_TRAILS) {
                String xml_trails = (String) data.get("xml_trails");
                addTrails(xml_trails);
            }else if (requestCode == MODE_HEATMAP){
                List<LatLng> list = (List<LatLng>) data.get("heatmap");
                addHeatMap(list);
            }

        }

        @Override
        public void onProgress(int requestCode, HTTPProgressStatus progressStatus, HashMap<String, Object> newdata) {

        }
    }
}
