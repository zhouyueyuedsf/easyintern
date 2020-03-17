package com.youdao.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.youdao.model.ConfigModel;
import com.youdao.util.RouterKt;
import com.youdao.util.UIUtilKt;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
    private JLabel JLableHeadColNum;
    private JTextField textFiledHeadColNum;
    private Project project;
    private ConfigModel configModel = new ConfigModel();
    public ConfigDialog() {

    }
    public ConfigDialog(AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        setContentPane(contentPane);
        UIUtilKt.center(this, 469, 360);
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
        checkBoxIncludeHead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBoxIncludeHead.isSelected()) {
                    textFiledHeadColNum.hide();
                    JLableHeadColNum.hide();
                } else {
                    textFiledHeadColNum.show();
                    JLableHeadColNum.show();
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        configModel.outFilePath = textFieldOutputFileChooser.getText();
        configModel.inputFilePath = textFieldInputFileChooser.getText();
        configModel.validArea.startCol = Integer.parseInt(textFieldStartCol.getText());
        configModel.validArea.endCol = Integer.parseInt(textFieldEndCol.getText());
        configModel.validArea.startRow = Integer.parseInt(textFieldStartRow.getText());
        configModel.validArea.endRow = Integer.parseInt(textFieldEndRow.getText());
        configModel.stringArrayArea.startRow = Integer.parseInt(textFieldSubStartRow.getText());
        configModel.stringArrayArea.endRow = Integer.parseInt(textFieldSubEndRow.getText());
        configModel.referColNum = Integer.parseInt(textFieldEnglishCol.getText());
        configModel.includeHead = checkBoxIncludeHead.isSelected();
        configModel.sheetIndex = Integer.parseInt(textFieldSheetIndex.getText());
        configModel.headRowNum = Integer.parseInt(textFiledHeadColNum.getText());
        if (configModel.checkModel()) {
            RouterKt.routerKeySettingDialog(configModel);
        } else {
            Messages.showMessageDialog(project, "Configuration error, please check it",  "Error", Messages.getErrorIcon());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
