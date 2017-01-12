package spreadsheet.mapper.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.LocalDate;

/**
 * Created by hanwen on 2017/1/10.
 */
public class Person extends ModelBase implements Model {

  private String name;

  private int age;

  private Gender gender = new Gender();

  private String idCardNumber;

  private IdCardType idCardType = new IdCardType();

  private LocalDate birthday;

  private String address;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getIdCardNumber() {
    return idCardNumber;
  }

  public void setIdCardNumber(String idCardNumber) {
    this.idCardNumber = idCardNumber;
  }

  public IdCardType getIdCardType() {
    return idCardType;
  }

  public void setIdCardType(IdCardType idCardType) {
    this.idCardType = idCardType;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("name", name)
        .append("age", age)
        .append("gender", gender)
        .append("idCardNumber", idCardNumber)
        .append("idCardType", idCardType)
        .append("birthday", birthday)
        .append("address", address)
        .toString();
  }
}
