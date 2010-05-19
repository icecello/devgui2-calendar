package gdcalendar.xml;

import gdcalendar.mvc.model.Category;
import gdcalendar.mvc.model.DayEvent;
import gdcalendar.mvc.model.DayEvent.Priority;
import java.io.*;
import java.text.*;
import java.util.*;
import org.jdom.DocType;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;
import org.jdom.output.Format;
import org.w3c.dom.*;

/**
 * A class for handling all XML related functionality.
 *
 * @author anve
 */
public class XMLUtils {

    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     *  Return an element given a Document, tag name and index
     * @param doc 
     * @param tagName
     * @param index
     * @return the element corresponding to the input data
     */
    public static Element getElement(Document doc, String tagName, int index) {
        NodeList rows = doc.getDocumentElement().getElementsByTagName(tagName);
        return (Element) rows.item(index);
    }

    /**
     * Return the number of items in the Document
     * @param doc
     * @param tagName
     * @return the number of items in the document
     */
    public static int getSize(Document doc, String tagName) {
        NodeList rows = doc.getElementsByTagName(tagName);
        return rows.getLength();
    }

    /**
     *  Given a todo element, we must get the element specified
     *  by the tagName, then must traverse that Node to get the
     *  value.
     * @param e
     * @param tagName
     * @return the value of the corresponding element
     */
    public static String getValue(Element e, String tagName) {
        try {
            // Get node lists of a tag name from an Element
            NodeList elements = e.getElementsByTagName(tagName);
            Node node = elements.item(0);
            NodeList nodes = node.getChildNodes();

            // Find a value whose value is non-whitespace
            String s;
            for (int i = 0; i < nodes.getLength(); i++) {
                s = ((Node) nodes.item(i)).getNodeValue().trim();
                if (s.equals("") || s.equals("\r")) {
                    continue;
                } else {
                    return s;
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    /**
     * Loads the categories from file.
     * @param filePath Path to the file holding category information.
     * @return An Arraylist of Category elements.
     */
    /*    public static ArrayList<Category> loadCategories(String filePath){

    ArrayList<Category> categoryList = new ArrayList<Category>();
    if(filePath == null || filePath.equals(""))
    return categoryList;
    try{
    SAXBuilder parser = new SAXBuilder();
    org.jdom.Document jdomDoc  = parser.build(filePath);
    DOMOutputter outputter = new DOMOutputter();
    Document doc = outputter.output(jdomDoc);

    // Loop through each todo-item in XML-file and extract properties
    for(int i=0; i < XMLUtils.getSize(doc, "category"); i++){
    Element element = XMLUtils.getElement(doc, "category", i);
    String name = XMLUtils.getValue(element, "name");

    // Create category item
    Category category = new Category(name);
    categoryList.add(category);
    }
    }
    catch(Exception e)
    {
    System.out.println(e.getMessage());
    }
    return categoryList;
    }
     */
    /**
     * Load tasks(todo items) from file.
     * @param filePath Path to the file holding tasks information.
     * @return An ArrayList of ToDoItem elements.
     */
    public static ArrayList<DayEvent> loadDayEvents(String filePath) {
        ArrayList<DayEvent> eventList = new ArrayList<DayEvent>();
        if (filePath == null || filePath.equals("")) {
            return eventList;
        }
        try {
            SAXBuilder parser = new SAXBuilder();
            org.jdom.Document jdomDoc = parser.build(filePath);
            DOMOutputter outputter = new DOMOutputter();
            Document doc = outputter.output(jdomDoc);

            // Loop through each todo-item in XML-file and extract properties
            for (int i = 0; i < XMLUtils.getSize(doc, "dayEvent"); i++) {
                Element element = XMLUtils.getElement(doc, "dayEvent", i);
                String name = XMLUtils.getValue(element, "name");
                String start = XMLUtils.getValue(element, "startDate");
                String end = XMLUtils.getValue(element, "endDate");
                String category = XMLUtils.getValue(element, "category");
                String priority = XMLUtils.getValue(element, "priority");

                Date startDate = null, endDate = null;
                try {
                    startDate = formatter.parse(start);
                    endDate = formatter.parse(end);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Error when parsing dates read from XML");
                    System.err.println("Start date: " + start);
                    System.err.println("End date: " + end);
                }

                // Create todo item
                DayEvent event = new DayEvent(name, startDate, endDate);
                event.setCategory(new Category(category));
                event.setPriority(Priority.valueOf(priority));

                // Make a SimpleDateFormat for toString()'s output
                eventList.add(event);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return eventList;
    }

    /**
     * Saves the given ToDoItems to the xml file using JDom.
     * @param todoList The items to store.
     */
    public static void saveDayEvents(ArrayList<DayEvent> todoList) {
        org.jdom.Element root = new org.jdom.Element("calendar");
        DocType type = new DocType("calendar", "calendar.dtd");
        org.jdom.Document doc = new org.jdom.Document(root, type);

        for (int i = 0; i < todoList.size(); i++) {
            DayEvent event = todoList.get(i);
            org.jdom.Element item = new org.jdom.Element("dayEven");
            item.addContent(new org.jdom.Element("name").setText(event.getEventName()));
            item.addContent(new org.jdom.Element("startDate").setText(formatter.format(event.getEndTime())));
            item.addContent(new org.jdom.Element("endDate").setText(formatter.format(event.getEndTime())));
            item.addContent(new org.jdom.Element("category").setText(event.getCategory().getName()));
            item.addContent(new org.jdom.Element("priority").setText(event.getPriority().name()));
            root.addContent(item);
        }

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        try {
            FileWriter writer = new FileWriter(Configuration.getProperty("calendar"));
            outputter.output(doc, writer);
        } catch (IOException e) {
            // Failed to save file, should we just quit?
        }
    }

    public static void main(String[] args) {
        ArrayList<DayEvent> events = XMLUtils.loadDayEvents(Configuration.getProperty("calendar"));
        System.out.println(events.get(0));
    }
}
