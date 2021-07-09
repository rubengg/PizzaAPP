package rgSoft.PizzeroPizzeria;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import rgSoft.PizzeroPizzeria.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;




public class catalogoActivity extends Activity { 
	
	private int _id;
	private EditText edNombre;
	private EditText edIngredientes;
	private EditText edPrecio;
	private ListView lvPizzas;
	private HttpClient servidorWeb;
	private ArrayList<Pizza> _pizzas;
	private Timer _temporizador;
	private String ip = "10.0.2.2:8080";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {  //se sobreescribe el metodo OnCreate
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalogo); 
		_id = -1;
		edNombre = (EditText) findViewById(R.id.et1);
		edIngredientes = (EditText) findViewById(R.id.et2);
		edPrecio = (EditText) findViewById(R.id.et3);
		lvPizzas = (ListView) findViewById(R.id.lv1);
		servidorWeb = new DefaultHttpClient();
		_temporizador = new Timer();		
		TareaTemporizador miTarea = new TareaTemporizador();
		_temporizador.schedule(miTarea,1,1000);	
			
	}
	
	//funcion para actualizar la tabla de las pizzas, mandando a llamar los datos del servidor en .js con ayuda de un objeto JSON 	
	private void actualizarTablaPizzas(){
		try
		{
			HttpGet peticion = new HttpGet("http://"+ip+"/pizzas");
			
			peticion.addHeader("content-type","application/json");
			HttpResponse respuesta = servidorWeb.execute(peticion);
			String texto_respuesta = EntityUtils.toString(respuesta.getEntity());
			JSONArray pizzas_servidor = new JSONArray(texto_respuesta);
			_pizzas= new ArrayList<Pizza>();			
			String[] pizzas = new String[pizzas_servidor.length()];
			for(int i=0; i<pizzas_servidor.length();i++){
				JSONObject pizza_servidor = pizzas_servidor.getJSONObject(i);			
				String nombre = pizza_servidor.getString("nombre");
				String ingredientes = pizza_servidor.getString("ingredientes");
				int precio = pizza_servidor.getInt("precio");
				pizzas[i] = i+": "+nombre+" - "+ingredientes+" - $"+precio;
				_pizzas.add(new Pizza(nombre,ingredientes,precio));
			}
												
			FilaListaAdaptador adaptador = new FilaListaAdaptador(getApplicationContext(),R.layout.fila_lista,pizzas);						
			lvPizzas.setAdapter(adaptador);	
		}
		
		catch(Exception ex){
			Log.e("excepcion", ex.getMessage());
		}
	}
	
	
	
	//Funcion para generar un temporizador y que se actualice la lista, reflejando que es en tiempo real
	//mandando a llamar la funcion que actualiza la tabla de las pizzas
	class TareaTemporizador extends TimerTask{
		public void run(){
			runOnUiThread(new Runnable(){
				public void run(){
					actualizarTablaPizzas();
				}				
			});					
		}
	}
	
	
	
	//funcion para el boton Nuevo, limpia los editText y pone el id de referencia en -1
	public void Nuevo(View view){		
		edNombre.setText("");		
		edIngredientes.setText("");		
		edPrecio.setText("");
		_id=-1;
	}
	
	//Funcion para el boton Guardar los datos en un arreglo en el servidor, con la creacion de un objeto JSON
	//Se tiene que ver que el id de referencia si es -1 indica que es agregar un nuevo arreglo en el servidor, esto sale del boton nuevo
	//Si no es nuevo entonces indica que se habia pedidopeticion de modificar los datos
	public void Guardar(View view){		
		JSONObject pizza = new JSONObject();
		try
		{
		pizza.put("nombre", edNombre.getText());
		pizza.put("ingredientes", edIngredientes.getText());
		pizza.put("precio", edPrecio.getText());
		StringEntity entidad = new StringEntity(pizza.toString());
			if(_id==-1){
				HttpPost peticion = new HttpPost("http://"+ip+"/pizzas");
				peticion.addHeader("content-type","application/json");				
				peticion.setEntity(entidad);
				HttpResponse respuesta = servidorWeb.execute(peticion);
				
			}
			else{
				HttpPut peticionput = new HttpPut("http://"+ip+"/pizzas/"+_id);
				peticionput.addHeader("content-type","application/json");	
				peticionput.setEntity(entidad);
				HttpResponse respuestaput = servidorWeb.execute(peticionput);				
			}				
		}catch (Exception ex){
		
		}		
	}
	
	//Funcion para el ImageButton EDITAR en el cual para los datos de la fila que se selecciono a los ediText
	//conservando el  id correspondiente para despues poder salvarlo en la misma posicion
	public void Editar(View vista){		
		ImageButton Editar = (ImageButton) vista;
		_id = (Integer)Editar.getTag();
		edNombre.setText(_pizzas.get(_id).getNombre());
		edIngredientes.setText(_pizzas.get(_id).getIngredientes());
		edPrecio.setText(_pizzas.get(_id).getPrecio()+"");				
	}
	
	//Funcion para el ImageButton ELIMINAR en el cual se elimina del servidor la fila seleccionada
	public void Eliminar(View vista){
		try{
			ImageButton Eliminar = (ImageButton) vista;
			Integer posicion = (Integer)Eliminar.getTag();
			HttpDelete peticion = new HttpDelete("http://"+ip+"/pizzas/"+posicion.toString());
			peticion.addHeader("content-type","application/json");
			HttpResponse respuesta = servidorWeb.execute(peticion);			
		}catch(Exception ex){
		
		}
	}
	
	

}
