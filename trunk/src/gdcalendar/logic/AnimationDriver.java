package gdcalendar.logic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

/**
 * The AnimationDriver is responsible for updating the <code>IAnimatedComponent</code> loaded
 * into the driver at their desired pace.
 * Note that the AnimationDriver is a singleton, and hence only one instance of the driver
 * can be present in a running program.
 * @author Tomas
 */
public class AnimationDriver {

    //Singleton instance of the driver
    private static AnimationDriver _instance = new AnimationDriver();
    //Hold all animation components
    private ArrayList<IAnimatedComponent> animatedComponents = new ArrayList<IAnimatedComponent>();
    //Single timer, responsible for updating the animatedComponents at the correct pace
    private Timer timer = new Timer("animation driver", true);  
    //The set of all animation tasks is gathered into this TimerTask
    private ArrayList<TimerTask> animations = new ArrayList<TimerTask>(); 
                                                                
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
     * Add a component that is suposed to be animated to the AnimationDriver.
     * This method should not be called while the AnimationDriver is running.
     * @param component The component which is supposed to be animated
     */
    public void add(IAnimatedComponent component) {
        animatedComponents.add(component);
    }

    /**
     * Add a component that is suposed to be animated to the AnimationDriver.
     * This method should not be called while the AnimationDriver is running.
     * 
     * @param component The component which is supposed to be animated
     */
    /*public void addAndRun(final IAnimatedComponent component) {
    	animatedComponents.add(component);
    	
    	TimerTask t = new TimerTask() {
    		
            @Override
            public void run() {
                if (!component.animationFinished()) {
                	component.computeAnimatation();

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                        	component.displayAnimatation();
                        }
                    });
                } else {
                	this.cancel();
                }
            }
        };
        animations.add(t);
        timer.scheduleAtFixedRate(t, 10,(long)1000/(component.preferredFPS()) );
        
    }*/
    
    /**
     * Remove a specific component from the AnimationDriver. The removed component
     * will no longer be updated by the driver.
     * This method should not be called while the AnimationDriver is running.
     * @param component The component to be removed from the driver
     * @return  true if the component could be removed
     *          false if the component couldn't be removed. I.e.
     *          the component was not loaded into the driver in the first place.
     */
    public boolean remove(IAnimatedComponent component) {
        return animatedComponents.remove(component);
    }

    /**
     * Run all animations loaded into the AnimationDriver. Each component is
     * run in it's own thread, getting updated and repainted at it's specified
     * rate. 
     */
    public void runAnimations() {
        
        for (int i = 0; i < animatedComponents.size(); i++) {
        	final int ii = i;
	        TimerTask t = new TimerTask() {
	
	            @Override
	            public void run() {
                    final IAnimatedComponent comp = animatedComponents.get(ii);

                    if (!comp.animationFinished()) {
                        comp.computeAnimatation();

                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                comp.displayAnimatation();
                            }
                        });
                    } else {
                    	this.cancel();
                    }
	            }
	        };
	        animations.add(t);
	        timer.scheduleAtFixedRate(animations.get(i), 10,(long)1000/(animatedComponents.get(i).preferredFPS()) );
	        
        }
        
        isRunning = true;
    }

    /**
     * All animations are stoped
     */
    public void stopAnimations() {
        timer.cancel();
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
