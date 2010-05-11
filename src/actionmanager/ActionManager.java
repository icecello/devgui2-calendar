package actionmanager;

import java.awt.event.ActionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;

/**
 * This class manages actions for an owner object class. The owner class has to
 * define methods prefixed by @Action that implements what action will be
 * performed for it.
 * 
 * The name of this method is used when calling getAction() to construct an
 * AbstractAction that uses the method's defined action. All properties related
 * to an AbstractAction are derived from the associated ResourceBundle, which
 * has to contain specific entries for the actions that are requested.
 * 
 * An entry in the ResourceBundle file should follow this template:
 * <i>full classname</i>.<i>method name</i>.<i>property</i>
 * where property refers to one of the following:
 * text				text to display as name for the associated component
 * tooltip			hovering popup description
 * desc				something else ??
 * 
 * Note: only the property <i>text</i> is actually supported in this current version of
 * ActionManager
 * 
 * @author Håkan
 *
 */
public class ActionManager {
	private Object owner;
	private ResourceBundle resource;
	
	/**
	 * 
	 * @param owner		owner class of this ActionManager, very likely equal to <i>this.getClass()</i>
	 */
	public ActionManager(Object owner, ResourceBundle resource) {
		this.owner = owner;
		this.resource = resource;
	}
	
	/**
	 * This method constructs an AbstractAction based on specified name. The name is used for looking up a
	 * method in the owner class using the same name and for looking up relevant information from a
	 * ResourceBundle.
	 * 
	 * @param actionName		name of the action, refers to method name and it's name in the ResourceBundle
	 * @return					an AbstractAction using available resources based on it's name
	 * @throws Exception 
	 */
	@SuppressWarnings("serial")
	public AbstractAction getAction(String actionName) throws Exception {
		
		AbstractAction newAction = null;
		Class<?> ownerClass = owner.getClass();
		
		/*
		 * iterate over all methods available in the specified owner class
		 */
		Method[] methods = ownerClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			
			/*
			 * See if this particular method is annotated by @Action and if so
			 * we compare it against the requested action to construct.
			 * The new action is then constructed using data from the specified
			 * ResourceBundle.
			 */
			if (method.isAnnotationPresent(Action.class) && method.getName() == actionName && newAction == null) {
				newAction = new AbstractAction() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							method.invoke(owner);
						} catch (IllegalArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvocationTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				};
				String prefix = ownerClass.getName() + "." + actionName + ".";
				newAction.putValue(AbstractAction.NAME, resource.getString(prefix + "text"));
			}
		}
		
		if (newAction != null)
			return newAction;
		else
			throw new Exception("Failed to create action object.");
	}
}
