package com.shadowcoder.courtneyscorner.generator.crossword;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.shadowcoder.courtneyscorner.Utils;
import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordLayout;
import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CoordinateRange;
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
                "australia", "germany", "shadow", "she", "hit", "courtney", "stomach", "punch",
                "baseballbat", "years", "mellow", "feet", "tires", "car", "corner", "foot", "watch"
        };

        TEST_STRINGS.addAll(Arrays.asList(test_values));
    }

    private static final int SIZE = CrosswordLayout.CROSSWORD_LENGTH;

    @Override
    public CrosswordData execute() throws Error {
        Result result = this.convert(TEST_STRINGS);
        SparseArray<List<Integer>> lookup = this.buildLookup(result);
        List<BoardConfiguration> configurations = Utils.readBoardConfigs();

        for (int max_try = 0; max_try < 20; max_try++) {
            CrosswordData data = this.generate(result, lookup, configurations);
            if (data != null) {
                return data;
            }
        }

        return null;
    }

    @Nullable
    private CrosswordData generate(Result result, SparseArray<List<Integer>> numberLookup, List<BoardConfiguration> configurations) {
        Location[][] board = new Location[SIZE][SIZE];
        BoardConfiguration configuration = this.initializeBoard(board, configurations);

//        for (int maxTries = 0; maxTries < 10000; maxTries++) {
//
//        }

        return this.compile(board, configuration);
    }

    @NonNull
    private BoardConfiguration initializeBoard(Location[][] board, List<BoardConfiguration> configurations) {
        int saltIndex = new Random().nextInt(configurations.size());
        BoardConfiguration configuration = configurations.get(saltIndex);

        for (int x = 0, y = 0; x < SIZE; x += (y == SIZE - 1 ? 1 : 0), y = (y == SIZE - 1 ? 0 : y + 1)) {
            Coordinate coordinate = new Coordinate(x, y);
            if (configuration.blanks.contains(coordinate)) {
                continue;
            }

            board[x][y] = new Location();
            board[x][y].letter = "a";
        }

        // salt the board
        for (int i = 0; i < new Random().nextInt(3) + 1; i++) {
            String text = TEST_STRINGS.get(new Random().nextInt(TEST_STRINGS.size()));

            int x = new Random().nextInt(SIZE);
            int y = new Random().nextInt(SIZE);
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
    private CrosswordData compile(Location[][] board, BoardConfiguration configuration) {
//        if (!this.isBoardCompleted(board)) {
//            return null;
//        }

        List<WordData> horizontalData = this.getHorizontalData(board, configuration);
        List<WordData> verticalData = this.getVerticalData(board, configuration);

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

    private List<WordData> getHorizontalData(Location[][] board, BoardConfiguration configuration) {
        List<WordData> horizontalData = new ArrayList<>();

        for (CoordinateRange range : configuration.horizontalRanges) {
            List<Coordinate> coordinates = range.inflate();
            Compiler compiler = new Compiler(coordinates.size());

            for (Coordinate coordinate : coordinates) {
                Location location = board[coordinate.x][coordinate.y];
                compiler.add(location.letter);
            }

            horizontalData.add(new WordData(compiler.toString(), range));
        }

        horizontalData.clear();
        return horizontalData;
    }

    private List<WordData> getVerticalData(Location[][] board, BoardConfiguration configuration) {
        List<WordData> verticalData = new ArrayList<>();

        for (int x = 0, y = 0; x < SIZE; y += (x == SIZE - 1 ? 1 : 0), x = (x == SIZE - 1 ? 0 : x + 1)) {
            Location location = board[x][y];
            if (location != null) {
                CoordinateRange range = new CoordinateRange(new Coordinate(x, y), new Coordinate(x, y), Direction.VERTICAL);
                verticalData.add(new WordData(Integer.toHexString(y), range));
            }
        }

//        for (CoordinateRange range : configuration.verticalRanges) {
//            List<Coordinate> coordinates = range.inflate();
//            Compiler compiler = new Compiler(coordinates.size());
//
//            for (Coordinate coordinate : coordinates) {
//                Location location = board[coordinate.x][coordinate.y];
//                compiler.add(location.letter);
//            }
//
//            verticalData.add(new WordData(compiler.toString(), range));
//        }

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

        private String data;
        private final StringBuilder builder;

        private Compiler(int wordSize) {
            this.builder = new StringBuilder(wordSize);
        }

        private void add(String letter) {
            this.builder.append(letter);
        }

        @Override
        public String toString() {
            return this.builder.toString();
        }
    }
}
