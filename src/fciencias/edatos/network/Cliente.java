package fciencias.edatos.network;

/**
* @author Daniel Aragón.
* @author Jorge Cortes Lopez.
* @author Kai Ueda Kawasaki.
*/

/* Clase Cliente para representar los clientes de la red de conexiones.*/

public class Cliente{
	//Atributos del cliente.
	private String nombre;
	private int telefono;

	/**
	* Construye una cliente de la red con todas sus propiedades.
	* @param nombre El nombre del cliente.
	* @param telefono El número de teléfono del clientes.
	*/
	public Cliente(String nombre, int telefono){
		this.nombre = nombre;
		this.telefono = telefono;
	}

	/**
	* Regresa el nombre del cliente.
	* @return El nombre del cliente.
	*/
	public String getNombre() {
		return nombre;
	}

	/**
	* Define el nombre del cliente.
	* @param nombre El nombre del cliente.
	*/
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	* Regresa el número telefónico del cliente.
	* @return El número telefónico del cliente.
	*/
	public int getPhone(){
		return telefono;
	}

	/**
	* Define el teléfono del cliente.
	* @param telefono El número telefónico del cliente.
	*/
	public void setPhone(int telefono){
		this.telefono = telefono;
	}
}