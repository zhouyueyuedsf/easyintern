package com.youdao.yexcel;

import com.youdao.model.ConfigModel;
import com.youdao.model.TableDataModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class ExcelUtil {
    public static void readSpecialArea(ConfigModel configModel, ModelGenCallBack callBack) {
        TableDataModel keySettingTableModel = new TableDataModel();
        try {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(configModel.inputFilePath)));
            SwingWorker<Vector<Vector<String>>, Void> swingWorker = new SwingWorker<Vector<Vector<String>>, Void>() {
                @Override
                protected Vector<Vector<String>> doInBackground() throws Exception {
                    return readExcel2VectorHandler(workbook, configModel.validArea.startCol - 1, configModel.validArea.endCol - 1,
                            configModel.validArea.startRow - 1, configModel.validArea.endRow - 1, configModel.sheetIndex);
                }

                @Override
                protected void done() {
                    try {
                        keySettingTableModel.data = get();
                        callBack.onSuccess(keySettingTableModel);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
            swingWorker.execute();
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
            long maxCol = row.getLastCellNum() > endCol ? endCol : row.getLastCellNum();
            for (int j = startCol; j < maxCol; j++) {
                String val = Converter.getCellValue(row.getCell(j));
                rows.add(val);
            }
            vector.add(rows);
        }
        return vector;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        String path = "D:\\Office\\国际词典安卓V4.3.2文案-完成.xlsx";
        Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(path)));
        int i = 0;
    }
}
