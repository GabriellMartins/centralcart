package br.com.centralcart.models;

public class QueuedCommand {
  private final long id;
  private final long store_id;
  private final String user_id;
  private final String command;
  private final boolean offline_execute;
  private final Order order;

  public QueuedCommand(long id, long store_id, String user_id, String command, boolean offline_execute, Order order) {
    this.id = id;
    this.store_id = store_id;
    this.user_id = user_id;
    this.command = command;
    this.offline_execute = offline_execute;
    this.order = order;
  }

  @Override
  public String toString() {
    return "QueuedCommand{" +
            "id=" + id +
            ", store_id=" + store_id +
            ", user_id='" + user_id + '\'' +
            ", command='" + command + '\'' +
            ", offline_execute=" + offline_execute +
            ", order=" + order +
            '}';
  }

  public long getId() {
    return id;
  }

  public long getStoreId() {
    return store_id;
  }

  public String getUserId() {
    return user_id;
  }

  public String getCommand() {
    return command;
  }

  public boolean getOfflineExecute() {
    return offline_execute;
  }

  public Order getOrder() {
    return order;
  }
}