package spreadsheet.mapper.utils;

import spreadsheet.mapper.model.core.Row;
import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.meta.FieldMeta;
import spreadsheet.mapper.model.meta.SheetMeta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Created by hanwen on 2017/1/24.
 */
public abstract class FieldValueCollector {

  public static Set<String> getDistinctValueOfField(Sheet sheet, SheetMeta sheetMeta, String fieldName) {

    FieldMeta fieldMeta = sheetMeta.getFieldMeta(fieldName);

    if (fieldMeta == null) {
      return Collections.emptySet();
    }

    Set<String> result = new HashSet<>();

    for (int i = sheetMeta.getDataStartRowIndex(); i <= sheet.sizeOfRows(); i++) {
      Row row = sheet.getRow(i);

      result.add(row.getCell(fieldMeta.getColumnIndex()).getValue());
    }

    return result;
  }

  public static <T> Set<T> getDistinctValueOfFields(Sheet sheet, SheetMeta sheetMeta, BiFunction<Row, SheetMeta, T> valueCollector) {

    Set<T> result = new HashSet<>();

    for (int i = sheetMeta.getDataStartRowIndex(); i <= sheet.sizeOfRows(); i++) {
      Row row = sheet.getRow(i);

      result.add(valueCollector.apply(row, sheetMeta));
    }

    return result;
  }
}
