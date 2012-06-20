package fr.utbm.lo52.sodia.ui;

import android.accounts.Account;
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
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.protocols.Protocol;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.Set;

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
	    
	    
	    for (Account account : Protocol.getAccountsByType(new Typa().getAccountType())){
		for (Contact contact : Contact.getAll(account)){
		     int[] coord = contact.getPosition();
		     GeoPoint point = new GeoPoint(coord[0],coord[1]);
		     OverlayItem overlayitem = new OverlayItem(point,contact.getName(), "Distance : ");
		     itemizedoverlay.addOverlay(overlayitem);
		}
	    }
	    

	}
	
}
