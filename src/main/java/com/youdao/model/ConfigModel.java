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
    public int sheetIndex = 0;

    public boolean checkModel() {
        return !StringUtil.isBlank(outFilePath) && !StringUtil.isBlank(inputFilePath) && validArea.checkModel() && stringArrayArea.checkModel() && validArea.startCol <= referColNum;
    }
}
