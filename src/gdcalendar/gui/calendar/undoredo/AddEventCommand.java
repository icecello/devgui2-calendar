package gdcalendar.gui.calendar.undoredo;

import commandmanager.ICommand;

import gdcalendar.mvc.model.CalendarModel;
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
	
	private CalendarModel model;
	private DayEvent event;
	
	/**
	 * 
	 * @param controller
	 * @param event
	 */
	public AddEventCommand(CalendarModel cModel, DayEvent event) {
		this.model = cModel;
		this.event = event;
	}
	
	@Override
	public void execute() {
		// add this command
        model.addDayEvent(event);
	}

	@Override
	public void undo() {
		// remove this command again, but keep it's state
		// so we can redo it
		model.removeDayEvent(event.getID());

	}
	
	@Override
	public void redo() {
		execute();
	}

}
