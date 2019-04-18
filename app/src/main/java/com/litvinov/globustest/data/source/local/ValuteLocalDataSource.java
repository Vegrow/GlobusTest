package com.litvinov.globustest.data.source.local;

import android.util.Log;

import com.litvinov.globustest.data.ValCurs;
import com.litvinov.globustest.data.source.ValuteDataSource;
import com.litvinov.globustest.util.AppExecutors;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

public class ValuteLocalDataSource implements ValuteDataSource {

    private static final String TAG = ValuteLocalDataSource.class.getSimpleName();
    private static ValuteLocalDataSource instance;
    private File storageFile;
    private AppExecutors appExecutors;
    private Serializer serializer = new Persister();

    private ValuteLocalDataSource (AppExecutors appExecutors, File file){
        this.storageFile = file;
        Log.d("sss", "ValuteLocalDataSource: " + storageFile.getAbsolutePath() );
        this.appExecutors = appExecutors;
    }

    public static ValuteLocalDataSource getInstance(AppExecutors appExecutors, File file) {
        ValuteLocalDataSource localInstance = instance;
        if (localInstance == null) {
            synchronized (ValuteLocalDataSource.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ValuteLocalDataSource(appExecutors, file);
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
                final ValCurs valCurs = loadValutesFromFile();
                appExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (valCurs != null) callBack.onValuteLoaded(valCurs);
                        else callBack.onError();
                    }
                });
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void saveValuteCurses(final ValCurs valCurs) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /*try {
                    String s = "<ValCurs Date=\"18.04.2019\" name=\"Foreign Currency Market\">\n" +
                            "<Valute ID=\"R01010\">\n" +
                            "<NumCode>036</NumCode>\n" +
                            "<CharCode>AUD</CharCode>\n" +
                            "<Nominal>1</Nominal>\n" +
                            "<Name>Австралийский доллар</Name>\n" +
                            "<Value>46,0724</Value>\n" +
                            "</Valute></ValCurs>";

                    try (FileOutputStream stream = new FileOutputStream(storageFile)){
                        stream.write(s.getBytes());
                    }

                    //serializer.write(valCurs, storageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                try {
                    serializer.write(valCurs, storageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        appExecutors.getDiskIO().execute(runnable);
    }

    private ValCurs loadValutesFromFile(){
        ValCurs valCurs = null;
        try {
            valCurs = serializer.read(ValCurs.class, storageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valCurs;
    }
}


