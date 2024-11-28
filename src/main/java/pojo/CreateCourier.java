package pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourier {

  private String login;
  private String password;
  private String firstName;
  private Integer id;


  public CreateCourier(String login, String password, String firstName) {
    this.login = login;
    this.password = password;
    this.firstName = firstName;
  }

  public CreateCourier() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}