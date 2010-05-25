/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gdcalendar.gui;

import gdcalendar.logic.AnimationDriver;
import gdcalendar.logic.IAnimatedComponent;
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
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
public class HelpGlassPane extends JPanel implements IAnimatedComponent {

    ArrayList<BufferedImage> imageList;
    BufferedImage imageToShow;
    File imgDir;
    File[] imgFiles;
    String helpType;
    JLabel textLabel;
    ResourceBundle resource;

    public HelpGlassPane(Container contentPane, String helpType) {
        setSize(new Dimension(686, 514));
        setOpaque(false);

        this.helpType = helpType;

        imageToShow = null;
        imageList = new ArrayList<BufferedImage>();
        loadImages();

        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");

        textLabel = new JLabel(resource.getString("onlinehelp.defaulttext.text"));
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
        g.drawImage(imageToShow, 140, 25, null);
    }
    
    private int step = 0;
    private boolean isAnimationFinished = false;
    private boolean hasMarker = true;
    private int inc = 1;

    @Override
    public boolean animationFinished() {
        // Check performed in computeAnimation().
        return false;
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
        }
    }

    @Override
    public void displayAnimatation() {
        this.repaint();
    }

    @Override
    public double preferredFPS() {
        // return 1.0;   // used for testing purposes
        return 0.15;
    }

    @Override
    public void cleanup() {
        // Do nothing for this.
    }
}
