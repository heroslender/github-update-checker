package com.heroslender.updater.bukkit;

import com.heroslender.updater.UpdateCheckResult;
import com.heroslender.updater.impl.GithubUpdateChecker;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.heroslender.updater.impl.GithubUpdateChecker.GITHUB_LATEST_RELEASE_URL;
import static org.bukkit.ChatColor.*;

public class BukkitPluginUpdater implements Listener {
    private final Plugin plugin;
    @Getter private final String repoOwner;
    @Getter private final String repoName;

    private UpdateCheckResult updateCheckResult;

    public BukkitPluginUpdater(Plugin plugin, String repoOwner, String repoName) {
        this.plugin = plugin;
        this.repoOwner = repoOwner;
        this.repoName = repoName;

        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                updateCheckResult = GithubUpdateChecker.getInstance().checkVersion(
                        plugin.getDescription().getVersion(),
                        repoOwner,
                        repoName
                );
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to check for plugin updates", e);
            }
        }, 0L, TimeUnit.HOURS.toSeconds(1) * 20);
    }

    public BukkitPluginUpdater(Plugin plugin, String repoOwner) {
        this(plugin, repoOwner, plugin.getDescription().getName());
    }

    public BukkitPluginUpdater(Plugin plugin) {
        this(plugin, plugin.getDescription().getAuthors().get(0), plugin.getDescription().getName());
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("updatechecker.admin")) {
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (updateCheckResult.getStatus() == UpdateCheckResult.Status.OUTDATED) {
                sendOutdatedMessage(player, updateCheckResult);
            }
        }, 20L);
    }

    public void sendOutdatedMessage(Player player, UpdateCheckResult updateCheckResult) {
        player.sendMessage(GREEN + "Está disponivel uma nova versão do plugin " +
                GRAY + plugin.getDescription().getName() + GREEN + "!");
        player.sendMessage(GRAY + "A tua versão: " + RED + updateCheckResult.getCurrentVersion() + DARK_GRAY + " | " +
                GRAY + "A versão disponivel: " + GREEN + updateCheckResult.getRemoteVersion());
        BaseComponent[] components = new ComponentBuilder("   ")
                .append("[Changelog]").color(ChatColor.BLUE)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format(GITHUB_LATEST_RELEASE_URL, repoOwner, repoName)))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Clique para ver as alterações").create()))
                .append(" | ").color(ChatColor.DARK_GRAY)
                .append("[Transferir]")
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, updateCheckResult.getDownloadUrl()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Clique para transferir").create()))
                .create();
        player.sendMessage(components);
    }

    public void sendOutdatedMessage(ConsoleCommandSender console, UpdateCheckResult updateCheckResult) {
        plugin.getLogger().info(" ");
        plugin.getLogger().info("Está disponivel uma nova versão do plugin!");
        plugin.getLogger().info("A tua versão: " + updateCheckResult.getCurrentVersion() + " | " +
                "A versão disponivel: " + updateCheckResult.getRemoteVersion());
        plugin.getLogger().info("Changelog: " + String.format(GITHUB_LATEST_RELEASE_URL, repoOwner, repoName));
        plugin.getLogger().info("Download: " + updateCheckResult.getDownloadUrl());
        plugin.getLogger().info(" ");
    }
}
