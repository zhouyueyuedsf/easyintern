package com.youdao.model;


import org.jsoup.helper.StringUtil;

public class ConfigModel {
    public String outFilePath;
    public String inputFilePath;
    public Rect validArea = new Rect();
    public Rect stringArrayArea = new Rect();
    /**
     * 在excel中的参照列号
     */
    public int referColNum = 0;
    public boolean includeHead = true;
    /**
     * 行号 默认为1
     */
    public int headRowNum = 1;
    public int sheetIndex = 0;

    public boolean checkModel() {
        return !StringUtil.isBlank(outFilePath) && !StringUtil.isBlank(inputFilePath) && validArea.checkModel() && stringArrayArea.checkModel() && validArea.startCol <= referColNum;
    }
}
