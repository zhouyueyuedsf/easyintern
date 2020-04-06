package com.youdao.ui;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import com.youdao.adapter.ConflictListAdapter;
import com.youdao.model.AndroidStringXmlModel;
import com.youdao.util.UIUtilKt;
import kotlin.Pair;
import kotlin.text.StringsKt;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ConflictSolveDialog extends JDialog {
    private JPanel contentPane;
    private JBTextField textFieldNewName;
    private JBTextField textFieldOldValue;
    private JBTextField textFieldOldName;
    private JBTextField textFieldNewValue;

    private JButton buttonOk;
    private JButton buttonCancel;
    private JBList listInfo;
    private JButton buttonFinish;
    private CallBack mCallBack;
    private AndroidStringXmlModel mNewAndroidStringXmlModel;
    private AndroidStringXmlModel mOldAndroidStringXmlModel;
    private ArrayList<Pair<Integer, Integer>>[] mPosDatas;
    private ConflictListAdapter conflictListAdapter;
    public ConflictSolveDialog() {

    }
    public ConflictSolveDialog(AndroidStringXmlModel newAndroidStringXmlModel, AndroidStringXmlModel oldAndroidStringXmlModel, ArrayList<kotlin.Pair<Integer, Integer>>[] datas) {
        this(newAndroidStringXmlModel, oldAndroidStringXmlModel, datas, null);
    }

    public ConflictSolveDialog(AndroidStringXmlModel newAndroidStringXmlModel, AndroidStringXmlModel oldAndroidStringXmlModel, ArrayList<kotlin.Pair<Integer, Integer>>[] datas, CallBack callBack) {
        setContentPane(contentPane);
        setModal(true);
        UIUtilKt.center(this, 402, 214);
        setTitle("ConflictSolve Dialog");
        mNewAndroidStringXmlModel = newAndroidStringXmlModel;
        mOldAndroidStringXmlModel = oldAndroidStringXmlModel;
        mPosDatas = datas;
        mCallBack = callBack;
        fillData();
        initListener();
    }

    public void fillData() {
        conflictListAdapter = new ConflictListAdapter(mPosDatas[0]);
        listInfo.setModel(conflictListAdapter);
        listInfo.setSelectedIndex(0);
        listInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Pair<String, String> namePair = stringPair(0);
                Pair<String, String> valuePair = stringPair(1);
                textFieldNewName.setText(namePair.getFirst());
                textFieldOldName.setText(namePair.getSecond());
                textFieldNewValue.setText(valuePair.getFirst());
                textFieldOldValue.setText(valuePair.getSecond());
            }
        });
    }

    private Pair<String, String> stringPair(int type) {
        int index = listInfo.getSelectedIndex();
        int newPos = indexToNewPos(index);
        int oldPos = indexToOldPos(index);
        if (type == 0) {
            return new Pair<>(mNewAndroidStringXmlModel.getStringMapModelList().get(newPos).getName(), mOldAndroidStringXmlModel.getStringMapModelList().get(oldPos).getName());
        } else {
            return new Pair<>(mNewAndroidStringXmlModel.getStringMapModelList().get(newPos).getValue(), mOldAndroidStringXmlModel.getStringMapModelList().get(oldPos).getValue());
        }
    }

    private int indexToNewPos(int index) {
        return mPosDatas[0].get(index).getFirst();
    }
    private int indexToOldPos(int index) {
        return mPosDatas[0].get(index).getSecond();
    }

    void setOldName(String name, String value) {
        int index = listInfo.getSelectedIndex();
        int oldPos = indexToOldPos(index);
        mOldAndroidStringXmlModel.getStringMapModelList().get(oldPos).setName(name);
        if (!StringsKt.isBlank(value)) {
            mOldAndroidStringXmlModel.getStringMapModelList().get(oldPos).setValue(value);
        }
    }

    private void initListener() {
        buttonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // new value replace
                setOldName(textFieldOldName.getText(), textFieldOldValue.getText());
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mCallBack != null) {
                    mCallBack.onOK(ConflictSolveDialog.this, mOldAndroidStringXmlModel);
                } else {
                    dispose();
                }
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public interface CallBack {
        void onOK(JDialog dialog, AndroidStringXmlModel outputModel);
    }
}
