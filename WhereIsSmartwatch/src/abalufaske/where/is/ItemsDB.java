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


public class ItemsDB extends BaseDatos{


	   private final String[] listColumns;

	    /**
	     * Constructor
	     * @param con Contexto
	     */
	    public ItemsDB(final Context con) {
	        super(con);
	       
	        	listColumns = new String[] {TABLE_ITEMS_ID,
	        			TABLE_ITEMS_NAME, 
	        			TABLE_ITEMS_LAST,																			  
	           			TABLE_ITEMS_LONGITUDE,
	        			TABLE_ITEMS_LATITUDE
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

	    	Cursor mCursor = myDbHelper.myDataBase.query(TABLE_ITEMS, listColumns, TABLE_ITEMS_ID + "=" + idPlace, null, null, null, null, null);
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
	
	   	    	mCursor = myDbHelper.myDataBase.query( TABLE_ITEMS, listColumns,null, null, null, null, TABLE_ITEMS_LAST + " DESC", null);

	  	    
	    	    	
	    	return mCursor;
	    }
	    
	    
	    

	    


	    /**
	     * Crea una lista de lugares a partir de lo que devuelve un cursor. Todos lo m√©todos get la usan pues 
	     * todos ellos devuelve una lista de lugares a partir de un cursor.
	     * @param crs
	     * @return
	     */
	    private List<Place> listPlaceFromCursor(Cursor crs){
	    	List<Place> mListPlaces = new ArrayList<Place>();
	    	crs.moveToFirst();
	    	while (!crs.isAfterLast()){
	    		Place place = new Place(crs);
	    		mListPlaces.add(place);
	    		crs.moveToNext();
	    	}
	    	crs.close();
	    	return mListPlaces;
	    }

	   
		public List<Place> getAll(Context applicationContext, int facultad) {
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
		  	  Log.d("byte"," entro en setLAt ? ");

		    SQLiteDatabase db = getWritableDatabase();
		  	  Log.d("byte"," entro en setLAt 2? ");

		    ContentValues cv = new ContentValues();

		  	  Log.d("byte"," entro en setLAt 3? ");

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

		
		public boolean setLong (final long idItem , final String Longitude)
		
		{
		  	  Log.d("byte"," entro en setLong ? ");

		    SQLiteDatabase db = getWritableDatabase();
		    ContentValues cv = new ContentValues();
		    cv.put(TABLE_ITEMS_LONGITUDE,Longitude); //These Fields should be your String values of actual column names
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

		public boolean setLast (final long idItem, final int Last)
		
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