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
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class pedidosActivity extends Activity {
	
	private int _id;
	private TextView Mensaje;
	private ListView lvPedidos;
	private HttpClient servidorWeb;
	private ArrayList<Pedido> _pedidos;
	private Timer _temporizador;
	private int contador;
	private int total;
	private String cadena;
	private String ip ="10.0.2.2:8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedidos); //para que enlace el .XML a este .java
		
		//esta parte es para que se muestre el contenido del servidor de los pedidos
		_id = -1;
		Mensaje = (TextView) findViewById(R.id.textView2);
		lvPedidos = (ListView) findViewById(R.id.listView1);
		servidorWeb = new DefaultHttpClient();
		_temporizador = new Timer();		
		TareaTemporizador miTarea = new TareaTemporizador();
		_temporizador.schedule(miTarea,1,1000);	
		
	}

	private void actualizarTablaPedidos(){
		try
		{
			HttpGet peticion = new HttpGet("http://"+ip+"/pedidos");
			
			peticion.addHeader("content-type","application/json");
			HttpResponse respuesta = servidorWeb.execute(peticion);
			String texto_respuesta = EntityUtils.toString(respuesta.getEntity());
			JSONArray pedidos_servidor = new JSONArray(texto_respuesta);
			_pedidos= new ArrayList<Pedido>();			
			String[] pedidos = new String[pedidos_servidor.length()];
			for(int i=0; i<pedidos_servidor.length();i++){
				JSONObject pizza_servidor = pedidos_servidor.getJSONObject(i);			
				String nombre = pizza_servidor.getString("nombre");
				String lista = pizza_servidor.getString("lista");				
				pedidos[i] = i+" - NOMBRE: "+nombre+"\n"+lista;
				_pedidos.add(new Pedido(nombre,lista));
			}
												
			FilaListaPedido adaptador = new FilaListaPedido(getApplicationContext(),R.layout.fila_pedidos,pedidos);						
			lvPedidos.setAdapter(adaptador);	
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
					actualizarTablaPedidos();
				}				
			});					
		}
	}
	
	public void Enviar(View vista){
		try{
			ImageButton Enviar = (ImageButton) vista;
			Integer posicion = (Integer)Enviar.getTag();
			HttpDelete peticion = new HttpDelete("http://"+ip+"/pedidos/"+posicion.toString());
			peticion.addHeader("content-type","application/json");
			HttpResponse respuesta = servidorWeb.execute(peticion);
			Mensaje.setText("El pedido ( "+posicion+" ) ya se ha enviado al cliente");
		}
		catch(Exception ex){
		}
	}
	

}
