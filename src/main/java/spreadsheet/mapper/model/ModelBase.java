package spreadsheet.mapper.model;

/**
 * Created by hanwen on 2017/1/10.
 */
public abstract class ModelBase implements Model {

  private long id;

  @Override
  public long getId() {
    return id;
  }

  @Override
  public void setId(long id) {
    this.id = id;
  }
}
