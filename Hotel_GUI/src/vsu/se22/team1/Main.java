package vsu.se22.team1;

import java.io.File;

import javax.swing.JFrame;

import vsu.se22.team1.gui.ManagerFrame;

public class Main {

    public static void main(String[] args) {
        JFrame frame;
        if(args.length > 0) {
            // For easier debugging
            frame = new ManagerFrame(new File(args[0]));
        } else {
            frame = new ManagerFrame();
        }
        frame.setVisible(true);
    }
}
