package com.xin.portal.system.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * excel公共方法
 */
public final class ExcelUtil {
	
	private final static String EXCEL_2007 = "xlsx";
     
    private ExcelUtil() {
         
    }
 
    /** 读取excel的结果集 */
    private static ArrayList<Map<Integer, String>> result = null;
     
    /** 记录表格中空行的行数 */
    private static int num = 0;
     
    /**
     * 获取需要传入数据库的数据
     */
    public static ArrayList<Map<Integer, String>> readExcelData(final String filePath) {
        try {
           return readExcelToObj(filePath);
        } catch (Exception e) {
        	System.out.println("导入失败");
        	e.printStackTrace();
        }
        return null;
    }
     
    /**
     * 读取excel数据
     */
    private static  ArrayList<Map<Integer, String>> readExcelToObj(final String path) throws Exception {
        Workbook wb = null;
        result = new ArrayList<Map<Integer, String>>();
        try {
            wb = WorkbookFactory.create(new File(path));
            Sheet sheet = wb.getSheetAt(0);
            result = readExcel(wb, sheet, 0, 0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
 
    /**
     * 读取excel文件
     * @param sheet sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param tailLine 去除最后读取的行
     */
    private static ArrayList<Map<Integer, String>> readExcel(final Workbook wb, final Sheet sheet, final int startReadLine, final int tailLine) {
        Row row = null;
        for (int i = startReadLine; i < sheet.getLastRowNum() - tailLine + 1; i++) {
            row = sheet.getRow(i);
            Map<Integer, String> map = new HashMap<Integer, String>(row.getLastCellNum());
            for (int j=0;j<row.getLastCellNum();j++) {
            	String returnStr = "";
            	if (row.getCell(j)!=null) {
            		boolean isMerge = isMergedRegion(sheet, i, row.getCell(j).getColumnIndex());
            		//判断是否具有合并单元格
            		if (isMerge) {
            			String rs = getMergedRegionValue(sheet, row.getRowNum(), row.getCell(j).getColumnIndex());
            			returnStr = rs;
            		} else {
            			//returnStr = c.getRichStringCellValue().getString();
            			returnStr = getCellValue(row.getCell(j));
            		}
            	}
                map.put(j, returnStr);
            }
            
//            for (Cell c : row) {
//                String returnStr = "";
//                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
//                //判断是否具有合并单元格
//                if (isMerge) {
//                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
//                    returnStr = rs;
//                } else {
//                    //returnStr = c.getRichStringCellValue().getString();
//                    returnStr = getCellValue(c);
//                }
//                
//                for (int j=0;j<row.getLastCellNum();j++) {
//                	map.put(String.valueOf(j), returnStr);
//                }
//            }
            result.add(map);
        }
        return result;
    }
 
     
     
    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     */
    private static String getMergedRegionValue(final Sheet sheet, final int row, final int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
 
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
 
            if (row >= firstRow && row <= lastRow) {
 
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
 
        return null;
    }
 
    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     */
    private static boolean isMergedRow(final Sheet sheet, final int row, final int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row == firstRow && row == lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
 
    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     */
    private static boolean isMergedRegion(final Sheet sheet, final int row, final int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
 
    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     */
    private static boolean hasMerged(final Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }
 
    /**
     * 合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private static void mergeRegion(final Sheet sheet, final int firstRow, final int lastRow, final int firstCol, final int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
 
    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    private static String getCellValue(final Cell cell) {
 
        if (cell == null) {
            return "";
        }
        
        cell.setCellType(CellType.STRING);
        
        switch (cell.getCellTypeEnum()) {
        case STRING:
        	return cell.getStringCellValue();
        case BOOLEAN:
        	return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
        	return cell.getCellFormula();
        case NUMERIC:
        	if (HSSFDateUtil.isCellDateFormatted(cell)) {
        		return DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd");
        	} else {
        		return String.valueOf(cell.getNumericCellValue());
        	}
        case BLANK:
        	return cell.getStringCellValue();
		default:
			break;
        }
 
        return "";
    }
     
    /**
     * 判断行为空 lineNum 为空行的开始位置
     */
    private static Integer checkRowNull(final Sheet sheet, final int rows) {
        Row row = null;
        for (int i = 0; i < rows; i++) {
            int cols = sheet.getRow(i).getPhysicalNumberOfCells();
            int col = 0;
            row = sheet.getRow(i);
            for (Cell c : row) {
                String returnStr = "";
                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                // 判断是否具有合并单元格
                if (isMerge) {
                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    returnStr = rs;
                } else {
                    returnStr = c.getRichStringCellValue().getString();
                }
                if (returnStr.trim() == null || returnStr.trim() == "") {
                    col++;
                }
            }
            if (cols == col) {
                num++;
            }
        }
        return num;
    }
     
    /**
     * 从excel读取内容
     */
    private static void readContent(final String fileName) {
    	//判断是否是excel2007格式
        boolean isE2007 = false;    
        if (fileName.endsWith(EXCEL_2007)) {
            isE2007 = true;
        }
        try {//建立输入流
            InputStream input = new FileInputStream(fileName);  
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if (isE2007) {
                wb = new XSSFWorkbook(input);
            } else {
                wb = new HSSFWorkbook(input);
            }
          //获得第一个表单
            Sheet sheet = wb.getSheetAt(0);   
          //获得第一个表单的迭代器
            Iterator<Row> rows = sheet.rowIterator(); 
            while (rows.hasNext()) {
            	//获得行数据
                Row row = rows.next();  
              //获得行号从0开始
                System.out.println("Row #" + row.getRowNum() + " "+ row.getLastCellNum());  
              //获得第一行的迭代器
                Iterator<Cell> cells = row.cellIterator();    
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    System.out.print(" ");
                  //根据cell中的类型来输出数据
                    switch (cell.getCellTypeEnum()) {   
                        case NUMERIC:
                        	if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        		System.out.print(DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd")  );
                        		
                        	} else {
                        		System.out.print((int)cell.getNumericCellValue());
                        		
                        	}
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula());
                            break;
                        case BLANK:
                            System.out.print(cell.getStringCellValue());
                            break;
                        default:
                            System.out.println("unsuported sell type=======" + cell.getCellTypeEnum());
                            break;
                    }
                }
                System.out.println("  ");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }
    
    public static void main(String[] args) {
    	//readContent("E:\\测试.xlsx");
    	//readContent("E:\\测试03.xls");
    	
    	ArrayList<Map<Integer, String>> list = readExcelData("E:\\测试03.xls");
    	for (Map<Integer, String> map : list) {
    		for (int i=0;i<map.size();i++) {
    			System.out.println(i + " " + map.get(String.valueOf(i)));
    			
    		}
    	}
    }
}
