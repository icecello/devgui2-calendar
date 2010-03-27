package gdcalendar.gui;

import gdcalendar.gui.calendar.CalendarContainer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Tomas
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        setLayout(new BorderLayout());
        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        Container leftContainer = collapsiblePanel.getContentPane();
        JPanel leftPanel = new JPanel(new GridLayout());
        leftPanel.add(new JLabel("hej hej hej"));

        leftContainer.add(leftPanel);

        add(new CalendarContainer(), BorderLayout.CENTER);
        add(collapsiblePanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
    }
}
