
package gdcalendar.xml;

import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * Configuration class handling window states and storing miscellaneous properties
 * such as XML file paths.
 * @author anve
 */
public class Configuration {

    static private Properties properties;
    static private Properties defaultProperties;
    static private String propertyFileName;

    static {
        defaultProperties = new Properties();

        // Default width and heigt
        int width = 650;
        int height = 300;

        // Get screen size and center of screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - width)/2;
        int y = (dim.height - height)/2;

        defaultProperties.setProperty("window.x", Integer.toString(x));
        defaultProperties.setProperty("window.y", Integer.toString(y));
        defaultProperties.setProperty("window.width", Integer.toString(width));
        defaultProperties.setProperty("window.height", Integer.toString(height));
        defaultProperties.setProperty("calendar", "./src/XML/calendar.xml");
        defaultProperties.setProperty("categoryList", "./src/XML/CategoryList.xml");
        properties = new Properties(defaultProperties);
    }

    /**
     * Loads properties from given file.
     *
     * @param fileName path of the properties file
     */
    public static void loadProperties(String fileName) {

        try {
            FileInputStream fis = new FileInputStream(fileName);
            properties.loadFromXML(fis);
        }
        catch (IOException e){
            // If file is not found, alert the user and use default properties.
            //JOptionPane.showMessageDialog(null, "There was a problem loading user settings. Default settings will be loaded.");
        }
        catch(Exception e){
            System.err.println("Error reading properties: " + e);
        }

        propertyFileName = fileName;
    }

    /**
     *  Saves properties to the file.
     */
    public static void saveProperties() {
        try {
            FileOutputStream fos = new FileOutputStream(propertyFileName);
            properties.storeToXML(fos, "Configuration", "UTF-8");
        }
        catch(Exception e){
            System.err.println("Error saving properties: " + e);
        }
    }

    /**
     *
     * @return properties from file (or default properties)
     */
    public static Properties getProperties() {
        return new Properties(properties);
    }

  /**
   * Returns the value of a property matching given key or a  default value
   * if the property is not found.
   *
   * @param key Key of the property to find
   * @return value of the property
   */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

  /**
   * Sets a given property. An already existing property will be overwritten.
   *
   * @param key Key of the property to set
   * @param value Value of the property
   */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    // Window specialized functions

    /**
     * Stores the window dimension in the properties
     * @param size The dimension of the window
     */
    private static void setWindowDimension(Dimension size){
        if(size != null){
            setProperty("window.width", Integer.toString(size.width));
            setProperty("window.height", Integer.toString(size.height));
        }
    }

    /**
     * Returns the window dimension stored in the properties file.
     *
     * @return Size of the window
     */
    public static Dimension getWindowDimension() {
        Dimension size = null;
        try {
            String w = getProperty("window.width");
            String h = getProperty("window.height");
            if ((w != null) && (h != null)) {
                int width = Integer.parseInt(w);
                int height = Integer.parseInt(h);
                size = new Dimension(width, height);
            }
        } catch (Exception e) {
            // The conversion might have failed. Just catch this and return
            // null.
        }
        return size;
    }

    /**
     * Stores the window position in the properties
     * @param position The position of the window
     */
    public static void setWindowPosition(Point position){
        if(position != null) {
            setProperty("window.x", Integer.toString(position.x));
            setProperty("window.y", Integer.toString(position.y));
        }
    }

    /**
     * Returns the window position stored in the properties file.
     *
     * @return Position of the window
     */
    public static Point getWindowPosition() {
        Point position = null;
        try {
            String x = getProperty("window.x");
            String y = getProperty("window.y");
            if ((x != null) && (y != null)) {
                int xPos = Integer.parseInt(x);
                int yPos = Integer.parseInt(y);
                position = new Point(xPos, yPos);
            }
        } catch (Exception e) {
            // The conversion might have failed. Just catch this and return
            // null.
        }
        return position;
    }

    /**
     * Stores the window state (size, location) in the properties file.
     *
     * @param window The window holding information about size and location.
     */
    public static void setWindowState(Component window) {
        if(window != null) {
            setWindowDimension(window.getSize());
            setWindowPosition(window.getLocation());
            saveProperties();
        }
    }
}
