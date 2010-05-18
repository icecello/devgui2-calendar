package gdcalendar;

import gdcalendar.gui.MainWindow;
import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;

/**
 *
 * @author Tomas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {



        MainWindow mainWindow = null;
        try {
            mainWindow = new MainWindow();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mainWindow.setVisible(true);


    }
}
