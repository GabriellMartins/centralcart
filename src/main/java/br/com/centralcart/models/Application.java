package br.com.centralcart.models;

public class Application {
  private final int id;
  private final String name;
  private final String plan;
  private final String overdue_date;
  private final String url;
  private final String logo;
  private final String primary_color;
  
  public Application(int id, String name, String plan, String overdue_date, String url, String logo,
      String primary_color) {
    this.id = id;
    this.name = name;
    this.plan = plan;
    this.overdue_date = overdue_date;
    this.url = url;
    this.logo = logo;
    this.primary_color = primary_color;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPlan() {
    return plan;
  }

  public String getOverdueDate() {
    return overdue_date;
  }

  public String getUrl() {
    return url;
  }

  public String getLogo() {
    return logo;
  }

  public String getPrimaryColor() {
    return primary_color;
  }


  
}