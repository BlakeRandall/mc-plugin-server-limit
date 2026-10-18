
package dev.example.fulljoinbypass;

import io.papermc.paper.event.player.PlayerServerFullCheckEvent;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class FullJoinBypassPlugin extends JavaPlugin
        implements Listener {

    private static final String BYPASS_PERMISSION =
            "fulljoin.bypass";

    private Permission vaultPermission;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<Permission> registration =
                getServer().getServicesManager()
                        .getRegistration(Permission.class);

        if (registration != null) {
            vaultPermission = registration.getProvider();
            getLogger().info(
                    "Vault permission provider found: "
                            + vaultPermission.getName()
            );
        } else {
            getLogger().warning(
                    "No Vault permission provider found. "
                            + "Full-server bypass is unavailable "
                            + "until a provider is registered."
            );
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onServerFullCheck(PlayerServerFullCheckEvent event) {

        var profile = event.getPlayerProfile();

        if (event.isAllowed()) {
            getLogger().info(
                    "Paper already allows this connection."
            );
            return;
        }

        if (vaultPermission == null) {
            getLogger().warning(
                    "No Vault permission provider; "
                            + "cannot grant bypass."
            );
            return;
        }

        if (profile.getId() == null || profile.getName() == null) {
            getLogger().warning(
                    "Missing UUID or player name."
            );
            return;
        }

        try {
            OfflinePlayer player =
                    Bukkit.getOfflinePlayer(profile.getId());

            boolean hasPermission = vaultPermission.playerHas(
                    null, player, BYPASS_PERMISSION
            );

            getLogger().info(
                    "" + profile.getName()
                            + " has " + BYPASS_PERMISSION
                            + " = " + hasPermission
            );

            if (hasPermission) {
                event.allow(true);
                getLogger().info(
                        "Bypass granted for "
                                + profile.getName()
                );
            }
        } catch (RuntimeException exception) {
            getLogger().warning(
                    "Permission lookup failed: "
                            + exception.getMessage()
            );
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        boolean hasPermission = event.getPlayer().hasPermission(BYPASS_PERMISSION);
        getLogger().info(
                    event.getPlayer().getName()
                            + (hasPermission
                                    ? " has "
                                    : " does not have ")
                            + BYPASS_PERMISSION
            );
    }
}
