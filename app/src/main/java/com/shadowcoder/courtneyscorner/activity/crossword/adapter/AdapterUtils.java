package com.shadowcoder.courtneyscorner.activity.crossword.adapter;

import android.support.annotation.Nullable;

import com.shadowcoder.courtneyscorner.data.Coordinate;
import com.shadowcoder.courtneyscorner.data.CrosswordData;
import com.shadowcoder.courtneyscorner.data.ItemData;
import com.shadowcoder.courtneyscorner.data.WordData;
import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordLayout;

import java.util.ArrayList;
import java.util.List;

class AdapterUtils {
    static List<ItemData> getItemData(CrosswordData data) {
        List<ItemData> items = new ArrayList<>();

        for (int x = 0, y = 0, index = 0; index < CrosswordLayout.CROSSWORD_SIZE; index++, x++) {
            if (index > 0 && index % CrosswordLayout.CROSSWORD_LENGTH == 0) {
                x = 0;
                y++;
            }

            Coordinate coordinate = new Coordinate(x, y);
            ItemData itemData = getData(coordinate, data.getHorizontalWords(), data.getVerticalWords());
            if (itemData != null) {
                items.add(itemData);
                continue;
            }

            items.add(new ItemData(coordinate));
        }

        return items;
    }

    @Nullable
    private static ItemData getData(Coordinate coordinate, List<WordData> horizontalData, List<WordData> verticalData) {
        for (WordData wordData: horizontalData) {
            ItemData data = wordData.getData(coordinate);
            if (data != null) {
                return data;
            }
        }

        for (WordData wordData: verticalData) {
            ItemData data = wordData.getData(coordinate);
            if (data != null) {
                return data;
            }
        }

        return null;
    }

}
