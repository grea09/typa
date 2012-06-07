package fr.utbm.lo52.sodia.ui;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import fr.utbm.lo52.sodia.R;

public class LocateActivity extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.locate);
	    setTitle("Locating friends");
	   
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
		MapController myMapController = mapView.getController();
	    myMapController.setCenter(new GeoPoint(48835797,2373047));
	    
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.ic_locate);
	    LocateItemizedOverlay itemizedoverlay = new LocateItemizedOverlay(drawable, this);
	    
	    GeoPoint point = new GeoPoint(48835797,2373047);
	    OverlayItem overlayitem = new OverlayItem(point, "Antoine", "Je suis ici !");
	    GeoPoint point2 = new GeoPoint(48830000, 2373000);
	    OverlayItem overlayitem2 = new OverlayItem(point2, "Geoffrey", "Je suis lˆ !");
	    itemizedoverlay.addOverlay(overlayitem2);
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	}
	
}
