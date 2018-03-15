package sample;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

/**
 * using to detect face and implement face recognition
 *
 * @author mac
 */
public class FaceDetection {

    private CascadeClassifier faceCascade; //face detection classifier
    private int faceSize; // face pic storation size
    private int initialLabel; //face storation ID
    private int[] calculation; // using to calculate accuracy
    private int index, indexMax; // the pic number needed to take

    /**
     * the protected prediction returned to FXcontroller
     */
    protected static int returnID;

    /**
     * constructor without parameters input
     */
    protected FaceDetection() {
        this.index = 0;
        this.indexMax = 50;//number of photos for each person
        this.faceCascade = new CascadeClassifier();
        this.faceCascade.load("resources/haarcascades/haarcascade_frontalface_alt.xml");//loading detection xml
        this.faceSize = 0;
        this.calculation = new int[1000000];//array to test accuracy

    }

    /**
     * constructor with input ID
     *
     * @param ID local label for each person
     */
    protected FaceDetection(int ID) {
        this.index = 0;
        this.indexMax = 50;
        this.faceCascade = new CascadeClassifier();
        this.faceCascade.load("resources/haarcascades/haarcascade_frontalface_alt.xml");
        this.faceSize = 0;
        this.initialLabel = ID;
        this.calculation = new int[1000000];
    }

    /**
     * main method to deal with face detection and recognition
     *
     * @param frame input frame by controller
     * @param choice input choice of detection or recognition
     * @return frame revised
     */
    protected Mat faceProcessing(Mat frame, int choice) {
        Rect[] features = faceDetection(frame);
        frame = displayRectangle(frame, features);//add rectangles of detection
        frame = faceManagement(features, choice, frame);
        return frame;
    }

    /**
     * detect arrays of face
     *
     * @param frame input FX frame by videoIO
     * @return details of each face
     */
    private Rect[] faceDetection(Mat frame) {
        MatOfRect faces = new MatOfRect();
        Mat grayPic = new Mat();
        Imgproc.cvtColor(frame, grayPic, Imgproc.COLOR_BGR2GRAY);//transite to gray photo
        Imgproc.equalizeHist(grayPic, grayPic);// make more accurate

        if (this.faceSize == 0) { //make uniform face photo size
            int height = grayPic.rows();
            if (Math.round(height * 0.2) > 0) {
                this.faceSize = (int) Math.round(height * 0.2);
            }
        }

        this.faceCascade.detectMultiScale(grayPic, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.faceSize, this.faceSize), new Size());// using classifier to detect faces

        Rect[] facesFeature = faces.toArray();
        return facesFeature;
    }

    /**
     * add rectangles on each face
     *
     * @param frame input frame
     * @param facesFeature features got from the previous method
     * @return frame
     */
    private Mat displayRectangle(Mat frame, Rect[] facesFeature) {
        for (int i = 0; i < facesFeature.length; i++) {
            Imgproc.rectangle(frame, facesFeature[i].tl(), facesFeature[i].br(), new Scalar(255, 0, 0), 4);//draw the rectangle
        }
        return frame;
    }

    /**
     * manage choices from detection and recognition
     *
     * @param facesFeature array from previous methods
     * @param choice detection or recognition
     * @param frame initial frame
     * @return returned frame
     */
    private Mat faceManagement(Rect[] facesFeature, int choice, Mat frame) {
        for (int i = 0; i < facesFeature.length; i++) {// recognition or detection of every face
            Rect croppedRect = new Rect(facesFeature[i].tl(), facesFeature[i].br());//get each one
            Mat croppedFace = new Mat(frame, croppedRect); // cropped matric
            Imgproc.cvtColor(croppedFace, croppedFace, Imgproc.COLOR_BGR2GRAY);// transit photo type
            Imgproc.equalizeHist(croppedFace, croppedFace);//make more accuary
            Mat resizedFace = new Mat();
            Size faceLocalSize = new Size(250, 250);//resize the face photo
            Imgproc.resize(croppedFace, resizedFace, faceLocalSize);//make face photos to proper size
            if (choice == 0) {//store the phot
                imageWriting(resizedFace);
            } else {//recognize the photo
                frame = imageRecognition(resizedFace, croppedRect, frame);
            }
        }
        return frame;
    }

    /**
     * writing photos into local directory
     *
     * @param resizedFace resized photo
     */
    private void imageWriting(Mat resizedFace) {
        File theDir = new File("./resources/trainingset/combined/" + initialLabel);//different label different directory
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        if (index < indexMax) {// store the number of indexMax
            Imgcodecs.imwrite("resources/trainingset/combined/"
                    + initialLabel + "/" + initialLabel + "_" + (index++) + ".png", resizedFace);
        }
    }

    /**
     * recognize faces
     *
     * @param resizedFace resized face photo
     * @param croppedRect matrix with initial face
     * @param frame initial frame
     * @return revised frame
     */
    private Mat imageRecognition(Mat resizedFace, Rect croppedRect, Mat frame) {
        Imgcodecs.imwrite("tmp.png", resizedFace); //using javacpp.opencv to open
        org.bytedeco.javacpp.opencv_core.Mat img = imread("tmp.png", CV_LOAD_IMAGE_GRAYSCALE);
        double[] returnedResults = FaceRecogntion.predict(img);
        double prediction = returnedResults[0];//predict
        //System.out.println(prediction);

        double confidence = returnedResults[1];//get confidence

        returnID = (int) prediction;//the predict label
        String studentID;
        if ((int) prediction != 0) {
            studentID = "ID: " + (int) prediction;
        } else {
            studentID = "Unknown";//if studentID is unknown
        }

        String box_text = "Student " + studentID + " Accuracy = " + (int) (accuracyCalculation(prediction, calculation) * 100) + "%";//calculate the accuaracy + " Confidence = " + confidence;
        if (prediction != 0) {
            Imgproc.putText(frame, box_text, new Point(Math.max(croppedRect.tl().x - 10, 0), Math.max(croppedRect.tl().y - 10, 0)),
                    Core.FONT_HERSHEY_PLAIN, 2.0, new Scalar(0, 255, 0, 2.0));
        }//put green words
        else {
            Imgproc.putText(frame, box_text, new Point(Math.max(croppedRect.tl().x - 10, 0), Math.max(croppedRect.tl().y - 10, 0)),
                    Core.FONT_HERSHEY_PLAIN, 2.0, new Scalar(0, 0, 255, 2.0));
        } //put red words for unknown
        File file = new File("tmp.png");//delete local photo for every process
        file.delete();
        return frame;
    }

    /**
     * calculate the prediction accuracy
     *
     * @param prediction predicted label
     * @param array array for store accuracy
     */
    private double accuracyCalculation(double prediction, int[] array) {
        array[(int) prediction]++;
        int max = 0, sum = 0, position = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                sum += array[i];
                position = i;
            }
        }
        //System.out.println("position " + position + " probability " + (double) max / sum);//proortion of maximum number in predictions
        return (double) max / sum;
    }

    /**
     * return returnID
     *
     * @return int of ID
     */
    protected int getReturnID() {
        return returnID;
    }

    /**
     * return whether finish writing
     *
     * @return boolean of finish
     */
    protected boolean finshWrite() {
        return index == indexMax;
    }
}
