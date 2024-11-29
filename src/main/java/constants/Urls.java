package constants;

public class Urls {

  public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";
  public static final String COURIER_POST_CREATE = "/api/v1/courier"; // Создание курьера
  public static final String COURIER_POST_LOGIN = "/api/v1/courier/login"; // Логин курьера
  public static final String COURIER_DELETE = "/api/v1/courier/"; // Удаление курьера
  public static final String ORDER_POST_CREATE = "/api/v1/orders"; // Создание заказа
  public static final String ORDER_GET_LIST = "/api/v1/orders"; // Получение списка заказов
  public static final String ORDER_GET_BY_NUMBER = "/api/v1/orders/track"; // Получение заказа по его номеру

}