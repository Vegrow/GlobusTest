package com.litvinov.globustest.util;

import android.content.Context;

import com.litvinov.globustest.data.source.ValuteRepository;
import com.litvinov.globustest.data.source.local.ValuteLocalDataSource;
import com.litvinov.globustest.data.source.remote.ValuteRemoteDataSource;

import java.io.File;

import androidx.annotation.NonNull;

public class Injection {

   public static ValuteRepository provideValuteRepository(@NonNull Context context) {

       AppExecutors appExecutors = AppExecutors.getInstance();
       File file = new File(context.getFilesDir(), "valutes.txt");
       final String url = "http://www.cbr.ru/scripts/XML_daily.asp";
       return ValuteRepository.getInstance(ValuteLocalDataSource.getInstance(appExecutors, file),
                ValuteRemoteDataSource.getInstance(appExecutors, url));
    }
}
