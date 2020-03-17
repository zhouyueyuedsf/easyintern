package com.youdao.util

import java.awt.GraphicsEnvironment
import javax.swing.JDialog

/**
 * JDialog初始化的时候 width并不能用getWidth去得到
 */
fun JDialog.center(width: Int, height: Int) {
    val point = GraphicsEnvironment.getLocalGraphicsEnvironment().centerPoint
    setBounds(point.x - width / 2, point.y - height / 2, width, height)
}