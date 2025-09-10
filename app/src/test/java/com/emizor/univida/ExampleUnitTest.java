package com.emizor.univida;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.emizor.univida.rest.DatosConexion;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /*@Test
    public void testApiCallReal() throws InterruptedException {
        // Arrange
        final CountDownLatch latch = new CountDownLatch(1);
        final Object[] resultHolder = new Object[1];
        final String[] errorHolder = new String[1];

        // Act - Ejecutar tu código exactamente como está
        ApiClient.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImxjaGF2ZXoiLCJ0b2tlbiI6Ijc4OTg2NDEyOCIsIm5iZiI6MTc1NzcxMTMwMiwiZXhwIjoxNzU3Nzk3NzAyLCJpYXQiOjE3NTc3MTEzMDJ9.pmVwTf_C9ujpKOev5NWRRv1N4oR0oinUFB5k9eGOEH4");//solo para pruebas
        ApiService api = ApiClient.getClient().create(ApiService.class);


        Map<String, String> params = new HashMap<>();
        params.put("limit", "10");
        params.put("page", "1");
        System.out.println("Preparando la llamada GET");

        Call<Object> getCall = api.genericGet(DatosConexion.URL_UNIVIDA_CONTROL_TURNOS_TIPO_PUNTO, params);

        ApiHelper.execute(getCall, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(Object result) {
                resultHolder[0] = result;
                System.out.println("Éxito en la respuesta: " + result.toString());
                latch.countDown();
            }

            @Override
            public void onError(String error) {
                errorHolder[0] = error;
                System.out.println("Error en la respuesta: " + error);
                latch.countDown();
            }
        });

        // Esperar máximo 30 segundos por la respuesta
        boolean requestCompleted = latch.await(30, TimeUnit.SECONDS);

        // Assert
        assertTrue("La llamada debería completarse en 30 segundos", requestCompleted);

        if (errorHolder[0] != null) {
            // Si hay error, podrías querer que falle el test o solo loggear
            System.out.println("Test completado con error: " + errorHolder[0]);
            // fail("La llamada a la API falló: " + errorHolder[0]); // Descomenta si quieres que falle
        } else {
            assertNotNull("El resultado no debe ser nulo", resultHolder[0]);
            System.out.println("Test completado exitosamente");
        }
    }*/
}