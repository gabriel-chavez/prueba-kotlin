package com.emizor.univida.dialogo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emizor.univida.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class DialogDetalleFragment extends DialogFragment {

    private static final String ARG_IMAGEN = "imagenBase64";
    private static final String ARG_LAT = "latitud";
    private static final String ARG_LON = "longitud";
    private String imagenBase64;
    private double latitud;
    private double longitud;

    // Variables para el zoom y pan de la imagen
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private float lastTouchX, lastTouchY;
    private float posX, posY;
    private static final float MIN_SCALE = 1.0f;
    private static final float MAX_SCALE = 5.0f;

    // Para detectar doble tap
    private long lastClickTime = 0;
    private static final int DOUBLE_CLICK_DELAY = 300;

    public DialogDetalleFragment() {
        // Constructor vacío requerido por Fragment
    }

    public static DialogDetalleFragment newInstance(String imagenBase64, double latitud, double longitud) {
        DialogDetalleFragment fragment = new DialogDetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGEN, imagenBase64);
        args.putDouble(ARG_LAT, latitud);
        args.putDouble(ARG_LON, longitud);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = (int) (displayMetrics.widthPixels * 0.98);
            int height = (int) (displayMetrics.heightPixels * 0.90); // Aumentamos la altura
            getDialog().getWindow().setLayout(width, height);
            getDialog().getWindow().setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imagenBase64  = getArguments().getString(ARG_IMAGEN);
            latitud = getArguments().getDouble(ARG_LAT);
            longitud = getArguments().getDouble(ARG_LON);
        }

        Configuration.getInstance().load(requireContext(),
                requireContext().getSharedPreferences("osmdroid", 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_detalle, container, false);

        ImageView imgDetalle = view.findViewById(R.id.imgDetalle);
        MapView mapView = view.findViewById(R.id.mapDetalle);

        loadBase64Image(imgDetalle, imagenBase64);
        setupImageZoomAndPan(imgDetalle);

        try {
            Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());

            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMultiTouchControls(true);
            mapView.setBuiltInZoomControls(true);
            mapView.setZoomRounding(true);
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setMinZoomLevel(15.0);
            mapView.setMaxZoomLevel(22.0);

            IMapController mapController = mapView.getController();
            GeoPoint point = new GeoPoint(latitud, longitud);
            mapController.setCenter(point);
            mapController.setZoom(20.0);

            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Ubicación registrada");
            mapView.getOverlays().add(marker);

            mapView.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void setupImageZoomAndPan(ImageView imageView) {
        // Inicializar el detector de gestos de escala
        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);

                final int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        // Para detectar doble tap
                        long clickTime = System.currentTimeMillis();
                        if (clickTime - lastClickTime < DOUBLE_CLICK_DELAY) {
                            // Doble tap - resetear zoom y posición
                            resetImageZoomAndPosition(imageView);
                            return true;
                        }
                        lastClickTime = clickTime;

                        lastTouchX = event.getX();
                        lastTouchY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (!scaleGestureDetector.isInProgress()) {
                            float x = event.getX();
                            float y = event.getY();

                            // Calcular el desplazamiento
                            float dx = x - lastTouchX;
                            float dy = y - lastTouchY;

                            // Aplicar el desplazamiento solo si estamos haciendo zoom
                            if (scaleFactor > 1.0f) {
                                posX += dx;
                                posY += dy;

                                // Limitar el desplazamiento para que la imagen no se salga de los bordes
                                limitImageMovement(imageView);

                                imageView.setTranslationX(posX);
                                imageView.setTranslationY(posY);
                            }

                            lastTouchX = x;
                            lastTouchY = y;
                        }
                        break;
                }

                return true;
            }
        });
    }

    private void limitImageMovement(ImageView imageView) {
        // Calcular los límites de movimiento basados en el nivel de zoom
        float scaledWidth = imageView.getWidth() * scaleFactor;
        float scaledHeight = imageView.getHeight() * scaleFactor;

        // Máximo desplazamiento permitido
        float maxDx = Math.max(0, (scaledWidth - imageView.getWidth()) / 2);
        float maxDy = Math.max(0, (scaledHeight - imageView.getHeight()) / 2);

        // Aplicar límites
        if (Math.abs(posX) > maxDx) {
            posX = posX > 0 ? maxDx : -maxDx;
        }

        if (Math.abs(posY) > maxDy) {
            posY = posY > 0 ? maxDy : -maxDy;
        }
    }

    private void resetImageZoomAndPosition(ImageView imageView) {
        scaleFactor = 1.0f;
        posX = 0f;
        posY = 0f;

        imageView.setScaleX(scaleFactor);
        imageView.setScaleY(scaleFactor);
        imageView.setTranslationX(posX);
        imageView.setTranslationY(posY);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_SCALE, Math.min(scaleFactor, MAX_SCALE));

            ImageView imageView = getView().findViewById(R.id.imgDetalle);
            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);

            // Ajustar la posición para mantener el punto focal del zoom
            if (scaleFactor > 1.0f) {
                float focusX = detector.getFocusX();
                float focusY = detector.getFocusY();

                // Calcular el desplazamiento necesario
                float dx = (focusX - imageView.getWidth() / 2) * (1 - detector.getScaleFactor());
                float dy = (focusY - imageView.getHeight() / 2) * (1 - detector.getScaleFactor());

                posX += dx;
                posY += dy;

                limitImageMovement(imageView);

                imageView.setTranslationX(posX);
                imageView.setTranslationY(posY);
            } else {
                // Si el zoom es 1.0, resetear la posición
                posX = 0f;
                posY = 0f;
                imageView.setTranslationX(posX);
                imageView.setTranslationY(posY);
            }

            return true;
        }
    }

    private void loadBase64Image(ImageView imageView, String base64String) {
        try {
            if (base64String == null || base64String.isEmpty()) {
                imageView.setImageResource(R.drawable.ic_launcher_background);
                return;
            }

            String pureBase64 = cleanBase64String(base64String);
            byte[] decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT);

            // Configurar scaleType para que la imagen se ajuste correctamente
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            Glide.with(requireActivity())
                    .load(decodedBytes)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .dontAnimate() // Eliminamos la animación problemática
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private String cleanBase64String(String base64String) {
        if (base64String.startsWith("data:image")) {
            int base64Index = base64String.indexOf("base64,");
            if (base64Index != -1) {
                return base64String.substring(base64Index + 7);
            }
        }
        return base64String;
    }

    @Override
    public void onResume() {
        super.onResume();
        MapView mapView = getView().findViewById(R.id.mapDetalle);
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MapView mapView = getView().findViewById(R.id.mapDetalle);
        if (mapView != null) {
            mapView.onPause();
        }
    }
}