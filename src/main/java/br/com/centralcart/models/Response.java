package br.com.centralcart.models;

public class Response {

  private final boolean isSuccessful;
  private final int statusCode;

  private final String data;

  public Response(boolean isSuccessful, int statusCode, String data) {
    this.isSuccessful = isSuccessful;
    this.statusCode = statusCode;
    this.data = data;
  }

  public int statusCode() {
    return statusCode;
  }

  public String body() {
    return data;
  }

  public boolean success() {
    return isSuccessful;
  };
}
