package com.dorvak.mpdk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseConnection {

    private final DatabaseCredentials credentials;
    private final UUID connectionId;
    private Connection connection;
    private boolean busy;

    public DatabaseConnection(DatabaseCredentials credentials) {
        this.credentials = credentials;
        this.busy = false;
        this.connectionId = UUID.randomUUID();
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connect();
        } else {
            if (!connection.isValid(5)) {
                connection.close();
                this.connect();
            }
        }
        this.busy = true;
        return connection;
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(credentials.getUrl(), credentials.username(), credentials.password());
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void release() {
        this.busy = false;
    }

    public void close() {
        try {
            this.connection.close();
            release();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatabaseConnection that)) return false;

        return connectionId.equals(that.connectionId);
    }

    @Override
    public int hashCode() {
        return connectionId.hashCode();
    }
}
