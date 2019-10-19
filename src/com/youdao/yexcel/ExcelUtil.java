package com.youdao.yexcel;

import com.youdao.model.ConfigModel;
import com.youdao.model.TableDataModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ExcelUtil {

    public static void readSpecialArea(ConfigModel configModel, ModelGenCallBack callBack) {
        File file = new File(configModel.inputFilePath);
        TableDataModel keySettingTableModel = new TableDataModel();
        try {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
            keySettingTableModel.data = readExcel2VectorHandler(workbook, configModel.validArea.startCol, configModel.validArea.endCol,
                    configModel.validArea.startRow, configModel.validArea.endRow, configModel.sheetIndex);
//            keySettingTableModel.columnNames =
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private static Vector<Vector<String>> readExcel2VectorHandler(Workbook workbook, int startCol, int endCol, int startRow,
                                                                  int endRow, int sheetIndex) {
        Vector<Vector<String>> vector = new Vector<>();
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        long maxRow = sheet.getLastRowNum() > endRow ?
                ((long) endRow) : sheet.getLastRowNum();

        for (int i = startRow; i < maxRow; i++) {
            Vector<String> rows = new Vector<>();
            Row row = sheet.getRow(i);
            if (null == row)
                continue;
            long maxCol = row.getRowNum() > endCol ? endCol : sheet.getLastRowNum();
            for (int j = startCol; j < endCol; j++) {
                String val = Converter.getCellValue(row.getCell(j));
                rows.add(val);
            }
            vector.add(rows);
        }
        return vector;
    }
}
