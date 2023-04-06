package com.dorvak.mpdk.database;

public record DatabaseCredentials(String host, int port, String database, String username, String password) {

    private static final String TYPE = "mariadb";

    public String getUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:");
        sb.append(TYPE);
        sb.append("://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        if (database != null && !database.trim().isEmpty()) {
            sb.append("/");
            sb.append(database);
        }

        return sb.toString();
    }
}
