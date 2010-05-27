package gdcalendar.gui;

import gdcalendar.mvc.model.Category;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * General window for showing and manipulating categories.
 * Notifies all that are listning when the save button is 
 * pressed. The notification message is specified at construction time.
 * All who are listening to the specified message are sent the DayEvent
 * described in the input fields.
 *
 * @author Tomas
 */
public class CategoryWindow extends JDialog {

    private JTextField titleField;
    private JTextArea descTextArea;
    private JColorChooser colorChooser;
    private JButton saveButton;
    private JButton cancelButton;
    private String notificationMessage;
    private ResourceBundle resource;
    private JPanel previewPanel = new JPanel();
    private Category category;

    public CategoryWindow(String notificationMessage, Category category) {
        this.notificationMessage = notificationMessage;
        this.category = category;
        setLayout(new GridBagLayout());
        setTitle(notificationMessage + " Category");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        setResizable(false);

        // TITLE PANEL //
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.CategoryWindow.border.title")));
        titleField = new JTextField(20);
        titlePanel.add(titleField);
        // DESCRIPTION PANEL
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.CategoryWindow.border.description")));
        descTextArea = new JTextArea(5, 20);
        descTextArea.setLineWrap(true);
        descriptionPanel.add(descTextArea);

        // COLOR PANEL
        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.CategoryWindow.border.color")));
        initColorChooser();
        colorPanel.add(colorChooser);

        // BUTTON PANEL //
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton(resource.getString("gdcalendar.gui.EventWindow.save"));
        saveButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Category cat = createCategory();
                firePropertyChange(CategoryWindow.this.notificationMessage, CategoryWindow.this.category, cat);
                CategoryWindow.this.dispose();
            }
        });
        cancelButton = new JButton(resource.getString("gdcalendar.gui.EventWindow.cancel"));
        cancelButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                CategoryWindow.this.dispose();
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        //If the DayEvent argument !=null, set the values of the fields that matches the input
        if (category != null) {
            titleField.setText(category.getName());
            descTextArea.setText(category.getDescription());
            colorChooser.setColor(category.getCategoryColor());
        }

        // ADD THE PANELS //
        c.gridx = 0;
        c.gridy = 0;
        add(titlePanel, c);
        c.gridy = 1;
        c.gridheight = 5;
        add(descriptionPanel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        add(colorPanel, c);
        c.gridx = 1;
        c.gridy = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.insets = new Insets(0, 50, 0, 50);
        add(previewPanel, c);
        c.gridx = 1;
        c.gridheight = 1;
        c.gridy = 5;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        add(buttonPanel, c);
        pack();

    }

    /**
     * Initialize a custom color chooser
     */
    private void initColorChooser() {
        colorChooser = new JColorChooser();

        //Make the preview panel show the selected color
        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        previewPanel.setBackground(colorChooser.getColor());
                    }
                });
            }
        });
        //Remove all default chooser panels
        AbstractColorChooserPanel[] choosers = colorChooser.getChooserPanels();
        for (int i = 0; i < choosers.length; i++) {
            colorChooser.removeChooserPanel(choosers[i]);
        }
        //Customize color chooser by setting custom preview panel
        //and custom colorChooserPanel
        colorChooser.setPreviewPanel(previewPanel);
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        colorChooser.addChooserPanel(new SimpleColorChooserPanel());
        colorChooser.setPreferredSize(new Dimension(250, 75));

    }

    private Category createCategory(){
        String name = titleField.getText();
        String desc = descTextArea.getText();
        Color color = colorChooser.getColor();
        return new Category(name, desc, color);
    }

    public static void main(String[] args) {
        Category cat = new Category("category", "this is a category", new Color(196,196,255));
        CategoryWindow c = new CategoryWindow("Edit", cat);
        c.setVisible(true);

    }
}
