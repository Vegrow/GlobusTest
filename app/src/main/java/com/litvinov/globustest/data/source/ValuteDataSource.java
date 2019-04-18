package com.litvinov.globustest.data.source;

import com.litvinov.globustest.data.ValCurs;

public interface ValuteDataSource {

    void getValuteCurses(LoadValuteCallBack callBack);
    void saveValuteCurses(ValCurs valCurs);

    interface LoadValuteCallBack{
        void onValuteLoaded(ValCurs valCurs);
        void onError();
    }

}
