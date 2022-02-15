package com.metcash.delilah.utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Xls_Reader {
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;

    public Xls_Reader(String path) {

        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***********************************************************************************************
     * Function Description : Returns the row count in a sheet
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            return sheet.getLastRowNum() + 1;
        }

    }

    /***********************************************************************************************
     * Function Description : Returns the data from a cell
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/


    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            int col_Num = -1;
            if (rowNum <= 0)
                return "";
            sheet = workbook.getSheet(sheetName);

            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                if (sheet.getRow(0).getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                    col_Num = i;
                    break;
                }
            }

            if (col_Num == -1)
                return "";

            row = sheet.getRow(rowNum - 1);
            if (row == null)
                return "";

            cell = row.getCell(col_Num);
            if (cell == null)
                return "";

            switch (cell.getCellTypeEnum()) {
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case STRING:
                    return cell.getRichStringCellValue().getString();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
                        return dateFormat.format(date);
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case FORMULA:
                    return cell.getStringCellValue();
                default:
                    return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Row " + rowNum + " or Column " + colName + " does not exist in xls";
        }
    }

    /***********************************************************************************************
     * Function Description : Stores the data into a cell
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1)
                return false;


            sheet = workbook.getSheetAt(index);


            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);


            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /***********************************************************************************************
     * Function Description : Find whether sheets exists
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public boolean isSheetExist(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            return index != -1;
        } else
            return true;
    }

    /***********************************************************************************************
     * Function Description : Returns number of columns in a sheet
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public int getColumnCount(String sheetName) {
        // check if sheet exists
        if (!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null)
            return -1;

        return row.getLastCellNum();


    }

    /***********************************************************************************************
     * Function Description : Returns the row number
     * author: Puneeta Mishra, date: 01-May-2018
     * *********************************************************************************************/
    public int getCellRowNum(String sheetName, String colName, String cellValue) {

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;

    }


    /***********************************************************************************************
     * Function Description : Set color into a cell
     * author: Puneeta Mishra, date: 15-june-2018
     * *********************************************************************************************/
    public boolean setCellColor(String sheetName, String colName, int rowNum, String data) {
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            CellStyle style = workbook.createCellStyle();

            switch (data) {
                case "SKIPPED":
                    style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    break;
                case "FAILED":
                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    break;
                case "PASSED":
                    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    break;
            }

            if (rowNum <= 0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1)
                return false;


            sheet = workbook.getSheetAt(index);


            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                //System.out.println(row.getCell(i).getStringCellValue().trim());
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}