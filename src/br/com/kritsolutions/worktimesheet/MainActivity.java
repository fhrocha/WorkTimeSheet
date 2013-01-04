package br.com.kritsolutions.worktimesheet;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private LocationManager locationManager;
	private Button position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		((Button) findViewById(R.id.buttonPosition)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCurrentLocation();
			}
		});
	}
	
	protected void showCurrentLocation() {
		
		String message = "";
		
		Location location = getGPSLocation();
		location = location == null ? getNETWORKLocation() : location;
		
		if( location != null ) {
			message = String.format("Current location \n Longitude: %1$s \n Latitude: %2$s", 
					location.getLongitude(), location.getLatitude());
			
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
		}		
	}

	private Location getGPSLocation() {
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, new MyLocationListener());		
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	private Location getNETWORKLocation() {

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, new MyLocationListener());
		return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	
	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location _location) {

			String message = String.format("New location \n Longitude: %1$s \n Latitude: %2$s", 
					_location.getLongitude(), _location.getLatitude());
			
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
