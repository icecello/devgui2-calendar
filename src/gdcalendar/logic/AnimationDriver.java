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
 * 
 * Note (Håkan): I removed array of TimerTask's since a cancelled TimerTask is useless, so there's
 * little point in keeping them stored.
 * 
 * @author Tomas
 * @author Håkan
 */
public class AnimationDriver {

    //Singleton instance of the driver
    private static AnimationDriver _instance = new AnimationDriver();
    //Hold all animation components
    private ArrayList<IAnimatedComponent> animatedComponents = new ArrayList<IAnimatedComponent>();
    //Single timer, responsible for updating the animatedComponents at the correct pace
    private Timer timer = new Timer("animation driver", true);  
                                                                
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
    public void add(final IAnimatedComponent component) {
        animatedComponents.add(component);
    }

    /**
     * Add a component that is supposed to be added and animated right away
     * to the AnimationDriver. This is not in use currently, we need to
     * modify the driver a bit to allow dynamically adding new animations
     * while it is active, or change something anyway, perhaps removing this
     * entirely if we deem it useless.
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
     * Run the animation for the specified component
     * This component can exist in the AnimationDriver's internal list
     * of animations, but it is not required. It can just as well be
     * invoked on a separate component that implements this interface.
     * 
     * @param component
     */
    public void run(final IAnimatedComponent component) {
    	timer.purge();
    	
    	if (animatedComponents.contains(component)) {
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
  
    		timer.scheduleAtFixedRate(t, 10, (long)1000/(component.preferredFPS()) );
    		
    	} else {
    		//component doesn't already exist, we need something different
    	}
    }
    /**
     * Run all animations loaded into the AnimationDriver. Each component is
     * run in it's own thread, getting updated and repainted at it's specified
     * rate. 
     */
    public void runAll() {
        timer.purge();
        for (int i = 0; i < animatedComponents.size(); i++) {
        	final IAnimatedComponent component = animatedComponents.get(i); 
        	
        	if (!component.animationFinished() ) {
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
                
		        timer.scheduleAtFixedRate(t, 10, (long)1000/(animatedComponents.get(i).preferredFPS()) );
        	}
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
