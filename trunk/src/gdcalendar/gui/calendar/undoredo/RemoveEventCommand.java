package gdcalendar.gui.calendar.undoredo;

import commandmanager.ICommand;
import gdcalendar.mvc.controller.CalendarController;

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

    private CalendarController controller;
    private DayEvent event;

    /**
     * Create a command that removes a DayEvent
     * @param controller the controller attached which should take care of
     * removing the DayEvent
     * @param event the event to be removed
     */
    public RemoveEventCommand(CalendarController cController, DayEvent event) {
        this.controller = cController;
        this.event = event;
    }

    @Override
    public void execute() {
        controller.removeDayEvent(event.getID());

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
