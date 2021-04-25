package br.com.renatoramos.buscadecep;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebService {

    public static String comunicacao(String urlEnd, String... param) {

        StringBuilder sb = new StringBuilder();

        URL url;
        HttpURLConnection conn = null;

        String urlString = param[0];

        try {
            url = new URL(urlEnd);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            StringBuilder parametrosFormatados = new StringBuilder();
            parametrosFormatados.append(URLEncoder.encode("json", "UTF-8"));
            parametrosFormatados.append("=");
            parametrosFormatados.append(URLEncoder.encode(urlString, "UTF-8"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(parametrosFormatados.toString());
            writer.flush();
            writer.close();

            sb.append(GDownload(urlEnd));

        } catch (Exception e) {
            sb.append("Error: " + e.toString());
        } finally {

        }

        return sb.toString();

    }

    public static String GDownload(String urlServer) throws Exception {
        URL url;
        url = new URL(urlServer);

        URLConnection connection = url.openConnection();

        InputStream inputStream = new BufferedInputStream(connection.getInputStream());

        byte[] buffer = new byte[1024];
        int n;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while ((n = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, n);
        }

        inputStream.close();

        return new String(baos.toByteArray());
    }
}
