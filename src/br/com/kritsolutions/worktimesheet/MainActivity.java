package br.com.kritsolutions.worktimesheet;

import java.math.BigDecimal;

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
	
	private static final int INTERVAL_MINUTES = 1000 * 60 * 1;
	private static final float THIRTY_METERS = 30;
	private LocationManager locationManager;
	private Double initialUserLatitude;
	private Double initialUserLongitude;

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
			
			initialUserLatitude = location.getLatitude();
			initialUserLongitude = location.getLongitude();
			
			message = String.format("Current location \n Longitude: %1$s \n Latitude: %2$s", 
					initialUserLongitude, initialUserLatitude);
			
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
		}		
	}

	private Location getGPSLocation() {
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
												INTERVAL_MINUTES, 
												THIRTY_METERS , 
												new MyLocationListener());		
		return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	private Location getNETWORKLocation() {

		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
												INTERVAL_MINUTES, 
												THIRTY_METERS, 
												new MyLocationListener());
		return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	
	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location _location) {
			
			double longitude = _location.getLongitude();
			double latitude = _location.getLatitude();
			Double distance = calculateDistance(initialUserLatitude, initialUserLongitude, latitude, longitude);

			String message = String.format("New location \n Longitude: %1$s \n Latitude: %2$s \n Distance: %3$s", 
					longitude, latitude, distance);
			
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
	
	private static Double calculateDistance(Double _latitudeOri, Double _longitudeOri, Double _latitudeDes, Double _longitudeDes) {
		
		Float EARTH_RADIUS_KM = 6371f;
		
		Double dLat = Math.toRadians( _latitudeDes - _latitudeOri );
		Double dLon = Math.toRadians( _longitudeDes - _longitudeOri );
		
		Double latOriRadius = Math.toRadians( _latitudeOri );
		Double latDesRadius = Math.toRadians( _latitudeDes );
		
		Double haversine_a = Math.sin( dLat / 2 ) * Math.sin( dLat / 2 ) 
							+ Math.sin( dLon / 2 ) * Math.sin( dLon / 2 )
							* Math.cos( latOriRadius ) * Math.cos( latDesRadius );
		
		Double haversine_c = 2 * Math.atan2( Math.sqrt( haversine_a ), Math.sqrt( 1 - haversine_a ) );
		
		return new BigDecimal(EARTH_RADIUS_KM * haversine_c).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
