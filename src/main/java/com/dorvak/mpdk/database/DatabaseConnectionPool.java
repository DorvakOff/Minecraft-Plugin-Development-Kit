package com.dorvak.mpdk.database;

import com.dorvak.mpdk.utils.MPDKLogger;
import com.dorvak.mpdk.utils.MultiThreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DatabaseConnectionPool {

    private final DatabaseCredentials credentials;
    private final List<DatabaseConnection> connections;

    public DatabaseConnectionPool(DatabaseCredentials credentials) {
        this.credentials = credentials;
        this.connections = new ArrayList<>();
        MultiThreading.schedule(this::cleanup, 0, 5, TimeUnit.MINUTES);
    }

    public void shutdown() {
        connections.forEach(DatabaseConnection::close);
        connections.clear();
    }

    public DatabaseConnection getConnection() {
        for (DatabaseConnection connection : connections) {
            if (!connection.isBusy()) {
                return connection;
            }
        }
        DatabaseConnection connection = new DatabaseConnection(credentials);
        connections.add(connection);
        return connection;
    }

    public void releaseConnection(DatabaseConnection connection) {
        connection.release();
    }

    public void closeConnection(DatabaseConnection connection) {
        connection.close();
        connections.remove(connection);
    }

    public int activeConnections() {
        return connections.stream().filter(DatabaseConnection::isBusy).mapToInt(i -> 1).sum();
    }

    public void cleanup() {
        int before = connections.size();
        connections.removeIf(connection -> !connection.isBusy());
        int after = connections.size();
        if (before != after) {
            MPDKLogger.info("Cleaned up %d connections.", before - after);
        }
    }

}
