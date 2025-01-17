package com.emizor.univida.fragmento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.seguridad.DatosUsuario;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatosUsuarioFragment extends Fragment {

    public DatosUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vistaDatosUsuario = inflater.inflate(R.layout.fragment_datos_usuario, container, false);
        TextView tvNombreUsuario, tvCargoUsuario, tvNombreSucursal, tvDepartamentoSucursal, tvCiudadSucursal;

        tvNombreUsuario = vistaDatosUsuario.findViewById(R.id.tvNombreUsuario);
        tvCargoUsuario = vistaDatosUsuario.findViewById(R.id.tvCargoUsuario);
        tvNombreSucursal = vistaDatosUsuario.findViewById(R.id.tvNombreSucursal);
        tvDepartamentoSucursal = vistaDatosUsuario.findViewById(R.id.tvDepartamentoSucursal);
        tvCiudadSucursal = vistaDatosUsuario.findViewById(R.id.tvCiudadSucursal);

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());

        DatosUsuario datosUsuario =  controladorSqlite2.obtenerUsuario().getDatosUsuario();

        controladorSqlite2.cerrarConexion();

        if (datosUsuario != null){
            tvNombreUsuario.setText(datosUsuario.getEmpleadoNombreCompleto());
            tvCargoUsuario.setText(datosUsuario.getEmpleadoCargo());
            if (datosUsuario.getSucursalNombre() != null) {
                String nombreSucursal = "SUCURSAL: " + datosUsuario.getSucursalNombre();
                tvNombreSucursal.setText(nombreSucursal);
            }else{
                tvNombreSucursal.setVisibility(View.GONE);
            }
            if (datosUsuario.getSucursalDepartamento() != null){
                String depSucursal = "DEPARTAMENTO: " + datosUsuario.getSucursalDepartamento();
                tvDepartamentoSucursal.setText(depSucursal);
            }else{
                tvDepartamentoSucursal.setVisibility(View.GONE);
            }

            if (datosUsuario.getSucursalCiudad() != null) {
                String ciudadSucursal = "CIUDAD: " + datosUsuario.getSucursalCiudad();
                tvCiudadSucursal.setText(ciudadSucursal);
            }else{
                tvCiudadSucursal.setVisibility(View.GONE);
            }
        }

        return vistaDatosUsuario;
    }

}
