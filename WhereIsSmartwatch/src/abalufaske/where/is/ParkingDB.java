package abalufaske.where.is;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import abalufaske.where.is.BaseDatos;
import abalufaske.where.is.commonconstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



import static abalufaske.where.is.commonconstants.*;


public class ParkingDB extends BaseDatos{


	   private final String[] listColumns;

	    /**
	     * Constructor
	     * @param con Contexto
	     */
	    public ParkingDB(final Context con) {
	        super(con);
	       
	    
	        
	        	listColumns = new String[] {TABLE_PARKING_ID,
	        			TABLE_PARKING_COLOR, 
	        			TABLE_PARKING_FLOOR,																			  
	        			TABLE_PARKING_NUM,
	        			TABLE_PARKING_SECTION,
	        			TABLE_PARKING_NOTES
	    			  };
	       
	
	    }
	    
	

	    /**
	     * Editar un lugar en la base de datos
	     * @param place Lugar a editar
	     * @return True si se ha realizado con √©xito. False en caso contrario.
	     * @throws JSONException 
	     */
	  
	    public Cursor fetch(long idPlace, Context con){
	    	
	        BaseDatos myDbHelper = new BaseDatos(con);

	    	try {
	    		 
		 		myDbHelper.openDataBase();
		 
		 	}catch(SQLException sqle){
		 
		 		throw sqle;
		 
		 	}
	    	
	    	Log.d("bit"," estoy aki");

	    	Cursor mCursor = myDbHelper.myDataBase.query(TABLE_PARKING, listColumns, TABLE_PARKING_ID + "=" + idPlace, null, null, null, null, null);
	  //  	Cursor mCursor = myDataBase.query(true, TABLE_FACULTY, listColumns, TABLE_FACULTY_FAV + "=" + 1, null, null, null, null, null);
	    	
	    	Log.d("bit"," datos "+mCursor.getCount());
	    	myDbHelper.close();
	    	if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
	    }
	    
	    /**
	     * Devuelve un objeto Place a partir de un id.
	     * @param idPlace id del objeto
	     * @return Un objeto Place
	     */
	    public Place get(long idPlace, Context con){
	    	Cursor crs = fetch(idPlace, con);       
	        Place place = new Place(crs);
	        crs.close();
	        return place;    	
	    }
	 

    
	    /**
	     * Devuelve un cursor con la lista de todos los lugares
	     * @param facultad 
	     * @return Un cursor
	     */
	    public Cursor fetchAll(Context con, int facultad){
	    	Log.d("byte"," fetchAll ");
	    			
	    	
	        BaseDatos myDbHelper = new BaseDatos(con);

	  	    	try {
	  	    		 
	  		 		myDbHelper.openDataBase();
	  		 
	  		 	}catch(SQLException sqle){
	  		 
	  		 		throw sqle;
	  		 
	  		 	}
	  	    	Cursor 	mCursor =null;
	  	    	//= myDbHelper.myDataBase.query( TABLE_FACULTY, listColumns,null, null, null, null, null, null);

	  	
	  	    	
	  	    	if (facultad == 0)
	  	    	{
	  	    		
	  	    	}
  	    //	mCursor = myDbHelper.myDataBase.query( TABLE_ITEMS, listColumns,null, null, null, null, TABLE_ITEMS_LAST , null);
	
	   	    	mCursor = myDbHelper.myDataBase.query( TABLE_PARKING, listColumns,null, null, null, null, null, null);

	  	    
	    	    	
	    	return mCursor;
	    }
	    
	    
	    

	    


	    /**
	     * Crea una lista de lugares a partir de lo que devuelve un cursor. Todos lo m√©todos get la usan pues 
	     * todos ellos devuelve una lista de lugares a partir de un cursor.
	     * @param crs
	     * @return
	     */
	    private List<Parking> listPlaceFromCursor(Cursor crs){
	    	List<Parking> mListPlaces = new ArrayList<Parking>();
	    	crs.moveToFirst();
	    	while (!crs.isAfterLast()){
	    		Parking parking = new Parking(crs);
	    		mListPlaces.add(parking);
	    		crs.moveToNext();
	    	}
	    	crs.close();
	    	return mListPlaces;
	    }

	   
		public List<Parking> getAll(Context applicationContext, int facultad) {
			Cursor crs = fetchAll(applicationContext, facultad);
	    	return listPlaceFromCursor(crs);
		}

		
	


		/*
		 * */
		
		 
	
