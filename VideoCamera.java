package sample;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * connect to face detection and Fx controller
 *
 * @author mac
 */
public class VideoCamera {

    private ImageView originalFrame;
    private FaceDetection faceDetection;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private int choice;
    private int ID;
    private FaceDetection facedetection;//FaceDetection Class

    /**
     * constructor
     *
     * @param choice input choice by users
     * @param originalFrame FXcontroller variable
     */
    protected VideoCamera(int choice, ImageView originalFrame) {
        this.capture = new VideoCapture();
        this.choice = choice;
        this.originalFrame = originalFrame;
        originalFrame.setPreserveRatio(true);
        faceDetection = new FaceDetection();
    }

    /**
     * constructor when face detection
     *
     * @param choice choose face detection
     * @param ID input label for person
     * @param originalFrame input Fxcontroller variabel
     */
    protected VideoCamera(int choice, int ID, ImageView originalFrame) {
        this(choice, originalFrame);
        this.ID = ID;
        faceDetection = new FaceDetection(ID);
    }

    /**
     * open the camera
     *
     * @return imageview controller
     */
    protected ImageView startCamera() {
        try {
            if ((choice == 0 && !faceDetection.finshWrite()) || (choice == 1 && faceDetection.getReturnID() != -1)) {
                videoUpdate();// update controller
            } else {
                stopTheThread();//close
                closeCamera(originalFrame);
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        return originalFrame;
    }

    /**
     * open the capture
     */
    protected void videoUpdate() {
        try {
            capture.open(0);
            if (!capture.isOpened()) {
                throw new Exception() {
                };
            }
            showFrame();

        } catch (Exception e) {
            System.err.println("Camera open failed");
        }
    }

    /**
     * show the frame
     */
    protected void showFrame() {
        Runnable grabbedFrame = new frameShowThread();
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(grabbedFrame, 0, 50, TimeUnit.MILLISECONDS);//time delay
    }

    /**
     * stop the thread
     *
     * @throws InterruptedException exception for interruption
     */
    protected void stopTheThread() throws InterruptedException {
        timer.shutdown();
        timer.awaitTermination(50, TimeUnit.MILLISECONDS);
    }

    /**
     * close the camera
     *
     * @param originalFrame
     */
    protected void closeCamera(ImageView originalFrame) {
        try {
            originalFrame.setImage(null);
            stopTheThread();
            capture.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * return the frame
     *
     * @param capture capture by camera
     * @return image changed
     */
    protected Image grabFrame(VideoCapture capture) {
        try {
            if (capture.isOpened()) {

                Image imageToDisplay = null;
                Mat frame = new Mat();

                capture.read(frame);

                if (frame.empty()) {
                    return null;
                }
                if (choice == 0) {
                    frame = faceDetection.faceProcessing(frame, 0);// face processing
                } else {
                    frame = faceDetection.faceProcessing(frame, 1);
                }
                imageToDisplay = mat2Image(frame);//change Mat to image
                return imageToDisplay;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * method to change to availabel image
     *
     * @param frame returned frame
     * @return image to show
     */
    protected static Image mat2Image(Mat frame) {
        // create buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer
        Imgcodecs.imencode(".png", frame, buffer);
        // return to image type
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    /**
     * class implements Runnable
     */
    class frameShowThread implements Runnable {

        @Override
        public void run() {
            Image imageToShow = grabFrame(capture);
            originalFrame.setImage(imageToShow);
        }
    }
}
