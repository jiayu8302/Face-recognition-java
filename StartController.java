package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author mac
 */
public class StartController {

    @FXML
    private Button btnLogin;//login in
    @FXML
    private Button register;//register new uer
    @FXML
    private TextField username;//for log in information
    @FXML
    private TextField password;
    @FXML
    private Button exit1;

    /**
     * log in button
     *
     * @param event click
     * @throws IOException handling
     */
    @FXML
    public void btn1Clicked(ActionEvent event) throws IOException {
        boolean matched = false;
        String name = username.getText();
        String pw = password.getText();

        try {
            Connect db = new Connect();
            java.sql.Connection conn = db.Connect();
            Statement st = conn.createStatement();//whether match
            ResultSet rs = st.executeQuery("select password from login where username='" + name + "'");
            while (rs.next()) {
                //System.out.println(rs.getString(1));
                if (rs.getString("password").equals(pw)) {
                    matched = true;
                }
            }
            //System.out.println("suceess");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (matched == true) {//stage transition
            Stage thisStage = (Stage) btnLogin.getScene().getWindow();
            thisStage.close();
            Parent root = FXMLLoader.load(getClass().getResource("recognition.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 618));
            stage.show();
        } else {// output shows it's wrong
            Parent root = FXMLLoader.load(getClass().getResource("Ok_1.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 300, 180));
            stage.show();
            username.setText("");//set null
            password.setText("");
        }

    }

    /**
     * register a new user
     *
     * @param event click
     * @throws IOException handling
     */
    @FXML
    public void registerClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));//register successfully
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 270, 120));
        stage.show();
        String name = username.getText();

        String pw = password.getText();
        try {
            Connect db = new Connect();
            java.sql.Connection conn = db.Connect();
            Statement st = conn.createStatement();
            st.execute("insert into login(username,password) values('" + name + "','" + pw + "')");
            System.out.println("suceess");//insert new user
            username.setText("");
            password.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit1Clicked(ActionEvent event) {
        System.exit(0);
    }
}
