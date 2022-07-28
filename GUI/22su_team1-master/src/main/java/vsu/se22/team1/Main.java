package vsu.se22.team1;

import java.io.File;

import vsu.se22.team1.gui.LoginFrame;
import vsu.se22.team1.gui.ManagerFrame;

public class Main {
    public static ManagerFrame managerFrame;

    public static void main(String[] args) {
        if(args.length > 0) {
            // For easier debugging
            managerFrame = new ManagerFrame(new File(args[0]));
        } else {
            managerFrame = new ManagerFrame();
        }

        new LoginFrame().setVisible(true);
    }

    public static void launchManager() {
        managerFrame.setVisible(true);
    }
}
