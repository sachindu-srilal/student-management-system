package lk.ijse.dep10.sas.controler;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import lk.ijse.dep10.sas.db.DBConnection;
import lk.ijse.dep10.sas.util.Student;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;

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
    private TableView<Student> tblStudent;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSearch;

    public void initialize(){
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("imageView"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("Id"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));

        btnDelete.setDisable(true);
        loadAllItems();

        txtSearch.textProperty().addListener((ov, s, current) ->{
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                Statement stm = connection.createStatement();
                String str ="SELECT * FROM Student WHERE name LIKE '%s'";
                String sql = String.format(str, "%" + current + "%");
                ResultSet rst = stm.executeQuery(sql);
                PreparedStatement stmPicture = connection.prepareStatement("SELECT * FROM Picture WHERE student_id=?");


                tblStudent.getItems().clear();
                while (rst.next()){
                    String id = rst.getString("id");
                    String name = rst.getString("name");
                    stmPicture.setString(1,id);
                    ResultSet rstPic = stmPicture.executeQuery();
                    Student student = new Student(id, name, null, null);
                    Image image = new Image("/image/noimage.png", 100.00, 100.00, true, true);
                    ImageView imgView = new ImageView(image);
                    student.setImageView(imgView);
                    if(rstPic.next()){
                        Blob picture = rstPic.getBlob("picture");
                        student.setPicture(picture);
                        Image img = new Image(picture.getBinaryStream(), 100.00, 100.00, true, true);
                        ImageView imgPic = new ImageView(img);
                        student.setImageView(imgPic);
                    }
                    tblStudent.getItems().add(student);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } );


        tblStudent.getSelectionModel().selectedItemProperty().addListener((ov, item, current) -> {
            btnDelete.setDisable(current==null);
            if(current==null) return;
            txtId.setText(current.getId());
            txtName.setText(current.getName());
            imageView.setImage(current.getImageView().getImage());

        });



    }

    private void loadAllItems() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Student ");
            PreparedStatement stmPicture = connection.prepareStatement("SELECT *FROM Picture WHERE student_id=?");

            while (rst.next()){
                String id = rst.getString("id");
                String name = rst.getString("name");

                Student student = new Student(id, name, null, null);
                Image noImg = new Image("/image/noimage.png",100.00,100.00,true,true);
                ImageView noImgView = new ImageView();
                noImgView.setImage(noImg);
                student.setImageView(noImgView);

                stmPicture.setString(1,id);
                ResultSet rstPic = stmPicture.executeQuery();
                if(rstPic.next()){
                    Blob picture = rstPic.getBlob("picture");
                    student.setPicture(picture);
                    Image image = new Image(picture.getBinaryStream(),100.00,100.00,true,true);
                    ImageView imgView = new ImageView();
                    imgView.setImage(image);
                    student.setImageView(imgView);

                }
                tblStudent.getItems().add(student);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnBrowsOnAction(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Profile Picture Window");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg","*.png","*.jpeg","*.gif","*.bmp"));
        File file = fileChooser.showOpenDialog(btnBrows.getScene().getWindow());
        if(file!=null){
            Image image = new Image(file.toURI().toURL().toString());
            imageView.setImage(image);
            btnClear.setDisable(false);
            btnClear.requestFocus();

        }

    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        Image image = new Image("/image/noimage.png");
        imageView.setImage(image);
        btnClear.setDisable(true);
        btnBrows.requestFocus();

    }

    @FXML
    void btnNewStudentOnAction(ActionEvent event) {
        if(tblStudent.getItems().size()==0){
            txtId.setText("DEP-10/S-001");
        }else {
            Student student = tblStudent.getItems().get(tblStudent.getItems().size() - 1);
            String id = student.getId();
            String[] arr = id.split("-");
            int idIntVal = Integer.parseInt(arr[2]);
            String idStr =String.format("DEP-10/S-%03d",idIntVal+1);
            txtId.setText(idStr);
        }
        Image noImg = new Image("/image/noimage.png");
        imageView.setImage(noImg);
        txtName.clear();
        btnClear.fire();
        txtName.requestFocus();

        tblStudent.getSelectionModel().clearSelection();



    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if(!isDatavalid()) return;
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Student (id,name) VALUES (?,?)");
            PreparedStatement stm1 = connection.prepareStatement("INSERT INTO Picture (student_id, picture) VALUES (?,?)");

            connection.setAutoCommit(false);

            stm.setString(1,txtId.getText());
            stm.setString(2,txtName.getText());
            stm.executeUpdate();
            stm1.setString(1,txtId.getText());

            Image image = imageView.getImage();

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",baos);
            byte[] bytes = baos.toByteArray();
            Blob personImg = new SerialBlob(bytes);
            stm1.setBlob(2,personImg);
            stm1.executeUpdate();

            ImageView imageView = new ImageView();
            imageView.setImage(new Image(personImg.getBinaryStream(),100.00,100.00,true,true));
            Student student = new Student(txtId.getText(), txtName.getText(), personImg, imageView);
            tblStudent.getItems().add(student);

            connection.commit();
            btnNewStudent.fire();

        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            new Alert(Alert.AlertType.ERROR, "Failed to save the Student").showAndWait();
            e.printStackTrace();

        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private boolean isDatavalid(){
        boolean dataValid =true;
        if(!txtName.getText().matches("[A-za-z]{3,}")){
            txtName.requestFocus();
            txtName.selectAll();
            dataValid=false;
        }
        return dataValid;
    }

    @FXML
    void btnDelete(ActionEvent event) {
        if(tblStudent.getSelectionModel().getSelectedItem()!=null){
            Student selectStudent = tblStudent.getSelectionModel().getSelectedItem();
            tblStudent.getItems().remove(selectStudent);
            tblStudent.getSelectionModel().clearSelection();
            btnNewStudent.fire();
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement stm = connection.prepareStatement("DELETE FROM Picture WHERE student_id=?");
                stm.setString(1,selectStudent.getId());
                stm.executeUpdate();
                PreparedStatement stm2 = connection.prepareStatement("DELETE FROM Student WHERE id=?");
                stm2.setString(1,selectStudent.getId());
                stm2.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
