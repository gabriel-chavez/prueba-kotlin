package com.emizor.univida.fragmento;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.emizor.univida.R;
import com.emizor.univida.modelo.dominio.univida.parametricas.ParametricaGenerica;
import com.emizor.univida.modelo.dominio.univida.reporte_cierre_caja.ArchivoAdjunto;
import com.emizor.univida.modelo.dominio.univida.seguridad.User;
import com.emizor.univida.modelo.dominio.univida.turnos.Evento;
import com.emizor.univida.modelo.dominio.univida.turnos.Punto;
import com.emizor.univida.modelo.manejador.ControladorSqlite2;
import com.emizor.univida.modelo.manejador.UtilRest;
import com.emizor.univida.rest.DatosConexion;
import com.emizor.univida.util.ConfigEmizor;
import com.emizor.univida.util.LogUtils;
import com.emizor.univida.utils.ParametricasCache;
import com.google.gson.Gson;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TurnoControlFragment extends Fragment {
//    private File mi_foto;
//    private SurfaceView surfaceView;
//    private ImageView ivIamgenDeposito;
//    private android.hardware.Camera camera;
//    private LinearLayout btnRegistrar;
//    private Spinner spinnerPunto, spinnerEvento;
//    private List<String> puntosList = new ArrayList<>();
//    private List<String> eventosList = new ArrayList<>();
    private double latitud, longitud;
//    private LocationManager locationManager;
//    private boolean isCameraReady = false;
//    private boolean isTakingPicture = false;
    private CameraView cameraView;
    private LinearLayout btnRegistrar;
    private Spinner spinnerPunto, spinnerEvento;
    private ProgressBar progressBar;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turno_control, container, false);

        cameraView = view.findViewById(R.id.cameraView);
        spinnerPunto = view.findViewById(R.id.spinnerPunto);
        spinnerEvento = view.findViewById(R.id.spinnerEvento);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        progressBar = view.findViewById(R.id.progressBar2);

        cameraView.setFacing(Facing.BACK);



        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                progressBar.setVisibility(View.GONE);

                if (jpeg == null) {
                    Toast.makeText(getContext(), "Error al capturar imagen", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                procesarFoto(bitmap);
            }
        });

        btnRegistrar.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            cameraView.capturePicture();
        });
        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());
        user = controladorSqlite2.obtenerUsuario();
        controladorSqlite2.cerrarConexion();
        if (checkPermissions()) {
            // No iniciar la cámara aquí todavía - esperar a surfaceCreated
            startGPS();
            cargarParametricas();
        } else {
            requestPermissions();
        }
        return view;

