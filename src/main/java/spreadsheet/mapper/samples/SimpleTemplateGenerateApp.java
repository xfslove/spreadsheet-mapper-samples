package spreadsheet.mapper.samples;

import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.SheetMeta;
import spreadsheet.mapper.o2w.compose.DefaultSheetComposeHelper;
import spreadsheet.mapper.o2w.compose.DefaultWorkbookComposeHelper;
import spreadsheet.mapper.o2w.compose.SheetComposeHelper;
import spreadsheet.mapper.o2w.compose.WorkbookComposeHelper;
import spreadsheet.mapper.o2w.compose.builder.SequenceBasedSheetMetaBuilder;
import spreadsheet.mapper.w2f.write.Workbook2ExcelWriter;
import spreadsheet.mapper.w2f.write.WorkbookWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by hanwen on 2017/1/10.
 */
public class SimpleTemplateGenerateApp {

  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(args[0]);
    generate(new FileOutputStream(file));
  }

  public static void generate(OutputStream outputStream) throws FileNotFoundException {

    SequenceBasedSheetMetaBuilder sheetMetaBuilder = new SequenceBasedSheetMetaBuilder();

    SheetMeta sheetMeta = sheetMetaBuilder
        .field("name").headers("name", "required").next()
        .field("birthday").headers("birthday", "required, format:yyyy-MM-dd").next()
        .field("age").headers("age", "required, positive integer").next()
        .field("idCardNumber").headers("idCardNumber", "required").next()
        .field("idCardType.name").headers("idCardType.name", "required").next()
        .field("address").header("address").next()
        .field("gender.name").headers("gender.name", "required").next()
        .toSheetMeta();

    SheetComposeHelper sheetComposeHelper = new DefaultSheetComposeHelper<>().sheetMeta(sheetMeta);

    WorkbookComposeHelper workbookComposeHelper = new DefaultWorkbookComposeHelper();

    Workbook workbook = workbookComposeHelper.sheetComposes(sheetComposeHelper).compose();

    WorkbookWriter workbookWriter = new Workbook2ExcelWriter(true);

    workbookWriter.write(workbook, outputStream);

  }
}
