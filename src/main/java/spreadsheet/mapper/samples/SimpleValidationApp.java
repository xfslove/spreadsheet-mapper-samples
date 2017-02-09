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
import spreadsheet.mapper.w2o.validation.rule.DefaultDependencyValidatorBuilder;
import spreadsheet.mapper.w2o.validation.rule.DependencyValidatorFactoryRegisterer;
import spreadsheet.mapper.w2o.validation.validator.cell.DependencyValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

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

    List<DependencyValidator> validators = new DefaultDependencyValidatorBuilder()
        .single(DependencyValidatorFactoryRegisterer.RULE_REQUIRE)
        .matchFields("name", "birthday", "age", "idCardNumber", "idCardType.name", "gender.name")
        .errorMessage("required")
        .end()
        .single(DependencyValidatorFactoryRegisterer.RULE_LOCAL_DATE)
        .matchFields("birthday")
        .param("yyyy-MM-dd")
        .errorMessage("format:[yyyy-MM-dd]")
        .end()
        .single(DependencyValidatorFactoryRegisterer.RULE_DIGITS)
        .matchFields("age")
        .errorMessage("not digits")
        .end()
        .multi(DependencyValidatorFactoryRegisterer.RULE_MULTI_UNIQUE)
        .group("identify.unique")
        .matchFields("idCardNumber", "idCardType.name")
        .dependsOn("idCardNumber", "idCardType.name")
        .errorMessage("identify must multi unique")
        .end()
        .build();

    for (DependencyValidator validator : validators) {
      sheetValidationHelper.addDependencyValidator(validator);
    }

    // do valid
    boolean valid = sheetValidationHelper.valid(workbook.getFirstSheet(), workbookMeta.getFirstSheetMeta());

    if (!valid) {
      // write error message
      Message2ExcelWriteHelper writer = new Message2ExcelWriteHelper(new FileInputStream(file));
      writer.write(sheetValidationHelper.getErrorMessages(), new FileOutputStream(file));
    }
  }
}
