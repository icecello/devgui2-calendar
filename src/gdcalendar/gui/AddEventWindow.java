/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import java.awt.Component;
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
    private JSpinner startYearSpinner;
    private JComboBox startMonthComboBox ;
    private JSpinner startDateSpinner;
    private JSpinner startHourSpinner;
    private JSpinner startMinuteSpinner;
    private JSpinner endYearSpinner;
    private JComboBox endMonthComboBox;
    private JSpinner endDateSpinner;
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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(8, 0));
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createTitledBorder("Title"));
        titleField = new JTextField(20);
        titlePanel.add(titleField);

        int currentYear = calendar.get(Calendar.YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = roundToFive(calendar.get(Calendar.MINUTE));
        SpinnerModel startYearModel = new SpinnerNumberModel(currentYear, currentYear-100, currentYear+100, 1);
        SpinnerModel startDayModel = new SpinnerNumberModel(currentDay, 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 1);
        SpinnerModel startHourModel = new SpinnerNumberModel(currentHour, 0, 23, 1);
        SpinnerModel startMinuteModel = new SpinnerNumberModel(currentMinute, 0, 59, 5);
        SpinnerModel endYearModel = new SpinnerNumberModel(currentYear, currentYear-100, currentYear+100, 1);
        SpinnerModel endDayModel = new SpinnerNumberModel(currentDay, 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 1);
        SpinnerModel endHourModel = new SpinnerNumberModel(currentHour + 1, 0, 23, 1);
        SpinnerModel endMinuteModel = new SpinnerNumberModel(currentMinute, 0, 59, 5);

        JPanel startTimePanel = new JPanel();
        startTimePanel.setBorder(BorderFactory.createTitledBorder("Start Time"));
        startHourSpinner = new JSpinner(startHourModel);
        startMinuteSpinner = new JSpinner(startMinuteModel);
        
        startTimePanel.add(startHourSpinner);
        startTimePanel.add(startMinuteSpinner);

        JPanel startDatePanel = new JPanel();
        startDatePanel.setBorder(BorderFactory.createTitledBorder("Start Date"));
        startDateSpinner = new JSpinner(startDayModel);
        startMonthComboBox = new JComboBox(months);
        startMonthComboBox.setSelectedIndex(calendar.get(Calendar.MONTH));
        startYearSpinner = new JSpinner(startYearModel);

        startDatePanel.add(startDateSpinner);
        startDatePanel.add(startMonthComboBox);
        startDatePanel.add(startYearSpinner);

        JPanel endTimePanel = new JPanel();
        endTimePanel.setBorder(BorderFactory.createTitledBorder("End Time"));
        endHourSpinner = new JSpinner(endHourModel);
        endMinuteSpinner = new JSpinner(endMinuteModel);

        endTimePanel.add(endHourSpinner);
        endTimePanel.add(endMinuteSpinner);

        JPanel endDatePanel = new JPanel();
        endDatePanel.setBorder(BorderFactory.createTitledBorder("End Date"));
        endDateSpinner = new JSpinner(endDayModel);
        endMonthComboBox = new JComboBox(months);
        endMonthComboBox.setSelectedIndex(calendar.get(Calendar.MONTH));
        endYearSpinner = new JSpinner(endYearModel);

        endDatePanel.add(endDateSpinner);
        endDatePanel.add(endMonthComboBox);
        endDatePanel.add(endYearSpinner);

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

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        descTextArea = new JTextArea(4, 20);
        descTextArea.setLineWrap(true);
        descriptionPanel.add(descTextArea);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(titlePanel);
        add(startTimePanel);
        add(startDatePanel);
        add(endTimePanel);
        add(endDatePanel);
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