//
//
//        ///////////////////////////////////////////////////////
//
//        btnRegistrar = view.findViewById(R.id.btnRegistrar);
//
//        // Configurar el SurfaceHolder callback PRIMERO
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//
//
//        btnRegistrar.setOnClickListener(v -> takePictureAndSend());
//        spinnerPunto = view.findViewById(R.id.spinnerPunto);
//        spinnerEvento = view.findViewById(R.id.spinnerEvento);
//
//        ControladorSqlite2 controladorSqlite2 = new ControladorSqlite2(getContext());
//        user = controladorSqlite2.obtenerUsuario();
//        controladorSqlite2.cerrarConexion();
//
//        return view;
    }
    private void procesarFoto(Bitmap bitmap) {
        processImageData(bitmap);
        Toast.makeText(getContext(), "Foto capturada", Toast.LENGTH_SHORT).show();
        // Aquí puedes convertir a Base64, guardar o enviar
    }
    @Override
    public void onResume() {
        super.onResume();
        if (cameraView != null) cameraView.start();
    }

    @Override
    public void onPause() {
        if (cameraView != null) cameraView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (cameraView != null) cameraView.destroy();
        super.onDestroy();
    }
    ///////////////////////////////////////////////////////////////////////////////
    private void cargarParametricas() {
        obtenerTiposEvento();
        obtenerTiposPunto();
    }
    private void obtenerTiposEvento() {
        List<Evento> eventos = ParametricasCache.getInstance().getEventos();

        if (eventos != null && !eventos.isEmpty()) {
            ArrayAdapter<Evento> adapter = new ArrayAdapter<>(
                    getContext(), R.layout.spinner_item, eventos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEvento.setAdapter(adapter);
        }
    }
    private void obtenerTiposPunto() {
        List<Punto> puntos = ParametricasCache.getInstance().getPuntos();

        if (puntos != null && !puntos.isEmpty()) {
            ArrayAdapter<Punto> adapter = new ArrayAdapter<>(
                    getContext(), R.layout.spinner_item, puntos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPunto.setAdapter(adapter);
        }
    }
    //    private void setupSpinnerPuntos(List<Punto> puntos) {
//        ArrayAdapter<Punto> adapter = new ArrayAdapter<>(
//                getContext(),
//                R.layout.spinner_item,
//                puntos
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerPunto.setAdapter(adapter);
//
//        if (!puntos.isEmpty()) {
//            spinnerPunto.setSelection(0);
//        }
//    }
//
//    private void setupSpinnerEventos(List<Evento> eventos) {
//        ArrayAdapter<Evento> adapter = new ArrayAdapter<>(
//                getContext(),
//                R.layout.spinner_item,
//                eventos
//        );
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerEvento.setAdapter(adapter);
//
//        if (!eventos.isEmpty()) {
//            spinnerEvento.setSelection(0);
//        }
//    }
    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
    }


    private void startGPS() {
        if (getActivity() == null) return;

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null && location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    Log.d("Location", "Ubicación válida - Latitud: " + latitud + ", Longitud: " + longitud);

                    // Remover updates una vez que tenemos una ubicación válida
                    locationManager.removeUpdates(this);

//                    // Opcional: Notificar que ya tenemos ubicación
//                    if (isTakingPicture) {
//                        // Si estábamos esperando para tomar foto, continuar
//                        new Handler().postDelayed(() -> takePictureAndSend(), 500);
//                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("GPS", "Proveedor habilitado: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("GPS", "Proveedor deshabilitado: " + provider);
            }
        };

        // Verificar qué proveedores están disponibles
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            Log.w("GPS", "Todos los proveedores de ubicación están desactivados");
            return;
        }

        if (gpsEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    locationListener
            );
        }

        if (networkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    locationListener
            );
        }

        // También obtener la última ubicación conocida inmediatamente
        Location lastKnownLocation = null;
        if (gpsEnabled) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (lastKnownLocation == null && networkEnabled) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0.0 && lastKnownLocation.getLongitude() != 0.0) {
            latitud = lastKnownLocation.getLatitude();
            longitud = lastKnownLocation.getLongitude();
            Log.d("Location", "Última ubicación conocida - Latitud: " + latitud + ", Longitud: " + longitud);
        }

        // Timeout: Si después de 15 segundos no tenemos ubicación, mostrar mensaje
        new Handler().postDelayed(() -> {
            locationManager.removeUpdates(locationListener);
            if (latitud == 0.0 && longitud == 0.0) {
                Log.w("Location", "Timeout: No se pudo obtener ubicación");
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "No se pudo obtener la ubicación. Verifique su conexión GPS.", Toast.LENGTH_LONG).show();
                    });
                }
            }
        }, 15000); // 15 segundos de timeout
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == 100) {
//            if (grantResults.length > 0 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                // Pequeño delay para asegurar que los permisos estén aplicados
//                new Handler().postDelayed(() -> {
//                    if (surfaceView.getHolder().getSurface().isValid()) {
//                        startCamera();
//                    }
//                    // Si la superficie no está lista, surfaceCreated se encargará de iniciar la cámara
//                    startGPS();
//                    cargarParametricas();
//                }, 300);
//            } else {
//                Toast.makeText(getContext(), "Permisos denegados", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    private boolean isGPSEnabled() {
        if (getActivity() == null) return false;

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;

        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.e("GPS", "Error verificando estado del GPS", e);
        }

        Log.d("GPS", "GPS enabled: " + isGPSEnabled + ", Network enabled: " + isNetworkEnabled);
        return isGPSEnabled || isNetworkEnabled;
    }
