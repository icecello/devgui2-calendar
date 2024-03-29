package commandmanager;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is the Invoker class from the Command pattern. It's a general purpose command
 * queue that can remember a specified amount of commands. This can for example be used
 * to implement undo/redo operations or macros.
 * 
 * Note: the idea here is that we associate one CommandManager with one calendar, that way
 * if we allow adding multiple calendars they have their own CommandManagers.
 * 
 * 
 * @author Håkan
 *
 */
public class CommandManager {

	private int commandLimit;
	private ArrayList<ICommand> commandQueue;
	/*
	 * Integer keeping track of which command in in the queue
	 * is the latest active one.
	 */
	private int lastCommand;
	
	/**
	 * constructor
	 * Initialize the CommandManager and specify how many levels of commands
	 * that it will remember.
	 * 
	 * @param limit		integer count for how many commands it will remember
	 */
	public CommandManager(int limit) {
		commandLimit = limit;
		commandQueue = new ArrayList<ICommand>();
		lastCommand = 0;
		
	}
	
	/**
	 * Execute all commands entered into the manager from
	 * start to finish.
	 */
	public void execute() {
		Iterator<ICommand> iterator = commandQueue.iterator();
		
		while (iterator.hasNext()) {
			ICommand command = iterator.next();
			command.execute();
		}
	}
	
	/**
	 * Execute and store specified command in the manager.
	 * 
	 * @param command
	 */
	public void execute(ICommand command) {
		/*
		 * If we have reached the command limit, we must remove the
		 * first element of the queue before we can add a new one.
		 * This automatically shifts all other elements of the queue
		 * one step to the left.
		 */
		if ((commandQueue.size() >= commandLimit) && (commandLimit > 0)) {
			commandQueue.remove(0);
			lastCommand--;
		}
		
		/* 
		 * If lastCommand is not equal to the current size of commandQueue
		 * we have undone commands and should thus overwrite those. 
		 * We do this by first removing all undone commands from the queue 
		 * and then adding the new command.
		 * 
		 */
		if (lastCommand < commandQueue.size()) {
			for (int i = (lastCommand); i < commandQueue.size(); i++)
				commandQueue.remove(i);
		}
		
		commandQueue.add(command);
		command.execute();
		lastCommand++;
	}
	
	/**
	 * Add specified command to the command manager, bypassing
	 * immediate execution. Use this if you want to store 
	 * a sequence of commands for later execution. 
	 * 
	 * @param command		command to add to the manager
	 */
	public void add(ICommand command) {
		/*
		 * If we have reached the command limit, we must remove the
		 * first element of the queue before we can add a new one.
		 * This automatically shifts all other elements of the queue
		 * one step to the left.
		 */
		if ((commandQueue.size() >= commandLimit) && (commandLimit > 0)) {
			commandQueue.remove(0);
			lastCommand--;
		}
		
		commandQueue.add(command);
		lastCommand++;
	}
	
	/**
	 * Remove specified command from the manager
	 * Throws an exception if the removal fails.
	 * 
	 * @param command	command to remove from the manager
	 */
	public void remove(ICommand command) {
		commandQueue.remove(commandQueue.lastIndexOf(command));
		lastCommand--;
	}
	
	/**
	 * Remove the last command entered into the manager
	 * Purely convenience function...
	 */
	public void removeLast() {
		ICommand command = commandQueue.get(commandQueue.size());
		/*
		 * we use the undo operation for removal as well since
		 * the behaviour is essentially the same
		 */
		command.undo();
		commandQueue.remove(commandQueue.size());
		lastCommand--;
	}
	
	/**
	 * Undo a specified level of commands. This calls the undo()
    	 * method of each command from the end of the queue and
	 * 'levels' amount of commands back, reverting anything they
	 * have previously done. They are all still kept in the
	 * manager so they can be redone. Adding new commands to the
	 * queue will overwrite any undone commands.
	 * 
	 * @param levels		how many levels of commands to undo
	 * @throws Exception 
	 */
	public void undo(int levels) throws Exception {
		//ensure we don't try to undo more commands than there are available in the queue
		if (commandQueue.size() < levels)
			throw new Exception("Unable to undo more commands than there are available in the queue");
		
		ICommand command = commandQueue.get(lastCommand-1);
		while (levels > 0) {			
			command.undo();
			levels--;
			/*
			 * only decrease lastCommand and fetch the commmand
			 * if we are above 0, otherwise we go out of bounds
			 */
			if (lastCommand > 0) {
				lastCommand--;
				command = commandQueue.get(lastCommand);
			}
		}
		
	}
	
	/**
	 * Redo a specified number of commands that have previously been
	 * undone. If no commands have been undone we throw an exception.
	 * 
	 * @param levels		how many levels of commands to redo
	 */
	public void redo(int levels) throws Exception {
		if (lastCommand == commandQueue.size())
			throw new Exception("There are no commands to redo");
		
		ICommand command = commandQueue.get(lastCommand);
		while (levels > 0) {	
			command.redo();
			levels--;
			
			/*
			 * conditional increase of lastCommand to avoid
			 * going out of bounds on the list of commands
			 */
			if (lastCommand < commandQueue.size()) {
				lastCommand++;
				command = commandQueue.get(lastCommand-1);
			}
		}
	}
	
	/**
	 * Check whether the command manager contains any
	 * commands or not.
	 * @return	true if the command queue is empty
	 */
	public boolean isEmpty() {
		return commandQueue.isEmpty();
	}
	
	/**
	 * Check whether the command manager has any commands
	 * that can be undone.
	 * 
	 * This is only a simple check against a counter of
	 * commands, nothing fancy.
	 * 
	 * @return		true if it is possible to undo commands
	 */
	public boolean canUndo() {
		return (lastCommand > 0);
	}
	
	/**
	 * Check if the command manager can redo any commands.
	 * This is only possible if at least one undo() operation
	 * has previously been performed.
	 * 
	 * @return		true if it is possible to redo any commands
	 */
	public boolean canRedo() {
		return (lastCommand < commandQueue.size());
	}
}
