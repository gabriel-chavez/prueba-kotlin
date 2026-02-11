package com.emizor.univida.fragmento;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emizor.univida.R;
import com.emizor.univida.activities.PrincipalActivity;
import com.emizor.univida.adapter.ItemSoatcRccListarAdapter;
import com.emizor.univida.modelo.dominio.univida.ApiResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarCobrosResponse;
import com.emizor.univida.modelo.dominio.univida.soatc.ListarRccResponse;
import com.emizor.univida.rest.ApiService;
import com.emizor.univida.rest.DatosConexion;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcRccListarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcRccListarFragment extends Fragment {

    private Button btnFecha, btnNuevoReporte;
    private ItemSoatcRccListarAdapter adapter;
    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat sdfApiFormato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final Gson gson = new Gson();
    private TextView tvTotalImporte, tvCantidadVendidos, tvCantidadValidos, tvCantidadRevertidos, tvCantidadAnulados;
    List<ListarRccResponse> datos;
    private OnRccListadoListener rccListadoListener;

    public interface OnRccListadoListener {
        void onRccListener();
    }

    public SoatcRccListarFragment() {
        // Required empty public constructor
    }

    public static SoatcRccListarFragment newInstance(String param1, String param2) {
        SoatcRccListarFragment fragment = new SoatcRccListarFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_soatc_rcc_listar, container, false);
        btnFecha = v.findViewById(R.id.btnFechaListaVenta);
        ListView listView = v.findViewById(R.id.listViewVentas);

        btnFecha.setText("Seleccionar fecha");
        btnFecha.setOnClickListener(_v -> mostrarDatePicker());

//        btnNuevoReporte=v.findViewById(R.id.btnNuevoReporte);
//
//        tvTotalImporte = v.findViewById(R.id.tvTotalImporte);
//        tvCantidadVendidos = v.findViewById(R.id.tvCantidadVendidos);
//        tvCantidadValidos = v.findViewById(R.id.tvCantidadValidos);
//        tvCantidadRevertidos = v.findViewById(R.id.tvCantidadRevertidos);
//        tvCantidadAnulados = v.findViewById(R.id.tvCantidadAnulados);
       // btnNuevoReporte.setOnClickListener(x -> mostrarDialogResumenRcc());
        rccListadoListener = new OnRccListadoListener() {
            @Override
            public void onRccListener() {
                // Recargar las ventas cuando se revierte una
                cargarRccPorFecha(cal);
            }
        };
        adapter = new ItemSoatcRccListarAdapter(getContext(),rccListadoListener);
        listView.setAdapter(adapter);
        cargarRccPorFecha(cal);
        return v;
    }
    private void mostrarDatePicker() {
        DatePickerDialog dp = new DatePickerDialog(
                requireContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cargarRccPorFecha(cal);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }
    private void cargarRccPorFecha(Calendar fecha) {
        String fechaStr = sdfApi.format(fecha.getTime());
        String fechaStrFormato = sdfApiFormato.format(fecha.getTime());
        btnFecha.setText(fechaStrFormato);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("fecha", fechaStr);


        ((PrincipalActivity) requireActivity()).mostrarLoading(true);
        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONCILIACION_LISTAR;

        ApiService api = new ApiService(getContext());
        api.solicitudPost(url, parametros, response -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);
            Type type = new TypeToken<ApiResponse<List<ListarRccResponse>>>() {}.getType();
            ApiResponse<List<ListarRccResponse>> apiResp = gson.fromJson(response, type);

            if (apiResp != null && apiResp.exito && apiResp.datos != null) {
                datos = apiResp.datos;

//                tvTotalImporte.setText(String.format(Locale.getDefault(), "Bs. %.2f", datos.RccFormularioImporte));
//                tvCantidadVendidos.setText(String.valueOf(datos.RccCantidadComprobante));
//                tvCantidadValidos.setText(String.valueOf(datos.RccCantidadComprobanteValidos));
//                tvCantidadRevertidos.setText(String.valueOf(datos.RccCantidadComprobanteRevertidos));
//                tvCantidadAnulados.setText(String.valueOf(datos.RccCantidadComprobanteAnulados));

                adapter.setData(datos);

            } else {
                String msg = (apiResp != null && !TextUtils.isEmpty(apiResp.mensaje))
                        ? apiResp.mensaje : "No se pudo obtener la información.";
                mostrarToast(msg);

                new AlertDialog.Builder(getContext())
                        .setMessage(msg)
                        .setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss())
                        .show();
                adapter.setData(null);
            }

        }, error -> {
            ((PrincipalActivity) requireActivity()).mostrarLoading(false);

            mostrarToast("Error de conexión. Intentá nuevamente.");
            adapter.setData(null);

            new AlertDialog.Builder(getContext())
                    .setMessage("Error de conexión. Intentá nuevamente.")
                    .setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss())
                    .show();
        });
    }

    private void mostrarToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

    }
}