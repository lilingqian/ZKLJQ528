package com.example.lenovo.zk_ljq_528.ui;

public interface BaseContract {

    interface BasePresenter<T extends BaseView> {
        void attachView(T view);

        void detachView();

    }

    interface BaseView {
        void showLoading();
    }
}
