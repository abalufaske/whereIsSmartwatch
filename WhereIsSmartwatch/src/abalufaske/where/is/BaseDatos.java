package abalufaske.where.is;








import static abalufaske.where.is.commonconstants.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDatos extends SQLiteOpenHelper{

	  //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/abalufaske.where.is/databases/";
 
    private static String DB_NAME = "DondeEsta";
 
    protected SQLiteDatabase myDataBase; 
 
    private final Context myContext;
    
    private BaseDatos mDatabaseHelper;

 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public BaseDatos(Context context) {
 
    	super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        /*	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}*/
    	}
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// Crea tablas
	
			
			
		
			addItems( db);
			addParkings( db);
			
			

		} catch (Exception e) {
			Log.e("app", "ERROR CREATING DATABASE:" + e.getMessage());
		}
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Parking");
		db.execSQL("DROP TABLE IF EXISTS Items");
		Log.d("", "All tables droped");
		onCreate(db);
	}
 
	
	public void addItems(SQLiteDatabase db)
	{
		
		
		db.execSQL(CREATE_TABLE_ITEMS);
		Log.d("app", "TABLE " + CREATE_TABLE_ITEMS + " CREATED");

		db.execSQL(INSERT_TABLE_ITEMS_1);
		db.execSQL(INSERT_TABLE_ITEMS_2);
		db.execSQL(INSERT_TABLE_ITEMS_3);
		db.execSQL(INSERT_TABLE_ITEMS_4);

	
	}
	
	public void addParkings(SQLiteDatabase db)
	{
		
		db.execSQL(CREATE_TABLE_PARKING);
		Log.d("app", "TABLE " + CREATE_TABLE_PARKING + " CREATED");

		
		db.execSQL(INSERT_TABLE_PARKING);
		
	}
	
	
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
	
	   /**
     * Crea la conexión con la base de datos
     * @return Devuelve verdadero si la conexión se ha realizado con éxito y falso en caso contrario
     */
    public boolean connect() {
        try {            
            
        	Log.d("byte", "entré?");
        	myDataBase = mDatabaseHelper.getWritableDatabase();
            
            
            return true;
        } catch (Exception e) {
            Log.d("byte", "ERROR ESTABLISHING CONNECTION WITH DATABASE");
            e.printStackTrace();
            myDataBase.close();
            return false;
        }
    }
    
    /**
     * Desconexión con la base de datos
     */
    public void disconnect() {
        try {
            if (this.myDataBase != null){
                this.myDataBase.close();
            }
            this.mDatabaseHelper.close();
            
        } catch (Exception e) {
            //Log.e(TAG_DB, "ERROR DESCONNECTING THE DATABASE" + e.getMessage());
        }
    }

    /**
     * Ejecuta una sentencia SQL en la base de datos
     * @param sql Sentencia SQL que se quiere ejecutar
     * @return Devuelve verdadero si la sentencia se ha realizado con éxito y falso en caso contrario
     */
    public boolean executeSQL(String sql) {
        try {
	 		Log.d("byte"," entro execute?");

        	myDataBase.execSQL(sql);
        	
	 		Log.d("byte"," entro execute 2?");

        	myDataBase.close();
            //Log.d(Constants.TAG_DB, "SQL EXECUTED: "+sql);
            return true;
        } catch (Exception e) {
            //Log.e(TAG_DB, "ERROR EXECUTING SQL: " + sql);
            return false;
        }
    } 

 
}