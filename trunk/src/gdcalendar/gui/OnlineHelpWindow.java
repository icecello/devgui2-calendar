/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author James
 */
public class OnlineHelpWindow extends JFrame {
    public OnlineHelpWindow() {
        super("Online Help");
        setSize(new Dimension(400, 400));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(new JLabel("Online Help goes here somewhere..."));
    }
}
