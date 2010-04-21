package gdcalendar.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 *
 * @author James
 */
public class TransparencyPanel extends JPanel {
    public TransparencyPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Opacity"), BorderLayout.PAGE_START);
        add(new JSlider(SwingConstants.VERTICAL, 0, 100, 100), BorderLayout.CENTER);
    }
}
