package com.emizor.univida.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.emizor.univida.dialogo.DialogDetalleFragment;
import com.emizor.univida.R;
import com.emizor.univida.fragmento.TurnoHistorialFragment;
import com.emizor.univida.modelo.dominio.univida.consultas.RespuestaHistorico;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.turnos.Turnos;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LoadingDialog;
import com.emizor.univida.util.LogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorialTurnosAdapter extends ArrayAdapter<Turnos> {
    private User user;
    private Context context;
    private List<Turnos> turnos;


    private TurnoHistorialFragment fragment;

    public HistorialTurnosAdapter(Context context, List<Turnos> turnos, TurnoHistorialFragment fragment) {
        super(context, R.layout.item_historial_qr, turnos);
        this.context = context;
        this.turnos = turnos;
        this.fragment = fragment;

        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());
        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_historial_turnos, parent, false);
        }


        Turnos turno = turnos.get(position);
        int secuencial = turno.getSecuencial();




        TextView textViewNombre = convertView.findViewById(R.id.textViewNombre);
        TextView textViewCargo = convertView.findViewById(R.id.textViewCargo);
        TextView textViewFechaRegistro = convertView.findViewById(R.id.textViewFechaRegistro);
        TextView textViewTipoEvento = convertView.findViewById(R.id.textViewTipoEvento);
        TextView textViewPuntoRegistro = convertView.findViewById(R.id.textViewPuntoRegistro);



        Button btnVerDetalle = convertView.findViewById(R.id.btnVerDetalle);



        btnVerDetalle.setOnClickListener(v -> {
            // Mostrar loading o progreso
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Cargando detalles...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Crear el cuerpo de la solicitud
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("registro_control_fk", turno.getSecuencial()); // Asumiendo que tienes este método
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONTROL_TURNOS_OBTENER;
            // Hacer la llamada API
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestBody,
                    response -> {
                        progressDialog.dismiss();
                        try {
                            RespuestaHistorico historicoResponse = new Gson().fromJson(response.toString(), RespuestaHistorico.class);

                            if (historicoResponse.isExito() && historicoResponse.getDatos() != null) {
                                RespuestaHistorico.HistoricoDatos datos = historicoResponse.getDatos();

                                // Mostrar el diálogo con los datos obtenidos
                                DialogDetalleFragment dialog = DialogDetalleFragment.newInstance(
                                        datos.getImageBase64(),
                                        datos.getLatitud(),
                                        datos.getLongitud()
                                );

                                if (context instanceof FragmentActivity) {
                                    dialog.show(((FragmentActivity) context).getSupportFragmentManager(), "detalleTurno");
                                }
                            } else {
                                Toast.makeText(context, "Error al obtener detalles: " + historicoResponse.getMensaje(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error de conexión intente nuevamente: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            ) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    LogUtils.i("ServerError", "++++++++++++++++++++++++ ---------------------------");
                    params.put("version-app-pax", ConfigEmizor.VERSION);
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");

                    if (user.getTokenAuth() != null) {

                        String xtoken = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);

                        LogUtils.i("ServerError", "getHeaders Enviando autorization :: " + xtoken);
                        params.put("Authorization", xtoken);
                    }
                    LogUtils.i("ServerError", "++++++++++++++++++++++++ ---------------------------");

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            // Agregar la solicitud a la cola de Volley
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        });

        textViewNombre.setText(turno.getNombre());
        textViewCargo.setText(turno.getEmpleadoCargo());
        textViewFechaRegistro.setText(turno.getFechaRegistro());
        textViewTipoEvento.setText(turno.getTipoEvento());
        textViewPuntoRegistro.setText(turno.getPuntoRegistro());

        return convertView;
    }

}
