package pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateCourier {

  private String login;
  private String password;
  private String firstName;
  @Setter
  @Getter
  private Integer id;


  public CreateCourier(String login, String password, String firstName) {
    this.login = login;
    this.password = password;
    this.firstName = firstName;
  }

}