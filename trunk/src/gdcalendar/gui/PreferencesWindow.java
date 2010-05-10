/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.JFrame;

/**
 *
 * @author James
 */
public class PreferencesWindow extends JFrame {
    private TransparencyPanel transparencyPanel;

    public PreferencesWindow(Window mainWindow) {
        setLayout(new BorderLayout());
        transparencyPanel = new TransparencyPanel(mainWindow);

        add(transparencyPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
