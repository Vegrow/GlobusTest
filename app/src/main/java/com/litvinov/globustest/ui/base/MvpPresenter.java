package com.litvinov.globustest.ui.base;

public interface MvpPresenter<V extends BaseView> {
       boolean isAttached();
       void attach(V view);
       void detach();
       V getView();
}
