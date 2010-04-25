package commandmanager;

/**
 * 
 * Any implementation on this interface is required to remember the
 * status of anything it affects, since it has to be possible
 * to undo/redo whatever this command does.
 * 
 * @author Håkan
 *
 */
public interface ICommand {
	
	/**
	 * perform whatever this command is supposed to do
	 */
	public void execute();
	
	/**
	 * this method should perform everything necessary to undo what happened
	 * from execute()
	 * 
	 */
	public void undo();
	
	/**
	 * special method handling redoing this commands operations
	 * could be same as execute() if it's simple enough
	 */
	public void redo();
}
