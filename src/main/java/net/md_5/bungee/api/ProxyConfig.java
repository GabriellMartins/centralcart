package net.md_5.bungee.api;

import java.util.Collection;
import java.util.Map;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;

@Deprecated
public interface ProxyConfig {
    int getTimeout();

    String getUuid();

    Collection<ListenerInfo> getListeners();

    Map<String, ServerInfo> getServers();

    boolean isOnlineMode();

    boolean isLogCommands();

    int getPlayerLimit();

    Collection<String> getDisabledCommands();

    @Deprecated
    int getThrottle();

    @Deprecated
    boolean isIpForward();

    @Deprecated
    String getFavicon();

    Favicon getFaviconObject();
}
