package com.emizor.univida;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class DialogDetalleFragment extends DialogFragment {

    private static final String ARG_IMAGEN = "imagenUrl";
    private static final String ARG_LAT = "latitud";
    private static final String ARG_LON = "longitud";
    private String imagenUrl;
    private double latitud;
    private double longitud;


    public DialogDetalleFragment() {
        // Constructor vacío requerido por Fragment
    }

    // Método recomendado para crear instancia con datos
    public static DialogDetalleFragment newInstance(String imagenUrl, double latitud, double longitud) {
        DialogDetalleFragment fragment = new DialogDetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGEN, imagenUrl);
        args.putDouble(ARG_LAT, latitud);
        args.putDouble(ARG_LON, longitud);
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

        View view = inflater.inflate(R.layout.fragment_dialog_detalle, container, false);

        ImageView imgDetalle = view.findViewById(R.id.imgDetalle);
        MapView mapView = view.findViewById(R.id.mapDetalle);

        // Cargar imagen con Glide
        Glide.with(requireActivity())

                .load(imagenUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgDetalle);

        // Configurar osmdroid
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Configurar vista del mapa
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint point = new GeoPoint(latitud, longitud);
        mapController.setCenter(point);

        // Agregar marcador
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Ubicación registrada");
        mapView.getOverlays().add(marker);

        return view;
    }
}