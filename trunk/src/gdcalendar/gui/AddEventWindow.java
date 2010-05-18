/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gdcalendar.gui;

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

/**
 *
 * @author Tomas
 */
public class AddEventWindow extends JDialog {

    private JSpinner startYearSpinner;
    private JComboBox startMonthComboBox ;
    private JSpinner startDateSpinner;
    private JSpinner startTimeSpinner;
    private JSpinner endYearSpinner;
    private JComboBox endMonth ;
    private JSpinner endDateSpinner;
    private JSpinner endTimeSpinner;
    private Calendar calendar;
    private String[] months = {"January","February","March","April","May","June",
                                "July", "August","September","October","November","Decemeber"};
    


    public AddEventWindow(Date date) {
        setLayout(new GridLayout(0, 2));
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentYear = calendar.get(Calendar.YEAR);
        SpinnerModel yearModel = new SpinnerNumberModel(currentYear, currentYear-100, currentYear+100, 1);

        startYearSpinner = new JSpinner(yearModel);
        startMonthComboBox = new JComboBox(months);
        startMonthComboBox.setSelectedIndex(calendar.get(Calendar.MONTH));
        JLabel l = new JLabel("Year: ");
        add(l);
        l.setLabelFor(startYearSpinner);

        add(startYearSpinner);
        l = new JLabel("Montha: ");
        add(l);
        l.setLabelFor(startYearSpinner);
        add(startMonthComboBox);

        pack();
    }

public static void main(String[] args){
    AddEventWindow window = new AddEventWindow(new Date());
    window.setVisible(true);
   
}

}
