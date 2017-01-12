package spreadsheet.mapper.samples;

import org.joda.time.LocalDate;
import spreadsheet.mapper.model.Gender;
import spreadsheet.mapper.model.IdCardType;
import spreadsheet.mapper.model.Person;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanwen on 2017/1/12.
 */
public class SimpleExportApp {

  public static void main(String[] args) throws FileNotFoundException {
    File file = new File(args[0]);
    generate(new FileOutputStream(file));
  }

  public static void generate(OutputStream outputStream) throws FileNotFoundException {

    SequenceBasedSheetMetaBuilder sheetMetaBuilder = new SequenceBasedSheetMetaBuilder();

    SheetMeta sheetMeta = sheetMetaBuilder
        .field("name").header("name").next()
        .field("birthday").header("birthday").next()
        .field("age").header("age").next()
        .field("idCardNumber").header("idCardNumber").next()
        .field("idCardType.name").header("idCardType.name").next()
        .field("address").header("address").next()
        .field("gender.name").header("gender.name").next()
        .toSheetMeta(2);

    SheetComposeHelper<Person> sheetComposeHelper = new DefaultSheetComposeHelper<Person>().sheetMeta(sheetMeta).data(getPersons());

    WorkbookComposeHelper workbookComposeHelper = new DefaultWorkbookComposeHelper();

    Workbook workbook = workbookComposeHelper.sheetComposes(sheetComposeHelper).compose();

    WorkbookWriter workbookWriter = new Workbook2ExcelWriter(true);

    workbookWriter.write(workbook, outputStream);

  }

  private static List<Person> getPersons() {

    List<Person> people = new ArrayList<>();

    IdCardType idCardType1 = new IdCardType();
    idCardType1.setName("idCardType1");

    IdCardType idCardType2 = new IdCardType();
    idCardType2.setName("idCardType2");

    Gender gender1 = new Gender();
    gender1.setName("male");

    Gender gender2 = new Gender();
    gender2.setName("female");

    Person person1 = new Person();
    person1.setGender(gender1);
    person1.setIdCardType(idCardType1);
    person1.setIdCardNumber("24398539034");
    person1.setName("person1");
    person1.setAddress("Pudong");
    person1.setAge(18);
    person1.setBirthday(LocalDate.now());

    people.add(person1);

    Person person2 = new Person();
    person2.setGender(gender2);
    person2.setIdCardType(idCardType1);
    person2.setIdCardNumber("24398539035");
    person2.setName("person2");
    person2.setAddress("Pudong");
    person2.setAge(18);
    person2.setBirthday(LocalDate.now());

    people.add(person2);

    Person person3 = new Person();
    person3.setGender(gender2);
    person3.setIdCardType(idCardType1);
    person3.setIdCardNumber("24398539036");
    person3.setName("person3");
    person3.setAddress("Pudong");
    person3.setAge(18);
    person3.setBirthday(LocalDate.now());

    people.add(person3);

    Person person4 = new Person();
    person4.setGender(gender1);
    person4.setIdCardType(idCardType2);
    person4.setIdCardNumber("24398539037");
    person4.setName("person4");
    person4.setAddress("Pudong");
    person4.setAge(18);
    person4.setBirthday(LocalDate.now());

    people.add(person4);

    Person person5 = new Person();
    person5.setGender(gender1);
    person5.setIdCardType(idCardType1);
    person5.setIdCardNumber("24398539038");
    person5.setName("person5");
    person5.setAddress("Puxi");
    person5.setAge(18);
    person5.setBirthday(LocalDate.now());

    people.add(person5);

    return people;
  }
}
