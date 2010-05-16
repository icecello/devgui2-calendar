package gdcalendar.mvc.controller;

import gdcalendar.mvc.model.AbstractModel;
import gdcalendar.mvc.model.Day;
import gdcalendar.mvc.view.AbstractViewPanel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * abstract controller
 * 
 * @author H�kan
 * @author Tomas
 * @author James
 *
 */
public abstract class AbstractController implements PropertyChangeListener {

    private ArrayList <AbstractViewPanel> registeredViews;
    private ArrayList <AbstractModel> registeredModels;

    public AbstractController() {
        registeredViews = new ArrayList<AbstractViewPanel>();
        registeredModels = new ArrayList<AbstractModel>();
    }


    public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    public void removeAllModels(){
        for(AbstractModel model : registeredModels){
            model.removePropertyChangeListener(this);
        }
        registeredModels.clear();
    }

    public void addView(AbstractViewPanel view) {
        registeredViews.add(view);
    }

    public void removeView(AbstractViewPanel view) {
        registeredViews.remove(view);
    }

        public void removeAllViews(){
        registeredViews.clear();
    }

    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.


    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractViewPanel view: registeredViews) {
            view.modelPropertyChange(evt);
        }
    }


    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value
     * of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {
        for (AbstractModel model: registeredModels) {
            try {

                Method method = model.getClass().
                    getMethod(propertyName, new Class[] {
                                                      newValue.getClass()
                                                  }
                    

                             );
                method.invoke(model, newValue);

            } catch (Exception ex) {
                //  Handle exception.
            }
        }
    }

//    protected void modifyModelProperty(String propertyName, Collection newValues) {
//        for (AbstractModel model: registeredModels) {
//            try {
//
//                ArrayList<Class> args = new ArrayList<Class>();
//                for(Object values : newValues){
//                    args.add(values.getClass());
//                }
//                Method method = model.getClass().
//                    getMethod(propertyName, (Class[])args.toArray());
//
//                method.invoke(model, newValues);
//
//            } catch (Exception ex) {
//                //  Handle exception.
//            }
//        }
//    }

    public static void main(String[] args){
        Day d = new Day(Calendar.getInstance());
        CalendarController def = new CalendarController();
        def.addModel(d);
    }


}
