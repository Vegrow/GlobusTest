package com.litvinov.globustest.data.source.remote;

import com.litvinov.globustest.data.ValCurs;
import com.litvinov.globustest.data.source.ValuteDataSource;
import com.litvinov.globustest.util.AppExecutors;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ValuteRemoteDataSource implements ValuteDataSource {

    private static final String TAG = ValuteRemoteDataSource.class.getSimpleName();
    private static ValuteRemoteDataSource instance;
    private Serializer serializer = new Persister();
    private  URL url;
    private AppExecutors appExecutors;

    private ValuteRemoteDataSource(AppExecutors appExecutors, String path) {
        try {
            this.url = new URL(path);
            this.appExecutors = appExecutors;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static ValuteRemoteDataSource getInstance(AppExecutors appExecutors, String path) {
        ValuteRemoteDataSource localInstance = instance;
        if (localInstance == null) {
            synchronized (ValuteRemoteDataSource.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ValuteRemoteDataSource(appExecutors, path);
                }
            }
        }
        return localInstance;
    }

    @Override
    public void getValuteCurses(final LoadValuteCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ValCurs valCurs = loadValutaFromUrl();
                appExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (valCurs != null) callBack.onValuteLoaded(valCurs);
                        else callBack.onError();
                    }
                });

            }
        };

        appExecutors.getNetworkIO().execute(runnable);
    }

    @Override
    public void saveValuteCurses(ValCurs valCurs) {

    }

    private ValCurs loadValutaFromUrl(){
        ValCurs valCurs = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return valCurs;
            }
            try(InputStream is = urlConnection.getInputStream()) {
                valCurs = serializer.read(ValCurs.class, is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return valCurs;
    }
}
