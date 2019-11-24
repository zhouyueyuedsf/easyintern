package com.youdao.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.youdao.model.ConfigModel;

import javax.swing.*;
import java.awt.event.*;

public class ConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonNext;
    private JButton buttonCancel;
    private JTextField textFieldStartCol;
    private JTextField textFieldEndCol;
    private JTextField textFieldStartRow;
    private JTextField textFieldEndRow;
    private JTextField textFieldSubStartRow;
    private JTextField textFieldSubEndRow;
    private JTextField textFieldEnglishCol;
    private TextFieldWithBrowseButton textFieldInputFileChooser;
    private TextFieldWithBrowseButton textFieldOutputFileChooser;
    private JCheckBox checkBoxIncludeHead;
    private JTextField textFieldSheetIndex;
    private Project project;
    private ConfigModel configModel = new ConfigModel();
    public ConfigDialog() {

    }
    public ConfigDialog(AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        setContentPane(contentPane);
        setModal(true);
        setTitle("配置");
        getRootPane().setDefaultButton(buttonNext);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initListener();
    }

    private void initListener() {
        buttonNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        FileChooserDescriptor inputFileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        textFieldInputFileChooser.addBrowseFolderListener(new TextBrowseFolderListener(inputFileChooserDescriptor, project));
        FileChooserDescriptor outputFileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        textFieldOutputFileChooser.addBrowseFolderListener(new TextBrowseFolderListener(outputFileChooserDescriptor, project));
    }

    private void onOK() {
        // add your code here
        configModel.outFilePath = textFieldOutputFileChooser.getText();
        configModel.inputFilePath = textFieldInputFileChooser.getText();
        configModel.validArea.startCol = Integer.parseInt(textFieldStartCol.getText());
        configModel.validArea.endCol = Integer.parseInt(textFieldEndCol.getText());
        configModel.validArea.startRow = Integer.parseInt(textFieldStartRow.getText());
        configModel.validArea.endRow = Integer.parseInt(textFieldEndRow.getText());
        configModel.englishNum = Integer.parseInt(textFieldEnglishCol.getText());
        configModel.includeHead = checkBoxIncludeHead.isSelected();
        configModel.sheetIndex = Integer.parseInt(textFieldSheetIndex.getText());
        if (configModel.checkModel()) {
            KeySettingDialog settingDialog = new KeySettingDialog(configModel);
            settingDialog.pack();
            settingDialog.setVisible(true);
        } else {
            Messages.showMessageDialog(project, "有配置数据未填写",  "Error", Messages.getErrorIcon());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
