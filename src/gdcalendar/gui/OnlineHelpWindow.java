/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import actionmanager.Action;
import actionmanager.ActionManager;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

    public OnlineHelpWindow() {
        super("Online Help");
        setSize(new Dimension(400, 400));
        setLayout(new GridLayout(3,0));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        actionManager = new ActionManager(this, resource);

        addEventHelpButton = new JButton(actionManager.getAction("addEventHelp"));
        transparencyHelpButton = new JButton(actionManager.getAction("transparencyHelp"));

        add(new JLabel("I want to see help for:"));
        add(addEventHelpButton);
        add(transparencyHelpButton);

        pack();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    /**
     * Shows help for adding an event.
     */
    @Action
    public void addEventHelp() {
        helpGlassPane = new HelpGlassPane(this.getContentPane(), "addevent");
        this.setGlassPane(helpGlassPane);
        helpGlassPane.setVisible(true);

        //System.out.println("Add Event Help");
        //new PreferencesWindow(this);
    }

    /**
     * Shows help for changing the transparency.
     */
    @Action
    public void transparencyHelp() {
        //System.out.println("Transparency Help");
        //new PreferencesWindow(this);
    }
}
