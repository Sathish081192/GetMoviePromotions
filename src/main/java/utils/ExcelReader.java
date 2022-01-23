package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	public class ExcelReader {

		public List<Map<String, String>> getData(String excelFilePath, String sheetName)
				throws InvalidFormatException, IOException {
			XSSFSheet sheet = getSheetByName(excelFilePath, sheetName);
			return readSheet(sheet);
		}

		public List<Map<String, String>> getData(String excelFilePath, int sheetNumber)
				throws InvalidFormatException, IOException {
			XSSFSheet sheet = getSheetByIndex(excelFilePath, sheetNumber);
			return readSheet(sheet);
		}

		private XSSFSheet getSheetByName(String excelFilePath, String sheetName) throws IOException, InvalidFormatException {
			XSSFSheet sheet = getWorkBook(excelFilePath).getSheet(sheetName);
			return sheet;
		}

		private XSSFSheet getSheetByIndex(String excelFilePath, int sheetNumber) throws IOException, InvalidFormatException {
			XSSFSheet sheet = getWorkBook(excelFilePath).getSheetAt(sheetNumber);
			return sheet;
		}

		private XSSFWorkbook getWorkBook(String excelFilePath) throws IOException, InvalidFormatException {
			FileInputStream fis = new FileInputStream(new File(excelFilePath));
			XSSFWorkbook workbook=new XSSFWorkbook (fis);
			return workbook;
		}

		private List<Map<String, String>> readSheet(XSSFSheet sheet) {
			XSSFRow row;
			int totalRow = sheet.getPhysicalNumberOfRows();
			List<Map<String, String>> excelRows = new ArrayList<Map<String, String>>();
			int headerRowNumber = getHeaderRowNumber(sheet);
			if (headerRowNumber != -1) {
				int totalColumn = sheet.getRow(headerRowNumber).getLastCellNum();
				int setCurrentRow = 1;
				for (int currentRow = setCurrentRow; currentRow <= totalRow; currentRow++) {
					row = (XSSFRow) getRow(sheet, sheet.getFirstRowNum() + currentRow);
					LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<String, String>();
					for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
						columnMapdata.putAll(getCellValue(sheet, row, currentColumn));
					}
					excelRows.add(columnMapdata);
				}
			}
			return excelRows;
		}

		private int getHeaderRowNumber(XSSFSheet sheet) {
			Row row;
			int totalRow = sheet.getLastRowNum();
			for (int currentRow = 0; currentRow <= totalRow + 1; currentRow++) {
				row = getRow(sheet, currentRow);
				if (row != null) {
					int totalColumn = row.getLastCellNum();
					for (int currentColumn = 0; currentColumn < totalColumn; currentColumn++) {
						Cell cell;
						cell = row.getCell(currentColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						if (cell.getCellType() == CellType.STRING) {
							return row.getRowNum();

						} else if (cell.getCellType() == CellType.NUMERIC) {
							return row.getRowNum();

						} else if (cell.getCellType() == CellType.BOOLEAN) {
							return row.getRowNum();
						} else if (cell.getCellType() == CellType.ERROR) {
							return row.getRowNum();
						}
					}
				}
			}
			return (-1);
		}

		private Row getRow(XSSFSheet sheet, int rowNumber) {
			return sheet.getRow(rowNumber);
		}

		private LinkedHashMap<String, String> getCellValue(XSSFSheet sheet, XSSFRow row, int currentColumn) {
			LinkedHashMap<String, String> columnMapdata = new LinkedHashMap<String, String>();
			XSSFCell cell;
			if (row == null) {
				if (sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getCellType() != CellType.BLANK) {
					String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(currentColumn)
							.getStringCellValue();
					columnMapdata.put(columnHeaderName, "");
				}
			} else {
				cell = row.getCell(currentColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if (cell.getCellType() == CellType.STRING) {
					if (sheet.getRow(sheet.getFirstRowNum())
							.getCell(cell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getCellType() != CellType.BLANK) {
						String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
								.getStringCellValue();
						columnMapdata.put(columnHeaderName, cell.getStringCellValue());
					}
				} else if (cell.getCellType() == CellType.NUMERIC) {
					if (sheet.getRow(sheet.getFirstRowNum())
							.getCell(cell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getCellType() != CellType.BLANK) {
						String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
								.getStringCellValue();
						columnMapdata.put(columnHeaderName, NumberToTextConverter.toText(cell.getNumericCellValue()));
					}
				} else if (cell.getCellType() == CellType.BLANK) {
					if (sheet.getRow(sheet.getFirstRowNum())
							.getCell(cell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getCellType() != CellType.BLANK) {
						String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
								.getStringCellValue();
						columnMapdata.put(columnHeaderName, "");
					}
				} else if (cell.getCellType() == CellType.BOOLEAN) {
					if (sheet.getRow(sheet.getFirstRowNum())
							.getCell(cell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getCellType() != CellType.BLANK) {
						String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
								.getStringCellValue();
						columnMapdata.put(columnHeaderName, Boolean.toString(cell.getBooleanCellValue()));
					}
				} else if (cell.getCellType() == CellType.ERROR) {
					if (sheet.getRow(sheet.getFirstRowNum())
							.getCell(cell.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
							.getCellType() != CellType.BLANK) {
						String columnHeaderName = sheet.getRow(sheet.getFirstRowNum()).getCell(cell.getColumnIndex())
								.getStringCellValue();
						columnMapdata.put(columnHeaderName, Byte.toString(cell.getErrorCellValue()));
					}
				}
			}
			return columnMapdata;
		}
	}
