package com.example.lenovo.zk_ljq_528.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.lenovo.zk_ljq_528.inter.IBase;

import javax.inject.Inject;

public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends AppCompatActivity implements IBase,
        BaseContract.BaseView {
    @Inject
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        inject();
        //绑定
        mPresenter.attachView(this);

    }

    @Override
    public void initView(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {

    }


}