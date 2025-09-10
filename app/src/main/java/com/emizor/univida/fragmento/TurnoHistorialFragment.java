package com.emizor.univida.fragmento;

import static com.emizor.univida.rest.DatosConexion.URL_UNIVIDA_CONTROL_TURNOS_LISTAR;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emizor.univida.R;
import com.emizor.univida.adapter.HistorialQrAdapter;
import com.emizor.univida.adapter.HistorialTurnosAdapter;
import com.emizor.univida.modelo.dominio.univida.consultas.RespuestaTurnos;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.turnos.Turnos;
import com.emizor.univida.modelo.dominio.univida.ventas.QrGenerado;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ConfigEmizor;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


public class TurnoHistorialFragment  extends Fragment {
    private View rootView;
    private ListView listView;
    private Date fechaSeleccion;
    private Button btnFechaListaVenta;
    private DatePickerDialog datePickerDialog;
    private User user;
    private List<Turnos> turnosRegistrados;
    private HistorialTurnosAdapter adapter;
    public TurnoHistorialFragment() {
        // Constructor vacío requerido
    }
    public static TurnoHistorialFragment newInstance() {
        return new TurnoHistorialFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Aquí inicializas variables que no dependan de la vista
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        rootView = inflater.inflate(R.layout.fragment_turno_historial, container, false);
        listView = rootView.findViewById(R.id.listViewMovimientos);
        btnFechaListaVenta = rootView.findViewById(R.id.btnFechaListaVenta);
        fechaSeleccion = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        btnFechaListaVenta.setText(simpleDateFormat.format(fechaSeleccion));

        datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            fechaSeleccion = calendar.getTime();

            btnFechaListaVenta.setText(simpleDateFormat.format(fechaSeleccion));

            // Llamar a obtenerHistorial() cuando se seleccione la fecha
            obtenerHistorial();
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Sin fecha", (dialog, which) -> {
//            // Configurar la fecha en 1900-01-01
//            fechaSeleccion = new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime();
//            btnFechaListaVenta.setText("1900-01-01");
//
//            // Llamar a obtenerHistorial() con la nueva fecha
//            obtenerHistorial();
//        });
        btnFechaListaVenta.setOnClickListener(v -> datePickerDialog.show());

        turnosRegistrados = new ArrayList<>();
        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());
        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();



        adapter = new HistorialTurnosAdapter(getContext(), turnosRegistrados,this);
        listView.setAdapter(adapter);

        obtenerHistorial();

        return  rootView;
    }
    public void obtenerHistorial() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando historial...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String fechaRegistro = btnFechaListaVenta.getText().toString();

        // Crear un objeto JSON con la fecha
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("fecha_registro", fechaRegistro);  // La clave es "fecha_registro" y el valor es la fecha del botón
        } catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
        String url = DatosConexion.SERVIDORUNIVIDA +URL_UNIVIDA_CONTROL_TURNOS_LISTAR;
        Log.i("url", url);
        // Crear una solicitud GET con Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {

                            Gson gson = new Gson();
                            RespuestaTurnos respuesta = gson.fromJson(response.toString(), RespuestaTurnos.class);

                            if (respuesta.exito) {

                                List<Turnos> turnosList = respuesta.getDatos();


                                for (Turnos turno : turnosList) {
                                    System.out.println(turno);
                                }


                                turnosRegistrados.clear();
                                turnosRegistrados.addAll(turnosList);


                                adapter.notifyDataSetChanged();
                            } else {
                                turnosRegistrados.clear();
                                adapter.notifyDataSetChanged();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setMessage(respuesta.mensaje)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", null);


                                builder.setTitle("Aviso")
                                        .setIcon(android.R.drawable.ic_dialog_info);

                                AlertDialog alert = builder.create();
                                alert.show();

                               // Toast.makeText(getContext(), respuesta.mensaje, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("VolleyError", "Error en el procesamiento de la respuesta: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // Manejar errores en la solicitud
                        Log.e("VolleyError", "Error en la conexión o en la respuesta de la API: " + error.getMessage());
                        Toast.makeText(getContext(), "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Configurar los headers personalizados aquí
                headers.put("version-app-pax", ConfigEmizor.VERSION);
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                // Si existe un token de autenticación, añadirlo al header
                if (user.getTokenAuth() != null) {
                    String xtoken = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);
                    Log.i("Volley", "Enviando Authorization: " + xtoken);
                    headers.put("Authorization", xtoken);
                }

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Añadir la solicitud a la cola de Volley
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Aquí enlazas vistas, listeners, adaptadores, etc.
    }

    @Override
    public void onStart() {
        super.onStart();
        // Fragmento visible al usuario
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragmento interactivo
    }

    @Override
    public void onPause() {
        super.onPause();
        // Guardar estado o pausar tareas
    }

    @Override
    public void onStop() {
        super.onStop();
        // Fragmento ya no visible
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Liberar referencias a vistas para evitar fugas de memoria
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Liberar otros recursos
    }
}
