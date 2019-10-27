package com.youdao.ui;

import com.intellij.ui.table.JBTable;
import com.youdao.adapter.KeySettingTableAdapter;
import com.youdao.model.ConfigModel;
import com.youdao.model.TableDataModel;
import com.youdao.yexcel.ExcelUtil;
import com.youdao.yexcel.ModelGenCallBack;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Vector;

public class KeySettingDialog extends JDialog {
    private static final int MAX_LEN = 4;
    private JPanel contentPane;
    private JButton buttonFinish;
    private JButton buttonPre;
    private JBTable tableKeySetting;
    private TableDataModel dataModel = new TableDataModel();
    private KeySettingTableAdapter adapter;
    private ConfigModel config;
    private static HashMap<String, String> abbrMap;
    static {
        abbrMap = new HashMap<>();
        abbrMap.put("English", "");

    }
    KeySettingDialog() {

    }
    public KeySettingDialog(ConfigModel configModel) {
        this.config = configModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonFinish);
        initListener();
        adapter = new KeySettingTableAdapter();
        ExcelUtil.readSpecialArea(configModel, new ModelGenCallBack() {
            @Override
            public void onSuccess(TableDataModel settingTableModel) {
                transposed(settingTableModel.data, dataModel.data);
                int rowCount = config.includeHead ? dataModel.data.size() - 1: dataModel.data.size();
                Vector<String> keyVector = new Vector<>();
                int englishIndex = config.englishNum - config.validArea.startCol;
                Vector<String> englishVector = null;
                Vector<String> tempEnglishVector = dataModel.data.get(englishIndex);
                if (configModel.includeHead) {
                    englishVector = new Vector<>(tempEnglishVector.subList(1, tempEnglishVector.size()));
                } else  {
                    englishVector = tempEnglishVector;
                }
//                int count = 0;
//                for (Vector<String> rows : dataModel.data) {
//                    if (config.includeHead && count == 0) {
//                        count++;
//                        continue;
//                    }
//                    String english = rows.get(englishIndex);
//                    englishVector.add(rows.get(englishIndex));
//                }
                int arrStartRow = config.stringArrayArea.startRow;
                int arrEndRow = config.stringArrayArea.endRow;
                int arrAnchorRow = (arrStartRow + arrEndRow) / 2;
                for (int i = 0; i < englishVector.size(); i++) {
                    if (configModel.includeHead && i == 0) {
                        continue;
                    }
                    if (i > arrStartRow && i < arrEndRow) {
                        if (i == arrAnchorRow) {
                            keyVector.add("请在此处填写string array的key值");
                            continue;
                        }
                        keyVector.add(0, "");
                        // todo 设置下划线
//                        tableKeySetting.getCellEditor()
                    } else{
                        String english = englishVector.get(i);
                        String[] keyString = english.split(" ");
                        StringBuilder keyBuilder = new StringBuilder();
                        int len = keyString.length;
                        int maxLen = Math.min(len, MAX_LEN);
                        for (int j = 0; j < maxLen - 1; j++) {
                            keyBuilder.append(keyString[j]);
                            keyBuilder.append("_");
                        }
                        keyBuilder.append(keyString[maxLen - 1]);
                        keyVector.add(keyBuilder.toString());
                    }
                }
                dataModel.data.add(0, keyVector);
//                for (int i = 0; i < englishVector.size(); i++) {
//                    Vector<String> vector = dataModel.data.get(i);
//                    if (i > arrStartRow && i < arrEndRow) {
//                        if (i == arrAnchorRow) {
//                            vector.add(0, "请在此处填写string array的key值");
//                            continue;
//                        }
//                        vector.add(0, "");
//                        // todo 设置下划线
////                        tableKeySetting.getCellEditor()
//                    }
//                    String english = englishVector.get(i);
//                    String[] keyString = english.split("");
//                    StringBuilder keyBuilder = new StringBuilder();
//                    for (int j = 0; j < 3; j++) {
//                        keyBuilder.append(keyString[j]);
//                    }
//                    String key = keyBuilder.toString();
//                    dataModel.data.get(i).add(0, key);
//                }

//                for (int i = 0; i < dataModel.data.size(); i++) {
//                    if (config.includeHead && i == 0) {
//                        continue;
//                    }
//                    Vector<String> vector = dataModel.data.get(i);
//                    keyVector.add(vector.get(0));
//                }
                adapter.addColumn("string key", dataModel.data.get(0));
                adapter.addColumn("string english value", dataModel.data.get(englishIndex + 1));
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
        // out put
        String outPutPath = config.inputFilePath;

        if (config.includeHead) {
            Vector<Vector<String>> matrixData = dataModel.data;
            Vector<Vector<String>> outputData = new Vector<>();
            transposed(matrixData, outputData);
            int arrStart = config.stringArrayArea.startRow;
            int arrEnd = config.stringArrayArea.endRow;
            int arrAnchor = (arrStart + arrEnd) / 2;
            for (int i = 0; i < outputData.size(); i++) {
                Vector<String> langVector = outputData.get(i);
                String abbr = abbrMap.get(langVector.get(0));
                File newFile = new File(outPutPath + "/strings-" + abbr +"xml");
                try {
                    for (int j = 1; j < langVector.size(); j++) {
                        if (j > arrStart && j < arrEnd) {
                            StringBuilder arrBuilder = new StringBuilder();
                            for (int k = j; k < arrEnd; k++) {
                                if (k == arrAnchor) {
                                    arrBuilder.insert(0, String.format("<string-array name=\"%s\">", langVector.get(i)));
                                } else {
                                    arrBuilder.append(String.format("<item>%s</item>", langVector.get(i)));
                                }
                            }
                            arrBuilder.append("</string-array>");
                            j = arrEnd;
                            FileUtils.write(newFile, arrBuilder.toString(), Charset.defaultCharset(), true);
                        } else {
                            FileUtils.write(newFile, langVector.get(j), Charset.defaultCharset(), true);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        dispose();
    }

    /**
     * 转置矩阵
     *
     * @param matrixData
     * @param outputData
     */
    private void transposed(Vector<Vector<String>> matrixData, Vector<Vector<String>> outputData) {
        int count = 0;
        int colNum = matrixData.get(0).size();
        int rowNum = matrixData.size();
        for (int i = 0; i < colNum; i++) {
            Vector v = new Vector<String>(rowNum);
            for (int j = 0; j < rowNum; j++) {
                Vector oldc = matrixData.get(j);
                v.add(j, (String)oldc.get(i));
            }
            outputData.add(i, v);
        }
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
