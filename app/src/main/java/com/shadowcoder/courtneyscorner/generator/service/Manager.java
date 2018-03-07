package com.shadowcoder.courtneyscorner.generator.service;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.shadowcoder.courtneyscorner.generator.Error;
import com.shadowcoder.courtneyscorner.generator.Generator;
import com.shadowcoder.courtneyscorner.generator.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller service for scheduling and executing any {@link Generator} objects
 */
public class Manager {

    private static final int MAX_IN_PROGESS = 3;

    private static final Manager singleton = new Manager();

    private List<Generator> queue = new ArrayList<>();
    private List<Generator> inProgress = new ArrayList<>();

    /*
     * CONSTRUCTOR METHODS
     */

    /**
     * A static instance of the {@link Manager}
     */
    public static Manager getSingleton() {
        return singleton;
    }

    private Manager() {

    }

    /*
     * PUBLIC METHODS
     */

    /**
     * Schedule a {@link Generator} to be executed.
     *
     * @param generator a {@link Generator} object to create usable data for our games
     */
    public <DATA> void schedule(@NonNull Generator<DATA> generator) {
        if (this.inProgress.size() < MAX_IN_PROGESS) {
            this.inProgress.add(generator);
            new Task<>(generator, new TaskCallback<DATA>()).execute();
        }
        else {
            this.queue.add(generator);
        }
    }

    /*
     * PRIVATE METHODS
     */

    // queue handling

    private void flushQueue() {
        if (this.queue.size() > 0 && this.inProgress.size() < MAX_IN_PROGESS) {
            Generator generator = this.queue.get(0);
            this.queue.remove(0);

            schedule(generator);
        }
    }

    private static class Task<DATA> extends AsyncTask<Void, Void, Result<DATA>> {

        private final Generator<DATA> generator;
        private final TaskCallback<DATA> callback;

        Task(Generator<DATA> generator, TaskCallback<DATA> callback) {
            this.generator = generator;
            this.callback = callback;
        }

        @Override
        protected Result<DATA> doInBackground(Void... voids) {
            try {
                DATA data = this.generator.execute();
                return new Result<>(data);
            }
            catch (Error e) {
                return new Result<>(e);
            }
        }

        @Override
        protected void onPostExecute(Result<DATA> result) {
            super.onPostExecute(result);

            this.callback.handle(this.generator, result);
        }
    }

    private class TaskCallback<DATA> {

        private void handle(Generator<DATA> generator, Result<DATA> result) {
            generator.onPost(result);

            inProgress.remove(generator);
            flushQueue();
        }
    }
}
