package net.md_5.bungee.conf;

import com.google.common.base.Preconditions;
import gnu.trove.map.TMap;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.util.CaseInsensitiveMap;
import net.md_5.bungee.util.CaseInsensitiveSet;

public class Configuration implements ProxyConfig {

    private int timeout = 30000;

    public int getTimeout() {
        return this.timeout;
    }

    private String uuid = UUID.randomUUID().toString();

    private Collection<ListenerInfo> listeners;

    private TMap<String, ServerInfo> servers;

    public String getUuid() {
        return this.uuid;
    }

    public Collection<ListenerInfo> getListeners() {
        return this.listeners;
    }

    public TMap<String, ServerInfo> getServers() {
        return this.servers;
    }

    private boolean onlineMode = true;

    private boolean logCommands;

    public boolean isOnlineMode() {
        return this.onlineMode;
    }

    public boolean isLogCommands() {
        return this.logCommands;
    }

    private int playerLimit = -1;

    private Collection<String> disabledCommands;

    public int getPlayerLimit() {
        return this.playerLimit;
    }

    public Collection<String> getDisabledCommands() {
        return this.disabledCommands;
    }

    private int throttle = 4000;

    private boolean ipForward;

    private Favicon favicon;

    public int getThrottle() {
        return this.throttle;
    }

    public boolean isIpForward() {
        return this.ipForward;
    }

    private int compressionThreshold = 256;

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    public void load() {
        ConfigurationAdapter adapter = ProxyServer.getInstance().getConfigurationAdapter();
        adapter.load();
        File fav = new File("server-icon.png");
        if (fav.exists())
            try {
                this.favicon = Favicon.create(ImageIO.read(fav));
            } catch (IOException|IllegalArgumentException ex) {
                ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not load server icon", ex);
            }
        this.listeners = adapter.getListeners();
        this.timeout = adapter.getInt("timeout", this.timeout);
        this.uuid = adapter.getString("stats", this.uuid);
        this.onlineMode = adapter.getBoolean("online_mode", this.onlineMode);
        this.logCommands = adapter.getBoolean("log_commands", this.logCommands);
        this.playerLimit = adapter.getInt("player_limit", this.playerLimit);
        this.throttle = adapter.getInt("connection_throttle", this.throttle);
        this.ipForward = adapter.getBoolean("ip_forward", this.ipForward);
        this.compressionThreshold = adapter.getInt("network_compression_threshold", this.compressionThreshold);
        this.disabledCommands = (Collection<String>)new CaseInsensitiveSet((Collection<? extends String>) adapter.getList("disabled_commands", Arrays.asList(new String[] { "disabledcommandhere" })));
        Preconditions.checkArgument((this.listeners != null && !this.listeners.isEmpty()), "No listeners defined.");
        Map<String, ServerInfo> newServers = adapter.getServers();
        Preconditions.checkArgument((newServers != null && !newServers.isEmpty()), "No servers defined");
        if (this.servers == null) {
            this.servers = (TMap<String, ServerInfo>)new CaseInsensitiveMap(newServers);
        } else {
            for (ServerInfo oldServer : this.servers.values()) {
                Preconditions.checkArgument(newServers.containsKey(oldServer.getName()), "Server %s removed on reload!", new Object[] { oldServer.getName() });
            }
            for (Map.Entry<String, ServerInfo> newServer : newServers.entrySet()) {
                if (!this.servers.containsValue(newServer.getValue()))
                    this.servers.put(newServer.getKey(), newServer.getValue());
            }
        }
        for (ListenerInfo listener : this.listeners) {
            Preconditions.checkArgument(this.servers.containsKey(listener.getDefaultServer()), "Default server %s is not defined", new Object[] { listener.getDefaultServer() });
            Preconditions.checkArgument(this.servers.containsKey(listener.getFallbackServer()), "Fallback server %s is not defined", new Object[] { listener.getFallbackServer() });
            for (String server : listener.getForcedHosts().values()) {
                if (!this.servers.containsKey(server))
                    ProxyServer.getInstance().getLogger().log(Level.WARNING, "Forced host server {0} is not defined", server);
            }
        }
    }

    @Deprecated
    public String getFavicon() {
        return getFaviconObject().getEncoded();
    }

    public Favicon getFaviconObject() {
        return this.favicon;
    }
}