	    /**
	     * Cuenta el n√∫mero de registros de la tabla places. Se utiliza para saber si la bbdd est√° vac√≠a
	     * @return Un entero con el n√∫mero de registros
	     */
	/*    public int getCount(){
	    	int count;
	    	Cursor crs = fetchAll();
	    	count = crs.getCount();
	    	crs.close();
	    	return count;
	    }*/


/**
 * Cambia el estado "favorito" de una evento.
 * @param idEvent Evento al que se le cambiar· el estado
 * @param idPoi Id del poi en el que est· el evento
 * @param status Entero que define el estado de favorito: 0=no favorito 1=favorito
 * @return True si se ha ejecutado la sentecia. False en caso contrario
 * 
 */
		
		public boolean setName (final long idItem , final String Name)
		
		{
		  	  Log.d("byte"," entro en setName ? ");

		    SQLiteDatabase db = getWritableDatabase();
		    ContentValues cv = new ContentValues();
		    cv.put(TABLE_ITEMS_NAME,Name); //These Fields should be your String values of actual column names
		  	  Log.d("byte"," ajusto el content values ");

		    if(  db.update(TABLE_ITEMS, cv, TABLE_ITEMS_ID +" = "+idItem, null) >0)
		    	  
		    {
		  	  db.close();
		  	  Log.d("byte"," asereje ja dejÈ ? ");
		  	  return true;
		    }    
		    
		    db.close();
		      return false;
		}

		public boolean setLat (final long idItem , final String Latitude)
		
		{
		  	  Log.d("byte"," entro en setName ? ");

		    SQLiteDatabase db = getWritableDatabase();
		    ContentValues cv = new ContentValues();
		    cv.put(TABLE_ITEMS_LATITUDE,Latitude); //These Fields should be your String values of actual column names
		  	  Log.d("byte"," ajusto el content values ");

		    if(  db.update(TABLE_ITEMS, cv, TABLE_ITEMS_ID +" = "+idItem, null) >0)
		    	  
		    {
		  	  db.close();
		  	  Log.d("byte"," asereje ja dejÈ ? ");
		  	  return true;
		    }    
		    
		    db.close();
		      return false;
		}

		
		public boolean setParking (final int color , final int floor, final int number, final String Section , final String Notes)
		
		{
		long id = 1;
			/*
			 * 		TABLE_PARKING_COLOR+" integer, " +
		TABLE_PARKING_FLOOR+" integer, " +
		TABLE_PARKING_NUM+" integer, " +
		TABLE_PARKING_SECTION+" varchar(1), " +
		TABLE_PARKING_NOTES+" varchar(200));" ;
			 */
		String temp ="";
			if(Section.length()==0)
			{
				
			}
			else
			{
				 temp = Section.substring(0, 1); 
			}
			Log.d("mes","entro al set parking? ");
		    SQLiteDatabase db = getWritableDatabase();
		    ContentValues cv = new ContentValues();
		    cv.put(TABLE_PARKING_COLOR,color);
		    cv.put(TABLE_PARKING_FLOOR,floor);
		    cv.put(TABLE_PARKING_NUM,number);
		    cv.put(TABLE_PARKING_SECTION,temp);
		    cv.put(TABLE_PARKING_NOTES,Notes);

		    
		    //These Fields should be your String values of actual column names
		  	  Log.d("byte"," ajusto el content values ");

		    if(  db.update(TABLE_PARKING, cv, TABLE_PARKING_ID +" = "+id, null) >0)
		    	  
		    {
		  	  db.close();
		  	  Log.d("byte"," Parking updated? ");
		  	  return true;
		    }    
		    
		    db.close();
		      return false;
		}

		public boolean setPark (final long idItem, final int Last)
		
		{
			
			 SQLiteDatabase db = getWritableDatabase();
			    ContentValues cv = new ContentValues();
			    cv.put(TABLE_ITEMS_LAST, 0); //These Fields should be your String values of actual column names
			  	  Log.d("byte"," ajusto el content values ");
				    if(  db.update(TABLE_ITEMS, cv, TABLE_ITEMS_LAST +" = "+Last, null) >0)
				    {	
					  	  Log.d("byte"," quito el last al que lo tenga ");

				    }

				    ContentValues cv2 = new ContentValues();
				    cv2.put(TABLE_ITEMS_LAST, String.valueOf(Last));
			    if(  db.update(TABLE_ITEMS, cv2, TABLE_ITEMS_ID +" = "+idItem, null) >0)
			    	  
			    {
				  	  Log.d("byte"," cambio el last al que modifiqué ");

				  	  
				  	  
			  	  db.close();
			  	  return true;
			        
			    
				    }
			    
			    db.close();
			  return false;
		}



	}