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
    private ArrayList<IAnimatedComponent> animatedComponents;   //Hold all animation components
    private Timer timer = new Timer("animation driver", true);  //Single timer, responsible for updating
    //the animatedComponents at the correct pace
    private TimerTask animations;                               //The set of all animation tasks is gathered
                                                                //into this TimerTask

    private boolean isRunning = false;                          //Indicate if the driver is running
    //Private constructor, making the AnimationDriver a singleton
    private AnimationDriver() {
    }

    /**
     * Get the single instance of the AnimationDriver
     * @return An instace of the AnimationDriver
     */
    public AnimationDriver getInstance() {
        return _instance;
    }

    /**
     * Add a component that is suposed to be animated to the AnimationDriver.
     * This method should not be called while the AnimationDriver is running.
     * @param component The component which is suposed to be animated
     */
    public void addAnimatedComponent(IAnimatedComponent component) {
        animatedComponents.add(component);
    }

    /**
     * Remove a specific component from the AnimationDriver. The removed component
     * will no longer be updated by the driver.
     * This method should not be called while the AnimationDriver is running.
     * @param component The component to be removed from the driver
     * @return  true if the component could be removed
     *          false if the component couldn't be removed. I.e.
     *          the component was not loaded into the driver in the first place.
     */
    public boolean removeAnimatedComponent(IAnimatedComponent component) {
        return animatedComponents.remove(component);

    }

    /**
     * Run all animations loaded into the AnimationDriver. Each component is
     * updated and repainted at it's own specified rate
     */
    public void runAnimations() {
        final int updateInterval = 10;
        animations = new TimerTask() {

            boolean render = false;
            int currentRenderTime = 100;
            int maxRenderTime = 0;

            @Override
            public void run() {
                for (int i = 0; i < animatedComponents.size(); i++) {
                    //Perhaps not final should be used
                    final IAnimatedComponent comp = animatedComponents.get(i);

                    render = (currentRenderTime % 100 / comp.preferredFPS() == 0) ? true : false;
                    if (render|| !comp.animationFinished()) {
                        comp.computeAnimatation();

                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                comp.displayAnimatation();
                            }
                        });
                    }
                }
                currentRenderTime++;
                if (currentRenderTime >= maxRenderTime) {
                    currentRenderTime = 0;
                }
            }
        };
        timer.schedule(animations, updateInterval);
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
