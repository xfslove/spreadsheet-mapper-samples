package spreadsheet.mapper.samples;

import spreadsheet.mapper.f2w.read.Excel2WorkbookReadHelper;
import spreadsheet.mapper.f2w.read.WorkbookReadHelper;
import spreadsheet.mapper.model.Person;
import spreadsheet.mapper.model.core.Row;
import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.SheetMeta;
import spreadsheet.mapper.model.meta.WorkbookMeta;
import spreadsheet.mapper.utils.SingleSheetWorkbookMetaFactory;
import spreadsheet.mapper.w2o.process.DefaultSheetProcessHelper;
import spreadsheet.mapper.w2o.process.ObjectFactory;
import spreadsheet.mapper.w2o.process.SheetProcessHelper;
import spreadsheet.mapper.w2o.process.setter.LocalDateSetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by hanwen on 2017/1/12.
 */
public class SimpleProcessApp {

  public static void main(String[] args) throws FileNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    File file = new File(args[0]);
    process(new FileInputStream(file));
  }

  public static void process(InputStream inputStream) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

    // read excel to workbook
    WorkbookReadHelper reader = new Excel2WorkbookReadHelper();

    Workbook workbook = reader.read(inputStream);

    Sheet sheet = workbook.getFirstSheet();

    // get sheet meta from excel
    SingleSheetWorkbookMetaFactory workbookMetaFactory = new SingleSheetWorkbookMetaFactory();

    WorkbookMeta workbookMeta = workbookMetaFactory.create(workbook);

    SheetProcessHelper<Person> sheetProcessHelper = new DefaultSheetProcessHelper<>();

    sheetProcessHelper.setObjectFactory(new PersonFactory());

    sheetProcessHelper.addFieldSetter(
        new LocalDateSetter<Person>().pattern("yyyy-MM-dd").matchField("birthday")
    );

    // workbook to objects
    List<Person> personList = sheetProcessHelper.process(workbook.getFirstSheet(), workbookMeta.getFirstSheetMeta());

    System.out.println("-------------------------blow is the person information from excel------------------------------");
    for (Person person : personList) {
      System.out.println(person);
    }
  }

  private static class PersonFactory implements ObjectFactory<Person> {

    @Override
    public Person create(Row row, SheetMeta sheetMeta) {
      return new Person();
    }
  }
}
