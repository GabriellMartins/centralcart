package br.com.centralcart.models;

public class Order {
  private String client_identifier;
  private String client_email;
  private String client_name;
  private String client_discord;
  private String internal_id;

  public Order(String client_identifier, String client_email, String client_name, String client_discord, String internal_id) {
    this.client_identifier = client_identifier;
    this.client_email = client_email;
    this.client_name = client_name;
    this.client_discord = client_discord;
    this.internal_id = internal_id;
  }

  @Override
  public String toString() {
    return "Order{" +
            "client_identifier='" + client_identifier + '\'' +
            ", client_email='" + client_email + '\'' +
            ", client_name='" + client_name + '\'' +
            ", client_discord='" + client_discord + '\'' +
            ", internal_id='" + internal_id + '\'' +
            '}';
  }

  public String getClientIdentifier() {
    return client_identifier;
  }

  public String getClientEmail() {
    return client_email;
  }

  public String getClientName() {
    return client_name;
  }

  public String getClientDiscord() {
    return client_discord;
  }

  public String getInternalId() {
    return internal_id;
  }
}
