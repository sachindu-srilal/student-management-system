package lk.ijse.dep10.sas.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection() {
        File file = new File("application.properties");
        Properties properties = new Properties();
        try {
            try {
                FileReader fr = new FileReader(file);
                try {
                    properties.load(fr);
                    fr.close();
                    String host = properties.getProperty("mysql.host", "localhost");
                    String port = properties.getProperty("mysql.port", "3306");
                    String database = properties.getProperty("mysql.database", "dep10_studentAttendance");
                    String username = properties.getProperty("mysql.username", "root");
                    String password = properties.getProperty("mysql.password", "");

                    String quaeryString="jdbc:mysql://dep10.lk:3306/dep10_studentAttendance?createDatabaseIfNotExist=true&allowMultiQueries=true";
                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,"Failed to obtain the data base connection").showAndWait();
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } catch (FileNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"Configuration file is not found").showAndWait();
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            connection = DriverManager.getConnection("jdbc:mysql://dep10.lk:3306/dep10_studentAttendance?createDatabaseIfNotExist=true&allowMultiQueries=true", "root", "mysql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBConnection getInstance() {
        return dbConnection==null?dbConnection=new DBConnection():dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
