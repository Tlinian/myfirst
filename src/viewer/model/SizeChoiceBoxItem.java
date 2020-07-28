package viewer.model;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by PanD
 */

public class SizeChoiceBoxItem {

    private String name;

    private Double width;

    private Double height;

    public SizeChoiceBoxItem(String name, Double width, Double height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }
}
