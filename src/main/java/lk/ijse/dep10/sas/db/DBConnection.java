package lk.ijse.dep10.sas.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    public DBConnection() {
        File file = new File("application.properties");
        Properties properties = new Properties();
        try {
            FileReader fr = new FileReader(file);
            properties.load(fr);
            fr.close();
            String host = properties.getProperty("mysql.host", "localhost");
            String port = properties.getProperty("mysql.port", "3306");
            String database = properties.getProperty("mysql.database", "dep10_studentAttendance");
            String username = properties.getProperty("mysql.username", "root");
            String password = properties.getProperty("mysql.password", "mysql");

            String url ="jdbc:mysql://"+host+":"+port+"/"+database+"?createDatabaseIfNotExist=true&allowMultiQueries=true";
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to obtain the data base connection").showAndWait();
            System.exit(1);
            throw new RuntimeException(e);
        }


    }

    public static DBConnection getInstance(){
        return (dbConnection==null)? dbConnection=new DBConnection():dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
