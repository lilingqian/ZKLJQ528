package com.example.lenovo.zk_ljq_528.ui;


public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        if (view != null) {
            this.mView = view;
        }
    }

    @Override
    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}