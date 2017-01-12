package spreadsheet.mapper.samples;

import spreadsheet.mapper.f2w.read.Excel2WorkbookReader;
import spreadsheet.mapper.f2w.read.WorkbookReader;
import spreadsheet.mapper.model.Person;
import spreadsheet.mapper.model.core.Row;
import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.SheetMeta;
import spreadsheet.mapper.w2o.process.DefaultSheetProcessHelper;
import spreadsheet.mapper.w2o.process.SheetProcessHelper;
import spreadsheet.mapper.w2o.process.factory.DefaultSheetMetaFactory;
import spreadsheet.mapper.w2o.process.factory.ObjectFactory;
import spreadsheet.mapper.w2o.process.factory.SheetMetaFactory;
import spreadsheet.mapper.w2o.process.setter.LocalDateValueSetter;

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

    SheetMetaFactory sheetMetaFactory = new DefaultSheetMetaFactory(1, 3, new int[]{1, 2});

    WorkbookReader reader = new Excel2WorkbookReader();

    Workbook workbook = reader.read(inputStream);

    Sheet sheet = workbook.getFirstSheet();

    SheetMeta sheetMeta = sheetMetaFactory.create(sheet);

    SheetProcessHelper<Person> sheetProcessHelper = new DefaultSheetProcessHelper<>();

    sheetProcessHelper.sheet(sheet).sheetMeta(sheetMeta).objectFactory(new PersonFactory());

    sheetProcessHelper.fieldValueSetters(
        new LocalDateValueSetter<>("yyyy-MM-dd", "birthday")
    );

    List<Person> personList = sheetProcessHelper.process();

    for (Person person : personList) {
      System.out.println(person);
    }
  }

  private static class PersonFactory implements ObjectFactory<Person> {

    @Override
    public Person create(Row row) {
      return new Person();
    }
  }
}
