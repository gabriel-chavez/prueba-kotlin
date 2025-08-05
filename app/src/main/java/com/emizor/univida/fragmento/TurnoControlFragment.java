package com.emizor.univida.fragmento;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;  // AndroidX
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emizor.univida.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TurnoControlFragment extends Fragment {

    private SurfaceView surfaceView;
    private ImageView ivIamgenDeposito;
    private android.hardware.Camera camera;
    private Button btnRegistrar;
    private Spinner spinnerPunto, spinnerEvento;
    private List<String> puntosList = new ArrayList<>();
    private List<String> eventosList = new ArrayList<>();
    private double latitud, longitud;
    private LocationManager locationManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turno_control, container, false);

        surfaceView = view.findViewById(R.id.surfaceView);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> takePictureAndSend());
        spinnerPunto = view.findViewById(R.id.spinnerPunto);
        spinnerEvento = view.findViewById(R.id.spinnerEvento);
        setupSpinnersWithSequentialData();
        if (checkPermissions()) {
            startCamera();
            startGPS();
        } else {
            requestPermissions();
        }
        return view;
    }
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    private void startGPS() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                        // Implementar otros métodos requeridos...
                    });
        }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
    }


    private void setupSpinnersWithSequentialData() {
        // Datos secuenciales para Puntos (ejemplo: Punto 1, Punto 2, etc.)
        for (int i = 1; i <= 10; i++) {
            puntosList.add("Punto " + i);
        }

        // Datos secuenciales para Eventos
        eventosList.add("Ingreso");
        eventosList.add("Salida");
        eventosList.add("Control Intermedio");
        eventosList.add("Incidente");
        eventosList.add("Observación");

        // Configurar adaptadores
        ArrayAdapter<String> puntoAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_item,
                puntosList
        );
        puntoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPunto.setAdapter(puntoAdapter);

        ArrayAdapter<String> eventoAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_item,
                eventosList
        );
        eventoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvento.setAdapter(eventoAdapter);

        // Opcional: Seleccionar valores por defecto
        spinnerPunto.setSelection(0);
        spinnerEvento.setSelection(0);
    }
    private void startCamera() {
        try {
            camera = android.hardware.Camera.open(0);  // Abre la cámara trasera
            setCameraDisplayOrientation();

            // Configurar parámetros de la cámara para autoenfoque
            Camera.Parameters params = camera.getParameters();

            // Verificar si el dispositivo soporta autoenfoque
            if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                camera.setParameters(params);
            }

            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        camera.setPreviewDisplay(holder);
                        camera.startPreview(); // Iniciar la vista previa
                        autoFocus();  // Intentar autoenfoque
                    } catch (IOException e) {
                        Log.e("TurnoControlFragment", "Error al configurar la vista previa", e);
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    // No se necesita nada aquí, se puede dejar vacío
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    // Liberamos la cámara cuando se destruye la superficie
                    camera.stopPreview();
                    camera.release();
                }
            });
        } catch (Exception e) {
            Log.e("TurnoControlFragment", "Error al iniciar la cámara", e);
        }
    }

    private void autoFocus() {
        if (camera != null) {
            try {
                camera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, android.hardware.Camera camera) {
                        if (success) {
                            Log.d("TurnoControlFragment", "Autoenfoque exitoso");
                        } else {
                            Log.d("TurnoControlFragment", "Autoenfoque falló");
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("TurnoControlFragment", "Error en autoenfoque", e);
            }
        }
    }

    // Método para ajustar la orientación de la cámara según la orientación del dispositivo
    private void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else { // cámara trasera
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }

    // Método para tomar una foto
    private void takePictureAndSend() {
        camera.takePicture(null, null, (data, camera1) -> {
            // Convierte la imagen en un Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Convierte el Bitmap en un String base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            // Mostrar el diálogo de confirmación con los botones de "Sí" y "No"
            showConfirmationDialog(encodedImage);
        });
    }


    private void showConfirmationDialog(String encodedImage) {
        String punto = spinnerPunto.getSelectedItem().toString();
        String evento = spinnerEvento.getSelectedItem().toString();

        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmar Registro")
                .setMessage("¿Confirmar registro con los siguientes datos?\n\n" +
                        "Punto: " + punto + "\n" +
                        "Evento: " + evento + "\n" +
                        "Coordenadas: " + latitud + ", " + longitud)
                .setCancelable(false)
                .setPositiveButton("Sí", (dialog, which) -> {
                    remitir(encodedImage);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    restartPreview();
                })
                .show();
    }
    private void restartPreview() {
        try {
            camera.startPreview();
            autoFocus(); // Volver a enfocar
        } catch (Exception e) {
            Log.e("TurnoControlFragment", "Error al reiniciar vista previa", e);
            // Si falla, reiniciar toda la cámara
            restartCamera();
        }
    }

    private void restartCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        startCamera();
    }
    // Enviar la imagen al servidor
    private void remitir(String encodedImage) {
        // Obtener valores seleccionados en los Spinners
        String punto = spinnerPunto.getSelectedItem().toString();
        String evento = spinnerEvento.getSelectedItem().toString();

        // Obtener fecha y hora actual
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
        String fechaHora = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            fechaHora = sdf.format(new Date());
        }

        // Crear objeto JSON con todos los datos
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("imagen", encodedImage);
            jsonData.put("punto", punto);
            jsonData.put("evento", evento);
            jsonData.put("latitud", latitud);
            jsonData.put("longitud", longitud);
            jsonData.put("fecha_hora", fechaHora);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar datos al servidor (ejemplo con Volley)


        //Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}