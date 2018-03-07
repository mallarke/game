package com.shadowcoder.courtneyscorner.lookup;

import com.shadowcoder.courtneyscorner.activity.crossword.view.CrosswordLayout;

class CrosswordLookup extends ViewLookup {
    CrosswordLookup() {
        super(LookupType.CROSSWORD);
    }

    @Override
    protected int rowLength() {
        return CrosswordLayout.CROSSWORD_LENGTH;
    }
}
