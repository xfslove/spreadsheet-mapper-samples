package spreadsheet.mapper.utils;

import org.apache.commons.lang3.StringUtils;
import spreadsheet.mapper.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanwen on 2017/1/18.
 */
public class PromptBuilder {

  private static final String REQUIRE = "必填";

  private static final String POS_NUMBER = "正数";

  private static final String NUMBER = "数字";

  private static final String POS_INTEGER = "正整数";

  private static final String INTEGER = "整数";

  private static final String BOOL = "\'是\':是,1,yes,y \'否\':否,0.no,n";

  private static final String DATE_FORMAT = "格式应为: %s";

  private Map<String, List<String>> prompts = new HashMap<>();

  public PromptBuilder require(String... fields) {
    if (fields == null) {
      return this;
    }

    for (String field : fields) {
      addPrompt(field, REQUIRE);
    }
    return this;
  }

  public PromptBuilder number(String... fields) {
    if (fields == null) {
      return this;
    }

    for (String field : fields) {
      addPrompt(field, NUMBER);
    }
    return this;
  }

  public PromptBuilder posNumber(String... fields) {
    if (fields == null) {
      return this;
    }
    for (String field : fields) {
      addPrompt(field, POS_NUMBER);
    }
    return this;
  }

  public PromptBuilder integer(String... fields) {
    if (fields == null) {
      return this;
    }
    for (String field : fields) {
      addPrompt(field, INTEGER);
    }
    return this;
  }

  public PromptBuilder posInteger(String... fields) {
    if (fields == null) {
      return this;
    }
    for (String field : fields) {
      addPrompt(field, POS_INTEGER);
    }
    return this;
  }

  public PromptBuilder bool(String... fields) {
    if (fields == null) {
      return this;
    }
    for (String field : fields) {
      addPrompt(field, BOOL);
    }
    return this;
  }

  public PromptBuilder format(String format, String... fields) {
    if (fields == null) {
      return this;
    }
    for (String field : fields) {
      addPrompt(field, String.format(DATE_FORMAT, format));
    }
    return this;
  }

  public Map<String, String> build() {

    Map<String, String> prompts = new HashMap<>();
    for (Map.Entry<String, List<String>> entry : this.prompts.entrySet()) {
      prompts.put(entry.getKey(), StringUtils.join(entry.getValue(), Constants.COMMA_SEPARATOR));
    }
    return prompts;
  }

  private void addPrompt(String field, String prompt) {
    if (!prompts.containsKey(field)) {
      prompts.put(field, new ArrayList<>());
    }
    prompts.get(field).add(prompt);
  }
}
