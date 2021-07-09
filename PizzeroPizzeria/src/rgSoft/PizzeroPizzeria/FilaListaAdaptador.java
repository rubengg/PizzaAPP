package rgSoft.PizzeroPizzeria;

import rgSoft.PizzeroPizzeria.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FilaListaAdaptador extends BaseAdapter {
	
	private String[] _filas;
	
	public FilaListaAdaptador(Context contexto, int id_recurso, String[] filas){
		_filas=filas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub		
		return _filas.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub		
		return _filas[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int posicion, View vista_conversion, ViewGroup padre) {
		// TODO Auto-generated method stub
		String cadena_pizza = _filas[posicion];
		View vista_fila = null;
		
		if (vista_conversion == null) {
			LayoutInflater inflador = (LayoutInflater) padre.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vista_fila = inflador.inflate(R.layout.fila_lista,null);
		} else {
			vista_fila = vista_conversion;
		}
		
		//LayoutInflater inflador = (LayoutInflater) padre.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//
		TextView tvFila = (TextView) vista_fila.findViewById(R.id.tvFila);
		tvFila.setText(cadena_pizza);
		
		ImageButton ibEliminar = (ImageButton) vista_fila.findViewById(R.id.ibEliminar);
		ibEliminar.setTag((Integer)posicion);
		
		ImageButton ibEditar = (ImageButton) vista_fila.findViewById(R.id.ibEditar);
		ibEditar.setTag((Integer)posicion);
		
		return vista_fila;
	}
}
