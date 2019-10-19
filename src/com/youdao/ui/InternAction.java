package com.youdao.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.PluginsAdvertiser;

public class InternAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ConfigDialog configDialog = new ConfigDialog(e);
        configDialog.pack();
        configDialog.setVisible(true);
    }
}
