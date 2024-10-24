package net.md_5.bungee.api;

import com.google.common.base.Preconditions;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public abstract class ProxyServer {
    private static ProxyServer instance;

    public static ProxyServer getInstance() {
        return instance;
    }

    public abstract Title createTitle();

    public abstract Collection<ProxiedPlayer> matchPlayer(String paramString);

    public abstract ProxyConfig getConfig();

    public abstract Collection<String> getDisabledCommands();

    public abstract void broadcast(BaseComponent paramBaseComponent);

    public abstract void broadcast(BaseComponent... paramVarArgs);

    @Deprecated
    public abstract void broadcast(String paramString);

    public abstract int getOnlineCount();

    public abstract TaskScheduler getScheduler();

    public abstract File getPluginsFolder();

    public abstract CommandSender getConsole();

    public abstract ServerInfo constructServerInfo(String paramString1, InetSocketAddress paramInetSocketAddress, String paramString2, boolean paramBoolean);

    @Deprecated
    public abstract int getProtocolVersion();

    @Deprecated
    public abstract String getGameVersion();

    public abstract Collection<String> getChannels();

    public abstract void unregisterChannel(String paramString);

    public abstract void registerChannel(String paramString);

    public abstract void start() throws Exception;

    public abstract void stop(String paramString);

    public abstract void stop();

    public abstract void setReconnectHandler(ReconnectHandler paramReconnectHandler);

    public abstract ReconnectHandler getReconnectHandler();

    public abstract void setConfigurationAdapter(ConfigurationAdapter paramConfigurationAdapter);

    public abstract ConfigurationAdapter getConfigurationAdapter();

    public abstract PluginManager getPluginManager();

    public abstract ServerInfo getServerInfo(String paramString);

    public abstract Map<String, ServerInfo> getServers();

    public abstract ProxiedPlayer getPlayer(UUID paramUUID);

    public abstract ProxiedPlayer getPlayer(String paramString);

    public abstract Collection<ProxiedPlayer> getPlayers();

    public abstract Logger getLogger();

    public abstract String getTranslation(String paramString, Object... paramVarArgs);

    public abstract String getVersion();

    public abstract String getName();

    public static void setInstance(ProxyServer instance) {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkArgument((ProxyServer.instance == null), "Instance already set");
        ProxyServer.instance = instance;
    }
}
