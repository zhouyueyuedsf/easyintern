package com.youdao.ui;

import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.youdao.adapter.KeySettingTableAdapter;
import com.youdao.model.AndroidStringXmlModel;
import com.youdao.model.ConfigModel;
import com.youdao.model.TableDataModel;
import com.youdao.util.Constant;
import com.youdao.util.RouterKt;
import com.youdao.util.UIUtilKt;
import com.youdao.util.XmlUtil;
import com.youdao.yexcel.ExcelUtil;
import com.youdao.yexcel.ModelGenCallBack;
import kotlin.Pair;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class KeySettingDialog extends JDialog {
    private static final int MAX_LEN = 4;
    private JPanel contentPane;
    private JButton buttonFinish;
    private JButton buttonPre;
    private JBTable tableKeySetting;
    private JScrollPane scrollPaneContainer;
    private TableDataModel dataModel = new TableDataModel();
    private KeySettingTableAdapter adapter;
    private ConfigModel config;
    private static HashMap<String, String> abbrMap = new HashMap<>();
    private Vector<String> headNameRow = new Vector<>();
    private HashMap<String, Vector<String>> headNameAndColMap = new HashMap<>();
    private Vector<String> keyCol = new Vector<>();
    /**
     * 配置给出的参照列
     */
    private Vector<String> referCol = new Vector<>();
    private final String KEY_COL_HEAD_NAME = "KEY_NAME";
    private final int KYE_COL_INDEX = 0;
    private final int REFER_COL_INDEX = 1;

    KeySettingDialog() {

    }

    private void initData(Vector<Vector<String>> rawData) {
        if (config.includeHead) {
            headNameRow = rawData.remove(0);
        } else {
            headNameRow = ExcelUtil.readHead(config);
            if (headNameRow == null) {
                headNameRow = new Vector<>();
            }
        }
        for (String name : headNameRow) {
            String abbr = Constant.sAbbrProviderMap.get(name);
            if (abbr == null) {
                abbr = name;
            }
            abbrMap.put(name, abbr);
        }
        // 转置后和headNameRow一一对应
        Vector<Vector<String>> transposedData = new Vector<>();
        transposed(rawData, transposedData);
        int colNum = headNameRow.size();
        for (int i = 0; i < colNum; i++) {
            headNameAndColMap.put(headNameRow.get(i), transposedData.get(i));
        }
        int referColIndex = config.referColNum - config.validArea.startCol;
        referCol = headNameAndColMap.get(headNameRow.get(referColIndex));
        initKeyCol();
    }

    private int getAnchorIndex(int arrStartIndex, int arrEndIndex) {
        return (arrStartIndex + arrEndIndex) / 2;
    }

    private int calStringArrIndex(int stringArrSubRow, int startRow) {
        int offset = config.includeHead ? -1 : 0;
        return stringArrSubRow - startRow + offset;
    }

    /**
     * keyCol的生成是由referCol决定的，默认给出x_x格式
     */
    private void initKeyCol() {
        int arrStartIndex = calStringArrIndex(config.stringArrayArea.startRow, config.validArea.startRow);
        int arrEndIndex = calStringArrIndex(config.stringArrayArea.endRow, config.validArea.startRow);
        int arrAnchorIndex = getAnchorIndex(arrStartIndex, arrEndIndex);
        for (int i = 0; i < referCol.size(); i++) {
            if (i >= arrStartIndex && i <= arrEndIndex) {
                if (i == arrAnchorIndex) {
                    keyCol.add("write string array key");
                    continue;
                }
                keyCol.add("");
            } else {
                String english = referCol.get(i);
                String[] keyString = english.split(" ");
                StringBuilder keyBuilder = new StringBuilder();
                int len = keyString.length;
                int maxLen = Math.min(len, MAX_LEN);
                for (int j = 0; j < maxLen - 1; j++) {
                    if (keyString[j].contains("&")) {
                        continue;
                    }
                    keyBuilder.append(keyString[j]);
                    keyBuilder.append("_");
                }
                keyBuilder.append(keyString[maxLen - 1]);
                keyCol.add(keyBuilder.toString().toLowerCase());
            }
        }
        headNameAndColMap.put(KEY_COL_HEAD_NAME, keyCol);
    }

    public KeySettingDialog(ConfigModel configModel) {
        this.config = configModel;
        setContentPane(contentPane);
        UIUtilKt.center(this, 469, 360);
        setModal(true);
        getRootPane().setDefaultButton(buttonFinish);
        adapter = new KeySettingTableAdapter();
        ExcelUtil.readSpecialArea(configModel, new ModelGenCallBack() {
            @Override
            public void onSuccess(TableDataModel settingTableModel) {
                initData(settingTableModel.data);
                adapter.addColumn("string key", keyCol);
                adapter.addColumn("string english value", referCol);
                tableKeySetting.setModel(adapter);
                initListener();

            }

            @Override
            public void onError(String message) {
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
        adapter.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int col = e.getColumn();
                int row = e.getFirstRow();
                String newValue = (String) adapter.getValueAt(row, col);
                if (col <= 1) {
                    if (col == KYE_COL_INDEX) {
                        keyCol.set(row, newValue);
                        headNameAndColMap.put(KEY_COL_HEAD_NAME, keyCol);
                    } else {
                        referCol.set(row, newValue);
                        headNameAndColMap.put(headNameRow.get(config.referColNum - config.validArea.startCol), referCol);
                    }
                }
            }
        });
    }

    private void onOK() {
        // output
        String outPutPath = config.outFilePath;
        Vector<String> outputKeyVector = headNameAndColMap.get(KEY_COL_HEAD_NAME);
        int arrStartIndex = calStringArrIndex(config.stringArrayArea.startRow, config.validArea.startRow);
        int arrEndIndex = calStringArrIndex(config.stringArrayArea.endRow, config.validArea.startRow);
        int arrAnchorIndex = getAnchorIndex(arrStartIndex, arrEndIndex);
        for (Map.Entry<String, Vector<String>> entry : headNameAndColMap.entrySet()) {
            if (!KEY_COL_HEAD_NAME.equals(entry.getKey())) {
                Vector<String> outValueVector = entry.getValue();
                String suffix = abbrMap.get(entry.getKey());
                if ("en".equals(suffix)) {
                    suffix = "";
                } else {
                    suffix = "-" + suffix;
                }
                File outFile = new File(outPutPath + "/values" + suffix + "/strings.xml");
                String filledXmlString = getFilledXmlString(outputKeyVector, arrStartIndex, arrEndIndex, arrAnchorIndex, outValueVector);
                if (outFile.exists()) {
                    // 替换逻辑
                    filledXmlString = "<resources>" + filledXmlString + "</resources>";
                    AndroidStringXmlModel newAndroidStringXmlModel = XmlUtil.INSTANCE.readStringsXmlByString(filledXmlString);
                    AndroidStringXmlModel oldAndroidStringXmlModel = XmlUtil.INSTANCE.readStringsXmlByPath(outFile.getPath());
                    ArrayList<Pair<Integer, Integer>> datas[] = XmlUtil.INSTANCE.syncXmlModelAndReturnUnAppendData(newAndroidStringXmlModel, oldAndroidStringXmlModel, outFile);
                    final ArrayList<Pair<Integer, Integer>> stringConflictPosInfo = datas[0];
                    // 进行新旧比较的算法
                    RouterKt.routerConflictSolveDialog(newAndroidStringXmlModel, oldAndroidStringXmlModel, datas, new ConflictSolveDialog.CallBack() {
                        @Override
                        public void onOK(JDialog dialog, AndroidStringXmlModel outputModel) {
                            XmlUtil.INSTANCE.writeModelToXml(outputModel, outFile.getPath(), false);
                        }
                    });
                } else {
                    try {
                        FileUtils.write(outFile, filledXmlString,
                                Charset.defaultCharset(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        dispose();
    }

    private String getFilledXmlString(Vector<String> outputKeyVector, int arrStartIndex, int arrEndIndex, int arrAnchorIndex, Vector<String> outValueVector) {
        StringBuilder filledXmlStringBuilder = new StringBuilder();
        int size = outputKeyVector.size();
        for (int i = 0; i < size; i++) {
            if (i >= arrStartIndex && i <= arrEndIndex) {
                StringBuilder arrBuilder = new StringBuilder();
                for (int k = i; k <= arrEndIndex; k++) {
                    if (k == arrAnchorIndex) {
                        //&#160;&amp;&#160;
                        arrBuilder.insert(0, String.format("<string-array name=\"%s\">\n", outputKeyVector.get(k)));
                    }
                    arrBuilder.append(String.format("<item>%s</item>\n", XmlUtil.INSTANCE.toTrim(outValueVector.get(k))));
                }
                arrBuilder.append("</string-array>");
                i = arrEndIndex;
                filledXmlStringBuilder.append(arrBuilder);
//                            FileUtils.write(outFile, arrBuilder.toString(), Charset.defaultCharset(), true);
                continue;
            }

            filledXmlStringBuilder.append(String.format("<string name = \"%s\">%s</string>\n",
                    outputKeyVector.get(i), XmlUtil.INSTANCE.toTrim(outValueVector.get(i))));
//                        FileUtils.write(outFile, String.format("<string key = \"%s\">%s</string>\n", outputKeyVector.get(i), outValueVector.get(i)).toString(),
//                                Charset.defaultCharset(), true);
        }
        return filledXmlStringBuilder.toString();
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
                v.add(j, (String) oldc.get(i));
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
