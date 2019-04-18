package com.litvinov.globustest.ui.main;

import android.util.Log;

import com.litvinov.globustest.data.ValCurs;
import com.litvinov.globustest.data.Valute;
import com.litvinov.globustest.data.source.ValuteDataSource;
import com.litvinov.globustest.data.source.ValuteRepository;
import com.litvinov.globustest.ui.base.BasePresenter;
import com.litvinov.globustest.util.ValuteHelper;

import java.math.BigDecimal;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private ValuteRepository valuteRepository;

    public MainPresenter(ValuteRepository valuteRepository) {
        this.valuteRepository = valuteRepository;
    }

    @Override
    public void loadExchangeData() {
        valuteRepository.getValuteCurses(new ValuteDataSource.LoadValuteCallBack() {
            @Override
            public void onValuteLoaded(ValCurs valCurs) {
                Log.d(TAG, "onValuteLoaded: " + valCurs.getValutes().size());
                if (getView() != null) {
                    getView().setUpCurrencyCharCodes(valCurs.getValutes());
                }
            }

            @Override
            public void onError() {
                Log.d(TAG, "onError: ");
            }
        });
    }

    @Override
    public void doExchange(String amount, Valute from, Valute to) {
        if (amount.length() > 0) {
            BigDecimal toAmount = ValuteHelper.exchange(new BigDecimal(amount), from, to);
            if (getView() != null) {
                getView().setExchangedAmount(toAmount.toString());
            }
        }
        else {
            if (getView() != null) getView().showToast("Не заданно количество валюты для обмена");
        }
    }
}
