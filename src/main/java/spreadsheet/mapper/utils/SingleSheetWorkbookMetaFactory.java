package spreadsheet.mapper.utils;

import org.apache.commons.lang3.StringUtils;
import spreadsheet.mapper.model.core.Cell;
import spreadsheet.mapper.model.core.Row;
import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.*;
import spreadsheet.mapper.w2o.WorkbookMetaFactory;

/**
 * Created by hanwen on 2017/1/18.
 */
public class SingleSheetWorkbookMetaFactory implements WorkbookMetaFactory {

  private static final int[] headerRowIndices = new int[]{1, 2, 3};

  private static final int fieldRowIndex = 2;

  private static final int dataStartRowIndex = 4;

  @Override
  public WorkbookMeta create(Workbook workbook) {

    Sheet sheet = workbook.getFirstSheet();

    SheetMeta sheetMeta = new SheetMetaBean(sheet.getName(), dataStartRowIndex);

    Row fieldRow = sheet.getRow(fieldRowIndex);

    for (int i = 1; i <= fieldRow.sizeOfCells(); i++) {

      String fieldName = fieldRow.getCell(i).getValue();

      if (StringUtils.isBlank(fieldName)) {
        continue;
      }

      FieldMeta fieldMeta = new FieldMetaBean(fieldName, i);

      for (int headerRowIndex : headerRowIndices) {
        Cell headerCell = sheet.getRow(headerRowIndex).getCell(i);
        if (StringUtils.isBlank(headerCell.getValue())) {
          continue;
        }

        HeaderMeta headerMeta = new HeaderMetaBean(headerRowIndex, headerCell.getValue());
        fieldMeta.addHeaderMeta(headerMeta);
      }

      sheetMeta.addFieldMeta(fieldMeta);
    }

    WorkbookMeta workbookMeta = new WorkbookMetaBean();
    workbookMeta.addSheetMeta(sheetMeta);

    return workbookMeta;
  }
}
