import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;


public class OrderTrackNumberGetTest {

  OrderSteps orderSteps;

  @Before
  public void setUp() {
    orderSteps = new OrderSteps();
  }


  @Test
  @DisplayName("Успешно получаем созданный заказ по его существующему номеру")
  @Description(value = "Проверка, что заказ находится по существующему номеру заказа")
  public void getOrderByValidTrackNumber() {
    //Создание заказа из JSON файла и проверка успешности его создания для наполнения списка заказов
    OrderSteps orderStep = new OrderSteps();
    Response createOrderResponse = orderStep.createNewOrderFromJsonInFile();
    orderStep.checkOrderTrackNotNullNew(createOrderResponse);
    System.out.println("Response of API track is " + createOrderResponse.asString());

    //Response class has method path() using that, user can give the json path to get the particular value.
    //Ответ API конвертируем в строку и из строки достаём значение по ключу track
    String orderNumberTrack = createOrderResponse.path("track").toString();

    //convert a String to an int in Java
    int trackNumber = Integer.parseInt(orderNumberTrack);
    System.out.println("New order number track is " + trackNumber);

    //Проверка ответа 200 и непустоты тела ответа при вводе существующего треккингового номера заказа
    orderStep.checkGetListOrderByValidTrackNumber(trackNumber);

    //Отмена заказа
    orderStep.checkCancelOrderByValidTrackNumber(trackNumber);

  }

  @Test
  @DisplayName("Получаем ошибку при попытке поиска заказа по несуществующему номеру")
  @Description(value = "Проверка, что выходит ошибка \"Заказ не найден\" со статусом 404")
  public void getOrderByInValidTrackNumber() {
    OrderSteps orderStep = new OrderSteps();
    orderStep.checkGetListOrderByInvalidTrackNumber();
  }

  @Test
  @DisplayName("Получаем ошибку при попытке поиска заказа без треккингового номера")
  @Description(value = "Проверка, что выходит ошибка \"Недостаточно данных для поиска\" со статусом 400")
  public void getOrderWithEmptyTrackNumber() {
    OrderSteps orderStep = new OrderSteps();
    orderStep.checkGetListOrderByEmptyTrackNumber();
  }
}
