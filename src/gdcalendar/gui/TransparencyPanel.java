package gdcalendar.gui;

import com.sun.awt.AWTUtilities;
import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author James
 */
@SuppressWarnings({ "restriction", "serial" })
public class TransparencyPanel extends JPanel {
    private Window transparencyFrame;
    private JSlider opacitySlider;
    private JLabel percentLabel;

    public TransparencyPanel(Window transparencyFrame) {
        setLayout(new BorderLayout());
        this.transparencyFrame = transparencyFrame;
        opacitySlider = new JSlider(SwingConstants.HORIZONTAL, 20, 100, 100);
        percentLabel = new JLabel(Integer.toString(opacitySlider.getValue()) + "%", JLabel.CENTER);

        add(new JLabel("Opacity", JLabel.CENTER), BorderLayout.PAGE_START);
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
        AWTUtilities.setWindowOpacity(transparencyFrame, (float) opacitySlider.getValue()/100);
    }
}
