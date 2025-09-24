package com.emizor.univida.fragmento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.emizor.univida.R;
import com.emizor.univida.adapter.VentasAdapter;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoatcRccNuevoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoatcRccNuevoFragment extends Fragment {

    private Button btnFecha;
    private VentasAdapter adapter;
    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat sdfApi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final Gson gson = new Gson();

    public SoatcRccNuevoFragment() {
        // Required empty public constructor
    }

    public static SoatcRccNuevoFragment newInstance(String param1, String param2) {
        SoatcRccNuevoFragment fragment = new SoatcRccNuevoFragment();
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
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_soatc_rcc_nuevo, container, false);
        btnFecha = v.findViewById(R.id.btnFechaListaVenta);
        ListView listView = v.findViewById(R.id.listViewVentas);
        adapter = new VentasAdapter(getContext());
        listView.setAdapter(adapter);

        return v;
    }
}