package gdcalendar.gui.calendar.undoredo;

import gdcalendar.mvc.controller.DefaultController;
import gdcalendar.mvc.model.DayEvent;
import undomanager.ICommand;

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
	private DefaultController controller;
	private DayEvent event;
	
	/**
	 * 
	 * @param controller
	 * @param event
	 */
	public RemoveEventCommand(DefaultController controller) {
		this.controller = controller;
		this.event = null;
	}

	@Override
	public void execute() {
		controller.removeEvent(event);

	}

	@Override
	public void undo() {
		controller.addEvent(event);
	}

}