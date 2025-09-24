package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.adapter.VentasAdapter;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ComprobanteDato;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarVentasResponse;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcListarVentasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcListarVentasFragment extends Fragment {
    private Button btnFecha;
    private VentasAdapter adapter;

    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final Gson gson = new Gson();

    public SoatcListarVentasFragment() {
        // Required empty public constructor
    }

    public static SoatcListarVentasFragment newInstance(String param1, String param2) {
        SoatcListarVentasFragment fragment = new SoatcListarVentasFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_soatc_listar_ventas, container, false);
        btnFecha = v.findViewById(R.id.btnFechaListaVenta);
        ListView listView = v.findViewById(R.id.listViewVentas);

//        fechaSeleccion = Calendar.getInstance().getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        btnFecha.setText(simpleDateFormat.format(fechaSeleccion));


        adapter = new VentasAdapter(getContext());
        listView.setAdapter(adapter);

        // Fecha por defecto: hoy
        btnFecha.setText("Seleccionar fecha");
        btnFecha.setOnClickListener(_v -> mostrarDatePicker());

        // Cargar hoy al abrir (opcional)
        cargarVentasDeFecha(cal);

        return v;
    }
    private void mostrarDatePicker() {
        DatePickerDialog dp = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cargarVentasDeFecha(cal);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }
    private void cargarVentasDeFecha(Calendar fecha) {
        String fechaStr = sdfApi.format(fecha.getTime());
        btnFecha.setText(fechaStr);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("fecha", fechaStr);


        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_EMISION_POLIZA_LISTAR;

        ApiService api = new ApiService(getContext());
        api.solicitudPost(url, parametros, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            Type type = new TypeToken<ApiResponse<List<ListarVentasResponse>>>() {}.getType();
            ApiResponse<List<ListarVentasResponse>> apiResp = gson.fromJson(response, type);

            if (apiResp != null && apiResp.exito && apiResp.datos != null) {
                List<ListarVentasResponse> lista = apiResp.datos;
                adapter.setData(lista);

            } else {
                String msg = (apiResp != null && !TextUtils.isEmpty(apiResp.mensaje))
                        ? apiResp.mensaje : "No se pudo obtener la información.";
                mostrarToast(msg);
                adapter.setData(null);
            }

        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);

            mostrarToast("Error de conexión. Intentá nuevamente.");
            adapter.setData(null);
        });
    }
    private void mostrarToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}