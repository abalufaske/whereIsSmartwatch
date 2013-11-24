package abalufaske.where.is;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;

import android.R.color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends android.support.v4.app.FragmentActivity
		implements OnMarkerClickListener, OnItemSelectedListener {
	
	private static GoogleMap mMap;
	private Marker mMelbourne;
	private LatLng poi = new LatLng(1.0000, -7);
	
	private Marker Me;
	public String[] strings = { "0", "1", "2", "3" };
	
	private int last = 0;
	private static List<Place> mListPlaces;
	private LocationManager mLocationManager;
	public static int currentItem;
	private Button savePos;
	private Button travel;
	private Button changeName;
	private Button parking;
	public int colorselected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bundle bundle = getIntent().getExtras();
		boolean isRedirected = bundle.getBoolean("smartWatch");
		
		startLocationListener(isRedirected);
		
		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
		}

		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {

			if (mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false) {

				Toast toast1 = Toast.makeText(getApplicationContext(),
						R.string.nolocationprovider, Toast.LENGTH_LONG);

				toast1.show();
			} else {

				Toast toast1 = Toast.makeText(getApplicationContext(),
						R.string.nogps, Toast.LENGTH_LONG);

				toast1.show();

			}
		}

		savePos = (Button) findViewById(R.id.savePos);
		travel = (Button) findViewById(R.id.travel);
		changeName = (Button) findViewById(R.id.changeName);
		parking = (Button) findViewById(R.id.parking);

		savePos.setOnClickListener(mOnClickListenerSavePos);
		travel.setOnClickListener(mOnClickListenerTravel);
		changeName.setOnClickListener(mOnClickListenerChangeName);
		parking.setOnClickListener(mOnClickListenerParking);
		ImageButton sharingButton = (ImageButton) findViewById(R.id.shareButton);

		sharingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				shareIt();
			}
		});

		Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.layers_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(this);
		BaseDatos myDbHelper = new BaseDatos(this.getApplicationContext());
		myDbHelper.openDataBase();

		ItemsDB mPlaceDB = new ItemsDB(this.getApplicationContext());
		mListPlaces = mPlaceDB.getAll(this.getApplicationContext(), last);

		strings[0] = mListPlaces.get(0).getName();
		strings[1] = mListPlaces.get(1).getName();
		strings[2] = mListPlaces.get(2).getName();
		strings[3] = mListPlaces.get(3).getName();

		mPlaceDB.close();
		myDbHelper.close();

		updateSpinner(strings);

		setUpMapIfNeeded();
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				poi.latitude, poi.longitude), 15));

	}
	
	private LocationListener locationListener;
	private LocationManager locationManager;
	
	private void startLocationListener(boolean saveLocationToDB) {
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		locationListener = new LocationListener() {
			
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
//		      updateLocation(location);
		      // TODO
		    }
		    
		    public void onStatusChanged(String provider, int status, Bundle extras) {}
		    
		    public void onProviderEnabled(String provider) {}
		    
		    public void onProviderDisabled(String provider) {}
		  };
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (long) 0, (long) 0, locationListener);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		locationManager.removeUpdates(locationListener);
		locationManager = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play
	 * services APK is correctly installed) and the map has not already been
	 * instantiated.. This will ensure that we only ever call
	 * {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt
	 * for the user to install/update the Google Play services APK on their
	 * device.
	 * <p>
	 * A user can return to this Activity after following the prompt and
	 * correctly installing/updating/enabling the Google Play services. Since
	 * the Activity may not have been completely destroyed during this process
	 * (it is likely that it would only be stopped or paused),
	 * {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera. In this case, we just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		/*
		 * mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new
		 * LatLng(PERTH.latitude,PERTH.longitude), 15));
		 */
		addMarkersToMap();
		// mMap.addMarker(new MarkerOptions().position(new LatLng(0,
		// 0)).title("Marker"));
		mMap.setOnMarkerClickListener(this);
		if (mMap.getMyLocation() != null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap
					.getMyLocation().getLatitude(), mMap.getMyLocation()
					.getLongitude()), 10));
		}
	}

	private void addMarkersToMap() {
		// Uses a colored icon.

		mMelbourne = mMap.addMarker(new MarkerOptions()
				.position(poi)
				.title(strings[currentItem])
				.snippet("")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// This causes the marker at Perth to bounce into position when it is
		// clicked.
		if (marker.equals(mMelbourne)) {

			final Handler handler = new Handler();
			final long start = SystemClock.uptimeMillis();
			Projection proj = mMap.getProjection();
			Point startPoint = proj.toScreenLocation(poi);
			startPoint.offset(0, -50);
			final LatLng startLatLng = proj.fromScreenLocation(startPoint);
			final long duration = 1500;

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					poi.latitude, poi.longitude), 15));

			final Interpolator interpolator = new BounceInterpolator();

			handler.post(new Runnable() {
				@Override
				public void run() {
					long elapsed = SystemClock.uptimeMillis() - start;
					float t = interpolator.getInterpolation((float) elapsed
							/ duration);
					double lng = t * poi.longitude + (1 - t)
							* startLatLng.longitude;
					double lat = t * poi.latitude + (1 - t)
							* startLatLng.latitude;
					marker.setPosition(new LatLng(lat, lng));

					if (t < 1.0) {
						// Post again 16ms later.
						handler.postDelayed(this, 16);
					}
				}
			});
		}
		// We return false to indicate that we have not consumed the event and
		// that we wish
		// for the default behavior to occur (which is for the camera to move
		// such that the
		// marker is centered and for the marker's info window to open, if it
		// has one).
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (!checkReady()) {
			return;
		}
		setLayer((String) parent.getItemAtPosition(position));
	}

	private void setLayer(String layerName) {
		if (layerName.equals(getString(R.string.normal))) {
			mMap.setMapType(MAP_TYPE_NORMAL);
		} else if (layerName.equals(getString(R.string.hybrid))) {
			mMap.setMapType(MAP_TYPE_HYBRID);
		} else if (layerName.equals(getString(R.string.satellite))) {
			mMap.setMapType(MAP_TYPE_SATELLITE);
		} else if (layerName.equals(getString(R.string.terrain))) {
			mMap.setMapType(MAP_TYPE_TERRAIN);
		} else {
			Log.i("LDA", "Error setting layer with name " + layerName);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do nothing.
	}

	private boolean checkReady() {
		if (mMap == null) {
			Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.textView1);
			label.setText(strings[position]);
			label.setHeight(80);
			label.setGravity(Gravity.CENTER);
			label.setGravity(Gravity.CENTER_VERTICAL);

			label.setTextColor(Color.GRAY);

			return row;
		}

	}

	public class MyAdapter2 extends ArrayAdapter<String> {

		public MyAdapter2(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			String[] colors = {
					getApplicationContext().getString(R.string.white),
					getApplicationContext().getString(R.string.green),
					getApplicationContext().getString(R.string.red),
					getApplicationContext().getString(R.string.blue),
					getApplicationContext().getString(R.string.yellow),
					getApplicationContext().getString(R.string.orange),
					getApplicationContext().getString(R.string.purple),
					getApplicationContext().getString(R.string.pink),
					getApplicationContext().getString(R.string.brown) };
			int[] colors2 = { Color.WHITE, Color.GREEN, Color.RED, Color.BLUE,
					Color.YELLOW, Color.rgb(250, 204, 67),
					Color.rgb(255, 48, 203), Color.rgb(255, 20, 147),
					Color.rgb(139, 69, 19) };

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			TextView label = (TextView) row.findViewById(R.id.textView1);
			label.setHeight(60);
			label.setText(colors[position]);
			row.setBackgroundColor(colors2[position]);
			label.setTextColor(Color.BLACK);

			return row;
		}

	}

	private void changeCamera(CameraUpdate update) {

		mMap.animateCamera(update);

	}

	OnItemSelectedListener mSpinnerItemSelected = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView parentView,
				View selectedItemView, int position, long id) {

			currentItem = position;
			mMap.clear();

			poi = new LatLng(mListPlaces.get(position).getLatitude(),
					mListPlaces.get(position).getLongitude());
			addMarkersToMap();
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					poi.latitude, poi.longitude), 15));

		}

		public void onNothingSelected(AdapterView parentView) {
			// your code here

		}

	};

	OnItemSelectedListener mSpinnerColorSelected = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView parentView,
				View selectedItemView, int position, long id) {

			colorselected = position;

		}

		public void onNothingSelected(AdapterView parentView) {
			// your code here

		}

	};

	OnClickListener mOnClickListenerSavePos = new OnClickListener() {

		public void onClick(View v) {

			try {
				ItemsDB mItemDB = new ItemsDB(getApplicationContext());
				mItemDB.setLat(mListPlaces.get(currentItem).getId(),
						String.valueOf(mMap.getMyLocation().getLongitude()));
				mItemDB.setLong(mListPlaces.get(currentItem).getId(),
						String.valueOf(mMap.getMyLocation().getLatitude()));
				mItemDB.setLast(mListPlaces.get(currentItem).getId(), 1);

				mMap.clear();

				poi = new LatLng(mMap.getMyLocation().getLatitude(), mMap
						.getMyLocation().getLongitude());
				addMarkersToMap();
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						poi.latitude, poi.longitude), 15));

			} catch (Exception e) {
				Toast toast1 = Toast.makeText(getApplicationContext(),
						R.string.nolocationprovider, Toast.LENGTH_LONG);

				toast1.show();

				return;
			}

		}
	};

	OnClickListener mOnClickListenerTravel = new OnClickListener() {

		public void onClick(View v) {
			final String[] items = {
					getApplicationContext().getString(R.string.onFoot),
					getApplicationContext().getString(R.string.driving),
					getApplicationContext().getString(R.string.publicTransport) };

			if (checkConnection() == true) {

				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.travel);
				dialog.setTitle(R.string.choosetravel);
				dialog.setCancelable(true);
				// there are a lot of settings, for dialog, check them all out!

				// set up text
				LinearLayout onFoot = (LinearLayout) dialog
						.findViewById(R.id.LinearLayoutOnFoot);
				LinearLayout driving = (LinearLayout) dialog
						.findViewById(R.id.LinearLayoutDriving);
				LinearLayout transport = (LinearLayout) dialog
						.findViewById(R.id.LinearLayoutTransport);

				onFoot.setOnClickListener(mOnClickListenerOnFoot);
				driving.setOnClickListener(mOnClickListenerDriving);
				transport.setOnClickListener(mOnClickListenerPublicTransport);

				// set up button
				Button cancel = (Button) dialog.findViewById(R.id.TravelCancel);
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});

				// set up button

				// now that the dialog is set up, it's time to show it
				dialog.show();
			} else {
				Toast toast1 = Toast.makeText(getApplicationContext(),
						R.string.nolocationprovider, Toast.LENGTH_LONG);

				toast1.show();

			}
		}
	};

	OnClickListener mOnClickListenerParking = new OnClickListener() {

		public void onClick(View v) {
			Log.d("prueba", " parking");

			final Dialog dialog = new Dialog(MainActivity.this);
			dialog.setContentView(R.layout.parking);
			dialog.setTitle(R.string.parkingDialog);
			dialog.setCancelable(true);
			// there are a lot of settings, for dialog, check them all out!

			String[] colors = {
					getApplicationContext().getString(R.string.white),
					getApplicationContext().getString(R.string.green),
					getApplicationContext().getString(R.string.red),
					getApplicationContext().getString(R.string.blue),
					getApplicationContext().getString(R.string.yellow),
					getApplicationContext().getString(R.string.orange),
					getApplicationContext().getString(R.string.purple),
					getApplicationContext().getString(R.string.pink),
					getApplicationContext().getString(R.string.brown) };

			ParkingDB mParkingDB = new ParkingDB(getApplicationContext());
			List<Parking> park = mParkingDB.getAll(getApplicationContext(), 0);
			Spinner mySpinner = (Spinner) dialog
					.findViewById(R.id.parkingSpinner);
			mySpinner.setAdapter(new MyAdapter2(MainActivity.this,
					R.layout.row, colors));

			mySpinner.setOnItemSelectedListener(mSpinnerColorSelected);
			mySpinner.setSelection(park.get(0).getColor());
			// set up text
			final EditText floor = (EditText) dialog
					.findViewById(R.id.editFloor);
			final EditText number = (EditText) dialog
					.findViewById(R.id.editNumber);
			final EditText section = (EditText) dialog
					.findViewById(R.id.editSection);
			final EditText notes = (EditText) dialog
					.findViewById(R.id.editNotes);

			floor.setText(String.valueOf(park.get(0).getFloor()));
			number.setText(String.valueOf(park.get(0).getNumber()));
			section.setText(park.get(0).getSection());
			notes.setText(park.get(0).getNotes());

			// set up button
			Button cancel = (Button) dialog.findViewById(R.id.parkingCancel);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});

			// set up button
			Button save = (Button) dialog.findViewById(R.id.parkingSave);
			save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					/**
					 * ItemsDB mItemDB = new ItemsDB(getApplicationContext());
					 * mItemDB.setName(mListPlaces.get(currentItem).getId(),
					 * floor.getText().toString());
					 * 
					 * strings[currentItem] = floor.getText().toString();
					 * updateSpinner(strings);
					 */

					ParkingDB mParkingDB = new ParkingDB(
							getApplicationContext());
					mParkingDB.setParking(colorselected, parseInt(floor),
							parseInt(number), section.getText().toString(),
							notes.getText().toString());
					// setParking (final int color , final int floor, final int
					// number, final String Section , final String Notes)

					updateWidget();

					dialog.cancel();

				}
			});

			// now that the dialog is set up, it's time to show it
			dialog.show();

		}
	};

	OnClickListener mOnClickListenerOnFoot = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(
					android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?saddr="
							+ Double.toString((double) mMap.getMyLocation()
									.getLatitude())
							+ ","
							+ Double.toString((double) mMap.getMyLocation()
									.getLongitude()) + "&daddr=" + poi.latitude
							+ "," + poi.longitude + "&dirflg=w&mra=ls&t=m&z=16"));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);

		}
	};

	OnClickListener mOnClickListenerDriving = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?saddr="
							+ Double.toString((double) mMap.getMyLocation()
									.getLatitude())
							+ ","
							+ Double.toString((double) mMap.getMyLocation()
									.getLongitude()) + "&daddr=" + poi.latitude
							+ "," + poi.longitude + "&dirflg=d"));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);
		}
	};

	OnClickListener mOnClickListenerPublicTransport = new OnClickListener() {

		public void onClick(View v) {

			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?saddr="
							+ Double.toString((double) mMap.getMyLocation()
									.getLatitude())
							+ ","
							+ Double.toString((double) mMap.getMyLocation()
									.getLongitude()) + "&daddr=" + poi.latitude
							+ "," + poi.longitude + "&dirflg=r"));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);

		}
	};

	OnClickListener mOnClickListenerChangeName = new OnClickListener() {

		public void onClick(View v) {

			final Dialog dialog = new Dialog(MainActivity.this);
			dialog.setContentView(R.layout.changename);
			dialog.setTitle(R.string.setName);
			dialog.setCancelable(true);
			// there are a lot of settings, for dialog, check them all out!

			// set up text
			final EditText input = (EditText) dialog
					.findViewById(R.id.editChangeName);

			// set up button
			Button cancel = (Button) dialog.findViewById(R.id.changeNameCancel);
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});

			// set up button
			Button save = (Button) dialog.findViewById(R.id.changeNameSave);
			save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					ItemsDB mItemDB = new ItemsDB(getApplicationContext());
					mItemDB.setName(mListPlaces.get(currentItem).getId(), input
							.getText().toString());
					strings[currentItem] = input.getText().toString();
					updateSpinner(strings);

					dialog.cancel();

				}
			});

			// now that the dialog is set up, it's time to show it
			dialog.show();

		}
	};

	public void updateSpinner(String[] strings2) {
		Spinner mySpinner = (Spinner) findViewById(R.id.item_spinner);
		mySpinner.setAdapter(new MyAdapter(MainActivity.this, R.layout.row,
				strings2));
		mySpinner.setOnItemSelectedListener(mSpinnerItemSelected);

	}

	private boolean checkConnection() {
		if (mMap.getMyLocation() == null) {

			return false;
		} else {
			return true;
		}

	}

	public static int parseInt(EditText text) {
		if (text.getText().toString().trim().length() != 0) {
			return Integer.parseInt(text.getText().toString());
		} else {
			return 0;
		}
	}

	public void updateWidget() {
		Intent intent = new Intent(this, WhereisWidgetService.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		// Use an array and EXTRA_APPWIDGET_IDS instead of
		// AppWidgetManager.EXTRA_APPWIDGET_ID,
		// since it seems the onUpdate() is only fired on that:
		int ids[] = AppWidgetManager.getInstance(getApplication())
				.getAppWidgetIds(
						new ComponentName(getApplication(),
								WhereisWidgetService.class));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		sendBroadcast(intent);

	}

	private void shareIt() {
		// sharing implementation here

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "https://maps.google.es/maps?q=" + poi.latitude
				+ "," + poi.longitude + " ";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getApplicationContext().getString(R.string.sharesubject));
		sharingIntent
				.putExtra(
						android.content.Intent.EXTRA_TEXT,
						getApplicationContext()
								.getString(R.string.sharestring1)
								+ " "
								+ strings[currentItem]
								+ " "
								+ getApplicationContext().getString(
										R.string.sharestring2)
								+ " "
								+ shareBody
								+ getApplicationContext().getString(
										R.string.sharestring3)
								+ " "
								+ getApplicationContext().getString(
										R.string.app_name)
								+ " https://play.google.com/store/apps/details?id=abalufaske.where.is");
		startActivity(Intent.createChooser(sharingIntent,
				getApplicationContext().getString(R.string.sharevia)));

	}
	
	public static void savePosition(Context mContext, int position) {
		
		try {
			
			if (mMap != null) {
				
				Toast.makeText(mContext, "Location is Saved", Toast.LENGTH_SHORT).show();
				
				ItemsDB mItemDB = new ItemsDB(mContext);
				mItemDB.setLat(mListPlaces.get(position).getId(),
						String.valueOf(mMap.getMyLocation().getLongitude()));
				mItemDB.setLong(mListPlaces.get(position).getId(),
						String.valueOf(mMap.getMyLocation().getLatitude()));
				mItemDB.setLast(mListPlaces.get(position).getId(), 1);
				
				mMap.clear();
//				android.os.Process.killProcess(android.os.Process.myPid());
			} else {
				
				Toast.makeText(mContext, "Map is Null", Toast.LENGTH_SHORT).show();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
