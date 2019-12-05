package com.youdao.adapter;

import kotlin.Pair;

import javax.swing.*;
import java.util.ArrayList;

public class ConflictListAdapter extends DefaultListModel<String> {
    public ConflictListAdapter(ArrayList<Pair<Integer, Integer>> data) {
        for (Pair<Integer, Integer> item : data) {
            addElement(item.toString());
        }
    }
}
