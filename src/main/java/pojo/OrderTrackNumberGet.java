package pojo;
import lombok.Getter;
import lombok.Setter;



public class OrderTrackNumberGet {


  //Трекинговый номер заказа
  private Integer trackNumber;

  public Integer getTrackNumber() {
    return trackNumber;
  }

  public void setTrackNumber(Integer t) {
    this.trackNumber = trackNumber;
  }

  public OrderTrackNumberGet(int t) {
    this.trackNumber = t;
  }

  public OrderTrackNumberGet( ) {

  }


}