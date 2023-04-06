package com.dorvak.mpdk.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static DatabaseManager instance;
    private final Map<Class<? extends JavaPlugin>, Map<String, DatabaseConnectionPool>> connectionPools;

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
        connectionPools.values().forEach(map -> map.values().forEach(DatabaseConnectionPool::shutdown));
        connectionPools.clear();
    }

    public DatabaseConnectionPool registerConnectionPool(Class<? extends JavaPlugin> clazz, DatabaseCredentials credentials) {
        return registerConnectionPool(clazz, clazz.getName(), credentials);
    }

    public DatabaseConnectionPool registerConnectionPool(Class<? extends JavaPlugin> clazz, String name, DatabaseCredentials credentials) {
        DatabaseConnectionPool connectionPool = connectionPools.computeIfAbsent(clazz, aClass -> new HashMap<>()).get(name);
        if (connectionPool == null) {
            connectionPool = new DatabaseConnectionPool(credentials);
            connectionPools.get(clazz).put(name, connectionPool);
        }
        return connectionPool;
    }

    public DatabaseConnectionPool getConnectionPool(Class<? extends JavaPlugin> clazz, String name) {
        return connectionPools.containsKey(clazz) ? connectionPools.get(clazz).get(name) : null;
    }

    public DatabaseConnectionPool getConnectionPool(Class<? extends JavaPlugin> clazz) {
        return getConnectionPool(clazz, clazz.getName());
    }
}
