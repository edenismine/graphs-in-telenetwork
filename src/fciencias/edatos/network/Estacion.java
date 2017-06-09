package fciencias.edatos.network;
import java.util.LinkedList;

/**
* @author Daniel Aragón.
* @author Jorge Cortes Lopez.
* @author Kai Ueda Kawasaki.
*/

/* Clase Estacion para representar las estaciones de la red.*/

public class Estacion{
	//Atributos de la estación.
	private String nombre;
	private int idCode;
	private LinkedList<Cliente> clientes;

	/**
	* Construye una estación con todas sus propiedades.
	* @param nombre El nombre de la estación.
	* @param idCode El código de estación único que se asocia a ella.
	* @param clientes La lista que contiene almacenados a los clientes de la estación.
	*/
	public Estacion(String nombre, int idCode){
		this.nombre = nombre;
		this.idCode = idCode;
		clientes = new LinkedList<Cliente>();
	}

	/**
	* Regresa el nombre de la estación.
	* @return El nombre del la estación.
	*/
	public String getStationName() {
		return nombre;
	}

	/**
	* Define el nombre de la estación.
	* @param nombre El nombre de la estación.
	*/
	public void setStationName(String nombre) {
		this.nombre = nombre;
	}

	/**
	* Regresa el número de identificación de la estación.
	* @return El número de identificación de la estación.
	*/
	public int getStationId(){
		return idCode;
	}

	/**
	* Define el número de identificación de la estación.
	* @param idCode El número de identificación de la estación.
	*/
	public void setStationId(int idCode){
		this.idCode = idCode;
	}

	/**
	* Regresa los clientes que se encuentran registrados en la estación.
	* @return La lista que contiene a los clientes de la estación
	*/
	public LinkedList<Cliente> getClientes(){
		return clientes;
	}

	/**
	* Agrega clientes a la estación de servicio.
	* @param cliente El cliente a agregar a la estación de servicio.
	*/
	public void addClient(Cliente cliente){
		clientes.add(cliente);
	}

}