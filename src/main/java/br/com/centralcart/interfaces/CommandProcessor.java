package br.com.centralcart.interfaces;

import br.com.centralcart.models.QueuedCommand;

@FunctionalInterface
public interface CommandProcessor {
  void processCommands(QueuedCommand[] commands);
}