package br.com.centralcart;

import br.com.centralcart.config.Properties;
import br.com.centralcart.interfaces.CommandProcessor;
import br.com.centralcart.models.Application;
import br.com.centralcart.models.QueuedCommand;
import br.com.centralcart.models.Response;
import br.com.centralcart.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralCart {

  private final String API_ENDPOINT = "https://api.centralcart.com.br/api/application/";

  //private final String API_ENDPOINT = "http://localhost:3333/api/application";

  private final String privateToken;

  private OkHttpClient client = new OkHttpClient();

  private Socket socket;

  public CentralCart(String private_token) {
    privateToken = private_token;
  }

  private String getToken() {
    return "Bearer " + privateToken;
  }

  public Response perform(String endpoint, String method, JsonObject... params) {
    Request.Builder request = new Request.Builder()
            .url(endpoint)
            .header("Authorization", getToken())
            .header("Content-Type", "application/json")
            .header("User-Agent", "CentralCart Java Plugin")
            .method(method, params.length > 0 ?
                    RequestBody.create(MediaType.parse("application/json"), params[0].toString()) :
                    null);

    try (okhttp3.Response response = client.newCall(request.build()).execute()) {
      String responseBody = response.body() == null ? "" : response.body().string();
      return new Response(response.isSuccessful(), response.code(), responseBody);
    } catch (IOException e) {
      Logger.debug("Não foi possível estabelecer uma conexão com a API.");
      Logger.debug(e.getMessage());

      for (StackTraceElement element : e.getStackTrace()) {
        Logger.debug(element.toString());
      }
      return null;
    }
  }

  public Application getApplication() {
    Response response = perform(API_ENDPOINT, "GET");

    if (response == null) return null;

    if (response.statusCode() >= 500)
      Logger.error("Ocorreu um erro interno ao conectar-se com o serviço de entregas.");
    if (response.statusCode() == 401)
      Logger.error("Ocorreu um erro ao conectar com sua loja. Verifique se o token está correto.");

    if (response.success()) {
      return new Gson().fromJson(response.body(), Application.class);
    }

    return null;
  }

  public QueuedCommand[] getQueuedCommands() {
    Response response = perform(API_ENDPOINT + "/queued_commands", "GET");

    if (response == null) return null;

    if (response.statusCode() >= 500)
      Logger.error("Ocorreu um erro interno ao buscar comandos na fila.");
    if (response.statusCode() == 401)
      Logger.error("Ocorreu um erro ao conectar com sua loja. Verifique se o token está correto.");

    if (response.success()) {
      assert response.body() != null;
      return new Gson().fromJson(response.body(), QueuedCommand[].class);
    }

    return null;
  }

  public QueuedCommand[] getPlayerQueuedCommands(String playerName) {
    Response response = perform(API_ENDPOINT + "/user_queue/" + playerName, "GET");

    if (response == null) return null;

    if (response.statusCode() >= 500)
      Logger.error("Ocorreu um erro interno ao buscar comandos na fila.");
    if (response.statusCode() == 401)
      Logger.error("Ocorreu um erro ao conectar com sua loja. Verifique se o token está correto.");

    if (response.success()) {
      assert response.body() != null;
      return new Gson().fromJson(response.body(), QueuedCommand[].class);
    }

    return null;
  }

  public void setCommandDispatched(QueuedCommand queuedCommand) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("status", "DONE");
    jsonObject.addProperty("response", "Command dispatched");

    Response response = perform(API_ENDPOINT + "/command/" + queuedCommand.getId(), "PATCH", jsonObject);

    if (response == null) return;

    if (!response.success()) {
      Logger.error("Error at command (DONE) update.");
      Logger.error("Command: " + queuedCommand.getId());
      Logger.error(response.body());
    }
  }

  public void initSocket(CommandProcessor processCommands) {
    Map<String, List<String>> stringListMap = new HashMap<>();
    stringListMap.put("Authorization", Collections.singletonList("Bearer " + Properties.getSecret()));
    stringListMap.put("x-extension", Collections.singletonList("plugin"));

    IO.Options options = IO.Options.builder()
            .setExtraHeaders(stringListMap)
            .build();

    socket = IO.socket(URI.create("wss://ws.centralcart.com.br"), options);
    socket.on("EXECUTE_COMMAND", new Emitter.Listener() {
      @Override
      public void call(Object... args) {
        assert args[0] != null;

        QueuedCommand[] queuedCommand = new QueuedCommand[]{new Gson().fromJson(args[0].toString(), QueuedCommand.class)};
        processCommands.processCommands(queuedCommand);
      }
    });

    socket.connect();
  }

  public void disableSocket() {
    socket.disconnect();
  }

}
