package com.shadowcoder.courtneyscorner.activity;

import android.content.Intent;
import android.os.Bundle;

import com.shadowcoder.courtneyscorner.R;
import com.shadowcoder.courtneyscorner.activity.crossword.CrosswordActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @OnClick(R.id.generator_button)
    void onGeneratorButtonClick() {
        Intent intent = new Intent(this, GeneratorActivity.class);
        this.startActivity(intent);
    }

    @OnClick(R.id.crossword_button)
    void onCrosswordButtonClick() {
        Intent intent = new Intent(this, CrosswordActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
