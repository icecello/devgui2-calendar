/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gdcalendar.gui;

import gdcalendar.mvc.model.Category;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.DayEvent.Priority;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;
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

/**
 *
 * @author Tomas
 */
public class AddEventWindow extends JDialog {


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
    private PropertyChangeSupport propSupport;

    public AddEventWindow(Date date) {
        setTitle("Add New Event");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setResizable(false);
        setLayout(new GridLayout(6, 0));
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
        cat[0] = new Category("category1", "descp");

        categoryComboBox = new JComboBox(cat);
        categoryPanel.add(categoryComboBox);

        JPanel priorityPanel = new JPanel();
        priorityPanel.setBorder(BorderFactory.createTitledBorder("Priority"));
        priorityComboBox = new JComboBox(Priority.values());
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
            }
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                AddEventWindow.this.dispose();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                AddEventWindow.this.dispose();
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // ADD THE PANELS //

        add(titlePanel);
        add(startTimePanel);
        add(endTimePanel);
        add(catPrioPanel);
        add(descriptionPanel);
        add(buttonPanel);

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
        firePropertyChange("add", null, dayEvent);
    }

    public static void main(String[] args) {
        AddEventWindow window = new AddEventWindow(new Date());
        window.setVisible(true);
    }
}
