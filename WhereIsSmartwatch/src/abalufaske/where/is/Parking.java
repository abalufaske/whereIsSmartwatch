package abalufaske.where.is;

import java.io.Serializable;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

/**
 * Modelo de datos para un parking.
 * @author Alberto Alonso
 * @version 1
 * @since 1.0
 * @see Zone
 */
@SuppressWarnings("serial")
public class Parking implements Serializable {

	/** Id del lugar */
    private long id;

    /** Nombre del lugar */
    private int color;

    
    
    


    private int floor;
    /** Distancia con respecto al usuario */
    private int number;
    
    private String section;
    private String notes;

 
   	public Parking(long id, String notes, int color,int floor, int number, String section) {
   		super();
   		this.id = id;
   		this.color = color;
   		this.floor = floor;

   		this.number = number;
   		this.section = section;
   		this.notes = notes;
   	}

   	public Parking(Cursor crs) {
   		super();
   		
   		

   		
   		this.id = crs.getLong(0);
   		this.color = crs.getInt(1);
   		this.floor= crs.getInt(2);
   		this.number = crs.getInt(3);
   		this.section = crs.getString(4);
   		this.notes = crs.getString(5);

   	}
   	
    /**
     * Constructor sin parámetros de la clase Place que instancia un objeto.
     */
    public Parking() {
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

  /*
   *  		this.color = crs.getInt(1);
   		this.floor= crs.getInt(2);
   		this.number = crs.getInt(3);
   		this.section = crs.getString(4);
   		this.notes = crs.getString(5);
   */
	public int getFloor()
	{
		return floor;
	}
	public int getNumber()
	{
		return number;
	}
	public int getColor()
	{
		return color;
	}

	public String getSection()
	{
		return section;
	}
	
	public String getNotes()
	{
		return notes;
	}
	
	
	public void setFloor(int floor)
	{
		this.floor=floor;
	}
	public void setNumber(int number)
	{
		this.number=number;
	}
	public void setColor(int color)
	{
		this.color=color;
	}
	public void setSection(String section)
	{
		this.section=section;
	}
	public void setNotes(String notes)
	{
		this.notes=notes;
	}
	
}
