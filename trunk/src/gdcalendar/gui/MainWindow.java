package gdcalendar.gui;

import gdcalendar.gui.calendar.CalendarContainer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import undomanager.CommandManager;

/**
 *
 * @author Tomas
 * @author Håkan
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    public MainWindow() {
        setLayout(new BorderLayout());
        final CommandManager cm = new CommandManager(10);

        /*
         * construct a simple menu, this is temporary since we
         * may want to change how we use actions for instance
         */
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("menu");
        JMenuItem undoItem = new JMenuItem(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					cm.undo(1);
				} catch (Exception e1) {
					// TODO add proper exception handling, stack trace
					// is good for debugging only
					e1.printStackTrace();
				}
			}
		});
        
        JMenuItem redoItem = new JMenuItem(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
					try {
						cm.redo(1);
					} catch (Exception e1) {
						// TODO add proper exception handling, stack trace
						// is good for debugging only
						e1.printStackTrace();
					}
			}
		});
        
        menu.add(undoItem);
        menu.add(redoItem);
        
        mb.add(menu);
        this.setJMenuBar(mb);
        
        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setCollapsButtonSize(5);
        collapsiblePanel.add(new LeftItemPanel(), BorderLayout.PAGE_START, -1);
        collapsiblePanel.add(new TransparencyPanel(this), BorderLayout.PAGE_END, -1);

        add(new CalendarContainer(cm), BorderLayout.CENTER);
        
        add(collapsiblePanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

   
        
        pack();
    }
}
