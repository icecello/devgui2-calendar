/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

/**
 *
 * @author Tomas
 */
public class AddEventWindow extends JDialog {

    private JTextField titleField;
    private JSpinner startDateSpinner;
    private JSpinner startHourSpinner;
    private JSpinner startMinuteSpinner;
    private JSpinner startTimeSpinner;
    private JSpinner endDateSpinner;
    private JSpinner endTimeSpinner;
    private JSpinner endHourSpinner;
    private JSpinner endMinuteSpinner;
    private JComboBox categoryComboBox;
    private JComboBox priorityComboBox;
    private JTextArea descTextArea;
    private JButton saveButton;
    private JButton cancelButton;
    private Calendar calendar;
    private String[] months = {"January","February","March","April","May","June",
                                "July", "August","September","October","November","Decemeber"};
    


    public AddEventWindow(Date date) {
        setTitle("Add New Event");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
        categoryComboBox = new JComboBox();
        categoryPanel.add(categoryComboBox);

        JPanel priorityPanel = new JPanel();
        priorityPanel.setBorder(BorderFactory.createTitledBorder("Priority"));
        priorityComboBox = new JComboBox();
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
        cancelButton = new JButton("Cancel");
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
        if (value > 55) { return 0; }
        while (value % 5 != 0) {
            value = value + 1;
        }
        return value;
    }

    public static void main(String[] args){
        AddEventWindow window = new AddEventWindow(new Date());
        window.setVisible(true);
    }

}
