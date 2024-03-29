package gdcalendar.gui;

import gdcalendar.logic.AnimationDriver;
import gdcalendar.logic.IAnimatedComponent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
@SuppressWarnings("serial")
public class HelpGlassPane extends JPanel implements IAnimatedComponent {

    ArrayList<BufferedImage> imageList;
    BufferedImage imageToShow;
    File imgDir;
    File[] imgFiles;
    String helpType;
    JLabel textLabel;
    ResourceBundle resource;

    Container contentPane;
    MainWindow parentFrame;

    public HelpGlassPane(MainWindow frame, String helpType) {
        contentPane = frame.getContentPane();
        parentFrame = frame;
        setSize(new Dimension(686, 514));
        setOpaque(true);
        setBackground(Color.black);

        this.helpType = helpType;

        imageToShow = null;
        imageList = new ArrayList<BufferedImage>();
        loadImages();

        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");

        textLabel = new JLabel(resource.getString("onlinehelp.defaulttext.text"));
        textLabel.setForeground(Color.white);
        add(textLabel);

        AnimationDriver.getInstance().run(this, "show help image");
    }

    private void loadImages() {
        BufferedImage img = null;
        imgDir = new File(System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "helpimages" + File.separator + helpType);
        imgFiles = imgDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.startsWith(".")) {
                    return false;
                } else {
                    return true;
                }
            }
        });

        for (int i = 0; i < imgFiles.length; i++) {
            try {
                img = ImageIO.read(imgFiles[i]);
                imageList.add(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int xOffset = 0;
        int yOffset = 0;

        try {
            xOffset = (this.getWidth() / 2) - (imageToShow.getWidth() / 2);
            yOffset = ((this.getHeight() / 2) - (imageToShow.getHeight() / 2)) + 18;
        } catch (NullPointerException e) {
            // Do nothing... Doesn't affect anything.
        }

        textLabel.setLocation(textLabel.getLocation().x, yOffset-25);
        g.drawImage(imageToShow, xOffset, yOffset, null);
    }
    
    private int step = 0;
    private int inc = 1;

    private boolean finished = false;

    @Override
    public boolean animationFinished() {
        return finished;
    }

    /*
     * switch between images in the help.
     */
    @Override
    public void computeAnimatation() {
        if (step < imgFiles.length) {
            imageToShow = imageList.get(step);
            try {
                textLabel.setText(resource.getString("onlinehelp." + helpType + "." + (step+1) + ".text"));
            }
            catch (MissingResourceException ex) {
                textLabel.setText(resource.getString("onlinehelp.defaulttext.text"));
            }
            step += inc;
        } else {
            AnimationDriver.getInstance().removeAll("show help image");
            setVisible(false);

            askDoAction();

            finished = true;
        }
    }

    private void askDoAction() {
        int doAction = JOptionPane.showConfirmDialog(
                parentFrame,
                resource.getString("help.doaction." + helpType + ".text"),
                "Complete the Task?",
                JOptionPane.YES_NO_OPTION);
        if (doAction == JOptionPane.YES_OPTION) {
            parentFrame.doAction(helpType);
        }
        else {
            // nothin'
        }
    }

    @Override
    public void displayAnimatation() {
        this.repaint();
    }

    @Override
    public double preferredFPS() {
        //return 1.0;   // used for testing purposes
        return 0.15;
    }

    @Override
    public void cleanup() {
        // Do nothing for this.
    }
}
