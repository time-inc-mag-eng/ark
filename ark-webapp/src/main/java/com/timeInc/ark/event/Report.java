/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.event;

import static com.timeInc.ark.event.ReportBean.*;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.DateFormatConverter;


/**
 * A report generator for {@link ReportBean}
 */
public interface Report {
	
	/**
	 * Generate report.
	 *
	 * @param stream the stream to write to
	 * @param logList list of logs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void generateReport(OutputStream stream, List<ReportBean> logList) throws IOException;

	/**
	 * Generates an excel report
	 */
	public static class ExcelReport implements Report {
		private static final int NO_COL = 7;
		private static TimeZone zone = TimeZone.getTimeZone("UTC");
		private static final String DATE_FORMAT = "MM/dd/yyyy h:mm a z";
		

		/* (non-Javadoc)
		 * @see com.timeInc.ark.log.Report#generateReport(java.io.OutputStream, java.util.List)
		 */
		@Override
		public void generateReport(OutputStream stream,
				List<ReportBean> logList) throws IOException {
			Workbook wb = new HSSFWorkbook();

			Sheet sheet = wb.createSheet("Report for Ark");

			SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
			ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("MOD(ROW(),2)");
			PatternFormatting fill1 = rule1.createPatternFormatting();
			fill1.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.index);
			fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
			
			int headerRow = 0;
			int startDataRow = headerRow + 2;

			sheetCF.addConditionalFormatting(new CellRangeAddress[] { new CellRangeAddress(startDataRow, logList.size(), 0, NO_COL - 1) }, rule1);


			insertHeader(headerRow, sheet, boldStyle(wb));

			CellStyle dataFormat = dateformat(wb);
			
			for(ReportBean log : logList) {
				insertData(sheet, startDataRow++, log, dataFormat);
			}

			autoSizeCols(sheet);

			wb.write(stream);
		}

		private static void autoSizeCols(Sheet sheet) {
			for(int i = 0; i < NO_COL; i++) {
				sheet.autoSizeColumn(i);
			}
		}

		private static void insertData(Sheet sheet, int row, ReportBean data, CellStyle cs) {
			int currentCol = 0;
			Row dataRow = sheet.createRow(row);

			insertCol(dataRow, currentCol++, data.getAppName(), cs);
			insertCol(dataRow, currentCol++, data.getName(), cs);
			insertCol(dataRow, currentCol++, data.getShortDate(), cs);
			insertCol(dataRow, currentCol++, data.getIssueName(), cs);
			insertCol(dataRow, currentCol++, data.getReferenceId(), cs);
			insertCol(dataRow, currentCol++, data.getDate(), cs);
			insertCol(dataRow, currentCol++, data.getDescription(), cs);			
		}
		
		private static CellStyle boldStyle(Workbook wb) {
			Font f = wb.createFont();
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);
			f.setUnderline(FontUnderline.SINGLE.getByteValue());
			CellStyle cs = wb.createCellStyle();
			cs.setFont(f);
			return cs;
		}
		
		private static CellStyle dateformat(Workbook wb) {
			CellStyle cs = wb.createCellStyle();
			DataFormat format = wb.createDataFormat();
			cs.setDataFormat(format.getFormat(DateFormatConverter.convert(Locale.US, DATE_FORMAT)));
			return cs;
		}
		
		private static void insertHeader(int row, Sheet sheet, CellStyle cs) {
			int currentCol = 0;
			Row headerRow = sheet.createRow(row);

			insertCol(headerRow, currentCol++, HEADER_APP_NAME, cs);
			insertCol(headerRow, currentCol++, HEADER_NAME, cs);
			insertCol(headerRow, currentCol++, HEADER_SHORT_DATE, cs);
			insertCol(headerRow, currentCol++, HEADER_ISSUE_NAME, cs);
			insertCol(headerRow, currentCol++, HEADER_REF_ID, cs);
			insertCol(headerRow, currentCol++, HEADER_ACTION_OCCURRED, cs);
			insertCol(headerRow, currentCol++, HEADER_ACTION_DESCRIPTION, cs);
		}


		private static void insertCol(Row row, int col, Object value, CellStyle style) {
			Cell c = row.createCell(col);

			if (value.getClass().isAssignableFrom(String.class)) {
				c.setCellValue((String) value);
			} else if (Date.class.isInstance(value)) {
				SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
				df.setTimeZone(zone);
				c.setCellValue(df.format((Date) value));
			}

			if(style != null) c.setCellStyle(style);
		}

	}



}
