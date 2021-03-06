/**
 * @category ContusMessanger
 * @package com.time.activities
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved. 
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.techuva.blackcard.contusfly.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techuva.blackcard.R;
import com.techuva.blackcard.contusfly.utils.Constants;
import com.techuva.blackcard.contusfly.utils.Utils;
import com.techuva.blackcard.contusfly.views.DoProgressDialog;


/**
 * The Activity class which acts as the base class for all other Activity classes for this
 * application, All other activities must extend this class so that they can receive the callbacks
 * of the chat service through the overriding methods of other this class all the callbacks
 * will be received from the broadcast receiver of this class.
 *
 * @author ContusTeam <developers@contus.in>
 * @version 1.1
 */
public class ActivitySelectMapView extends ActivityBase implements LocationListener, View.OnClickListener {

	private GoogleMap map;
	private double latitue;
	private double longitute;
	private MarkerOptions mp;

	/**
	 * The progress dialog.
	 */
	private DoProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_mapview);
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onPostCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		progressDialog = new DoProgressDialog(this);
		progressDialog.showProgress();
		Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.text_user_location));
		TextView txtNext = (TextView) findViewById(R.id.txt_create);
		txtNext.setText(getString(R.string.text_send));
		txtNext.setOnClickListener(this);
		mp = new MarkerOptions();

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    Activity#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for Activity#requestPermissions for more details.
			return;
		}
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		//map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.view_map)).getMap();

		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {

				map.clear();
				latitue =latLng.latitude;
				longitute = latLng.longitude;
				map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {
		progressDialog.dismiss();
		map.clear();
		mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
		mp.title("my position");
		map.addMarker(mp);
		latitue = location.getLatitude();
		longitute = location.getLongitude();
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(location.getLatitude(), location.getLongitude()),14));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// override method
	}

	@Override
	public void onProviderEnabled(String provider) {
        // override method
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
        // override method
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.txt_create){
			Intent result = new Intent();
			result.putExtra(Constants.LATITUDE,latitue);
			result.putExtra(Constants.LONGITUDE,longitute);
			setResult(Activity.RESULT_OK, result);
			finish();
		}
	}
}