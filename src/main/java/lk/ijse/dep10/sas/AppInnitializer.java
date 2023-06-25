package lk.ijse.dep10.sas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.sas.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppInnitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Database connection is about to close");
            try {
                if(!DBConnection.getInstance().getConnection().isClosed() && DBConnection.getInstance().getConnection()!=null){
                    DBConnection.getInstance().getConnection().close();

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        generateSchemaIfNotExist();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        AnchorPane root =fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Student Detail Window");
        primaryStage.setMaximized(true);
    }



    private void generateSchemaIfNotExist() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SHOW TABLES ");
            if(!rst.next()){
                InputStream is = getClass().getResourceAsStream("/schema.sql");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder dbScript = new StringBuilder();
                while ((line= br.readLine())!=null){
                    dbScript.append(line).append("\n");
                }
                br.close();
                System.out.println(dbScript.toString());
                stm.execute(dbScript.toString());
            }else {
                System.out.println("No table Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
