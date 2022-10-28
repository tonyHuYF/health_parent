package com.tony.health_common.untils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class POIUtils {
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 读入文件，解析后返回
     */
    public static List<String[]> readExcel(MultipartFile file) throws IOException {
        //检查文件
        checkFile(file);
        //获取Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每一行的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获取当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获取当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获取当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除第一行外的所有行，因为第一行一般是标题
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
                    short firstCellNum = row.getFirstCellNum();
                    //获取当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[lastCellNum];
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
            workbook.close();
        }
        return list;
    }

    /**
     * 检查文件是否合法
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (file == null) {
            throw new FileNotFoundException("文件不存在");
        }
        //获得文件名
        String filename = file.getOriginalFilename();
        //判断是否excel文件
        if (!filename.endsWith(XLS) && !filename.endsWith(XLSX)) {
            throw new IOException(filename + "不是excel文件");
        }
    }


    public static Workbook getWorkBook(MultipartFile file) {
        //获取文件名
        String filename = file.getOriginalFilename();
        //创建Workbook工作簿对象，表示整个excel
        Workbook workbook = null;

        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件名后缀获得不同的Workbook实现类对象
            if (filename.endsWith(XLS)) {
                workbook = new HSSFWorkbook(is);
            }
            if (filename.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(is);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //如果当前单元格内容为日期类型，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if (dataFormatString.equals("m/d/yy")) {
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellType() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }

        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


}
