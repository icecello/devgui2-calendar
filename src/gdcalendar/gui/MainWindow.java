package gdcalendar.gui;

import gdcalendar.gui.calendar.CalendarContainer;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Tomas
 */
public class MainWindow extends JFrame {

    public MainWindow() {
        setLayout(new BorderLayout());


        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        collapsiblePanel.setCollapsButtonSize(5);
        collapsiblePanel.add(new LeftItemPanel());

        add(new CalendarContainer(), BorderLayout.CENTER);
        add(collapsiblePanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();
    }
}
