package com.cc5c.utils;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 4you
 * @date 2019/7/11
 */
@Slf4j
public class ExcelUtil {

    public static String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {
            //数字
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                value = cell.getNumericCellValue() + " ";
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        //日期格式化
                        value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    } else {
                        value = "";
                    }
                } else {
                    //解析cell时候 数字类型默认是double类型的 但是想要获取整数类型 需要格式化
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }
            } else if (cell.getCellTypeEnum() == CellType.STRING) {
                //字符串
                value = cell.getStringCellValue();
            } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                //Boolean类型
                value = cell.getBooleanCellValue() + "";
            } else if (cell.getCellTypeEnum() == CellType._NONE) {
                //空值
                value = "";
            } else if (cell.getCellTypeEnum() == CellType.ERROR) {
                //错误类型
                value = "非法字符";
            } else {
                value = "未知类型";
            }
        }
        return value.trim();
    }

    public static boolean getHSSFWorkbook(String sheetName, List<String> title, Map<String, List<String>> values, String fileName, HttpServletResponse response) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        //声明列对象
        HSSFCell cell = null;
        //创建标题
        for (int i = 0; i < title.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(title.get(i));
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 0; i < values.size(); i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values.get(String.valueOf(i)).size(); j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values.get(String.valueOf(i)).get(j));
            }
        }
        try {
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), "iso8859-1") + "\"");
            wb.write(response.getOutputStream());
            return true;
        } catch (Exception e) {
            log.error("cast exception:{}", e.getMessage());
            return false;
        }
    }

    /**
     * easyExcel导出
     *
     * @param data
     * @param sheetName
     * @param fileName
     * @param response
     * @return
     */
    public static boolean exportExcelToBrowser(List<? extends BaseRowModel> data, String sheetName, String fileName, HttpServletResponse response, Class<? extends BaseRowModel> clazz) {
        try (OutputStream os = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), "iso8859-1") + "\"");
            ExcelWriter writer = new ExcelWriter(os, ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0, clazz);
            sheet.setSheetName(sheetName);
            writer.write(data, sheet);
            writer.finish();
            os.flush();
            return true;
        } catch (Exception e) {
            log.error("cast exception:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 读取上传文件excel
     *
     * @param multipartIs
     * @param model
     * @return
     */
    public static <T> List<T> readExcel(InputStream multipartIs, Class<? extends BaseRowModel> model) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        try (InputStream inputStream = new BufferedInputStream(multipartIs)) {
            ExcelReader excelReader = new ExcelReader(inputStream, null, excelListener, false);
            excelReader.read(new Sheet(1, 1, model));
        } catch (Exception e) {
            log.error("cast exception:{}", e.getMessage());
        }
        return excelListener.getData();
    }
}
