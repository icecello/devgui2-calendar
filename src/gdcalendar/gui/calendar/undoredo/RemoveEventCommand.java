package gdcalendar.gui.calendar.undoredo;

import commandmanager.ICommand;

import gdcalendar.mvc.controller.CalendarController;
import gdcalendar.mvc.model.DayEvent;
import java.util.UUID;

/**
 * This class implements the remove event command.
 * It's just the opposite of AddEvent but it should
 * exist so we can undo the remove operations.
 * 
 * 
 * @author Håkan
 *
 */
public class RemoveEventCommand implements ICommand {
	private CalendarController controller;
	private DayEvent event;
	
	/**
	 * 
	 * @param controller
	 */
	public RemoveEventCommand(CalendarController controller) {
		this.controller = controller;
		this.event = null;
	}

	@Override
	public void execute() {
		controller.removeEvent(event.getID());

	}

	@Override
	public void undo() {
		controller.addDayEvent(event);
	}

	@Override
	public void redo() {
		execute();
	}
}
