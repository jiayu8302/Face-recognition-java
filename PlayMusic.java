/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

/**
 *
 * @author mac
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;

/**
 *
 * @author mingfeng
 */
public class PlayMusic {

    public static AudioClip loadSound(String filename) {
        URL url = null;
        try {
            url = new URL("file:" + filename);
        } catch (MalformedURLException e) {;
        }
        return JApplet.newAudioClip(url);
    }

    public void play() {
        AudioClip christmas = loadSound("bg.wav");
        christmas.play();
    }
    /**
     * @param args the command line arguments
     */
    //public static void main(String[] args) {
    // TODO code application logic here
    //   PlayMusic p=new PlayMusic();  
    // p.play();  
    //}

}