//    private void startCamera() {
//        try {
//            if (isCameraReady) {
//                Log.d("Camera", "Camera already ready");
//                return;
//            }
//
//            Log.d("Camera", "Attempting to start camera");
//            releaseCamera(); // Asegurarse de liberar primero
//
//            camera = Camera.open(0);
//
//            if (camera == null) {
//                Log.e("Camera", "Failed to open camera");
//                Toast.makeText(getContext(), "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            setCameraDisplayOrientation();
//
//            Camera.Parameters params = camera.getParameters();
//
//            // Configurar parámetros
//            List<String> focusModes = params.getSupportedFocusModes();
//            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//            }
//
//            // Configurar tamaño de preview
//            List<Camera.Size> supportedSizes = params.getSupportedPreviewSizes();
//            if (!supportedSizes.isEmpty()) {
//                Camera.Size optimalSize = supportedSizes.get(0);
//                params.setPreviewSize(optimalSize.width, optimalSize.height);
//            }
//
//            camera.setParameters(params);
//
//            // Configurar el preview display
//            SurfaceHolder holder = surfaceView.getHolder();
//            if (holder.getSurface().isValid()) {
//                camera.setPreviewDisplay(holder);
//                camera.startPreview();
//                isCameraReady = true;
//                Log.d("Camera", "Camera started successfully");
//
//                // Pequeño delay antes del autoenfoque
//                new Handler().postDelayed(() -> {
//                    if (camera != null && isCameraReady) {
//                        autoFocus();
//                    }
//                }, 300);
//            } else {
//                Log.d("Camera", "Surface not valid in startCamera");
//            }
//
//        } catch (Exception e) {
//            Log.e("Camera", "Error startCamera", e);
//            Toast.makeText(getContext(), "Error al abrir cámara", Toast.LENGTH_SHORT).show();
//            isCameraReady = false;
//        }
//    }
//    private void releaseCamera() {
//        try {
//            if (camera != null) {
//                camera.stopPreview();
//                camera.release();
//                camera = null;
//            }
//            isCameraReady = false;
//        } catch (Exception e) {
//            Log.e("Camera", "Error releaseCamera", e);
//        }
//    }
//    private void autoFocus() {
//        if (camera != null && isCameraReady) {
//            try {
//                camera.autoFocus(new Camera.AutoFocusCallback() {
//                    @Override
//                    public void onAutoFocus(boolean success, Camera camera) {
//                        // Solo registrar, no hacer nada más
//                        Log.d("Camera", "Autoenfoque: " + (success ? "éxito" : "falló"));
//                    }
//                });
//            } catch (Exception e) {
//                Log.e("Camera", "Error en autoFocus", e);
//            }
//        }
//    }

    // Método para ajustar la orientación de la cámara según la orientación del dispositivo
//    private void setCameraDisplayOrientation() {
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(0, info);
//
//        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//
//        switch (rotation) {
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//
//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;
//        } else { // cámara trasera
//            result = (info.orientation - degrees + 360) % 360;
//        }
//
//        camera.setDisplayOrientation(result);
//    }

    // Método para tomar una foto
//    private void takePictureAndSend() {
//        // Verificar si el GPS está activado
//        if (!isGPSEnabled()) {
//            showGPSDisabledAlert();
//            return;
//        }
//
//        // Verificar si tenemos ubicación válida
//        if (latitud == 0.0 && longitud == 0.0) {
//            Toast.makeText(getContext(), "Obteniendo ubicación, espere por favor...", Toast.LENGTH_SHORT).show();
//            startGPS(); // Reiniciar la obtención de ubicación
//            return;
//        }
//
//        if (!isCameraReady || camera == null || isTakingPicture) {
//            Toast.makeText(getContext(), "Cámara no disponible", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        isTakingPicture = true;
//        try {
//            camera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    // Independientemente de si el autoenfoque tuvo éxito, intentar tomar la foto
//                    try {
//                        camera.takePicture(null, null, new Camera.PictureCallback() {
//                            @Override
//                            public void onPictureTaken(byte[] data, Camera camera) {
//                                isTakingPicture = false;
//                                //processImageData(data, camera);
//                            }
//                        });
//                    } catch (Exception e) {
//                        isTakingPicture = false;
//                        Log.e("Camera", "Error en takePicture", e);
//                     //   restartPreview();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            isTakingPicture = false;
//            Log.e("Camera", "Error en autoFocus", e);
//        //    restartPreview();
//        }
//    }
    private void showGPSDisabledAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("GPS Desactivado");
        builder.setMessage("Para registrar un control de turno es necesario activar el GPS. ¿Desea activarlo ahora?");
        builder.setCancelable(false);

        builder.setPositiveButton("Activar GPS", (dialog, which) -> {
            // Abrir configuración de ubicación
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
    private void processImageData(Bitmap bitmap) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            showConfirmationDialog(encodedImage);

        } catch (Exception e) {
            Log.e("Camera", "Error procesando imagen", e);
          //  restartPreview();
        }
    }

    private void showConfirmationDialog(String encodedImage) {
        // Verificar nuevamente que tenemos ubicación válida
        if (latitud == 0.0 || longitud == 0.0) {
            Toast.makeText(getContext(), "Error: No se pudo obtener la ubicación GPS", Toast.LENGTH_LONG).show();
           // restartPreview();
            return;
        }

        String punto = spinnerPunto.getSelectedItem().toString();
        String evento = spinnerEvento.getSelectedItem().toString();

        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmar Registro")
                .setMessage("¿Confirmar registro con los siguientes datos?\n\n" +
                        "Punto: " + punto + "\n" +
                        "Evento: " + evento + "\n"
                )
                .setCancelable(false)
                .setPositiveButton("Sí", (dialog, which) -> {
                    remitir(encodedImage);
                })
                .setNegativeButton("No", (dialog, which) -> {
                  //  restartPreview();
                })
                .show();
    }
