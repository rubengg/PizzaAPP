package rgSoft.PizzeroPizzeria;

public class Pedido {

	private String _nombre;
	private String _lista;

	
	public void setNombre(String nombre){
		_nombre=nombre;
	}
	
	public String getNombre(){
		return _nombre;
	}
	
	
	public void setLista(String lista){
		_lista=lista;
	}
	
	public String getLista(){
		return _lista;
	}
	
	public Pedido(String nombre, String lista){
		_nombre = nombre;
		_lista=lista;
	}	
	
	public Pedido(){
	
	}
}
