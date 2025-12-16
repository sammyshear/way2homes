package dev.sshear.way2homes;

import dev.sshear.way2homes.essentials.EssentialsListener;
import dev.sshear.way2homes.huskhomes.HuskHomesListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class Way2Homes extends JavaPlugin {

    @Override
    public void onEnable() {
        Constants.LOG.info("Way2Homes has been enabled!");

        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        if (getServer().getPluginManager().getPlugin("HuskHomes") != null) {
            config.addDefault("HuskHomes", true);
            if (getConfig().getBoolean("HuskHomes"))
                Constants.LOG.info("Way4Homes setup detected HuskHomes, enabling in config");
        } else {
            config.addDefault("HuskHomes", false);
        }
        if (getServer().getPluginManager().getPlugin("EssentialsX") != null) {
            config.addDefault("EssentialsX", true);
            if (getConfig().getBoolean("EssentialsX"))
                Constants.LOG.info("Way4Homes setup detected Essentials, enabling in config");
        } else {
            config.addDefault("EssentialsX", false);
        }
        config.options().copyDefaults(true);
        saveConfig();
        if (this.getConfig().getBoolean("EssentialsX") &&
                !this.getConfig().getBoolean("HuskHomes")) {
            Constants.LOG.info("hooking into essentialsx");
            getServer().getPluginManager().registerEvents(new EssentialsListener(), this);
        } else if (this.getConfig().getBoolean("HuskHomes")) {
            Constants.LOG.info("hooking into huskhomes");
            getServer().getPluginManager().registerEvents(new HuskHomesListener(), this);
        }
    }

    @Override
    public void onDisable() {
        Constants.LOG.info("Way2Homes has been disabled!");
    }
}
