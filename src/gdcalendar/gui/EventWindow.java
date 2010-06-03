package gdcalendar.gui;

import gdcalendar.Main;
import gdcalendar.mvc.model.Category;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.DayEvent.Priority;
import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;

/**
 *General window for showing and manipulating events.
 * Notifies all that are listning when the save button is 
 * pressed. The notification message is specified at construction time.
 * All who are listening to the specified message are sent the DayEvent
 * described in the input fields.
 *
 * @author Tomas
 * @author James
 */
@SuppressWarnings("serial")
public class EventWindow extends JDialog {

    private JLabel addCategory;
    private JLabel removeCategory;
    private JLabel editCategory;
    private JTextField titleField;
    private JSpinner startDateSpinner;
    private JSpinner startTimeSpinner;
    private JSpinner endDateSpinner;
    private JSpinner endTimeSpinner;
    private JComboBox categoryComboBox;
    private JComboBox priorityComboBox;
    private JTextArea descTextArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Calendar calendar;
    private String notificationMessage;
    private ResourceBundle resource;

    public EventWindow(Date date, DayEvent dayEvent, String notificationString) {
        resource = ResourceBundle.getBundle("gdcalendar.resource_en_US");
        setTitle(notificationString + resource.getString("gdcalendar.gui.EventWindow.windowTitle"));
        this.notificationMessage = notificationString;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        // TITLE PANEL //

        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.EventWindow.border.title")));
        titleField = new JTextField(20);
        titlePanel.add(titleField);

        // START TIME //

        Calendar startTimeCal = (Calendar) calendar.clone();
        Date startInitTime = startTimeCal.getTime();
        startTimeCal.add(Calendar.YEAR, -100);
        Date startEarliestTime = startTimeCal.getTime();
        startTimeCal.add(Calendar.YEAR, 200);
        Date startLatestTime = startTimeCal.getTime();
        SpinnerModel startTimeModel = new SpinnerDateModel(startInitTime,
                startEarliestTime,
                startLatestTime,
                Calendar.YEAR);    //ignored for user input

        startTimeSpinner = new JSpinner(startTimeModel);
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, resource.getString("gdcalendar.gui.EventWindow.format.time")));

        // START DATE //

        Calendar startDateCal = (Calendar) calendar.clone();
        Date startInitDate = startDateCal.getTime();
        startDateCal.add(Calendar.YEAR, -100);
        Date startEarliestDate = startDateCal.getTime();
        startDateCal.add(Calendar.YEAR, 200);
        Date startLatestDate = startDateCal.getTime();
        SpinnerModel startDateModel = new SpinnerDateModel(startInitDate,
                startEarliestDate,
                startLatestDate,
                Calendar.YEAR);    //ignored for user input

        startDateSpinner = new JSpinner(startDateModel);
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, resource.getString("gdcalendar.gui.EventWindow.format.date")));

        // START TIME PANEL //

        JPanel startTimePanel = new JPanel();
        startTimePanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.EventWindow.border.startTime")));

        startTimePanel.add(startTimeSpinner);
        startTimePanel.add(startDateSpinner);

        // END TIME //

        Calendar endTimeCal = (Calendar) calendar.clone();
        endTimeCal.add(Calendar.HOUR, 1);
        Date endInitTime = endTimeCal.getTime();
        endTimeCal.add(Calendar.YEAR, -100);
        Date endEarliestTime = endTimeCal.getTime();
        endTimeCal.add(Calendar.YEAR, 200);
        Date endLatestTime = endTimeCal.getTime();
        SpinnerModel endTimeModel = new SpinnerDateModel(endInitTime,
                endEarliestTime,
                endLatestTime,
                Calendar.YEAR);    //ignored for user input

        endTimeSpinner = new JSpinner(endTimeModel);
        endTimeSpinner.setEditor(new JSpinner.DateEditor(endTimeSpinner, resource.getString("gdcalendar.gui.EventWindow.format.time")));


        // END DATE //

        Calendar endDateCal = (Calendar) calendar.clone();
        Date endInitDate = endDateCal.getTime();
        endDateCal.add(Calendar.YEAR, -100);
        Date endEarliestDate = endDateCal.getTime();
        endDateCal.add(Calendar.YEAR, 200);
        Date endLatestDate = endDateCal.getTime();
        SpinnerModel endDateModel = new SpinnerDateModel(endInitDate,
                endEarliestDate,
                endLatestDate,
                Calendar.YEAR);    //ignored for user input

        endDateSpinner = new JSpinner(endDateModel);
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, resource.getString("gdcalendar.gui.EventWindow.format.date")));

        //Set the caret to an appropriate position, making the spinner
        //edit the correct field
        JTextField field = ((JSpinner.DateEditor) endTimeSpinner.getEditor()).getTextField();
        field.addFocusListener(new FocusSpinnerLister(field, 3, 4));
        field = ((JSpinner.DateEditor) startTimeSpinner.getEditor()).getTextField();
        field.addFocusListener(new FocusSpinnerLister(field, 3, 4));
        field = ((JSpinner.DateEditor) endDateSpinner.getEditor()).getTextField();
        field.addFocusListener(new FocusSpinnerLister(field, 3, 4));
        field = ((JSpinner.DateEditor) startDateSpinner.getEditor()).getTextField();
        field.addFocusListener(new FocusSpinnerLister(field, 3, 4));


        // END TIME PANEL //

        JPanel endTimePanel = new JPanel();
        endTimePanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.EventWindow.border.endTime")));

        endTimePanel.add(endTimeSpinner);
        endTimePanel.add(endDateSpinner);

        // CATEGORY & PRIORITY PANEL //

        JPanel catPrioPanel = new JPanel(new GridBagLayout());

        JPanel categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.EventWindow.border.category")));

        addCategory = new JLabel("<html><a href='' color=#6666ff>Add</a></html>");
        addCategory.setForeground(Color.BLACK);
        addCategory.addMouseListener(new MouseLinkListner(addCategory, "Add"));
        removeCategory = new JLabel("<html><a href='' color=#6666ff>Remove</a></html>");
        removeCategory.addMouseListener(new MouseLinkListner(removeCategory, "Remove"));
        editCategory = new JLabel("<html><a href='' color=#6666ff>Edit</a></html>");
        editCategory.addMouseListener(new MouseLinkListner(editCategory, "Edit"));

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 5, 10, 5);
        categoryPanel.add(addCategory, c);
        c.gridx = 1;
        categoryPanel.add(removeCategory, c);
        c.gridx = 2;
        categoryPanel.add(editCategory, c);
        categoryComboBox = new JComboBox(Main.categories.values().toArray());
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        categoryPanel.add(categoryComboBox, c);

        JPanel priorityPanel = new JPanel();
        priorityPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.EventWindow.border.priority")));
        priorityComboBox = new JComboBox(Priority.values());
        priorityComboBox.setSelectedItem(Priority.MEDIUM);
        priorityPanel.add(priorityComboBox);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        catPrioPanel.add(categoryPanel, c);
        c.gridx = 1;
        catPrioPanel.add(priorityPanel, c);

        // DESCRIPTION PANEL //

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(resource.getString("gdcalendar.gui.EventWindow.border.description")));
        descTextArea = new JTextArea(4, 20);
        descTextArea.setLineWrap(true);
        descriptionPanel.add(descTextArea);

        // BUTTON PANEL //

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton(resource.getString("gdcalendar.gui.EventWindow.save"));
        saveButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                addEvent();
                EventWindow.this.dispose();
            }
        });
        cancelButton = new JButton(resource.getString("gdcalendar.gui.EventWindow.cancel"));
        cancelButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                EventWindow.this.dispose();
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        //If the DayEvent argument !=null, set the values of the fields that matches the input
        if (dayEvent != null) {
            titleField.setText(dayEvent.getEventName());
            categoryComboBox.setSelectedItem(dayEvent.getCategory());
            priorityComboBox.setSelectedItem(dayEvent.getPriority());
            descTextArea.setText(dayEvent.getDescription());
            startTimeSpinner.setValue(dayEvent.getStartTime());
            startDateSpinner.setValue(dayEvent.getStartTime());
            endTimeSpinner.setValue(dayEvent.getEndTime());
            endDateSpinner.setValue(dayEvent.getEndTime());
        }
        // ADD THE PANELS //
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(titlePanel, c);
        c.gridy = 1;
        c.gridwidth = 1;
        add(startTimePanel, c);
        c.gridy = 2;
        add(endTimePanel, c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(catPrioPanel, c);
        c.gridy = 4;
        c.gridwidth = 2;
        c.gridheight = 3;
        add(descriptionPanel, c);
        c.gridy = 8;
        c.gridheight = 1;
        add(buttonPanel, c);

        pack();
    }

    private class MouseLinkListner extends MouseAdapter {

        private JLabel linkLabel;
        private String linkText;

        public MouseLinkListner(JLabel linkLabel, String linkText) {
            this.linkLabel = linkLabel;
            this.linkText = linkText;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Category cat = null;
            if (linkText.equals("Edit")) {
                cat = (Category) categoryComboBox.getSelectedItem();
            } else if (linkText.equals("Remove")) {
                Main.categories.remove(((Category) categoryComboBox.getSelectedItem()).getName());
                categoryComboBox.removeItemAt(categoryComboBox.getSelectedIndex());
                return;
            }
            CategoryWindow cWindow = new CategoryWindow(linkText, cat);
            cWindow.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    Category newCategory = (Category) evt.getNewValue();
                    Category oldCategory = (Category) evt.getOldValue();
                    if (oldCategory != null) {
                        Main.categories.remove(oldCategory.getName());
                        categoryComboBox.removeItem(oldCategory);
                    }
                    categoryComboBox.addItem(newCategory);
                    Main.categories.put(newCategory.getName(), newCategory);
                    categoryComboBox.setSelectedItem(newCategory);
                    if(linkText.equals("Edit")){
                        EventWindow.this.firePropertyChange(CategoryWindow.CATEGORY_EDITED, oldCategory, newCategory);
                    }
                }
            });
            cWindow.setVisible(true);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkLabel.setText("<html><a href='' color=#6666aa>" + linkText + "</a></html>");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkLabel.setText("<html><a href='' color=#6666ff>" + linkText + "</a></html>");
        }
    };

    /**
     * Get all  values from the input fields, create a DayEvent and fire
     * a propertyChangeEvent
     */
    private void addEvent() {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime((Date) startTimeSpinner.getValue());
        int minute = tempCal.get(Calendar.MINUTE);
        int hour = tempCal.get(Calendar.HOUR_OF_DAY);
        tempCal.setTime((Date) startDateSpinner.getValue());
        tempCal.set(Calendar.HOUR_OF_DAY, hour);
        tempCal.set(Calendar.MINUTE, minute);
        Date startDate = tempCal.getTime();

        tempCal.setTime((Date) endTimeSpinner.getValue());
        minute = tempCal.get(Calendar.MINUTE);
        hour = tempCal.get(Calendar.HOUR_OF_DAY);
        tempCal.setTime((Date) endDateSpinner.getValue());
        tempCal.set(Calendar.HOUR_OF_DAY, hour);
        tempCal.set(Calendar.MINUTE, minute);
        Date endDate = tempCal.getTime();

        String name = titleField.getText();
        Category category = (Category) categoryComboBox.getSelectedItem();
        Priority prio = (Priority) priorityComboBox.getSelectedItem();
        String desc = (String) descTextArea.getText();

        DayEvent dayEvent = new DayEvent(name, startDate, endDate, category, prio);
        dayEvent.setDescription(desc);
        firePropertyChange(notificationMessage, null, dayEvent);
    }

    private class FocusSpinnerLister extends FocusAdapter {

        private int selectionStart, selectionEnd;
        private JTextField field;

        public FocusSpinnerLister(JTextField field, int selectionStart, int selectionEnd) {
            this.field = field;
            this.selectionStart = selectionStart;
            this.selectionEnd = selectionEnd;
        }

        @Override
        public void focusGained(FocusEvent e) {

            if (isEnabled()) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        field.setSelectionStart(selectionStart);
                        field.setCaretPosition(selectionEnd);
                        field.setSelectionEnd(selectionEnd);
                    }
                });
            }
        }
    }

    public static void main(String[] args) {
        Main.categories = XMLUtils.loadCategories(Configuration.getProperty("categories"));
        EventWindow window = new EventWindow(new Date(), null, "Edit");
        window.setVisible(true);
    }
}
