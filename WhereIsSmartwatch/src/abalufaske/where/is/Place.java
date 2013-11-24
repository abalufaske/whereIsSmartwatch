package abalufaske.where.is;

import java.io.Serializable;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

/**
 * Modelo de datos para un lugar.
 * @author Alberto Alonso
 * @version 1
 * @since 1.0
 * @see Zone
 */
@SuppressWarnings("serial")
public class Place implements Serializable {

	/** Id del lugar */
    private long id;

    /** Nombre del lugar */
    private String name;

    
    
    /** Latitud del lugar */
    private double latitude;
    
    /** Longitud del lugar */
    private double longitude;


    private int last;
    /** Distancia con respecto al usuario */
    private int distance;
    
    private int fav;


    /**
  	public static final String TABLE_FACULTY_ID="facultades_id";
	public static final String TABLE_FACULTY_NAME="facultades_name";
	public static final String TABLE_FACULTY_TELF="facultades_telf";
	public static final String TABLE_FACULTY_FAX="facultades_fax";
	public static final String TABLE_FACULTY_EMAIL="facultades_email";
	public static final String TABLE_FACULTY_WEB="facultades_web";
	public static final String TABLE_FACULTY_LONGITUDE="facultades_longitude";
	public static final String TABLE_FACULTY_LATITUDE="facultades_latitude";

   	 */
   	public Place(long id, String name, String tlf, String fax, 
   			String email, String web,String services,String campus,int type,int fav, double latitude, double longitude) {
   		super();
   		this.id = id;
   		this.name = name;
   		this.last = type;

   		this.latitude = latitude;
   		this.longitude = longitude;

   	}

   	public Place(Cursor crs) {
   		super();
   		
   		

   		
   		this.id = crs.getLong(0);
   		this.name = crs.getString(1);
   		this.last= crs.getInt(2);
   		this.latitude = crs.getDouble(3);
   		this.longitude = crs.getDouble(4);

   	}
   	
    /**
     * Constructor sin parámetros de la clase Place que instancia un objeto.
     */
    public Place() {
    }

	/**
     * @return El idenficador del lugar
     */
    public long getId() {
        return id;
    }

    /**
     * @param id El id para poner
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return El nombre del lugar
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - El nombre para poner
     */
    public void setName(final String name) {
        this.name = name;
    }

    
	/**
	 * @return La latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude - La latitude para cambiar
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return La longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude - La longitude para cambiar
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
	
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}


	
	/**
	 * @return La tlf
	 */
	public int getType() {
		return last;
	}	
	
	/**
	 * @return La tlf
	 */
	public void SetType(int last) {
		this.last=last;
	}
	

	
}
