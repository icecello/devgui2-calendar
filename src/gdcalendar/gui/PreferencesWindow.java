package gdcalendar.gui;

import gdcalendar.gui.calendar.daycard.MonthDayCard.Marker;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

/**
 * A simple preferences window that allows changing opacity and choosing
 * a animation marker.
 * 
 * @author James
 * @author Håkan
 */
@SuppressWarnings("serial")
public class PreferencesWindow extends JFrame {
    private TransparencyPanel transparencyPanel;

    public PreferencesWindow(final MainWindow mainWindow) {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
                
        transparencyPanel = new TransparencyPanel(mainWindow);
        JPanel panel = new JPanel();
        
        ButtonGroup buttonGroup = new ButtonGroup();
        
        JRadioButton b1 = new JRadioButton(new AbstractAction("None") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.changeMarker(Marker.NONE);
			}
		});
        JRadioButton b2 = new JRadioButton(new AbstractAction("Triangle gradient") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.changeMarker(Marker.TRIANGLE_GRADIENT);
			}
        });
        buttonGroup.add(b1);
        buttonGroup.add(b2);
        
        panel.add(new JLabel("Animation marker:"));
        panel.add(b1);
        panel.add(b2);
        
        tabbedPane.add("Opacity", transparencyPanel);
        tabbedPane.add("Animation marker", panel);

        add(tabbedPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
