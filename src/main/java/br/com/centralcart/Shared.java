package br.com.centralcart;

public class Shared {

  /*private static void showBanner(Application application, String version) {
    String base64String = "CiAgIF9fX19fXyAgICAgICAgICAgICAgICAgX18gICAgICAgICAgICAgICAgICAgICBfXyAgIF9fX19fXyAgICAgICAgICAgICAgICAgICBfXyAKICAvIF9fX18vICBfX18gICAgX19fXyAgIC8gL18gICBfX19fXyAgX19fXyBfICAgLyAvICAvIF9fX18vICBfX19fIF8gICBfX19fXyAgLyAvXwogLyAvICAgICAgLyBfIFwgIC8gX18gXCAvIF9fLyAgLyBfX18vIC8gX18gYC8gIC8gLyAgLyAvICAgICAgLyBfXyBgLyAgLyBfX18vIC8gX18vCi8gL19fXyAgIC8gIF9fLyAvIC8gLyAvLyAvXyAgIC8gLyAgICAvIC9fLyAvICAvIC8gIC8gL19fXyAgIC8gL18vIC8gIC8gLyAgICAvIC9fICAKXF9fX18vICAgXF9fXy8gL18vIC9fLyBcX18vICAvXy8gICAgIFxfXyxfLyAgL18vICAgXF9fX18vICAgXF9fLF8vICAvXy8gICAgIFxfXy8gIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICB3d3cuY2VudHJhbGNhcnQuY29tLmJyIC0ge3ZlcnNpb259CiAgICAgICAgICAgICAgICAgICAgICAgTW9uZXRpemUgbyBzZXUgc2Vydmlkb3IgZGUgTWluZWNyYWZ0IQ==";

    byte[] decodedBytes = Base64.getDecoder().decode(base64String);

    String decodedString = new String(decodedBytes);

    String finalString = "§a" + decodedString.replace("{version}", "v" + version);

    if (BungeePlugin.getInstance() != null) {
      System.out.println("IS BUNGEE");
    }else Bukkit.getConsoleSender().sendMessage(finalString);

    Logger.info("Plano: §a" + application.getPlan() + " §f- Expira em: §a" + Utils.dateFormat(application.getOverdueDate()));
  }

  public static CentralCart setupCentralCart(String version) {
    //BungeePlugin.getInstance().getProxy().getScheduler().cancel(BungeePlugin.getInstance())
    //Bukkit.getScheduler().cancelTasks(this);

    CentralCart centralCart = new CentralCart(Properties.getSecret());

    Application application = centralCart.getApplication();

    if (application == null) return centralCart;

    showBanner(application, version);

    return centralCart;
  }*/
}
