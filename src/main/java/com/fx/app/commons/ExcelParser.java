package com.fx.app.commons;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelParser {
	
	private static class ExcelRowIterable implements Iterable<Map<String, Object>>
	{
		private Iterator<Row> rowIterator;
		private String[] columns;		
		

		public ExcelRowIterable(Iterator<Row> rowIterator, String[] columns) {
			this.rowIterator = rowIterator;
			this.columns = columns;
		}

		@Override
		public Iterator<Map<String, Object>> iterator() {
			return new ExcelRowIterator(rowIterator, columns);
		}
		
	}
	
	private static class ExcelRowIterator implements Iterator<Map<String, Object>>{
		private Iterator<Row> rowIterator;
		private String[] columns;		

		public ExcelRowIterator(Iterator<Row> rowIterator, String[] columns) {
			this.rowIterator = rowIterator;
			this.columns = columns;
		}

		@Override
		public boolean hasNext() {
			return rowIterator.hasNext();
		}

		@Override
		public Map<String, Object> next() {
			Row row = rowIterator.next();
			Map<String, Object> cellValuePair = new LinkedHashMap<String, Object>();
			Iterator<Cell> cellIterator = row.cellIterator();
			int index = 0;
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				if (index < columns.length){
					cellValuePair.put(columns[index], getCellValue(cell));
				}
				index++;
			}
			return cellValuePair;
		}

		private Object getCellValue(Cell cell) {
			switch (cell.getCellType()) {
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case NUMERIC:
				return cell.getNumericCellValue();
			case STRING:
				return cell.getStringCellValue();
			default:
				return "";
			}
		}
	}
	
	
	private String[] columns;
	private boolean skipHeader;
	
	public ExcelParser withHeader(String... headers)
	{
		columns = (String[]) ArrayUtils.clone(headers);
		return this;
	}
	
	public ExcelParser withSkipHeaderRecord(boolean skip)
	{
		skipHeader = skip;
		return this;
	}
	
	public Iterable<Map<String, Object>> parse(File inputFile, int sheetIndex) throws InvalidFormatException, IOException
	{
		Workbook workbook = WorkbookFactory.create(inputFile);
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		
		Iterator<Row> rowIterator = sheet.iterator();
		if(skipHeader){
			rowIterator.next();
		}
		return new ExcelRowIterable(rowIterator, columns);
	}

}
