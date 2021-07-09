package rgSoft.ClientePizzeria;

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

import rgSoft.ClientePizzeria.R;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	
	private int _id;
	private EditText edNombre;
	private TextView edPedido;
	private ListView lvPizzas;
	private HttpClient servidorWeb;
	private ArrayList<Pizza> _pizzas;
	private Timer _temporizador;
	private int contador;
	private int total;
	private String cadena;
	private String ip =  "10.0.2.2:8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
				_id = -1;
				edNombre = (EditText) findViewById(R.id.editText1);
				edPedido = (TextView) findViewById(R.id.textView3);
				lvPizzas = (ListView) findViewById(R.id.listView1);
				servidorWeb = new DefaultHttpClient();
				_temporizador = new Timer();		
				TareaTemporizador miTarea = new TareaTemporizador();
				_temporizador.schedule(miTarea,1,1000);	
		
	}
	 	
		private void actualizarTablaPizzas(){
			try
			{
				HttpGet peticion = new HttpGet("http://"+ip+"/pizzas");
				//http://10.0.2.2:8080/pizzas
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
					pizzas[i] = nombre+" - "+ingredientes+" - $"+precio;
					_pizzas.add(new Pizza(nombre,ingredientes,precio));
				}
													
				FilaListaAdaptador adaptador = new FilaListaAdaptador(getApplicationContext(),R.layout.fila_lista,pizzas);						
				lvPizzas.setAdapter(adaptador);	
			}
			
			catch(Exception ex){
				Log.e("excepcion", ex.getMessage());
			}
		}
		
		class TareaTemporizador extends TimerTask{
			public void run(){
				runOnUiThread(new Runnable(){
					public void run(){
						actualizarTablaPizzas();
					}				
				});					
			}
		}
		
		public void Nuevo(View view){		
			edNombre.setText("");
			edPedido.setText("Nuevo Pedido: ");
			_id=-1;
			contador = 0;
			total = 0;
			cadena = "";
		}
		
		public void Guardar(View view){		
			JSONObject pedido = new JSONObject();
			try
			{
			pedido.put("nombre", edNombre.getText());
			pedido.put("lista", edPedido.getText());
	
			StringEntity entidad = new StringEntity(pedido.toString());
				if(_id==-1){
					HttpPost peticion = new HttpPost("http://"+ip+"/pedidos");
					peticion.addHeader("content-type","application/json");				
					peticion.setEntity(entidad);
					HttpResponse respuesta = servidorWeb.execute(peticion);
					
				}
				else{
					HttpPut peticionput = new HttpPut("http://"+ip+"/pedidos/"+_id);
					peticionput.addHeader("content-type","application/json");	
					peticionput.setEntity(entidad);
					HttpResponse respuestaput = servidorWeb.execute(peticionput);				
				}				
			}catch (Exception ex){
			
			}	
			
			edPedido.setText("Se ha pedido una pizza: "+ edNombre.getText()+"'");
			edNombre.setText("");
			contador = 0;
			total = 0;
			cadena = "";
		}

		public void Agregar(View vista){		
			ImageButton Editar = (ImageButton) vista;
			_id = (Integer)Editar.getTag();
			
			if(contador ==0){
				
				edPedido.setText("PEDIDO: \n"+_pizzas.get(_id).getNombre()+"-"+_pizzas.get(_id).getIngredientes()+"\n TOTAL: $"+_pizzas.get(_id).getPrecio()+"");			
				cadena = ""+_pizzas.get(_id).getNombre()+"-"+_pizzas.get(_id).getIngredientes();
				contador = 1;
				total = (Integer)_pizzas.get(_id).getPrecio();
			}
			else{
				total = total+(Integer)_pizzas.get(_id).getPrecio();
				edPedido.setText("Usted Ordeno una pizza \n"+cadena+" "+_pizzas.get(_id).getNombre()+" "+_pizzas.get(_id).getIngredientes()+"\n Precio: $"+total);
				cadena = cadena+"\n"+_pizzas.get(_id).getNombre()+"-"+_pizzas.get(_id).getIngredientes();
				}
			_id = -1;
		}

}
