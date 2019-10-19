package com.youdao.ui;

import com.intellij.ui.table.JBTable;
import com.youdao.adapter.KeySettingTableAdapter;
import com.youdao.model.ConfigModel;
import com.youdao.model.TableDataModel;
import com.youdao.yexcel.ExcelUtil;
import com.youdao.yexcel.ModelGenCallBack;

import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

public class KeySettingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonFinish;
    private JButton buttonPre;
    private JBTable tableKeySetting;
    private TableDataModel dataModel;
    private KeySettingTableAdapter adapter;
    KeySettingDialog() {

    }
    public KeySettingDialog(ConfigModel configModel) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonFinish);
        initListener();
        adapter = new KeySettingTableAdapter();
        ExcelUtil.readSpecialArea(configModel, new ModelGenCallBack() {
            @Override
            public void onSuccess(TableDataModel settingTableModel) {
                dataModel = settingTableModel;
                int rowCount = configModel.includeHead ? dataModel.data.size() - 1: dataModel.data.size();
                Vector<String> keyVector = new Vector<>();
                Vector<String> englishVector = new Vector<>();
                int englishIndex = configModel.englishNum - configModel.validArea.startCol;
                for (Vector<String> rows : dataModel.data) {
                    String english = rows.get(englishIndex);
                    String[] keyString = english.split("");
                    StringBuilder keyBuilder = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        keyBuilder.append(keyString[i]);
                    }
                    keyVector.add(keyBuilder.toString());
                    englishVector.add(rows.get(englishIndex));
                }
                adapter.addColumn("string key", keyVector);
                adapter.addColumn("string english value", englishVector);
                tableKeySetting.setModel(adapter);
            }
        });
    }

    private void initListener() {
        buttonFinish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonPre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    public static void main(String[] args) {
        KeySettingDialog dialog = new KeySettingDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
