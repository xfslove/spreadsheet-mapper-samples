package spreadsheet.mapper.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by hanwen on 2017/1/10.
 */
public class Gender extends ModelBase implements Model {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("name", name)
        .toString();
  }
}

