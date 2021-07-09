package rgSoft.PizzeroPizzeria;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import rgSoft.PizzeroPizzeria.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void lanzaCatalogo(View view) {
        Intent i = new Intent(this, catalogoActivity.class );
        startActivity(i);
	} 
	
	public void lanzaPedidos(View view) {
        Intent i = new Intent(this, pedidosActivity.class );
        startActivity(i);
	} 
	
	
	//Cuando se presiona el bot�n finalizar llamando al m�todo finish() que tiene por objetivo liberar el espacio de memoria de esta actividad y pedir que se active la actividad anterior.
	public void cerrar(View view) { //funcion para el boton salir, nos permite que se cierre la aplicacion
    	finish();
    }  

}
