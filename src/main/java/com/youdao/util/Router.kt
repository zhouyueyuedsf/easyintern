package com.youdao.util

import com.youdao.model.AndroidStringXmlModel
import com.youdao.model.ConfigModel
import com.youdao.ui.ConflictSolveDialog
import com.youdao.ui.KeySettingDialog
import java.util.ArrayList
import javax.swing.JDialog

fun routerKeySettingDialog(configModel: ConfigModel) {
    val settingDialog = KeySettingDialog(configModel)
    settingDialog.visible()
}

fun JDialog.visible() {
    pack()
    isVisible = true
}

fun routerConflictSolveDialog(newStringMapModel: AndroidStringXmlModel, oldStringMapModel: AndroidStringXmlModel, data: Array<ArrayList<Pair<Int, Int>>>) {
    ConflictSolveDialog(newStringMapModel, oldStringMapModel, data).visible()
}

fun routerConflictSolveDialog(newStringMapModel: AndroidStringXmlModel, oldStringMapModel: AndroidStringXmlModel, data: Array<ArrayList<Pair<Int, Int>>>, callback: ConflictSolveDialog.CallBack) {
    ConflictSolveDialog(newStringMapModel, oldStringMapModel, data, callback).visible()
}