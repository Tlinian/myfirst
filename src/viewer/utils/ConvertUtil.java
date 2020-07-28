package viewer.utils;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import viewer.model.ImagePreViewItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PanD
 * 进行类型转换的工具类
 */

public class ConvertUtil {

    /**
     * description: 将propertySet转为List，提取其中的File
     * @param selectedImagePreViewList
     * @return java.util.List<java.io.File>
     */
    public static List<File> simpleArrayListPropertyToList(SimpleListProperty<ImagePreViewItem> selectedImagePreViewList) {
        List<File> fileList = new ArrayList<>();
        selectedImagePreViewList.forEach(imagePreViewItem -> {
            fileList.add(imagePreViewItem.getImageFile());
        });
        return fileList;
    }
}
