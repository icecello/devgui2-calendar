package gdcalendar.gui.calendar.undoredo;

import commandmanager.ICommand;

import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.DayEvent;

/**
 * This class implements the add event command. By making
 * this into an explicit command we can construct a queue
 * of commands to undo/redo.
 * 
 * @author Håkan
 *
 */
public class AddEventCommand implements ICommand {
	
	private CalendarController controller;
	private DayEvent event;
	
	/**
	 * 
	 * @param controller
	 * @param event
	 */
	public AddEventCommand(CalendarController controller, DayEvent event) {
		this.controller = controller;
		this.event = event;
	}
	
	@Override
	public void execute() {
		// add this command
        controller.addDayEvent(event);
	}

	@Override
	public void undo() {
		// remove this command again, but keep it's state
		// so we can redo it
		controller.removeEvent(event.getID());
	}
	
	@Override
	public void redo() {
		execute();
	}

}
