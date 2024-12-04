package pojo;

public class OrderCreatedMain {
//поле типа OrderFieldsGetByTrackNumber
  private OrderFieldsGetByTrackNumber order;

  //конструктор с параметрами
  public OrderCreatedMain(OrderFieldsGetByTrackNumber orderFieldsGetByTrackNumber) {
    this.order = orderFieldsGetByTrackNumber;
  }

  //конструктор без параметров
  public OrderCreatedMain() {
      }

  //геттер
  public OrderFieldsGetByTrackNumber getOrder() {
    return order;
  }

  //сеттер
  public void setOrder(OrderFieldsGetByTrackNumber order) {
    this.order = order;
  }


}
