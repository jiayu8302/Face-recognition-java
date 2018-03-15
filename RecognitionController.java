/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.text.Position;
import org.opencv.core.Core;

/**
 * FXML Controller class
 *
 * @author mac
 */
public class RecognitionController implements Initializable {

    @FXML
    private TitledPane recognition;//sub pane
    @FXML
    private Button btnStartCamera; //button for camera
    @FXML
    private TitledPane add;
    @FXML
    private Button startadd;
    @FXML
    private Button save;//save button
    @FXML
    private TitledPane report;
    @FXML
    private ImageView Recongnitionvideo;//different for imageview
    @FXML
    private ImageView Recognitionpic;
    @FXML
    private ImageView addvideo;
    @FXML
    private ImageView addpic;
    @FXML
    private Button stoprecognition;
    @FXML
    private Button Stop;//stop the camera
    private VideoCamera videoCamera;
    private int ID = 0;//initial id
    @FXML
    private Button Information;//for load photos
    @FXML
    private Button generate;
    @FXML
    private Text sID;
    @FXML
    private TextField addName;
    @FXML
    private ChoiceBox addProgram;//choicebox
    @FXML
    private TextField addAge;
    @FXML
    private ChoiceBox addGender;
    @FXML
    private ChoiceBox visitReason;
    @FXML
    private Text name;
    @FXML
    private Text program;
    @FXML
    private Text age;
    @FXML
    private Text gender;
    @FXML
    private Text last;
    @FXML
    private Text lastReason;
    @FXML
    private Text historic;
    @FXML
    private Button save1;
    ObservableList<String> genders = FXCollections.observableArrayList("female", "male");//object for choose
    ObservableList<String> programs = FXCollections.observableArrayList("MISM", "MSIT", "MISPPM");
    ObservableList<String> reasons = FXCollections.observableArrayList("stapler", "tuitio fees", "complaints", "collect assignment", "meet people");
    private BarChart<String, Number> barChart1;//bar chart
    private NumberAxis Salary;
    private CategoryAxis Position;
    Date date = new Date();
    @FXML
    private Button chart;
    @FXML
    private DatePicker start;
    @FXML
    private DatePicker end;
    @FXML
    private ChoiceBox gend;
    @FXML
    private Button search;
    @FXML
    private TableColumn<StudentInfor, String> visitdate;//table view
    @FXML
    private TableColumn<StudentInfor, String> idss;
    @FXML
    private TableColumn<StudentInfor, String> names;
    @FXML
    private TableColumn<StudentInfor, String> gen;
    @FXML
    private TableColumn<StudentInfor, String> pro;
    @FXML
    private TableColumn<StudentInfor, String> reason;
    private ObservableList<StudentInfor> data;
    @FXML
    private ChoiceBox reasonss;
    @FXML
    private ChoiceBox programss;
    @FXML
    private TableView<StudentInfor> all;
    @FXML
    private Button exit2;
    static boolean c1Started = false;
    static boolean IDgenerated = false;
    static boolean c2Started = false;

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);//train the model for the first time
        FaceRecogntion.faceTrainAndRec();
        sID.setText("000000");
        addName.setText("BUMBLE");
        addAge.setText("22");
        addGender.setValue("male");
        addGender.setItems(genders);
        gend.setValue("male");
        gend.setItems(genders);
        LocalDate firstDay_2017 = LocalDate.of(2017, Month.NOVEMBER, 1);
        LocalDate firstDay_2018 = LocalDate.of(2017, Month.DECEMBER, 1);
        start.setValue(firstDay_2017);
        end.setValue(firstDay_2018);
        //visitReason.setValue("stapler");
        visitReason.setItems(reasons);
        reasonss.setValue("stapler");
        reasonss.setItems(reasons);
        programss.setValue("MISM");
        programss.setItems(programs);
        addProgram.setValue("MISM");
        addProgram.setItems(programs);
        PlayMusic p = new PlayMusic();
        p.play();
    }

    /**
     * recognition button
     *
     * @param event click
     */
    @FXML
    private void Recongnitionaction(ActionEvent event) throws IOException {
        //System.out.println(c2Started);
        if (btnStartCamera.getText().equals("Start")) {
            if (c2Started == false) {
                c1Started = true;
                btnStartCamera.setText("Stop");
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                FaceRecogntion.faceTrainAndRec();
                videoCamera = new VideoCamera(1, Recongnitionvideo);
                videoCamera.startCamera();
                btnStartCamera.setText("Stop");
            } else if (c2Started == true) {
                Parent root = FXMLLoader.load(getClass().getResource("stopC1.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 300, 180));
                stage.show();
            }

        } else {
            videoCamera.closeCamera(Recongnitionvideo);
            Recongnitionvideo.setImage(null);
            btnStartCamera.setText("Start");
            c1Started = false;
        }

    }

    /**
     * start add
     *
     * @param event click
     */
    @FXML
    private void startaddaction(ActionEvent event) throws IOException {
        if (startadd.getText().equals("Start")) {

            if (c1Started == false && IDgenerated == true) {
                c2Started = true;
                startadd.setText("Stop");
                String input = sID.getText();
                //String input = "787124";
                if (input != null) {
                    ID = Integer.parseInt(input);
                    videoCamera = new VideoCamera(0, ID, addvideo);
                    videoCamera.startCamera();
                }

            } else if (c1Started == true) {
                Parent root = FXMLLoader.load(getClass().getResource("stopC1.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 300, 180));
                stage.show();
            } else if (IDgenerated == false) {
                Parent root = FXMLLoader.load(getClass().getResource("generateID.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 300, 180));
                stage.show();
            }

        } else {
            startadd.setText("Start");
            videoCamera.closeCamera(addvideo);
            addvideo.setImage(null);
            c2Started = false;

        }

    }

    /**
     * save registration
     *
     * @param event click
     */
    @FXML
    private void saveaction(ActionEvent event) {
        addpic.setImage(FaceRecogntion.getPic(ID));
        int studentID = Integer.valueOf(sID.getText());
        String name = addName.getText();
        String program = (String) addProgram.getValue().toString();
        //int age = Integer.valueOf(addAge.getText());
        String age = addAge.getText();
        String gender = (String) addGender.getValue().toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dates = df.format(date);
        try {//save in database
            Connect db = new Connect();
            java.sql.Connection conn = db.Connect();
            Statement st = conn.createStatement();
            st.execute("insert into studentInformatt(ID,program,name,age,gender) values(" + studentID + ",'" + program + "','" + name + "','" + age + "','" + gender + "')");
            //INSERT INTO visitor (visitdate,name,program,gender,reason) VALUES ('2017-10-31','Troy','MISM','male','stapler')
            st.execute("insert into viss(visitdate,name,program,gender,reason) values ('" + dates + "','" + name + "','" + program + "','" + gender + "','stapler')");
            //System.out.println("suceess");
            Parent root = FXMLLoader.load(getClass().getResource("Ok.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 270, 120));
            stage.show();
            sID.setText("");
            addName.setText("");
            addProgram.setValue(null);
            addGender.setValue(null);
            addAge.setText("");
            addpic.setImage(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * stop the camera
     *
     * @param event click
     */
    @FXML
    private void stopaction2(ActionEvent event) {
        videoCamera.closeCamera(Recongnitionvideo);
        Recongnitionvideo.setImage(null);
    }

    /**
     * stop the camera
     *
     * @param event click
     */
    @FXML
    private void stopaction1(ActionEvent event) {
        videoCamera.closeCamera(addvideo);
        addvideo.setImage(null);
    }

    /**
     * show personal information
     *
     * @param event click
     */
    @FXML
    private void getInformation(ActionEvent event) throws IOException {
        int i = 0;
        if (c1Started == true) {
            //name.setText("Vic");
            if (FaceDetection.returnID == 0) {//stranger
                //System.out.println("Stranger");
                Parent root = FXMLLoader.load(getClass().getResource("Ok_2.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 300, 180));
                stage.show();
            } else {//return information
                Recognitionpic.setImage(FaceRecogntion.getPic(FaceDetection.returnID));
                int ID = FaceDetection.returnID;
                try {
                    Connect db = new Connect();
                    java.sql.Connection conn = db.Connect();
                    Statement st = conn.createStatement();
                    Statement st2 = conn.createStatement();
                    //INSERT INTO visitor (visitdate,name,program,gender,reason) VALUES ('2017-10-31','Troy','MISM','male','stapler')
                    //insert into students(ID,program,name,age,gender) VALUES (1,'MISM','Nion','22','male')
                    ResultSet rs1 = st.executeQuery("select name, program, age, gender from studentInformatt where ID=" + ID);  // create table
                    while (rs1.next()) {
                        name.setText(rs1.getString("name"));
                        program.setText(rs1.getString("program"));
                        age.setText(rs1.getString("age"));
                        gender.setText(rs1.getString("gender"));
                    }
                    ResultSet rs2 = st2.executeQuery("select visitdate,reason from viss where name=(select name from studentInformatt where ID=" + ID + ")");

                    while (rs2.next()) {
                        //last.setText(new SimpleDateFormat("yyyy-MM-dd").format(rs2.getDate("visitdate")));
                        last.setText(rs2.getString("visitdate"));
                        lastReason.setText(rs2.getString("reason"));
                        i = i + 1;

                    }
                    historic.setText(String.valueOf(i));
                    //historic.setText(String.valueOf(i));
                    //System.out.println("suceess");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            Parent root = FXMLLoader.load(getClass().getResource("startC1.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 300, 180));
            stage.show();
        }

    }

    /**
     * generate random ID
     *
     * @param event click
     */
    @FXML
    public void generateClicked(ActionEvent event) {
        IDgenerated = true;
        Random random = new Random();
        int studentID = random.nextInt(999999) % 900000 + 100000;
        boolean test = false;
        do {
            try {
                Connect db = new Connect();
                java.sql.Connection conn = db.Connect();
                Statement st = conn.createStatement();
                //INSERT INTO visitor (visitdate,name,program,gender,reason) VALUES ('2017-10-31','Troy','MISM','male','stapler')
                //insert into students(ID,program,name,age,gender) VALUES (1,'MISM','Nion','22','male')
                ResultSet rs = st.executeQuery("select ID from studentInformatt");  // create table

                while (rs.next()) {
                    if (studentID == rs.getInt("ID"));
                    {
                        test = true;
                        studentID = random.nextInt(999999) % 900000 + 100000;
                        test = false;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (test == true);
        //System.out.println("suceess1");
        //System.out.println(studentID);
        sID.setText(String.valueOf(studentID));
        IDgenerated = true;

    }

    /**
     * save information
     *
     * @param event click
     */
    @FXML
    public void save1Clicked(ActionEvent event) {
//        String str = name.getText();
//        System.out.println(str);
        String reason = (String) visitReason.getValue().toString();
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dates = df.format(date);
        String Name = name.getText();
        String Program = program.getText();
        //int age = Integer.valueOf(addAge.getText());      
        String Gender = gender.getText();
        try {
            Connect db = new Connect();
            java.sql.Connection conn = db.Connect();
            Statement st = conn.createStatement();
            //st.execute("insert into studentInforssss(ID,program,name,age,gender) values(" + studentID + ",'" + program + "','" + name + "','" + age + "','" + gender + "')");
            //INSERT INTO visitor (visitdate,name,program,gender,reason) VALUES ('2017-10-31','Troy','MISM','male','stapler')
            st.execute("insert into viss(visitdate,name,program,gender,reason) values ('" + dates + "','" + Name + "','" + Program + "','" + Gender + "','" + reason + "')");
            //System.out.println("suceess");
            Parent root = FXMLLoader.load(getClass().getResource("save.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 270, 120));
            stage.show();
            name.setText("");
            age.setText("");
            program.setText("");
            gender.setText("");
            last.setText("");
            lastReason.setText(" ");
            historic.setText("");
            visitReason.setValue(null);
            Recognitionpic.setImage(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * show barchart
     *
     * @param event click
     */
    @FXML
    private void showChart(ActionEvent event) {
        int year = start.getValue().getYear();
        int month = start.getValue().getMonthValue();
        int day = start.getValue().getDayOfMonth();
        String starts = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        //System.out.println(starts);

        int years = end.getValue().getYear();
        int months = end.getValue().getMonthValue();
        int days = end.getValue().getDayOfMonth();
        String ends = Integer.toString(years) + "-" + Integer.toString(months) + "-" + Integer.toString(days);
        //System.out.println(ends);
        String gen = (String) gend.getValue().toString();
        try {
            Connect db = new Connect();
            java.sql.Connection conn = db.Connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select REASON,COUNT(REASON) as numbers from viss where "
                    + "GENDER='" + gen + "' AND " + "VISITDATE>='" + starts + "' and VISITDATE<='" + ends + "'" + "GROUP BY REASON");

            Position = new CategoryAxis();//for bar chart
            Position.setLabel("Attendance Reasons");
            Salary = new NumberAxis();
            Salary.setLabel("Attendance Frequencies");
            barChart1 = new BarChart<>(Position, Salary);
            barChart1.setTitle("Frequencies for different attendance reasons");
            while (rs.next()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(rs.getString(1));
                series.getData().add(new XYChart.Data<>("Attendance Reasons", rs.getInt(2)));
                barChart1.getData().add(series);
                //System.out.println(rs.getString(1) + rs.getInt(2));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(barChart1);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * search for tableview
     *
     * @param event click
     */
    @FXML
    private void getSearch(ActionEvent event) {

        //System.out.println("You clicked me!");
        //label.setText("Hello World!");
        int year = start.getValue().getYear();
        int month = start.getValue().getMonthValue();
        int day = start.getValue().getDayOfMonth();
        String starts = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        //System.out.println(starts);
        int years = end.getValue().getYear();
        int months = end.getValue().getMonthValue();
        int days = end.getValue().getDayOfMonth();
        String ends = Integer.toString(years) + "-" + Integer.toString(months) + "-" + Integer.toString(days);
        //System.out.println(ends);
        String test = (String) reasonss.getValue().toString();
        String gends = (String) gend.getValue().toString();
        String pros = (String) programss.getValue().toString();
        //System.out.println(test);
        try {
            Connect db = new Connect();
            Connection conn = db.Connect();
            data = FXCollections.observableArrayList();
            ResultSet rs = conn.createStatement().executeQuery("select visitdate,ids,name,program,gender,reason from viss where reason='" + test + "' and gender='" + gends + "' and program='" + pros + "'and"
                    + " VISITDATE>='" + starts + "' and VISITDATE<='" + ends + "'");
            /*                  String s = "2017-11-9";
            String query = "select PROGRAM,GENDER,"
            + "COUNT(NAME) AS FREQUENCY,VISITDATE from VISIT "
            + "where VISITDATE>='2017-10-31' and VISITDATE<='"  +s+  " ' "
            + "GROUP BY PROGRAM,GENDER,VISITDATE";*/
            while (rs.next()) {   //int id=rs.getInt(2);
                //String s=new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate(1));
                //System.out.println(s);
                // System.out.print(rs.getString("visitdate"));

                data.add(new StudentInfor(rs.getString("visitdate"), Integer.toString(rs.getInt("ids")), rs.getString("NAME"), rs.getString("PROGRAM"), rs.getString("GENDER"), rs.getString("REASON")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        visitdate.setCellValueFactory(new PropertyValueFactory<>("visitdate"));
        idss.setCellValueFactory(new PropertyValueFactory<>("ids"));
        names.setCellValueFactory(new PropertyValueFactory<>("name"));
        pro.setCellValueFactory(new PropertyValueFactory<>("program"));
        gen.setCellValueFactory(new PropertyValueFactory<>("gender"));
        reason.setCellValueFactory(new PropertyValueFactory<>("reason"));

        all.setItems(null);
        all.setItems(data);
    }

    @FXML
    public void exit2Clicked(ActionEvent event) {
        System.exit(0);
    }

}
