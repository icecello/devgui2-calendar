package gdcalendar.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * About window showing some quick info about this application.
 *
 * @author James
 */
public class AboutWindow extends JDialog {

    public AboutWindow(Frame parent) {
        super(parent);
        initializeComponents();
    }

    /**
     * Initializes the components within this dialog.
     *
     * TODO: Use ResourceBundle
     */
    private void initializeComponents() {
        JLabel appTitleLabel = new JLabel("GDCalendar");
        JLabel versionLabel = new JLabel("Version");
        JLabel appVersionLabel = new JLabel("0.4");
        JLabel vendorLabel = new JLabel("Author:");
        JLabel appVendorLabel = new JLabel("Group D");
        JLabel appDescLabel = new JLabel("A calendar application.");
        JLabel imageLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About GDCalendar");
        setModal(true);
        setResizable(false);

        // Set appropriate fonts  on labels
        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+4));
        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));

        //  Set up the layout for the components
        //   _____________________________________
        //  | TITLE             | TITLE           |
        //  | Descriptive text  | Descriptive text|
        //  | Version:          | ######          |
        //  | Author:           | name            |
        //  |                   |                 |
        //  |                   | |Close button|  |
        //  |___________________|_________________|

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(imageLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(versionLabel)
                            .addComponent(vendorLabel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(appVersionLabel)
                            .addComponent(appVendorLabel)))
                    .addComponent(appTitleLabel, GroupLayout.Alignment.LEADING)
                    .addComponent(appDescLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
        )));
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appTitleLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appDescLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(versionLabel)
                    .addComponent(appVersionLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(vendorLabel)
                    .addComponent(appVendorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }
}
