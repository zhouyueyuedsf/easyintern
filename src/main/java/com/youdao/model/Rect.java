package com.youdao.model;

public class Rect {
    public int startCol = 1;
    public int endCol = 1;
    public int startRow = 1;
    public int endRow = 1;

    public boolean checkModel() {
        return startRow <= endRow && startCol <= endCol;
    }
}
