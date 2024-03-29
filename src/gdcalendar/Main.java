package gdcalendar;

import gdcalendar.gui.MainWindow;
import gdcalendar.mvc.model.Category;
import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;
import java.util.HashMap;

import javax.swing.UIManager;

/**
 *
 * @author Tomas
 */
public class Main {

    /**
     * Categories stored in XML are available from this
     * static variable
     */
    public static HashMap<String, Category> categories;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MainWindow mainWindow = null;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //Load the categories from file
            categories = XMLUtils.loadCategories(Configuration.getProperty("categories"));
            mainWindow = new MainWindow();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mainWindow.setVisible(true);
    }
}
