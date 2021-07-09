package rgSoft.PizzeroPizzeria;

public class Pizza {
	
	private String _nombre;
	private String _ingredientes;
	private int _precio;
	
	public void setNombre(String nombre){
		_nombre=nombre;
	}
	
	public String getNombre(){
		return _nombre;
	}
	
	public void setPrecio(int precio){
		_precio=precio;
	}
	
	public int getPrecio(){
		return _precio;
	}
	
	public void setIngredientes(String ingredientes){
		_ingredientes = ingredientes;
	}
	
	public String getIngredientes(){
		return _ingredientes;
	}
	
	public Pizza(String nombre, String ingredientes, int precio){
		_nombre = nombre;
		_ingredientes = ingredientes;
		_precio = precio;
	}	
	
	public Pizza(){
	
	}

}
