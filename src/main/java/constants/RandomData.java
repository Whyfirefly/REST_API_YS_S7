package constants;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {

  //генерирование случайных значение типа String
  public static final String RANDOM_LOGIN = RandomStringUtils.randomAlphabetic(10);
  public static final String RANDOM_PASS = RandomStringUtils.randomNumeric(10);
  public static final String RANDOM_NAME = RandomStringUtils.randomAlphabetic(10);
  public static final String RANDOM_COURIER_ID = RandomStringUtils.randomNumeric(3);
  public static final int RANDOM_ORDER_TRACK = Integer.parseInt(RandomStringUtils.randomNumeric(6));


}
