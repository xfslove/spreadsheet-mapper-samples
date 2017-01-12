package spreadsheet.mapper.samples;

import spreadsheet.mapper.f2w.read.Excel2WorkbookReader;
import spreadsheet.mapper.f2w.read.WorkbookReader;
import spreadsheet.mapper.m2f.write.Message2ExcelWriter;
import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.SheetMeta;
import spreadsheet.mapper.w2o.process.factory.DefaultSheetMetaFactory;
import spreadsheet.mapper.w2o.process.factory.SheetMetaFactory;
import spreadsheet.mapper.w2o.validation.DefaultSheetValidationHelper;
import spreadsheet.mapper.w2o.validation.validator.cell.DigitsValidator;
import spreadsheet.mapper.w2o.validation.validator.cell.LocalDateValidator;
import spreadsheet.mapper.w2o.validation.validator.cell.RequireValidator;
import spreadsheet.mapper.w2o.validation.validator.row.MultiUniqueInImportFileValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by hanwen on 2017/1/11.
 */
public class SimpleValidationApp {

  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(args[0]);
    valid(file);
  }

  public static void valid(File file) throws FileNotFoundException {

    SheetMetaFactory sheetMetaFactory = new DefaultSheetMetaFactory(1, 3, new int[]{1, 2});

    WorkbookReader reader = new Excel2WorkbookReader();

    Workbook workbook = reader.read(new FileInputStream(file));

    Sheet sheet = workbook.getFirstSheet();

    SheetMeta sheetMeta = sheetMetaFactory.create(sheet);

    DefaultSheetValidationHelper sheetValidationHelper = new DefaultSheetValidationHelper();

    sheetValidationHelper.sheet(sheet).sheetMeta(sheetMeta);

    sheetValidationHelper.cellValidators(
        new RequireValidator()
            .group("require.fields")
            .matchFields("name", "birthday", "age", "idCardNumber", "idCardType.name", "gender.name")
            .errorMessage("required")
            .end()
    );
    sheetValidationHelper.cellValidators(
        new LocalDateValidator().matchField("birthday").pattern("yyyy-MM-dd").dependsOn("require.fields").end()
    );
    sheetValidationHelper.cellValidators(
        new DigitsValidator().matchField("age").dependsOn("require.fields").end()
    );
    sheetValidationHelper.rowValidators(
        new MultiUniqueInImportFileValidator()
            .multiUniqueFields("idCardNumber", "idCardType.name")
            .group("identify.unique")
            .dependsOn("require.fields")
            .errorMessage("identify must multi unique")
            .end()
    );

    // do valid
    boolean valid = sheetValidationHelper.valid();

    if (!valid) {
      // write error message
      Message2ExcelWriter writer = new Message2ExcelWriter(new FileInputStream(file));
      writer.write(sheetValidationHelper.getErrorMessages(), new FileOutputStream(file));
    }
  }
}
