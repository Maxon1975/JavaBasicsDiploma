package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    private final char[] SYMBOLS = new char[]{'#', '$', '@', '%', '*', '+', '-', '\"', '\''};

    @Override
    public char convert(int color) {
        return SYMBOLS[(int) Math.floor(color / 256.0 * SYMBOLS.length)];
    }
}