/**
 * 
 */
package abalufaske.where.is;



import static abalufaske.where.is.commonconstants.DB_VERSION;

import java.io.IOException;

import abalufaske.where.is.BaseDatos;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Alberto Alonso
 *
 */
public class InitActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);
		
	
		
		final int welcomeScreenDisplay = 500;
		/** create a thread to show splash up to splash time */
		Thread welcomeThread = new Thread() {

			int wait = 0;

			@Override
			public void run() {
			try {
			super.run();
			/**
			* use while to get the splash time. Use sleep() to increase
			* the wait variable for every 100L.
			*/
			while (wait < welcomeScreenDisplay) {
			sleep(100);
			wait += 100;
			}
			} catch (Exception e) {
			System.out.println("EXc=" + e);
			} finally {
			/**
			* Called after splash times up. Do some action after splash
			* times up. Here we moved to another main activity class
			*/
				
				 BaseDatos myDbHelper = new BaseDatos(getApplicationContext());
				  //      myDbHelper = new BaseDatos(this);
				 
				        try {
				        	myDbHelper.createDataBase();
				 
				 	} catch (IOException ioe) {
				 
				 		throw new Error("Unable to create database");
				 
				 	}
				 
				 	try {
				 
				 		myDbHelper.openDataBase();
				 
				 	}catch(SQLException sqle){
				 
				 		throw sqle;
				 
				 	}
					   if(myDbHelper.myDataBase.getVersion() < DB_VERSION)
					   {
					   myDbHelper.onUpgrade(myDbHelper.myDataBase, myDbHelper.myDataBase.getVersion(),DB_VERSION);
					   }

				 	myDbHelper.close();
					Intent startIntent = new Intent(InitActivity.this,
							MainActivity.class);
					startIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(startIntent);
			finish();
			}
			}
			};
			welcomeThread.start();
		
		


		
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	

}
