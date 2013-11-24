package abalufaske.where.is;

public class commonconstants {
	
	/* TAGs para debugear*/	
	public static final String TAG_APP="app";	
	public static final String TAG_DB="bd";
	
	/* Nombres de las tablas */	
	public static final String TABLE_ITEMS ="Items";

	public static final String TABLE_PARKING ="Parking";

	
	/* Parï¿½metro de configuraciï¿½n de la base de datos */	
	public static final String DB_NAME = "WhereIs";
	
	
public static final int DB_VERSION = 2;
	
	
	




public static final String TABLE_ITEMS_ID="items_id";
public static final String TABLE_ITEMS_NAME="items_name";
public static final String TABLE_ITEMS_LONGITUDE="items_longitude";
public static final String TABLE_ITEMS_LATITUDE="items_latitude";
public static final String TABLE_ITEMS_LAST="items_last";





public static final String CREATE_TABLE_ITEMS = "CREATE TABLE IF NOT EXISTS "+TABLE_ITEMS+"("+TABLE_ITEMS_ID+" integer primary key," + 
		TABLE_ITEMS_NAME+" varchar(200), " +
		
TABLE_ITEMS_LAST+" varchar(1) NOT NULL DEFAULT '0'," +
TABLE_ITEMS_LONGITUDE+" varchar(200)," +
TABLE_ITEMS_LATITUDE+" varchar(200));" ;




public static final String TABLE_PARKING_ID="parking_id";
public static final String TABLE_PARKING_COLOR="parking_color";
public static final String TABLE_PARKING_FLOOR="parking_floor";
public static final String TABLE_PARKING_NUM="parking_num";
public static final String TABLE_PARKING_SECTION="parking_section";
public static final String TABLE_PARKING_NOTES="parking_notes";



public static final String CREATE_TABLE_PARKING = "CREATE TABLE IF NOT EXISTS "+TABLE_PARKING+"("+TABLE_PARKING_ID+" integer primary key," + 
		TABLE_PARKING_COLOR+" integer, " +
		TABLE_PARKING_FLOOR+" integer, " +
		TABLE_PARKING_NUM+" integer, " +
		TABLE_PARKING_SECTION+" varchar(1), " +
		TABLE_PARKING_NOTES+" varchar(200));" ;

//Insertamos residencias que son pocas y cobardes

public static final String INSERT_TABLE_PARKING = "INSERT INTO "+TABLE_PARKING+" ("+TABLE_PARKING_ID+", "+
		TABLE_PARKING_COLOR+", "+
		TABLE_PARKING_FLOOR+", "+
		TABLE_PARKING_NUM+", "+
		TABLE_PARKING_SECTION+", "+
		TABLE_PARKING_NOTES+") VALUES ("+
		 1+"," +
		 "'0'," +
		 "''," +
		 "''," +
		 "''," +
		 "'')";




//Insertamos facultades e edificios importantes




public static final String INSERT_TABLE_ITEMS_1 = "INSERT INTO "+TABLE_ITEMS+" ("+TABLE_ITEMS_ID+", "+
		TABLE_ITEMS_NAME+", "+
		TABLE_ITEMS_LAST+", "+
		TABLE_ITEMS_LONGITUDE+", "+
		TABLE_ITEMS_LATITUDE+") VALUES ("+
		 1+"," +
		 "'1'," +
		 "'0'," +
		 "'42.3426700596822'," +
		 "'-7.851190567016602')";

public static final String INSERT_TABLE_ITEMS_2 = "INSERT INTO "+TABLE_ITEMS+" ("+TABLE_ITEMS_ID+", "+
		TABLE_ITEMS_NAME+", "+
		TABLE_ITEMS_LAST+", "+
		TABLE_ITEMS_LONGITUDE+", "+
		TABLE_ITEMS_LATITUDE+") VALUES ("+
		 2+"," +
		 "'2'," +
		 "'0'," +
		 "'42.16757838710186'," +
		 "'-8.685346841812134')";


public static final String INSERT_TABLE_ITEMS_3 = "INSERT INTO "+TABLE_ITEMS+" ("+TABLE_ITEMS_ID+", "+
		TABLE_ITEMS_NAME+", "+
		TABLE_ITEMS_LAST+", "+
		TABLE_ITEMS_LONGITUDE+", "+
		TABLE_ITEMS_LATITUDE+") VALUES ("+
		 3+"," +
		 "'3'," +
		 "'0'," +
		 "'42.433007073860914'," +
		 "'-8.639030456542969')";


public static final String INSERT_TABLE_ITEMS_4 = "INSERT INTO "+TABLE_ITEMS+" ("+TABLE_ITEMS_ID+", "+
		TABLE_ITEMS_NAME+", "+
		TABLE_ITEMS_LAST+", "+
		TABLE_ITEMS_LONGITUDE+", "+
		TABLE_ITEMS_LATITUDE+") VALUES ("+
		 4+"," +
		 "'4'," +
		 "'0'," +
		 "'0'," +
		 "'0')";




/* Situaci—n del mapa por defecto */
public static final double DEFAULT_LAT = 42.557506; 
public static final double DEFAULT_LON = -8.465438;
public static final int DEFAULT_ZOOM_LEVEL = 9;








}