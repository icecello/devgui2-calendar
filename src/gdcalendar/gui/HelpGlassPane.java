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
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author James
 */
public class HelpGlassPane extends JComponent implements IAnimatedComponent {

    ArrayList<BufferedImage> imageList;
    BufferedImage imageToShow;
    File imgDir;
    File[] imgFiles;

    public HelpGlassPane(Container contentPane) {
        setSize(new Dimension(400, 400));

        imageToShow = null;
        imageList = new ArrayList<BufferedImage>();
        loadImages();

        AnimationDriver.getInstance().run(this, "show help image");
    }

    private void loadImages() {
        BufferedImage img = null;
        imgDir = new File(System.getProperty("user.dir") + "\\src\\helpimages\\addevent");
        imgFiles = imgDir.listFiles();

        for (int i=0; i<imgFiles.length; i++) {
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
        g.drawImage(imageToShow, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        /*if (img == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(img.getWidth(null), img.getHeight(null));
       }*/
        return new Dimension(600, 600);
    }

    private int step = 0;
    private boolean isAnimationFinished = false;
    private boolean hasMarker = true;
    private int inc = 1;

    @Override
    public boolean animationFinished() {
            //cancel the animation if it has finished or if this daycard has no marker
            //to animate
            //if (isAnimationFinished || highlightMarker == Marker.NONE)
            //	return true;
            return false;
    }

    /*
     * perform a simple animation that smoothly changes between two colors
     * in the triangle, any other animation is not yet supported
     */
    @Override
    public void computeAnimatation() {
        if (step < imgFiles.length) {
            imageToShow = imageList.get(step);
            step += inc;
        } else {
            AnimationDriver.getInstance().stopThread("show help image");
            setVisible(false);
        }
    }

    @Override
    public void displayAnimatation() {
            this.repaint();
    }

    @Override
    public double preferredFPS() {
            return 0.3;
    }

    @Override
    public void cleanup() {
        // Do nothing for this.
    }
}
