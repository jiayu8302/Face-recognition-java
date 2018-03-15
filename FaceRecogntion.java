package sample;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import javafx.scene.image.Image;
import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * using for face recognition
 *
 * @author mac
 */
public class FaceRecogntion {

    static private org.bytedeco.javacpp.opencv_core.MatVector image; //input image

    static private org.bytedeco.javacpp.opencv_core.Mat labels;//label for recognition

    static private int fileNum; //store the file

    static private String csvPath = "./imageLable.csv";// csv store photos information

    static private String rootPath = "resources/trainingset/combined/";// finding dataset path

    static private String testPath = "resources/trainingset/test/";//path for testing pho

    static opencv_face.FaceRecognizer eigenRecer = opencv_face.createEigenFaceRecognizer();// three different recognition classfier
    static opencv_face.FaceRecognizer fisherRecer = opencv_face.createFisherFaceRecognizer();
    static opencv_face.FaceRecognizer lbphRecer = opencv_face.createLBPHFaceRecognizer(1, 8, 8, 8, 40);//the accuarcy of recogntion

    /**
     * methods using to train models
     */
    public static void faceTrainAndRec() {

        creCsv();//make csv
        readCsv(csvPath);//read all files
        train();//train models
    }

    /**
     * methods for prediction
     *
     * @param img reading the img for prediction
     * @return return prediction result
     */
    protected static double[] predict(org.bytedeco.javacpp.opencv_core.Mat img) {
        // read images
//        org.bytedeco.javacpp.opencv_core.Mat testImg = imread(testPath + "4.png", CV_LOAD_IMAGE_GRAYSCALE);

//        eigenRecer.read("data.xml");
        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        lbphRecer.predict(img, label, confidence);// method for classificaiton
        return new double[]{label.get(0), confidence.get(0)};
    }

    /**
     * train the model
     */
    protected static void train() {
//        eigenRecer.train(image, labels);
//        fisherRecer.train(image, labels);
        lbphRecer.train(image, labels);
//        eigenRecer.save("data.xml");
    }

    /**
     * get each training pic
     *
     * @param ID storing id
     * @return return image
     */
    protected static Image getPic(int ID) {
        String IDpath = "resources/trainingset/combined/" + ID + "/" + ID + "_9.png";
        org.opencv.core.Mat img = Imgcodecs.imread(IDpath, CV_LOAD_IMAGE_GRAYSCALE);
        Image studentPic = VideoCamera.mat2Image(img);
        return studentPic;
    }

    /**
     * read images from paths stored in Csv
     *
     * @param filename each filename
     */
    protected static void readCsv(final String filename) {
        File csv = new File(filename);
        String tmpLine = "", imagePath = "", labelsValue = "";
        long tmpRow = 0;

        image = new org.bytedeco.javacpp.opencv_core.MatVector(fileNum); //image get
        labels = new org.bytedeco.javacpp.opencv_core.Mat(fileNum, 1, CV_32SC1);
        IntBuffer labelsBuffer = labels.createBuffer();

        try { //reading 
            InputStreamReader reader = new InputStreamReader(new FileInputStream(csv));
            BufferedReader bufReader = new BufferedReader(reader);

            // read all the images and labels
            while (null != (tmpLine = bufReader.readLine())) {
                imagePath = tmpLine.substring(0, tmpLine.indexOf(','));
                labelsValue = tmpLine.substring(tmpLine.indexOf(',') + 1, tmpLine.length());

                org.bytedeco.javacpp.opencv_core.Mat img = imread(imagePath, CV_LOAD_IMAGE_GRAYSCALE);
                int tmpLabel = Integer.parseInt(labelsValue);

                image.put(tmpRow, img);
                labelsBuffer.put((int) tmpRow, tmpLabel);

                tmpRow++;
            }
            bufReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        // test
        //System.out.println(image.size());
        //System.out.println(labels.toString());
    }

    /**
     * each time renew the Csv to check the local photo databases
     */
    protected static void creCsv() {
        File trainingDataDir = new File(rootPath);
        File[] fileArray = trainingDataDir.listFiles();
        String path = "", tmp = "";
        File[] imagePath;
        fileNum = 0;

        ArrayList<String> lines = new ArrayList<String>();

        for (int i = 0; i < fileArray.length; i++) {// check local databases
            if (fileArray[i].isDirectory()) {
                imagePath = fileArray[i].listFiles();
                for (int j = 0; j < imagePath.length; j++) {

                    if (imagePath[j].getPath().toString().indexOf(".jpg") == -1
                            && imagePath[j].getPath().toString().indexOf(".png") == -1) {
                        continue;
                    }

                    path = imagePath[j].getPath().toString();
                    tmp = path.substring(path.substring(0, path.lastIndexOf('/')).lastIndexOf('/'), path.lastIndexOf('/'));
                    path += "," + tmp.substring(1, tmp.length());
                    path += '\n';
                    lines.add(path);
                }
            }
        }

        // write csv
        File csv = new File(csvPath);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(csv));

            for (int i = 0; i < lines.size(); i++) {
                out.write(lines.get(i));
                fileNum++;
            }
            out.flush();
            out.close();
        } catch (java.io.IOException e) {
            return;
        }
    }
}
