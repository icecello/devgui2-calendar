package gdcalendar.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Tomas
 */
@SuppressWarnings("serial")
public class LeftItemPanel extends JPanel {

    public LeftItemPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        add(new JLabel("Item 1"), c);
        c.gridy = 1;
        add(new JLabel("Item 2"), c);
        c.gridy = 2;
        add(new JLabel("Item 3"), c);
        c.gridy = 3;
        add(new JLabel("Item 4"), c);
        c.gridy = 4;
        add(new JLabel("Item 5"), c);
    }
}
