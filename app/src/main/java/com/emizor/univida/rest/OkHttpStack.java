package com.emizor.univida.rest;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;

/**
 * Created by posemizor on 07-12-17.
 */

public class OkHttpStack extends HurlStack {
    private final OkUrlFactory mFactory;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        super();
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        mFactory = new OkUrlFactory(client);

    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection http = mFactory.open(url);
        http.setInstanceFollowRedirects(true);
        http.setUseCaches(false);
        http.setDefaultUseCaches(false);
        return http;
    }
}