//    private void restartPreview() {
//        try {
//            if (camera != null) {
//                camera.stopPreview();
//                camera.startPreview();
//                isCameraReady = true;
//                // No llamar a autoFocus() inmediatamente aquí
//            }
//        } catch (Exception e) {
//            Log.e("Camera", "Error al reiniciar preview", e);
//            isCameraReady = false;
//            releaseCamera();
//            startCamera(); // Reiniciar completamente la cámara
//        }
//    }
//    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            Log.d("Camera", "Surface created");
//            if (checkPermissions() && !isCameraReady) {
//                startCamera();
//            }
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            Log.d("Camera", "Surface changed: " + width + "x" + height);
//            if (camera != null && isCameraReady) {
//                try {
//                    camera.stopPreview();
//                    setCameraDisplayOrientation();
//                    camera.setPreviewDisplay(holder);
//                    camera.startPreview();
//                    Log.d("Camera", "Preview restarted after surface change");
//                } catch (Exception e) {
//                    Log.e("Camera", "Error en surfaceChanged", e);
//                    restartCamera();
//                }
//            }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            Log.d("Camera", "Surface destroyed");
//            if (camera != null) {
//                try {
//                    camera.stopPreview();
//                    Log.d("Camera", "Preview stopped in surfaceDestroyed");
//                } catch (Exception e) {
//                    Log.e("Camera", "Error stopping preview", e);
//                }
//            }
//            isCameraReady = false;
//        }
//    };
//    private int getCameraRotation() {
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        Camera.getCameraInfo(0, info);
//
//        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//        int degrees = 0;
//
//        switch (rotation) {
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            return (info.orientation + degrees) % 360;
//        } else {
//            return (info.orientation - degrees + 360) % 360;
//        }
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("Camera", "onPause called");
//
//        // Solo detener el preview, no liberar la cámara completamente
//        if (camera != null) {
//            try {
//                camera.stopPreview();
//                Log.d("Camera", "Preview stopped in onPause");
//            } catch (Exception e) {
//                Log.e("Camera", "Error stopping preview in onPause", e);
//            }
//        }
//        isCameraReady = false;
//    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.d("Camera", "onDestroyView called");
//
//        // Liberar completamente la cámara cuando el fragmento se destruye
//        releaseCamera();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("Camera", "onResume called");
//
//        if (checkPermissions()) {
//            // Verificar si la superficie está lista y la cámara no está iniciada
//            if (surfaceView.getHolder().getSurface().isValid() && !isCameraReady) {
//                Log.d("Camera", "Surface is valid, starting camera");
//                startCamera();
//            }
//            // Si la superficie no está lista, surfaceCreated se encargará de iniciar la cámara
//        } else {
//            requestPermissions();
//        }
//    }
//    private void restartCamera() {
//        if (camera != null) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }
//        startCamera();
//    }
    // Enviar la imagen al servidor
    private void remitir(String encodedImage) {
        mostrarLoading(true);
        // Obtener valores seleccionados en los Spinners
        Punto puntoSeleccionado = (Punto) spinnerPunto.getSelectedItem();
        Evento eventoSeleccionado = (Evento) spinnerEvento.getSelectedItem();

        if (puntoSeleccionado == null || eventoSeleccionado == null) {
            Toast.makeText(getContext(), "Seleccione punto y evento", Toast.LENGTH_SHORT).show();
            //restartPreview();
            return;
        }

        // Usar los IDs directamente desde los objetos
        int tipoPuntoFk = puntoSeleccionado.getId();
        int tipoEventoFk = eventoSeleccionado.getId();

        // Obtener fecha y hora actual en formato ISO 8601
        SimpleDateFormat sdf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        }
        String fechaHoraDispositivo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fechaHoraDispositivo = sdf.format(new Date());
        }

        // Obtener ID del dispositivo (puedes usar IMEI, Android ID, o un valor fijo)
        String dispositivoId = getDeviceId();

        // Crear objeto JSON con el formato requerido
        JSONObject jsonBody = new JSONObject();
        try {
            // Datos principales
            jsonBody.put("tipo_evento_fk", tipoEventoFk);
            jsonBody.put("tipo_punto_fk", tipoPuntoFk);
            jsonBody.put("latitud", latitud);
            jsonBody.put("longitud", longitud);
            jsonBody.put("dispositivo_id", dispositivoId);
            jsonBody.put("fecha_hora_dispositivo", fechaHoraDispositivo);

            // Crear el objeto ArchivoAdjunto
            ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
            archivoAdjunto.setDescripcion("");
            archivoAdjunto.setExtension(".jpg");
            archivoAdjunto.setImagenBase64(encodedImage);
            archivoAdjunto.setNombre("control_" + System.currentTimeMillis());  // Establece el nombre del archivo

            // Convertir ArchivoAdjunto en un JSONObject

            Gson gson = new Gson();
            String archivoAdjuntoJsonString = gson.toJson(archivoAdjunto);
            JSONObject archivoAdjuntoJson = new JSONObject(archivoAdjuntoJsonString);

            // Añadir el archivo adjunto al cuerpo de la solicitud
            jsonBody.put("archivo_adjunto", archivoAdjuntoJson);

        } catch (JSONException e) {
            mostrarLoading(false);
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear JSON", Toast.LENGTH_SHORT).show();
            //restartPreview();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        String url = DatosConexion.SERVIDORUNIVIDA + DatosConexion.URL_UNIVIDA_CONTROL_TURNOS_REGISTRAR;

        // Crear la solicitud Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("ServerResponse", "Respuesta completa: " + response.toString());

                            // Extraer los campos de la respuesta
                            boolean exito = response.getBoolean("exito");
                            int codigoRetorno = response.getInt("codigo_retorno");
                            String mensaje = response.getString("mensaje");

                            builder.setMessage(mensaje)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", null);
                            if (exito) {
                                builder.setTitle("Registro exitoso")
                                        .setIcon(android.R.drawable.ic_dialog_info);

                                // Opcional: obtener el ID del registro creado si lo necesitas
                                if (response.has("datos")) {
                                    Object datos = response.get("datos");
                                    if (datos instanceof Integer) {
                                        int idRegistro = (Integer) datos;
                                        Log.d("ServerResponse", "ID del registro: " + idRegistro);
                                    } else if (datos instanceof String) {
                                        String datosStr = (String) datos;
                                        Log.d("ServerResponse", "Datos: " + datosStr);
                                    }
                                }

                            } else {
                                // Error del servidor - mostrar mensaje de error

                                builder.setTitle("Error")
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                Log.e("ServerResponse", mensaje);
                            }
                            AlertDialog alert = builder.create();
                            alert.show();
                            mostrarLoading(false);

                        } catch (JSONException e) {
                            mostrarLoading(false);
                            Log.e("ServerResponse", "Error parsing JSON response", e);
                            Toast.makeText(getContext(), "Error al procesar respuesta del servidor", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            mostrarLoading(false);
                            Log.e("ServerResponse", "Error inesperado en respuesta", e);
                            Toast.makeText(getContext(), "Error inesperado", Toast.LENGTH_SHORT).show();
                        } finally {
                        //    restartPreview();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarLoading(false);
                        Log.e("ServerError", "Error: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("ServerError", "Código: " + error.networkResponse.statusCode);
                            Log.e("ServerError", "Datos: " + new String(error.networkResponse.data));
                        }
                        Toast.makeText(getContext(), "Error al enviar registro", Toast.LENGTH_SHORT).show();
                      //  restartPreview();
                    }
                }
        ) {
            /*@Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");

                return headers;
            }*/
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
        jsonObjectRequest.setShouldCache(false);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(ConfigEmizor.VOLLEY_TIME_MLS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag("registrar");
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    private String getDeviceId() {
        try {
            // Intentar obtener Android ID
            String androidId = Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );

            if (androidId != null && !androidId.isEmpty()) {
                return "ANDROID-" + androidId;
            }

            // Si no se puede obtener Android ID, usar un ID aleatorio
            return "DISP-" + System.currentTimeMillis();

        } catch (Exception e) {
            return "DISP-" + System.currentTimeMillis();
        }
    }
    private void mostrarLoading(boolean mostrar) {
        if (getView() != null) {
            ProgressBar progressBar = getView().findViewById(R.id.progressBar2);
            if (progressBar != null) {
                progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
            }

            // Opcional: deshabilitar botones mientras carga
            btnRegistrar.setEnabled(!mostrar);
            spinnerPunto.setEnabled(!mostrar);
            spinnerEvento.setEnabled(!mostrar);
        }
    }
}