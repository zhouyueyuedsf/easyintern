package com.youdao.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.youdao.util.XmlUtil;
import com.youdao.util.XmlUtilKt;

public class InternAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        XmlUtil.INSTANCE.readStringsXml("E:\\Yueyue Projects\\Java Projects\\easyintern\\src\\main\\resources\\META-INF\\strings.xml");
        ConfigDialog configDialog = new ConfigDialog(e);
        configDialog.pack();
        configDialog.setVisible(true);
    }
}
