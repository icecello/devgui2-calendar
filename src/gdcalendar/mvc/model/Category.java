package gdcalendar.mvc.model;

import java.awt.Color;

/**
 * Category, connected to DayEvents. A category consists of a name
 * , (a description) and a color.
 * @author Tomas
 */
public class Category {
    private String name;
    private String description;
    private Color categoryColor;

    public Category(String name){
        this.name = name;
    }
    public Category(String name, String description){
        this(name);
        this.description = description;
    }
    public Category(String name, String description, Color categoryColor){
        this(name,description);
        this.categoryColor = categoryColor;
    }


    public Color getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(Color categoryColor) {
        this.categoryColor = categoryColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

}
