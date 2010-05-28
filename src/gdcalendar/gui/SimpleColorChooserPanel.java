package gdcalendar.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * ColorChooserPanel showing a simplyfied selection of colorButtons
 * @author Tomas
 */
public class SimpleColorChooserPanel extends AbstractColorChooserPanel
        implements ActionListener {

    //List of all selectable colors
    ArrayList<JToggleButton> colorButtons = new ArrayList<JToggleButton>();

    public void updateChooser() {
    }

    protected void buildChooser() {
        for (int r = 75; r < 256; r += 60) {
            for (int g = 75; g < 256; g += 60) {
                for (int b = 75; b < 256; b += 60) {
                    JToggleButton button = new JToggleButton();
                    Color color = new Color(r, g, b);
                    button.setBackground(color);
                    button.setActionCommand(color.toString());
                    button.addActionListener(this);
                    button.setPreferredSize(new Dimension(15, 15));
                    button.setBorder(BorderFactory.createLineBorder(Color.black));
                    colorButtons.add(button);
                }
            }
        }
        setLayout(new GridLayout(0, 16));
        ButtonGroup boxOfColors = new ButtonGroup();
        for (JToggleButton button : colorButtons) {
            boxOfColors.add(button);
            add(button);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Color newColor = ((JToggleButton) e.getSource()).getBackground();
        ((JToggleButton) e.getSource()).setSelected(true);
        getColorSelectionModel().setSelectedColor(newColor);
    }

    public String getDisplayName() {
        return "SimpleColors";
    }

    public Icon getSmallDisplayIcon() {
        return null;
    }

    public Icon getLargeDisplayIcon() {
        return null;
    }
}

