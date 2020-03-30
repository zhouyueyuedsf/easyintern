package com.youdao.model;


import org.jsoup.helper.StringUtil;

public class ConfigModel {
    public String outFilePath;
    public String inputFilePath;
    public Rect validArea = new Rect();
    public Rect stringArrayArea = new Rect();
    /**
     * excel reference col num, such as english num
     */
    public int referColNum = 0;
    public boolean includeHead = true;
    /**
     * line number default = 1
     */
    public int headRowNum = 1;
    public int sheetIndex = 0;

    public boolean checkModel() {
        return !StringUtil.isBlank(outFilePath) && !StringUtil.isBlank(inputFilePath) && validArea.checkModel() && stringArrayArea.checkModel() && validArea.startCol <= referColNum;
    }
}
