package com.shadowcoder.courtneyscorner.activity.crossword;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.shadowcoder.courtneyscorner.R;
import com.shadowcoder.courtneyscorner.activity.BaseActivity;
import com.shadowcoder.courtneyscorner.activity.crossword.adapter.CrosswordAdapter;
import com.shadowcoder.courtneyscorner.activity.crossword.adapter.DataAdapter;
import com.shadowcoder.courtneyscorner.activity.crossword.mapper.Focus;
import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordLayout;
import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.MutableCoordinate;
import com.shadowcoder.courtneyscorner.data.WordData;
import com.shadowcoder.courtneyscorner.generator.Result;
import com.shadowcoder.courtneyscorner.generator.crossword.CrosswordGenerator;
import com.shadowcoder.courtneyscorner.generator.service.Manager;
import com.shadowcoder.courtneyscorner.view.Header;
import com.shadowcoder.courtneyscorner.view.Keyboard;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CrosswordActivity extends BaseActivity {

    @BindView(R.id.gridview)
    CrosswordLayout gridView;

    @BindView(R.id.header)
    Header header;

    @BindView(R.id.keyboard)
    Keyboard keyboard;

    private Focus focus = new Focus();
    private ProgressDialog progressDialog;

    private CrosswordAdapter viewAdapter;
    private DataAdapter dataAdapter = new DataAdapter(this.focus);

    private EventListener eventListener = new EventListener();

    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_crossword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewAdapter = new CrosswordAdapter();
        this.viewAdapter.setOnItemClickListener(new MyItemClickListener());

        this.gridView.setOnViewLoadListener(new MyLoadListener());
        this.gridView.setAdapter(this.viewAdapter);

        this.header.setOnHeaderTouchListener(new MyHeaderTouchListener());
        this.keyboard.setOnKeyboardClickListener(new MyKeyboardClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.viewAdapter != null) {
            this.viewAdapter.registerViews();
        }

        this.eventBus.register(this.eventListener);

        if (!this.dataAdapter.hasData()) {
            this.loadData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.viewAdapter != null) {
            this.viewAdapter.unregisterViews();
        }

        this.eventBus.unregister(this.eventListener);
    }

    private void loadData() {
        Manager.getSingleton().schedule(new CrosswordGenerator());

        if (this.progressDialog != null) {
            return;
        }

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Creating Crossword...");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    private void setData(CrosswordData data) {
        // this has to be first or the focus won't work right
        this.dataAdapter.setCrosswordData(data);
        this.viewAdapter.setCrosswordData(data);

        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private class MyItemClickListener implements CrosswordAdapter.OnItemClickListener {
        @Override
        public void onClick(@NonNull Coordinate coordinate) {
            dataAdapter.move(coordinate);
        }
    }

    private class MyHeaderTouchListener implements Header.OnHeaderTouchListener {
        @Override
        public void onLeft() {
            dataAdapter.previous();
        }

        @Override
        public void onRight() {
            dataAdapter.next();
        }

        @Override
        public void onFlipDirection() {
            dataAdapter.flipDirection();
        }
    }

    private class MyKeyboardClickListener implements Keyboard.OnKeyboardClickListener {
        @Override
        public void onClick(@NonNull String text) {
            dataAdapter.add(text);
        }

        @Override
        public void onBackspace() {
            dataAdapter.delete();
        }
    }

    private class MyLoadListener implements CrosswordLayout.OnViewLoadListener {
        @Override
        public void onStart() {
            focus.lock();
        }

        @Override
        public void onFinish() {
            focus.unlock();
        }
    }

    public class EventListener {

        @Subscribe
        @SuppressWarnings("unused")
        public void handleDataCompiled(@NonNull Result<CrosswordData> result) {
            if (result.successful) {
                setData(result.result);
            }
            else {
                Toast.makeText(CrosswordActivity.this, "Error while creating the crossword", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private CrosswordData buildData() {
        DataBuilder builder = new DataBuilder()
                .addHorizontalData("abba")
                .addHorizontalData("nab")
                .addHorizontalData("opah")
                .newHorizontalLine()
                .addHorizontalData("loam")
                .addHorizontalData("err")
                .addHorizontalData("role")
                .newHorizontalLine()
                .addHorizontalData("politico")
                .addHorizontalData("glow")
                .newHorizontalLine()
                .addHorizontalData("spigot")
                .addHorizontalData("awaken")
                .newHorizontalLine()
                .setCoordinateCacheX(3)
                .addHorizontalData("ooh")
                .addHorizontalData("dana")
                .newHorizontalLine()
                .addHorizontalData("asp")
                .addHorizontalData("leo")
                .addHorizontalData("rider")
                .newHorizontalLine()
                .addHorizontalData("boom")
                .addHorizontalData("rap")
                .addHorizontalData("coke")
                .newHorizontalLine()
                .addHorizontalData("allay")
                .addHorizontalData("rag")
                .addHorizontalData("ted")
                .newHorizontalLine()
                .setCoordinateCacheX(3)
                .addHorizontalData("ages")
                .addHorizontalData("tap")
                .newHorizontalLine()
                .addHorizontalData("serene")
                .addHorizontalData("eraser")
                .newHorizontalLine()
                .addHorizontalData("axon")
                .addHorizontalData("polygamy")
                .newHorizontalLine()
                .addHorizontalData("spit")
                .addHorizontalData("ill")
                .addHorizontalData("ague")
                .newHorizontalLine()
                .addHorizontalData("soda")
                .addHorizontalData("ada")
                .addHorizontalData("ness");

        builder.resetCoordinateCache();

        builder = builder
                .addVerticalData("alps")
                .newVerticalLine()
                .addVerticalData("boops")
                .newVerticalLine()
                .addVerticalData("bali")
                .newVerticalLine()
                .addVerticalData("amigo");

        return builder.build();
    }

    private static class DataBuilder {
        private MutableCoordinate coordinateCache = new MutableCoordinate();
        private List<WordData> horizontalData = new ArrayList<>();
        private List<WordData> verticalData = new ArrayList<>();

        DataBuilder addHorizontalData(@NonNull String word) {
            WordData data = WordData.horizontalData(word, this.coordinateCache.copy());
            this.coordinateCache.set(data.getEndPosition());
            this.coordinateCache.x += 2;

            this.horizontalData.add(data);
            return this;
        }

        DataBuilder addVerticalData(@NonNull String word) {
            WordData data = WordData.verticalData(word, this.coordinateCache.copy());
            this.coordinateCache.set(data.getEndPosition());
            this.coordinateCache.y += 2;

            this.verticalData.add(data);
            return this;
        }

        DataBuilder newHorizontalLine() {
            this.coordinateCache.x = 0;
            this.coordinateCache.y += 1;
            return this;
        }

        DataBuilder newVerticalLine() {
            this.coordinateCache.x += 1;
            this.coordinateCache.y = 0;
            return this;
        }

        DataBuilder setCoordinateCacheX(int x) {
            this.coordinateCache.x = x;
            return this;
        }

        DataBuilder setCoordinateCacheY(int y) {
            this.coordinateCache.y = y;
            return this;
        }

        void resetCoordinateCache() {
            this.coordinateCache.x = 0;
            this.coordinateCache.y = 0;
        }

        DataBuilder setCoordinateCache(int x, int y) {
            this.coordinateCache.x = x;
            this.coordinateCache.y = y;
            return this;
        }

        CrosswordData build() {
            return new CrosswordData(this.horizontalData, this.verticalData);
        }
    }
}
