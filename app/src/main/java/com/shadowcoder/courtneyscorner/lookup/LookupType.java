package com.shadowcoder.courtneyscorner.lookup;

public enum LookupType {
    CROSSWORD;

    ViewLookup get() {
        switch (this) {
            case CROSSWORD:
                return new CrosswordLookup();
        }

        throw new IllegalStateException("unhandled LookupType.get()");
    }
}
