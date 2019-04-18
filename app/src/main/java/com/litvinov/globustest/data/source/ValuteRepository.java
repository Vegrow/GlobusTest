package com.litvinov.globustest.data.source;

import android.util.Log;

import com.litvinov.globustest.data.ValCurs;
import com.litvinov.globustest.data.Valute;

import java.util.ArrayList;

public class ValuteRepository implements ValuteDataSource {

    private static final String TAG = ValuteRepository.class.getSimpleName();
    private static ValuteRepository instance;
    private ValuteDataSource localDataSource;
    private ValuteDataSource remoteDataSource;
    private ValCurs cachedValutes;

    private ValuteRepository(ValuteDataSource localDataSource, ValuteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static ValuteRepository getInstance(ValuteDataSource localDataSource, ValuteDataSource remoteDataSource) {
        ValuteRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (ValuteRepository.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ValuteRepository(localDataSource, remoteDataSource);
                }
            }
        }
        return localInstance;
    }

    @Override
    public void getValuteCurses(final LoadValuteCallBack callBack) {
        if (cachedValutes != null){
            callBack.onValuteLoaded(cachedValutes);

        } else {
            this.localDataSource.getValuteCurses(new LoadValuteCallBack() {
                @Override
                public void onValuteLoaded(ValCurs valCurs) {
                    Log.d(TAG, "onValuteLoaded: ");
                    updateCache(valCurs);
                    callBack.onValuteLoaded(cachedValutes);;
                }

                @Override
                public void onError() {
                    Log.d(TAG, "onError: ");
                    callBack.onError();
                }
            });
            this.remoteDataSource.getValuteCurses(new LoadValuteCallBack() {
                @Override
                public void onValuteLoaded(ValCurs valCurs) {
                    Log.d(TAG, "onValuteLoaded: ");
                    updateCache(valCurs);
                    updateLocalDataSource(valCurs);
                    callBack.onValuteLoaded(cachedValutes);
                }

                @Override
                public void onError() {
                    Log.d(TAG, "onError: ");
                    callBack.onError();
                }
            });
        }


    }

    @Override
    public void saveValuteCurses(ValCurs valCurs) {
        this.localDataSource.saveValuteCurses(valCurs);
    }

    private void updateCache(ValCurs valCurs){
        if (cachedValutes == null){
            cachedValutes = new ValCurs();
            cachedValutes.setValutes(new ArrayList<Valute>());
        }
        cachedValutes.getValutes().clear();
        Valute rusValute = new Valute();
        rusValute.setCharCode("RUB");
        rusValute.setNominal(1);
        rusValute.setValue("1");
        rusValute.setNumCode(643);
        rusValute.setName("российские рубли");
        cachedValutes.getValutes().add(rusValute);
        for (Valute valute : valCurs.getValutes()){
            cachedValutes.getValutes().add(valute);
        }
    }

    private void updateLocalDataSource(ValCurs valCurs){
        this.localDataSource.saveValuteCurses(valCurs);
    }

    public static void destroyInstance() {
        instance = null;
    }
}
