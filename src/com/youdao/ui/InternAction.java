package com.youdao.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class InternAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ConfigDialog configDialog = new ConfigDialog();
        configDialog.pack();
        configDialog.setVisible(true);
    }
}
