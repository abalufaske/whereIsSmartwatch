package abalufaske.where.is;

import java.util.Arrays;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


public class WhereisWidgetService extends AppWidgetProvider {

	public int[] colors2 = {Color.WHITE,Color.GREEN,Color.RED, Color.BLUE,Color.YELLOW,
    		Color.rgb(250, 204, 67),Color.rgb(255, 48, 203),
    		Color.rgb(255, 20, 147),Color.rgb(139, 69,19)};
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

	/*	if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
			Toast.makeText(context, "onReceiver()", Toast.LENGTH_LONG).show();
		}*/
		
		
	}
	
	@Override
	public void onEnabled(Context context)
	{
		
		ParkingDB mParkingDB = new ParkingDB(context);
		List<Parking> park = mParkingDB.getAll(context, 0);
		String parking = (context.getString(R.string.FloorParking) +":"+ park.get(0).getFloor()+" - " + context.getString(R.string.NumberParking) +":"+ park.get(0).getNumber()+" - " + context.getString(R.string.SectionParking) +":"+ park.get(0).getSection() );
		Log.d("mes","ola que tal esto tiene datos? "+ park.get(0).getNotes());
		
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, 0);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		views.setOnClickPendingIntent(R.id.widgetlinearlayout, pendingIntent);
		// To update a label
		//views.setInt(R.id.widgetlinearlayout, "setBackgroundResource", R.color.red);
		views.setImageViewBitmap(R.id.bgcolor, getBackground(colors2[park.get(0).getColor()]));
	//	views.setInt(R.id.widgetText, "setBackgroundColor", android.graphics.Color.RED);

		views.setTextViewText(R.id.widgetText,parking);
		views.setTextViewText(R.id.notewidget, park.get(0).getNotes());
		
		 switch (park.get(0).getColor()) {
		 case 0:
				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

		        break;
		    case 1:

				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);
		        break;
		    case 2:
				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

		        break;
		    case 3:

				views.setTextColor(R.id.widgetText, android.graphics.Color.WHITE);
				views.setTextColor(R.id.notewidget, android.graphics.Color.WHITE);
		        break;
		    case 4:
				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

		        break;
		    case 5:

				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);
		        break;
		    case 6:
				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

		        break;
		    case 7:

				views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
				views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);
		        break;
		    
		    }
		
		
		mParkingDB.close();
		
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appwidgets)
	
	{
		final int N = appwidgets.length;

		Log.i("ExampleWidget",	"Updating widgets " + Arrays.asList(appwidgets));

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appwidgets[i];

			ParkingDB mParkingDB = new ParkingDB(context);
			List<Parking> park = mParkingDB.getAll(context, 0);
			String parking = (context.getString(R.string.FloorParking) +":"+ park.get(0).getFloor()+" - " + context.getString(R.string.NumberParking) +":"+ park.get(0).getNumber()+" - " + context.getString(R.string.SectionParking) +":"+ park.get(0).getSection() );

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget);
			views.setOnClickPendingIntent(R.id.widgetlinearlayout, pendingIntent);

			views.setImageViewBitmap(R.id.bgcolor, getBackground(colors2[park.get(0).getColor()]));

			// To update a label
			views.setTextViewText(R.id.widgetText, parking);
			views.setTextViewText(R.id.notewidget, park.get(0).getNotes());
			 switch (park.get(0).getColor()) {
			 case 0:
					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

			        break;
			    case 1:

					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);
			        break;
			    case 2:
					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

			        break;
			    case 3:

					views.setTextColor(R.id.widgetText, android.graphics.Color.WHITE);
					views.setTextColor(R.id.notewidget, android.graphics.Color.WHITE);
			        break;
			    case 4:
					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

			        break;
			    case 5:

					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);
			        break;
			    case 6:
					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);

			        break;
			    case 7:

					views.setTextColor(R.id.widgetText, android.graphics.Color.BLACK);
					views.setTextColor(R.id.notewidget, android.graphics.Color.BLACK);
			        break;
			    
			    }
			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
			mParkingDB.close();
		}

		
		
	}
	

	
	public static Bitmap getBackground (int bgcolor)
	{
	try
	    {
	        Bitmap.Config config = Bitmap.Config.ARGB_8888; // Bitmap.Config.ARGB_8888 Bitmap.Config.ARGB_4444 to be used as these two config constant supports transparency
	        Bitmap bitmap = Bitmap.createBitmap(2, 2, config); // Create a Bitmap
	 
	        Canvas canvas =  new Canvas(bitmap); // Load the Bitmap to the Canvas
	        canvas.drawColor(bgcolor); //Set the color
	        Log.d("mes"," get background ? te llamé?");
	        return bitmap;
	    }
	    catch (Exception e)
	    {
	        return null;
	    }
	}
	
}
