package com.dorvak.mpdk.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static DatabaseManager instance;
    private final Map<Class<? extends JavaPlugin>, DatabaseConnectionPool> connectionPools;

    private DatabaseManager() {
        connectionPools = new HashMap<>();
    }

    public static void init() {
        instance = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public void shutdown() {
        connectionPools.values().forEach(DatabaseConnectionPool::shutdown);
    }

    public DatabaseConnectionPool registerConnectionPool(Class<? extends JavaPlugin> clazz, DatabaseCredentials credentials) {
        DatabaseConnectionPool connectionPool = connectionPools.getOrDefault(clazz, new DatabaseConnectionPool(credentials));
        connectionPools.put(clazz, connectionPool);
        return connectionPool;
    }

    public DatabaseConnectionPool getConnectionPool(Class<? extends JavaPlugin> clazz) {
        return connectionPools.get(clazz);
    }
}
