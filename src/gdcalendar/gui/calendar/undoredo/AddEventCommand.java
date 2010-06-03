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
 * @author Tomas
 */
public class AddEventCommand implements ICommand {

    private CalendarController controller;
    private DayEvent event;

    /**
     * Create a command that adds a DayEvent
     * @param controller the controller attached which should take care of
     * adding the DayEvent
     * @param event the event to be added
    **/
    public AddEventCommand(CalendarController cConttroller, DayEvent event) {
        this.controller = cConttroller;
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
        controller.removeDayEvent(event.getID());

    }

    @Override
    public void redo() {
        execute();
    }
}
