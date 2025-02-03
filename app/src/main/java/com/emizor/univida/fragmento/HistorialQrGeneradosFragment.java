package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emizor.univida.R;
import com.emizor.univida.adapter.MovimientoAdapter;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.ventas.QrGenerado;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ConfigEmizor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.EditText;
import android.widget.Spinner;
public class HistorialQrGeneradosFragment extends Fragment {
    private ListView listView;
    private MovimientoAdapter adapter;
    private List<QrGenerado> qrGenerados;
    private User user;

    private EditText editTextIdentificador;
    private Spinner spinnerEstado;
    private Button btnFechaListaVenta;
    private Date fechaSeleccion;
    private DatePickerDialog datePickerDialog;
    TextView textViewMensaje;
    public HistorialQrGeneradosFragment() {
        // Required empty public constructor
    }

    public static HistorialQrGeneradosFragment newInstance() {
        return new HistorialQrGeneradosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_historial_qr_generados, container, false);

        listView = rootView.findViewById(R.id.listViewMovimientos);


        fechaSeleccion = Calendar.getInstance().getTime();

        editTextIdentificador = rootView.findViewById(R.id.editTextIdentificador);
        spinnerEstado = rootView.findViewById(R.id.spinnerEstado);
        btnFechaListaVenta = rootView.findViewById(R.id.btnFechaListaVenta);
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

        btnFechaListaVenta.setOnClickListener(v -> datePickerDialog.show());

        Button btnBuscar = rootView.findViewById(R.id.btnBuscar);
        textViewMensaje = rootView.findViewById(R.id.textViewMensaje);

        btnBuscar.setOnClickListener(v -> obtenerHistorial());

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) != null) {
                    obtenerHistorial();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no hay selección
            }
        });


        qrGenerados = new ArrayList<>();
        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());
        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();



        adapter = new MovimientoAdapter(getContext(), qrGenerados);
        listView.setAdapter(adapter);

        obtenerHistorial();

        return rootView;
    }

    private void obtenerHistorial() {
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_VENTAS_QR_LISTAR_GENERADOS;

        String identificadorVehi = editTextIdentificador.getText().toString();

        String estadoSeleccionado = spinnerEstado.getSelectedItem().toString();
        int valorEstado = 0;


        switch (estadoSeleccionado) {
            case "Solicitado":
                valorEstado = 1;
                break;
            case "Habilitado":
                valorEstado = 2;
                break;
            case "Pagado":
                valorEstado = 3;
                break;
            case "Anulado":
                valorEstado = 4;
                break;
            case "Vencido":
                valorEstado = 5;
                break;
            case "Rechazado":
                valorEstado = 6;
                break;
            case "Error de imagen":
                valorEstado = 7;
                break;
        }

        // Crear los parámetros para la solicitud
        JSONObject params = new JSONObject();
        try {
            SimpleDateFormat sm = new SimpleDateFormat("MM-dd-yyyy");
            String strDate = sm.format(fechaSeleccion);
            params.put("venta_vendedor", user.getDatosUsuario().getEmpleadoUsuario());
            params.put("fecha", strDate);
            params.put("t_par_simple_estado_solicitud_fk", valorEstado);
            params.put("identificador_vehiculo", identificadorVehi);
            params.put("t_par_venta_canal_fk", "28");

            // Agregar seguridad externa
            JSONObject seguridadExterna = new JSONObject();
            seguridadExterna.put("seg_ext_usuario", "");
            seguridadExterna.put("seg_ext_token", 0);
            params.put("seguridad_externa", seguridadExterna);

            // Agregar transacción de origen
            JSONObject transaccionOrigen = new JSONObject();
            transaccionOrigen.put("tra_ori_intermediario", 0);
            transaccionOrigen.put("tra_ori_entidad", "000");
            transaccionOrigen.put("tra_ori_sucursal", "Oficina Nacional");
            transaccionOrigen.put("tra_ori_agencia", "");
            transaccionOrigen.put("tra_ori_canal", 28);
            transaccionOrigen.put("tra_ori_cajero", user.getDatosUsuario().getEmpleadoUsuario());
            params.put("transaccion_origen", transaccionOrigen);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean exito = jsonResponse.getBoolean("exito");
                        if (exito) {
                            textViewMensaje.setText("");
                            JSONArray datos = jsonResponse.getJSONArray("datos");

                            qrGenerados.clear();
                            for (int i = 0; i < datos.length(); i++) {
                                JSONObject movimientoObj = datos.getJSONObject(i);
                                // Procesar los datos y agregar a la lista
                                String secuencial = movimientoObj.getString("Secuencial");
                                String identificadorVehiculo = movimientoObj.getString("IdentificadorVehiculo");
                                String gestion = movimientoObj.getString("Gestion");
                                String fechaHoraSolicitud = movimientoObj.getString("FechaHoraSolicitud");
                                String estadoSolicitud = movimientoObj.getString("TParSimpleEstadoSolicitudDescripcion");
                                String fechaHoraEstado = movimientoObj.getString("FechaHoraEstado");
                                String efectivizado = movimientoObj.getString("Efectivizado");
                                String mensajeEfectivizacion = movimientoObj.getString("MensajeEfectivizacion");
                                int tVehiSoatPropFk = movimientoObj.getInt("TVehiSoatPropFk");

                                QrGenerado qrGenerado = new QrGenerado(secuencial, identificadorVehiculo, gestion,
                                        fechaHoraSolicitud, estadoSolicitud, fechaHoraEstado,
                                        efectivizado, mensajeEfectivizacion, tVehiSoatPropFk);
                                qrGenerados.add(qrGenerado);
                            }

                            adapter.notifyDataSetChanged();

                        } else {
                            qrGenerados.clear();
                            String mensaje = jsonResponse.optString("mensaje", "Error al obtener datos.");
                            textViewMensaje.setText(mensaje);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al procesar la respuesta.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error de red, intente nuevamente.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                return params.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return construirEncabezados();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private Map<String, String> construirEncabezados() {
        Map<String, String> params = new HashMap<>();
        params.put("version-app-pax", ConfigEmizor.VERSION);

        if (user.getTokenAuth() != null) {
            String xtoken = UtilRest.getInstance().procesarDatosInterno(user.getTokenAuth(), 1);
            params.put("Authorization", xtoken);
        }
        return params;
    }

    private void abrirdatePicker(){

        Calendar c = Calendar.getInstance();
        if (fechaSeleccion != null) {
            c.setTime(fechaSeleccion);
        } else {
            c.setTime(Calendar.getInstance().getTime());
        }

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog.updateDate(mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}

