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
import spreadsheet.mapper.w2o.validation.validator.row.MultiUniqueValidator;

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


    // read excel to workbook
    WorkbookReader reader = new Excel2WorkbookReader();

    Workbook workbook = reader.read(new FileInputStream(file));

    Sheet sheet = workbook.getFirstSheet();

    // get sheet meta from excel
    SheetMetaFactory sheetMetaFactory = new DefaultSheetMetaFactory(1, 3, 1, 2);

    SheetMeta sheetMeta = sheetMetaFactory.create(sheet);

    DefaultSheetValidationHelper sheetValidationHelper = new DefaultSheetValidationHelper();

    sheetValidationHelper.sheet(sheet).sheetMeta(sheetMeta);
    sheetValidationHelper.cellValidators(
        new RequireValidator().matchField("name").errorMessage("required"),
        new RequireValidator().matchField("birthday").errorMessage("required"),
        new RequireValidator().matchField("age").errorMessage("required"),
        new RequireValidator().matchField("idCardNumber").errorMessage("required"),
        new RequireValidator().matchField("idCardType.name").errorMessage("required"),
        new RequireValidator().matchField("gender.name").errorMessage("required")
    );
    sheetValidationHelper.cellValidators(
        new LocalDateValidator().matchField("birthday").pattern("yyyy-MM-dd")
    );
    sheetValidationHelper.cellValidators(
        new DigitsValidator().matchField("age")
    );
    sheetValidationHelper.rowValidators(
        new MultiUniqueValidator()
            .multiUniqueFields("idCardNumber", "idCardType.name")
            .group("identify.unique")
            .dependsOn("idCardType.name", "idCardNumber")
            .errorMessage("identify must multi unique")
            
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
