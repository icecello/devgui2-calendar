package gdcalendar.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

/**
 * The AnimationDriver is responsible for updating the <code>IAnimatedComponent</code> loaded
 * into the driver at their desired pace.
 * Note that the AnimationDriver is a singleton, and hence only one instance of the driver
 * can be present in a running program.
 * 
 * Note (HÃ¥kan): I removed array of TimerTask's since a cancelled TimerTask is useless, so there's
 * little point in keeping them stored.
 * 
 * @author Håkan
 * @author Tomas
 */
public class AnimationDriver {

    //Singleton instance of the driver
    private static AnimationDriver _instance = new AnimationDriver();

    //map of different timers, one for each thread
    private HashMap<String, Timer> timerList = new HashMap<String, Timer>();
    private HashMap<String, ArrayList<IAnimatedComponent> > threadAnimatedComponents = 
    	new HashMap<String, ArrayList<IAnimatedComponent> >();
    //Indicate if the driver is running
    private boolean isRunning = false;
    //Private constructor, making the AnimationDriver a singleton (impossible to instantiate)
    private AnimationDriver() {}

    /**
     * Get the single instance of the AnimationDriver
     * @return An instace of the AnimationDriver
     */
    public static AnimationDriver getInstance() {
        return _instance;
    }

    /**
     * Add to the AnimationDriver a component that should be animated
     * in the standard background thread.
     *  
     * @param component 	The component which is supposed to be animated
     */
    public void add(final IAnimatedComponent component) {
    	ArrayList<IAnimatedComponent> array;
    	if (threadAnimatedComponents.containsKey("GDCalendar_generic_list")) {
    		array = threadAnimatedComponents.get("GDCalendar_generic_list");
    	} else {
    		array = new ArrayList<IAnimatedComponent>();
    	}
    	
    	array.add(component);
    	threadAnimatedComponents.put("GDCalendar_generic_list", array);
    }
    
    /**
     * Add to the AnimationDriver a component that should be animated
     * in a specified thread. If the name has not been used previously,
     * it will be added to the internal list of threadnames.
     *  
     * @param component 	The component which is supposed to be animated
     * @param threadName	Name of thread to add the component to
     */
    public void add(final IAnimatedComponent component, String threadName) {
    	if (!timerList.containsKey(threadName)) {
    		timerList.put(threadName, new Timer(threadName));
    	}
    	
    	ArrayList<IAnimatedComponent> array;
    	if (threadAnimatedComponents.containsKey(threadName)) {
    		array = threadAnimatedComponents.get(threadName);
    	} else {
    		array = new ArrayList<IAnimatedComponent>();
    	}
    	
    	array.add(component);
    	threadAnimatedComponents.put(threadName, array);
    }

    
    /**
     * Remove a specific component from the AnimationDriver. The removed component
     * will no longer be associated with any available threads. It can still be run
     * individudally however by calling run() on it.
     * 
     * If this is called while the animation is running, it will run to completion
     * before being cancelled and removed.
     * 
     * @param component The component to be removed from the driver
     * @return  true if the component could be removed
     *          false if the component couldn't be removed. I.e.
     *          the component was not loaded into the driver in the first place.
     */
    public boolean remove(IAnimatedComponent component, String threadName) {
    	
    	if (threadAnimatedComponents.containsKey(threadName)) {
    		return threadAnimatedComponents.get(threadName).remove(component);
    	}
    	
    	return false;
    }
    
    
    public boolean removeAll(String threadName) {
    	return false;
    }

    /**
     * Run the animation for the specified component in the thread specified
     * by <i>threadName</i>.
     * This component can exist in the AnimationDriver's internal list
     * of animations, but it is not required. It can just as well be
     * invoked on a separate component that implements this interface.
     * 
     * @param component
     */
    public void run(final IAnimatedComponent component, String threadName) {
    	if (!timerList.containsKey(threadName)) {
    		timerList.put(threadName, new Timer(threadName));
    	}
    	
    	add(component, threadName);
    	
    	

    	TimerTask t = new TimerTask() {
    		
            @Override
            public void run() {
                if (component.animationFinished()) {
                	this.cancel();
                } else {
                	component.computeAnimatation();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                        	component.displayAnimatation();
                        }
                    });
                }
            }
        };
  
        timerList.get(threadName).purge();
    	timerList.get(threadName).scheduleAtFixedRate(t, 10, (long)(1000/(component.preferredFPS())) );
   
    }
    /**
     * Run all animations that have been added to the specified thread.
     * 
     * @param threadName	name of thread to run
     */
    public void runThread(String threadName) {
    	
    	

    	for (int i = 0; i < threadAnimatedComponents.get(threadName).size(); i++) {
    		final IAnimatedComponent component = threadAnimatedComponents.get(threadName).get(i);
    	
	    	TimerTask t = new TimerTask() {
	    		
	            @Override
	            public void run() {
	                if (component.animationFinished()) {
	                	this.cancel();
	                } else {
	                	component.computeAnimatation();
	                    SwingUtilities.invokeLater(new Runnable() {
	
	                        public void run() {
	                        	component.displayAnimatation();
	                        }
	                    });
	                }
	            }
	        };
	        timerList.get(threadName).purge();
	        timerList.get(threadName).scheduleAtFixedRate(t, 10, (long)(1000/(component.preferredFPS())) );
    	}
    }
    
    /**
     * Stop all animations running in the specified thread.
     * 
     * @param threadName	name of thread to stop
     * @return 	true if the thread was stopped
     * 			false if the thread doesn't exist or failed to stop
     */
    public boolean stopThread(String threadName) {
    	if (timerList.containsKey(threadName)) {
    		ArrayList<IAnimatedComponent> ar = threadAnimatedComponents.get(threadName);
    		for (int i = 0; i < ar.size(); i++) {
    			ar.get(i).cleanup();
    		}
    		timerList.get(threadName).cancel();
    		timerList.get(threadName).purge();
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * All animation threads are stopped
     * 
     * TODO: stopAll doesn't call cleanup() on components
     */
    public void stopAll() {
    	Collection<Timer> collection = timerList.values();

    	Iterator<Timer> iter = collection.iterator();
    	
    	while (iter.hasNext()) {
    		Timer t = (Timer)iter.next();
    		t.cancel();
    	}
    	
        isRunning = false;
    }
/**
 * Determines if the AnimationDriver is running.
 * A call to <code>runAnimations</code> will make the driver run
 * and the isRunning will return true.
 * A call to <code>stopAnimations</code> will make the driver stop
 * and the isRunning will return false.
 * @return  True if the AnimationDriver is running
 *          False if the AnimationDriver is idle
 */
    public boolean isRunning(){
        return isRunning;
    }
}
