package com.litvinov.globustest.ui.base;

public abstract class BasePresenter<V extends BaseView> implements MvpPresenter<V> {

    private V view;

    @Override
    public boolean isAttached() {
        return view != null;
    }

    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public V getView() {
        return view;
    }
}
