package gdcalendar.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI.ChangeHandler;

/**
 *
 * @author James
 */
public class TransparencyPanel extends JPanel {
    
    private JSlider opacitySlider;
    private JLabel percentLabel;

    public TransparencyPanel() {
        setLayout(new BorderLayout());
        
        opacitySlider = new JSlider(SwingConstants.VERTICAL, 0, 100, 100);
        percentLabel = new JLabel(Integer.toString(opacitySlider.getValue()) + "%");

        add(new JLabel("Opacity"), BorderLayout.PAGE_START);
        add(opacitySlider, BorderLayout.CENTER);
        add(percentLabel, BorderLayout.PAGE_END);

        initListeners();
    }

    private void initListeners() {

        opacitySlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                TransparencyPanel.this.sliderChanged(e);
            }
        });
    }

    private void sliderChanged(ChangeEvent e) {
        percentLabel.setText(Integer.toString(opacitySlider.getValue()) + "%");
    }
}
