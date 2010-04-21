package gdcalendar.gui.calendar.undoredo;

import gdcalendar.mvc.controller.DefaultController;
import gdcalendar.mvc.model.DayEvent;
import undomanager.ICommand;

/**
 * This class implements the add event command. By making
 * this into an explicit command we can construct a queue
 * of commands to undo/redo.
 * 
 * @author Håkan
 *
 */
public class AddEventCommand implements ICommand {
	
	private DefaultController controller;
	private DayEvent event;
	
	/**
	 * 
	 * @param controller
	 * @param event
	 */
	public AddEventCommand(DefaultController controller, DayEvent event) {
		this.controller = controller;
		this.event = event;
	}
	
	@Override
	public void execute() {
		// add this command
        controller.addEvent(event);
	}

	@Override
	public void undo() {
		// remove this command again, but keep it's state
		// so we can redo it
		controller.removeEvent(event);
	}

}
