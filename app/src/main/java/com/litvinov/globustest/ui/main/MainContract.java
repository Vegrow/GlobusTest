package com.litvinov.globustest.ui.main;

import com.litvinov.globustest.data.Valute;
import com.litvinov.globustest.ui.base.BaseView;
import com.litvinov.globustest.ui.base.MvpPresenter;

import java.util.List;

public interface MainContract {
    interface View extends BaseView{
        void setUpCurrencyCharCodes(List<Valute> valutes);
        void setExchangedAmount(String amount);
        void showToast(String text);
    }
    interface Presenter extends MvpPresenter<View>{
        void loadExchangeData();
        void doExchange(String fromAmount, Valute from, Valute to);
    }
}
