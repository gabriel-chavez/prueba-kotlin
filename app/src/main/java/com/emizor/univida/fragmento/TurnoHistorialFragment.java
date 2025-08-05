package com.emizor.univida.fragmento;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.emizor.univida.R;
import com.emizor.univida.adapter.HistorialQrAdapter;
import com.emizor.univida.adapter.HistorialTurnosAdapter;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.turnos.Turnos;
import com.emizor.univida.modelo.dominio.univida.ventas.QrGenerado;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.List;

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
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Sin fecha", (dialog, which) -> {
            // Configurar la fecha en 1900-01-01
            fechaSeleccion = new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime();
            btnFechaListaVenta.setText("1900-01-01");

            // Llamar a obtenerHistorial() con la nueva fecha
            obtenerHistorial();
        });
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
        List<Turnos> historial = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Turnos turno = new Turnos(
                    i,                              // secuencial
                    "Nombre " + i,                  // nombre
                    "Cargo " + i,                   // cargo
                    "2025-08-0" + i,                 // fechaRegistro
                    (i % 2 == 0) ? "Entrada" : "Salida", // tipoEvento
                    "Punto " + i,                   // puntoRegistro
                    "imagen_" + i + ".jpg",         // imagen
                    "-17.38" + i,                   // latitud
                    "-66.15" + i                    // longitud
            );
            historial.add(turno);
        }

        // Ejemplo: imprimir en consola para verificar
        for (Turnos t : historial) {
            System.out.println(t);
        }
        turnosRegistrados.clear();
        turnosRegistrados.addAll(historial);

        // Notificar al adaptador que los datos cambiaron
        adapter.notifyDataSetChanged();
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
