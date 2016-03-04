package utils;

import org.postgresql.ds.PGSimpleDataSource;

public class DataSourceFactory {

    public static PGSimpleDataSource createPGSimpleDataSource(
            final String server, final int port, final String user, final String password,
            final String database, final String schema) {

        final PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setServerName(server);
        pgSimpleDataSource.setPortNumber(port);
        pgSimpleDataSource.setUser(user);
        pgSimpleDataSource.setPassword(password);
        pgSimpleDataSource.setDatabaseName(database);
        pgSimpleDataSource.setCurrentSchema(schema);
        return pgSimpleDataSource;
    }

    private DataSourceFactory() {
    }
}
