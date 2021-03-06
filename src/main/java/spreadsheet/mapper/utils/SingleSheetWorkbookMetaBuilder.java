package spreadsheet.mapper.utils;

import org.apache.commons.lang3.StringUtils;
import spreadsheet.mapper.model.meta.*;
import spreadsheet.mapper.o2w.WorkbookMetaBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by hanwen on 2017/1/18.
 */
public class SingleSheetWorkbookMetaBuilder implements WorkbookMetaBuilder {

  private static final int titleRowIndex = 1;

  private static final int fieldRowIndex = 2;

  private static final int promptRowIndex = 3;

  private static final int dataStartRowIndex = 4;

  private SheetMeta sheetMeta = new SheetMetaBean(dataStartRowIndex);

  public SingleSheetWorkbookMetaBuilder fields(String... fields) {
    for (int i = 0; i < fields.length; i++) {
      FieldMetaBean fieldMeta = new FieldMetaBean(fields[i], i + 1);
      fieldMeta.addHeaderMeta(new HeaderMetaBean(fieldRowIndex, fields[i]));
      sheetMeta.addFieldMeta(fieldMeta);
    }
    return this;
  }

  public SingleSheetWorkbookMetaBuilder titles(String... titles) {
    List<FieldMeta> fieldMetas = sheetMeta.getFieldMetas();
    for (int i = 0; i < titles.length; i++) {
      fieldMetas.get(i).addHeaderMeta(new HeaderMetaBean(titleRowIndex, titles[i]));
    }
    return this;
  }

  public SingleSheetWorkbookMetaBuilder prompts(Map<String, String> prompts) {
    for (Map.Entry<String, String> entry : prompts.entrySet()) {
      FieldMeta fieldMeta = sheetMeta.getFieldMeta(entry.getKey());
      if (fieldMeta == null) {
        continue;
      }
      HeaderMeta headerMeta = fieldMeta.getHeaderMeta(promptRowIndex);

      if (headerMeta == null) {
        fieldMeta.addHeaderMeta(new HeaderMetaBean(promptRowIndex, entry.getValue()));
      } else {
        List<String> newHeaders = Arrays.asList(headerMeta.getValue(), entry.getValue());
        fieldMeta.removeHeaderMeta(promptRowIndex);
        fieldMeta.addHeaderMeta(new HeaderMetaBean(promptRowIndex, StringUtils.join(newHeaders, ",")));
      }
    }
    return this;
  }

  public SingleSheetWorkbookMetaBuilder prompt(String field, String... prompts) {
    FieldMeta fieldMeta = sheetMeta.getFieldMeta(field);
    if (fieldMeta != null) {
      HeaderMeta headerMeta = fieldMeta.getHeaderMeta(promptRowIndex);

      if (headerMeta == null) {
        fieldMeta.addHeaderMeta(new HeaderMetaBean(promptRowIndex, StringUtils.join(prompts, ",")));
      } else {
        List<String> newHeaders = new ArrayList<>();
        newHeaders.add(headerMeta.getValue());
        newHeaders.addAll(Arrays.asList(prompts));

        fieldMeta.removeHeaderMeta(promptRowIndex);
        fieldMeta.addHeaderMeta(new HeaderMetaBean(promptRowIndex, StringUtils.join(newHeaders, ",")));
      }

    }
    return this;
  }

  @Override
  public WorkbookMeta toWorkbookMeta() {
    WorkbookMeta workbookMeta = new WorkbookMetaBean();
    workbookMeta.addSheetMeta(sheetMeta);
    return workbookMeta;
  }

}
