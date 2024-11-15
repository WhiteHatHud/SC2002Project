package Utilities;

import java.util.List;

public interface CSVHandler {
    void updateField(String key, String fieldName, String newValue);
    void addNewLine(String[] newData);
}

