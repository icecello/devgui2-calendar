package gdcalendar.gui;

import gdcalendar.gui.calendar.CalendarChangeEvent;
import gdcalendar.gui.calendar.CalendarContainer;
import gdcalendar.gui.calendar.CalendarDataChangedListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import commandmanager.CommandManager;


/**
 *
 * @author Tomas
 * @author Håkan
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JMenuItem undoItem;
	private JMenuItem redoItem;
	
    public MainWindow() {
        setLayout(new BorderLayout());
        final CommandManager cm = new CommandManager(10);

        
        /*
         * construct a simple menu, this is temporary since we
         * may want to change how we use actions for instance
         */
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("Edit");
        undoItem = new JMenuItem(new AbstractAction("Undo") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					cm.undo(1);
					setUndoEnabled(cm.canUndo());
					setRedoEnabled(cm.canRedo());
				} catch (Exception e1) {
					// TODO add proper exception handling, stack trace
					// is good for debugging only
					e1.printStackTrace();
				}
			}
		});
        
        redoItem = new JMenuItem(new AbstractAction("Redo") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					
					try {
						cm.redo(1);
						setUndoEnabled(cm.canUndo());
						setRedoEnabled(cm.canRedo());
					} catch (Exception e1) {
						// TODO add proper exception handling, stack trace
						// is good for debugging only
						e1.printStackTrace();
					}
			}
		});
        
        menu.add(undoItem);
        menu.add(redoItem);
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);
        mb.add(menu);
        this.setJMenuBar(mb);
        
        CollapsiblePanel collapsiblePanel = new CollapsiblePanel(CollapsiblePanel.EAST);
        collapsiblePanel.setLayout(new BorderLayout());
        collapsiblePanel.setCollapsButtonSize(5);
        collapsiblePanel.add(new LeftItemPanel(), BorderLayout.PAGE_START, -1);
        collapsiblePanel.add(new TransparencyPanel(this), BorderLayout.PAGE_END, -1);

        CalendarContainer cc = new CalendarContainer(cm);
        /*
         * Add listener for data change events from the calendar
         * to set undo/redo properly
         * 
         * Not much else is done currently but it's possible
         * to see what kind of change was made if we need that
         * for anything...
         */
        cc.addDataChangeListener(new CalendarDataChangedListener() {
			
			@Override
			public void dataChanged(CalendarChangeEvent e) {
				setUndoEnabled(cm.canUndo());
				setRedoEnabled(cm.canRedo());
			}
		});
        
        add(cc, BorderLayout.CENTER);
        
        add(collapsiblePanel, BorderLayout.LINE_START);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

   
        
        pack();
    }
    
    /**
     * set the undo menu item's enabled state
     * 
     * @param state		true for enabled
     */
    public void setUndoEnabled(boolean state) {
    	undoItem.setEnabled(state);
    }
    
    /**
     * set the redo menu item's enabled state
     * 
     * @param state		true for enabled
     */
    public void setRedoEnabled(boolean state) {
    	redoItem.setEnabled(state);
    }
}
