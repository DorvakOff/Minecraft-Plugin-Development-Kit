package com.dorvak.mpdk;

import com.dorvak.mpdk.database.DatabaseManager;
import com.dorvak.mpdk.utils.MultiThreading;
import org.bukkit.plugin.java.JavaPlugin;

public class MPDKPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DatabaseManager.init();
    }

    @Override
    public void onDisable() {
        DatabaseManager.getInstance().shutdown();
        MultiThreading.shutdown();
    }
}
