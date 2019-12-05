package com.youdao.ui;

import com.youdao.adapter.ConflictListAdapter;
import com.youdao.model.AndroidStringXmlModel;
import kotlin.Pair;
import org.jsoup.helper.StringUtil;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ConflictSolveDialog extends JDialog {
    private JPanel contentPane;
    private JTextField textFieldNewName;
    private JTextField textFieldOldValue;
    private JTextField textFieldOldName;
    private JTextField textFieldNewValue;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JList listInfo;
    private CallBack mCallBack;
    private AndroidStringXmlModel mNewAndroidStringXmlModel;
    private AndroidStringXmlModel mOldAndroidStringXmlModel;
    private ArrayList<Pair<Integer, Integer>>[] mPosDatas;
    private ConflictListAdapter conflictListAdapter;
    public ConflictSolveDialog() {

    }
    public ConflictSolveDialog(AndroidStringXmlModel newAndroidStringXmlModel, AndroidStringXmlModel oldAndroidStringXmlModel, ArrayList<kotlin.Pair<Integer, Integer>>[] datas) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("配置");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mNewAndroidStringXmlModel = newAndroidStringXmlModel;
        mOldAndroidStringXmlModel = oldAndroidStringXmlModel;
        mPosDatas = datas;

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

    public int indexToNewPos(int index) {
        return mPosDatas[0].get(index).getFirst();
    }
    public int indexToOldPos(int index) {
        return mPosDatas[0].get(index).getSecond();
    }

    void setOldName(String name, String value) {
        int index = listInfo.getSelectedIndex();
        int oldPos = indexToOldPos(index);
        mOldAndroidStringXmlModel.getStringMapModelList().get(oldPos).setName(name);
        if (!StringUtil.isBlank(value)) {
            mOldAndroidStringXmlModel.getStringMapModelList().get(oldPos).setValue(value);
        }
    }

    private void initListener() {
        buttonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 替换，用新值去替换旧值
                setOldName(textFieldOldName.getText(), textFieldOldValue.getText());
//                mNewStringMapModel.setName(textFieldNewName.getText());
//                mNewStringMapModel.setName(textFieldNewValue.getText());
//                mOldStringMapModel.setName(textFieldOldName.getText());
//                mOldStringMapModel.setName(textFieldOldValue.getText());
//                mCallBack.onOK(ConflictSolveDialog.this, mNewStringMapModel, mOldStringMapModel);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public interface CallBack {
        void onOK(JDialog dialog, AndroidStringXmlModel.StringMapModel newStringMapModel, AndroidStringXmlModel.StringMapModel oldStringMapModel);
    }
}
