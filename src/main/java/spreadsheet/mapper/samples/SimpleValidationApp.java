package spreadsheet.mapper.samples;

import spreadsheet.mapper.f2w.read.Excel2WorkbookReadHelper;
import spreadsheet.mapper.f2w.read.WorkbookReadHelper;
import spreadsheet.mapper.m2f.write.Message2ExcelWriteHelper;
import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.WorkbookMeta;
import spreadsheet.mapper.utils.SingleSheetWorkbookMetaFactory;
import spreadsheet.mapper.w2o.WorkbookMetaFactory;
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
    WorkbookReadHelper reader = new Excel2WorkbookReadHelper();

    Workbook workbook = reader.read(new FileInputStream(file));

    Sheet sheet = workbook.getFirstSheet();

    // get sheet meta from excel
    WorkbookMetaFactory workbookMetaFactory = new SingleSheetWorkbookMetaFactory();
    WorkbookMeta workbookMeta = workbookMetaFactory.create(workbook);

    DefaultSheetValidationHelper sheetValidationHelper = new DefaultSheetValidationHelper();

    sheetValidationHelper.addCellValidator(
        new RequireValidator().matchField("name").errorMessage("required"));
    sheetValidationHelper.addCellValidator(
        new RequireValidator().matchField("birthday").errorMessage("required"));
    sheetValidationHelper.addCellValidator(
        new RequireValidator().matchField("age").errorMessage("required"));
    sheetValidationHelper.addCellValidator(
        new RequireValidator().matchField("idCardNumber").errorMessage("required"));
    sheetValidationHelper.addCellValidator(
        new RequireValidator().matchField("idCardType.name").errorMessage("required"));
    sheetValidationHelper.addCellValidator(
        new RequireValidator().matchField("gender.name").errorMessage("required"));
    sheetValidationHelper.addCellValidator(
        new LocalDateValidator().matchField("birthday").pattern("yyyy-MM-dd"));
    sheetValidationHelper.addCellValidator(
        new DigitsValidator().matchField("age"));
    sheetValidationHelper.addRowValidator(
        new MultiUniqueValidator()
            .multiUniqueFields("idCardNumber", "idCardType.name")
            .group("identify.unique")
            .dependsOn("idCardType.name", "idCardNumber")
            .errorMessage("identify must multi unique")

    );

    // do valid
    boolean valid = sheetValidationHelper.valid(workbook.getFirstSheet(), workbookMeta.getFirstSheetMeta());

    if (!valid) {
      // write error message
      Message2ExcelWriteHelper writer = new Message2ExcelWriteHelper(new FileInputStream(file));
      writer.write(sheetValidationHelper.getErrorMessages(), new FileOutputStream(file));
    }
  }
}
