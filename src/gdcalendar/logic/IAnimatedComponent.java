package gdcalendar.logic;

/**
 * Suggestion of interface for animated components, need a review!
 * Interface for animated components. Class instances implementing this interface
 * can be loaded into an <code>AnimtionDriver</code>
 * 
 * Comment (Håkan): This is a great start :) I imagined something similar, but hadn't even begun
 * on any implementation of the ideas. Now we need to write something up to hand in by tomorrow.
 * 
 * @author Tomas
 */
public interface IAnimatedComponent {

    /**
     * Computations needed for the animation should be done here. However,
     * no non-thread safe GUI updates should be done in this method. These should
     * instead be performed in <code>computeAnimation</code>, guaranteeing that the
     * updates are done in the EDT.
     */
    public void computeAnimatation();

    /**
     *
     * Visual updates to the component´s animation should be done in this method.
     * (Or code needed to be run in the EDT)
     */
    public void displayAnimatation();

    /**
     * Indicate at which rate the animation driver is trying to update the
     * animation of the component. The frames per second value should range from 0 - 100.
     * The value is an estimate, which the animation driver is aiming at.
     * @return The preferred frames per second for the animation, loaded into the animation driver.
     */
    public int preferredFPS();

    /**
     * Show if the animation is done. If the animation is finished, it will no longer
     * be updated by the AnimationDriver in which it¨s loaded.
     * @return True if the animation is finished, false otherwise
     */
    public boolean animationFinished();
}
