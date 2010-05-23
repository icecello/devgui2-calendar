/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gdcalendar.gui;

import gdcalendar.Main;
import gdcalendar.mvc.model.Category;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.DayEvent.Priority;
import gdcalendar.xml.Configuration;
import gdcalendar.xml.XMLUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tomas
 */
public class EventWindow extends JDialog {

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

    public EventWindow(Date date, DayEvent dayEvent, String notificationString) {
        setTitle(notificationString +" Event");
        this.notificationMessage = notificationString;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        // TITLE PANEL //

        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder("Title"));
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
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, "HH:mm"));

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
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "MMM dd yyyy"));

        // START TIME PANEL //

        JPanel startTimePanel = new JPanel();
        startTimePanel.setBorder(BorderFactory.createTitledBorder("Start Time"));

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
        endTimeSpinner.setEditor(new JSpinner.DateEditor(endTimeSpinner, "HH:mm"));


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
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "MMM dd yyyy"));
        
        //Make the minute text get highlighted, dunno if this is good or not
        //but the technique could be used in other places
        final JTextField field = ((JSpinner.DateEditor) endTimeSpinner.getEditor()).getTextField();
        field.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {

                if (isEnabled()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            field.setCaretPosition(5);
                            field.setSelectionEnd(5);
                            field.setSelectionStart(3);
                        }
                    });
                }
            }
        });


        // END TIME PANEL //

        JPanel endTimePanel = new JPanel();
        endTimePanel.setBorder(BorderFactory.createTitledBorder("End Time"));

        endTimePanel.add(endTimeSpinner);
        endTimePanel.add(endDateSpinner);

        // CATEGORY & PRIORITY PANEL //

        JPanel catPrioPanel = new JPanel();

        JPanel categoryPanel = new JPanel();
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Category"));
        Category[] cat = new Category[1];


        categoryComboBox = new JComboBox(Main.categories.values().toArray());
        categoryPanel.add(categoryComboBox);

        JPanel priorityPanel = new JPanel();
        priorityPanel.setBorder(BorderFactory.createTitledBorder("Priority"));
        priorityComboBox = new JComboBox(Priority.values());
        priorityComboBox.setSelectedItem(Priority.MEDIUM);
        priorityPanel.add(priorityComboBox);

        catPrioPanel.add(categoryPanel);
        catPrioPanel.add(priorityPanel);

        // DESCRIPTION PANEL //

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        descTextArea = new JTextArea(4, 20);
        descTextArea.setLineWrap(true);
        descriptionPanel.add(descTextArea);

        // BUTTON PANEL //

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save");
        saveButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                addEvent();
                EventWindow.this.dispose();
            }
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                addEvent();
                EventWindow.this.dispose();
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);


        if(dayEvent != null){
            titleField.setText(dayEvent.getEventName());
            categoryComboBox.setSelectedItem(dayEvent.getCategory());
            priorityComboBox.setSelectedItem(dayEvent.getPriority());
            descTextArea.setText(dayEvent.getDescription());
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

    public int roundToFive(int value) {
        if (value > 55) {
            return 0;
        }
        while (value % 5 != 0) {
            value = value + 1;
        }
        return value;
    }

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

    public static void main(String[] args) {
        Main.categories = XMLUtils.loadCategories(Configuration.getProperty("categories"));
        EventWindow window = new EventWindow(new Date(),null, "Edit");
        window.setVisible(true);
    }
}
