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
        Container leftContainer = collapsiblePanel.getContentPanel();


        JPanel leftPanel = new JPanel(new GridLayout(0, 1));
        leftPanel.add(new JLabel("Item 1"));
        leftPanel.add(new JLabel("Item 2"));
        leftPanel.add(new JLabel("Item 3"));
        leftPanel.add(new JLabel("Item 4"));

        leftContainer.add(leftPanel);

        add(new CalendarContainer(), BorderLayout.CENTER);
        add(collapsiblePanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
    }
}
