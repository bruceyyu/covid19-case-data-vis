package comp3111.covid.ui;

import javafx.collections.FXCollections;
import org.controlsfx.control.CheckListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom CheckListViewClass, it maintains a map for checking if the items have been checked
 * @param <String>
 */
public class CheckListViewWithList<String> extends CheckListView<String> {
    private Map<String, Boolean> countryNamesMap;

    /**
     * constructor
     */
    public CheckListViewWithList() {
        super();
        countryNamesMap = new HashMap<>();
    }

    /**
     * Update the CheckListView, remembering the old state as well as displaying a new list
     * @param newCountryNames a list of new country names
     */
    public void update(List<String> newCountryNames) {
        if (countryNamesMap.size() == 0) { // initialization case
            for (String name: newCountryNames
                 ) {
                countryNamesMap.put(name, false);
            }
            this.setItems(FXCollections.observableArrayList(newCountryNames));
            return;
        }

        for (String countryName: getItems()
             ) { // save checked state of old items
            if (getItemBooleanProperty(countryName).get()) {
                countryNamesMap.put(countryName, true);
            } else {
                countryNamesMap.put(countryName, false);
            }
        }
        // set new items
        setItems(FXCollections.observableArrayList(newCountryNames));
        for (String countryName: getItems()
        ) { // set property of new items
           getItemBooleanProperty(countryName).set(countryNamesMap.get(countryName));
        }
    }

    /**
     * save the current state of the list
     */
    public void saveState() {
        for (String countryName: getItems()
        ) { // save checked state of old items
            if (getItemBooleanProperty(countryName).get()) {
                countryNamesMap.put(countryName, true);
            } else {
                countryNamesMap.put(countryName, false);
            }
        }
    }

    /**
     * Clear the map.
     */
    public void clear() {
        countryNamesMap.clear();
    }

    /**
     * return all checked items
     * @return list of checked items
     */
    public List<String> getCheckedItems() {
        ArrayList<String> result = new ArrayList<>();
        for (String key: countryNamesMap.keySet()
             ) {
            if (countryNamesMap.get(key))
                result.add(key);
        }
        return result.stream().sorted().collect(Collectors.toList());
    }
}
