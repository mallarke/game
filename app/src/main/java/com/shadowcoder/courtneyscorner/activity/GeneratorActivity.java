package com.shadowcoder.courtneyscorner.activity;

import android.support.annotation.NonNull;

import com.shadowcoder.courtneyscorner.R;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.generator.Result;
import com.shadowcoder.courtneyscorner.generator.crossword.CrosswordGenerator;
import com.shadowcoder.courtneyscorner.generator.service.Manager;
import com.squareup.otto.Subscribe;

import butterknife.OnClick;

public class GeneratorActivity extends BaseActivity {

    @OnClick(R.id.generator_start)
    void onStartClick() {
        Manager.getSingleton().schedule(new CrosswordGenerator());
    }

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_generator;
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.eventBus.unregister(this);
    }

    @Subscribe
    public void onPost(@NonNull Result<CrosswordData> result) {
        int bob = 0;
        bob = 0;
    }
}
