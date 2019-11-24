package com.youdao.model;

public class Rect {
    public int startCol = 0;
    public int endCol = 0;
    public int startRow = 0;
    public int endRow = 0;

    public boolean checkModel() {
        return startRow <= endRow && startCol <= endCol;
    }
}
