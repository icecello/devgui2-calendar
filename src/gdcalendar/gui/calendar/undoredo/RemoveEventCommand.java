package gdcalendar.gui.calendar.undoredo;

import commandmanager.ICommand;

import gdcalendar.mvc.model.CalendarModel;
import gdcalendar.mvc.model.DayEvent;

/**
 * This class implements the remove event command.
 * It's just the opposite of AddEvent but it should
 * exist so we can undo the remove operations.
 * 
 * 
 * @author Håkan
 * @author Tomas
 */
public class RemoveEventCommand implements ICommand {
	private CalendarModel model;
	private DayEvent event;
	
	/**
	 * 
	 * @param controller
	 */
	public RemoveEventCommand(CalendarModel cModel, DayEvent event) {
		this.model = cModel;
		this.event = event;
	}

	@Override
	public void execute() {
		model.removeDayEvent(event.getID());

	}

	@Override
	public void undo() {
		model.addDayEvent(event);
	}

	@Override
	public void redo() {
		execute();
	}
}
