package com.shadowcoder.courtneyscorner.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.shadowcoder.courtneyscorner.Utils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    protected Unbinder unbinder;
    protected abstract @LayoutRes Integer getLayoutId();

    protected final Utils utils = new Utils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Integer layoutId = getLayoutId();
        if (layoutId != null) {
            this.setContentView(layoutId);
        }

        this.unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (this.unbinder != null) {
            this.unbinder.unbind();
            this.unbinder = null;
        }

        super.onDestroy();
    }
}
