package com.mcverify;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class MCVerifyPlugin extends JavaPlugin {

    private static MCVerifyPlugin instance;
    private BridgeClient bridgeClient;
    private VerifyManager verifyManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save default config
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        String bridgeUrl  = config.getString("bridge.url",    "http://localhost:8765");
        String bridgeSecret = config.getString("bridge.secret", "changeme");

        bridgeClient  = new BridgeClient(bridgeUrl, bridgeSecret, getLogger());
        verifyManager = new VerifyManager(this, bridgeClient);

        // Register listeners & commands
        getServer().getPluginManager().registerEvents(new ChatListener(this, bridgeClient), this);
        getCommand("dc-verify").setExecutor(new VerifyCommand(verifyManager));

        getLogger().info("MCVerify plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MCVerify plugin disabled.");
    }

    public static MCVerifyPlugin getInstance() { return instance; }
    public BridgeClient getBridgeClient()       { return bridgeClient; }
    public VerifyManager getVerifyManager()     { return verifyManager; }
}
