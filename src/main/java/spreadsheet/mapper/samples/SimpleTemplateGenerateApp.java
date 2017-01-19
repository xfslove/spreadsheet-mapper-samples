package spreadsheet.mapper.samples;

import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.WorkbookMeta;
import spreadsheet.mapper.o2w.compose.DefaultSheetComposeHelper;
import spreadsheet.mapper.o2w.compose.DefaultWorkbookComposeHelper;
import spreadsheet.mapper.o2w.compose.SheetComposeHelper;
import spreadsheet.mapper.o2w.compose.WorkbookComposeHelper;
import spreadsheet.mapper.utils.PromptBuilder;
import spreadsheet.mapper.utils.SingleSheetWorkbookMetaBuilder;
import spreadsheet.mapper.w2f.write.Workbook2ExcelWriteHelper;
import spreadsheet.mapper.w2f.write.WorkbookWriteHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;

/**
 * Created by hanwen on 2017/1/10.
 */
public class SimpleTemplateGenerateApp {

  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(args[0]);
    generate(new FileOutputStream(file));
  }

  public static void generate(OutputStream outputStream) throws FileNotFoundException {

    SingleSheetWorkbookMetaBuilder workbookMetaBuilder = new SingleSheetWorkbookMetaBuilder();

    WorkbookMeta workbookMeta = workbookMetaBuilder
        .fields("name", "birthday", "age", "idCardNumber", "idCardType.name", "address", "gender.name")
        .titles("name", "birthday", "age", "idCardNumber", "idCardType.name", "address", "gender.name")
        .prompts(
            new PromptBuilder()
                .require(
                    "name", "birthday", "age", "idCardNumber", "idCardType.name", "gender.name"
                ).number("age")
                .build()
        )
        .prompt("birthday", "format:yyyy-MM-dd")
        .toWorkbookMeta();

    SheetComposeHelper sheetComposeHelper = new DefaultSheetComposeHelper<>();

    WorkbookComposeHelper workbookComposeHelper = new DefaultWorkbookComposeHelper();

    Workbook workbook = workbookComposeHelper.addSheetComposeHelper(sheetComposeHelper).compose(Collections.singletonList(null), workbookMeta);

    WorkbookWriteHelper workbookWriter = new Workbook2ExcelWriteHelper();

    workbookWriter.write(workbook, outputStream);

  }
}
