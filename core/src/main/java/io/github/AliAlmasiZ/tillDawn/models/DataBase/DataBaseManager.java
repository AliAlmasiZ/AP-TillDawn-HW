package io.github.AliAlmasiZ.tillDawn.models.DataBase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
    private static final String DB_URL = "jdbc:sqlite:game_data.db";
    private static Connection connection;

    public static void initializeFromSchema() throws SQLException {

        FileHandle schemaFile = Gdx.files.internal("schema.sql");
        String schemaSql = schemaFile.readString(); // Load entire file as string

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(schemaSql); // Execute the schema
        }
    }
    public static void connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
