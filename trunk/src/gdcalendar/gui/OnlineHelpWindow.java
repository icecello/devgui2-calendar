/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import actionmanager.Action;
import actionmanager.ActionManager;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author James
 */
public class OnlineHelpWindow extends JFrame {

    static private HelpGlassPane helpGlassPane;

    ResourceBundle resource;
    ActionManager actionManager;
    JButton addEventHelpButton;
    JButton transparencyHelpButton;
    JPanel panel;

    public OnlineHelpWindow() {
        super("Online Help");
        setSize(new Dimension(842, 527));
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);

        addEventHelpButton = new JButton(actionManager.getAction("addEventHelp"));
        transparencyHelpButton = new JButton(actionManager.getAction("transparencyHelp"));

        panel.add(new JLabel("I want to see help for:"));
        panel.add(addEventHelpButton);
        panel.add(transparencyHelpButton);

        add(panel);

        pack();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(842, 527);
    }

    /**
     * Shows help for adding an event.
     */
    @Action
    public void addEventHelp() {
        helpGlassPane = new HelpGlassPane(this.getContentPane(), "addevent");
        this.setGlassPane(helpGlassPane);
        helpGlassPane.setVisible(true);
    }

    /**
     * Shows help for changing the transparency.
     */
    @Action
    public void transparencyHelp() {
        helpGlassPane = new HelpGlassPane(this.getContentPane(), "transparency");
        this.setGlassPane(helpGlassPane);
        helpGlassPane.setVisible(true);
    }
}
