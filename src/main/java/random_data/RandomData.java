package random_data;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {

  //генерирование случайных значение типа String
  public static String RANDOM_LOGIN = RandomStringUtils.randomAlphabetic(11);
  public static String RANDOM_PASS = RandomStringUtils.randomNumeric(10);
  public static String RANDOM_NAME = RandomStringUtils.randomAlphabetic(10);
  public static String RANDOM_COURIER_ID = RandomStringUtils.randomNumeric(4);
  public static int RANDOM_ORDER_TRACK = Integer.parseInt(RandomStringUtils.randomNumeric(5));
  public static String RANDOM_ORDER_ID = RandomStringUtils.randomNumeric(8);
}
