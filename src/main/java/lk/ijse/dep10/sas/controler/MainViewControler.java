package lk.ijse.dep10.sas.controler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;

public class MainViewControler {

    public ImageView imageView;
    @FXML
    private Button btnBrows;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewStudent;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<> tblStudent;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearch;




    @FXML
    void btnBrowsOnAction(ActionEvent event) throws MalformedURLException {
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
    }

    @FXML
    void btnNewStudentOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnDelete(ActionEvent event) {
    }

}
