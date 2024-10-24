package br.com.centralcart.utils;

import br.com.centralcart.BungeePlugin;

public class Logger {

  private static boolean isBukkit;
  private static boolean isBungee;


  private static void send(String message) {
    Class<?> clazz = null;

    try {
      clazz = Class.forName("net.md_5.bungee");
      isBungee = true;
    } catch (ClassNotFoundException e) {
      isBungee = false;
      //throw new RuntimeException(e);
    }

    if (clazz != null) {
    }else System.out.println(message);
  }

  public static void info(String message) {
    send("§a[CentralCart] => §r" + message);
  }

  public static void error(String message) {
   send("§c[CentralCart] => §4" + message);
  }

  public static void debug(String message) {
    if (isBungee) {
      if (BungeePlugin.getConfig().getBoolean("debug_mode"))
        send("§e[CentralCart] => §r" + message);
      }
    }
}
