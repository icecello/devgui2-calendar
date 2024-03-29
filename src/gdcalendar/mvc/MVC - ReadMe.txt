MVC model ReadMe

Structure of the MVC:

Each custom componenent in the MVC (model, view or controller) is supposed to be extend from its corresponding base class (AbstractModel, AbstractViewPanel or AbstractController).
The base classes enable the custom implemetend model, controller and view to communicate with each other. 
The communication between the model and the view should go through the controller. Changes to the model and the view should be taken care of by the controller. Hence, there should be no direct calls to the access methods in the model or view.


The communication pattern:

To change an entry in the model, call the corresponding method in the controller. It is up to the controller to call the correct access method in the model! When the model is updated it will send an appropriate notification message back to the controller. The controller will pass this message to the connected views, which in its turn will have to take appropriate action.

A simple example:

____MODEL___ 

public class Book extends AbstractModel{

	String title = "";

	public void setTitle(String newValue){
		String oldValue = title;
		title = newValue;
		firePropertyChange("title",oldValue,newValue);
	}

	public String getTitle(){
		return title;
	}

	...
}


____CONTROLLER___

public class BookController extends AbstractContoller{

	public void setTitle(String title){
		setModelProperty("Title",title);
	}

	...
}

____VIEW____

public class BookView extends AbstractViewPanel{
	
JLabel titleLabel = new JLabel();

	public void modelPropertyChange(PropertyChangeEvent evt){
		if(evt.getPropertyName.equals("title"){
			titleLabel.setText(evt.getNewValue);
		} else if ...
		
		...	
	}
}

______________________________________________

To setup the MVC the following code is needed:

BookModel model = new BookModel();
BookController controller = new BookController();
BookView view = new BookView();

controller.addModel(model);
controller.addView(view);
______________________________________________

Updating a field in model and the view is now done by simply calling the correct method in the controller i.e.

controller.setTitle("This is the new Title");


