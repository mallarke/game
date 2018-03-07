package com.shadowcoder.courtneyscorner.generator.crossword;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.shadowcoder.courtneyscorner.Utils;
import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordLayout;
import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.Direction;
import com.shadowcoder.courtneyscorner.data.WordData;
import com.shadowcoder.courtneyscorner.generator.Error;
import com.shadowcoder.courtneyscorner.generator.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CrosswordGenerator extends Generator<CrosswordData> {

    private static final List<String> TEST_STRINGS = new ArrayList<>();

    static {
        String[] test_values = new String[] {
                "bob", "turtle", "bullet", "skippy", "valentine", "lily", "max", "molly", "tasha",
                "row", "length", "moon", "name", "hold", "shame", "wrong", "fucking", "anybody", "tears",
                "for", "fall", "crash", "activity", "guitar", "ohh", "event", "bus", "icon", "for", "hire",
                "onetrick", "volume", "bones", "choking", "darthvader", "dildo", "lube", "nipples", "vagina",
                "penis", "luke", "skywalker", "falcon", "hawk", "albatross", "connect", "mudvayne", "listen",
                "falling", "not", "end", "start", "middle", "center", "left", "right", "top", "bottom", "up",
                "down", "circle", "square", "triangle", "angel", "demon", "puppy", "kitty", "dog", "cat", "over",
                "under", "need", "advisory", "crossword", "coordinate", "cache", "notfalling", "collection", "rhombus",
                "australia", "germany", "shadow"
        };

        TEST_STRINGS.addAll(Arrays.asList(test_values));

        List<Coordinate> c1 = new ArrayList<>();
        c1.add(new Coordinate(1, 0));
        c1.add(new Coordinate(4, 0));
        c1.add(new Coordinate(6, 0));

        c1.add(new Coordinate(2, 3));
        c1.add(new Coordinate(11, 3));

        c1.add(new Coordinate(2, 4));
        c1.add(new Coordinate(8, 4));
        c1.add(new Coordinate(11, 4));

        c1.add(new Coordinate(0, 5));
        c1.add(new Coordinate(3, 5));
        c1.add(new Coordinate(4, 5));
        c1.add(new Coordinate(5, 5));
        c1.add(new Coordinate(6, 5));
        c1.add(new Coordinate(7, 5));
        c1.add(new Coordinate(8, 5));
        c1.add(new Coordinate(9, 5));

        c1.add(new Coordinate(2, 6));
        c1.add(new Coordinate(8, 6));
        c1.add(new Coordinate(9, 6));
        c1.add(new Coordinate(10, 6));

        c1.add(new Coordinate(3, 7));
        c1.add(new Coordinate(8, 7));

        c1.add(new Coordinate(1, 8));
        c1.add(new Coordinate(8, 8));
        c1.add(new Coordinate(10, 8));
        c1.add(new Coordinate(12, 8));

        c1.add(new Coordinate(2, 10));
        c1.add(new Coordinate(3, 10));
        c1.add(new Coordinate(10, 10));
        c1.add(new Coordinate(11, 10));

//        BOARD_CONFIGURATIONS.add(c1);
    }

    private static final int SIZE = CrosswordLayout.CROSSWORD_LENGTH;

    @Override
    public CrosswordData execute() throws Error {
        Result result = this.convert(TEST_STRINGS);
        SparseArray<List<Integer>> lookup = this.buildLookup(result);
        List<BoardConfiguration> configurations = Utils.readBoardConfigs();

        CrosswordData data = null;

        for (int max_try = 0; max_try < 20; max_try++) {
            data = this.generate(result, lookup, configurations);
            if (data != null) {
                break;
            }
        }

        return (data != null && data.hasData() ? data : null);
    }

    @Nullable
    private CrosswordData generate(Result result, SparseArray<List<Integer>> numberLookup, List<BoardConfiguration> configurations) {
        Location[][] board = new Location[SIZE][SIZE];
        BoardConfiguration configuration = this.saltBoard(board, configurations);

        for (int maxTries = 0; maxTries < 10000; maxTries++) {

        }

        return this.compile(board);
    }

    @NonNull
    private BoardConfiguration saltBoard(Location[][] board, List<BoardConfiguration> configurations) {
        Random random = new Random();

        int saltIndex = random.nextInt(configurations.size());
        BoardConfiguration configuration = configurations.get(saltIndex);

        for (int x = 0, y = 0; x < SIZE; x += (y == SIZE - 1 ? 1 : 0), y = (y == SIZE - 1 ? 0 : y + 1)) {
            Coordinate coordinate = new Coordinate(x, y);
            if (configuration.blanks.contains(coordinate)) {
                continue;
            }

            board[x][y] = new Location();
            board[x][y].set((int) 'a');
        }

        return configuration;
    }

    private Result convert(List<String> strings) {
        SparseArray<Character> lookup = new SparseArray<>(26);
        int[][] values = new int[strings.size()][];

        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            List<Integer> items = new ArrayList<>();

            for (int j = 0; j < string.length(); j++) {
                char letter = string.charAt(j);
                int value = (int) letter;

                items.add(value);
                lookup.put(value, letter);
            }

            values[i] = Utils.convert(items);
        }

        return new Result(values, lookup);
    }

    private SparseArray<List<Integer>> buildLookup(Result result) {
        SparseArray<List<Integer>> numberLookup = new SparseArray<>();
        for (int i = 0; i < result.values.length; i++) {
            int[] values = result.values[i];

            for (int value : values) {
                List<Integer> set = numberLookup.get(value);
                if (set == null) {
                    set = new ArrayList<>();
                    numberLookup.put(value, set);
                }

                if (!set.contains(i)) {
                    set.add(i);
                }
            }
        }

        return numberLookup;
    }

    private boolean fits(Location[][] board, int[] word, Direction direction, Coordinate startingPoint) {
        if (direction == Direction.HORIZONTAL) {
            for (int i = 0, x = startingPoint.x, y = startingPoint.y; i < word.length; i++, x++) {
                Location location = board[x][y];

                if (location == null || !location.compare(word[i])) {
                    return false;
                }
            }
        }
        else {
            for (int i = 0, x = startingPoint.x, y = startingPoint.y; i < word.length; i++, y++) {
                Location location = board[x][y];

                if (location == null || !location.compare(word[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    private void update(Location[][] board, int[] word, Direction direction, Coordinate startingPoint) {
        if (direction == Direction.HORIZONTAL) {
            for (int i = 0, x = startingPoint.x, y = startingPoint.y; i < word.length; i++, x++) {
                Location location = board[x][y];

                if (location == null) {
                    throw new IllegalStateException("unsynced board.  call #fits first");
                }

                location.set(word[i]);
            }
        }
        else {
            for (int i = 0, x = startingPoint.x, y = startingPoint.y; i < word.length; i++, y++) {
                Location location = board[x][y];

                if (location == null) {
                    throw new IllegalStateException("unsynced board.  call #fits first");
                }

                location.set(word[i]);
            }
        }
    }

    @Nullable
    private CrosswordData compile(Location[][] board) {
        if (!this.isBoardCompleted(board)) {
            return null;
        }

        List<WordData> horizontalData = this.getHorizontalData(board);
        List<WordData> verticalData = this.getVerticalData(board);

        return new CrosswordData(horizontalData, verticalData);
    }

    private boolean isBoardCompleted(Location[][] board) {
        for (int x = 0, y = 0; x < SIZE; x += (y == SIZE - 1 ? 1 : 0), y = (y == SIZE - 1 ? 0 : y + 1)) {
            Location location = board[x][y];
            if (location != null && location.letter == null) {
                return false;
            }
        }

        return true;
    }

    private List<WordData> getHorizontalData(Location[][] board) {
        List<WordData> horizontalData = new ArrayList<>();
        Compiler compiler = new Compiler();

        for (int x = 0, y = 0; y < SIZE; y += (x == SIZE - 1 ? 1 : 0), x = (x == SIZE - 1 ? 0 : x + 1)) {
            Location location = board[x][y];
            if (location != null) {
                if (!compiler.running) {
                    compiler.start(new Coordinate(x, y));
                }

                compiler.add(location.letter);
            }

            if (location == null || x == SIZE - 1) {
                compiler.stop();
            }

            if ((!compiler.running || y == SIZE - 1) && compiler.hasText()) {
                WordData data = WordData.horizontalData(compiler.text, compiler.startPosition);
                horizontalData.add(data);

                compiler.reset();
            }
        }

        return horizontalData;
    }

    private List<WordData> getVerticalData(Location[][] board) {
        List<WordData> verticalData = new ArrayList<>();
        Compiler compiler = new Compiler();

        for (int x = 0, y = 0; x < SIZE; x += (y == SIZE - 1 ? 1 : 0), y = (y == SIZE - 1 ? 0 : y + 1)) {
            Location location = board[x][y];
            if (location != null) {
                if (!compiler.running) {
                    compiler.start(new Coordinate(x, y));
                }

                compiler.add(location.letter);
            }

            if (location == null || y == SIZE - 1) {
                compiler.stop();
            }

            if ((!compiler.running || y == SIZE - 1) && compiler.hasText()) {
                WordData data = WordData.verticalData(compiler.text, compiler.startPosition);
                verticalData.add(data);

                compiler.reset();
            }
        }

        return verticalData;
    }

    private static class Result {
        private final int[][] values;
        private final SparseArray<Character> lookup;

        Result(int[][] values, SparseArray<Character> lookup) {
            this.values = values;
            this.lookup = lookup;
        }
    }

    private static class Location {

        private String letter;

        private void set(int letter) {
            this.letter = String.valueOf((char) letter);
        }

        private boolean compare(int letter) {
            if (this.letter == null) {
                return false;
            }

            String l = String.valueOf((char) letter);
            return (this.letter.equalsIgnoreCase(l));
        }
    }

    private static class Compiler {

        private boolean running;
        private Coordinate startPosition;
        private String text = "";

        private void start(Coordinate coordinate) {
            this.running = true;
            this.startPosition = coordinate;
            this.text = "";
        }

        private void stop() {
            this.running = false;
        }

        private void add(String text) {
            if (this.running) {
                this.text += text;
            }
        }

        private boolean hasText() {
            return (this.text.length() > 0 && this.startPosition != null);
        }

        private void reset() {
            this.startPosition = null;
            this.text = "";
        }
    }
}
