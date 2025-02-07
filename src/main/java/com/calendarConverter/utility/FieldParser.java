package com.calendarConverter.utility;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FieldParser {
    @Autowired
    private Utility utility;

    public TextFormatter<String> createDayFormatter(Calendar calendar, TextField monthField, TextField yearField) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty() || newText.matches("\\d{0,2}")) {
                if (newText.isEmpty()){
                    return change;
                }

                int[] date = parseDateFields(change.getText(), monthField, yearField);
                date[0] = parseIntOrZero(newText);

                return utility.validateDate(date, calendar) ? change : null;
            } else {
                return null;
            }
        });
    }

    public TextFormatter<String> createMonthFormatter(Calendar calendar, TextField dayField, TextField yearField) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d{0,2}")) {
                if (newText.isEmpty()){
                    return change;
                }
                int[] date = parseDateFields(dayField, change.getText(), yearField);
                date[1] = parseIntOrZero(newText);
                return utility.validateDate(date, calendar) ? change : null;
            } else {
                return null;
            }
        });
    }

    public TextFormatter<String> createYearFormatter(Calendar calendar, TextField dayField, TextField monthField) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d{0,4}")) {
                if (newText.isEmpty()){
                    return change;
                }
                int[] date = parseDateFields(dayField, monthField, change.getText());
                date[2] = parseIntOrZero(newText);
                if(date[2]< 10) return null;
                return utility.validateDate(date, calendar) ? change : null;
            } else {
                return null;
            }
        });
    }

    public TextFormatter<String> createDayInputFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("\\d{0,6}")) {
                return change;
            } else {
                return null;
            }
        });
    }

    public int[] parseDateFields(TextField dayField, TextField monthField, String year) {
        return new int[]{
                parseIntOrZero(dayField.getText()),
                parseIntOrZero(monthField.getText()),
                parseIntOrZero(year)
        };
    }

    public int[] parseDateFields(String day, TextField monthField, TextField yearField) {
        return new int[]{
                parseIntOrZero(day),
                parseIntOrZero(monthField.getText()),
                parseIntOrZero(yearField.getText())
        };
    }

    public int[] parseDateFields(TextField dayField, String month, TextField yearField) {
        return new int[]{
                parseIntOrZero(dayField.getText()),
                parseIntOrZero(month),
                parseIntOrZero(yearField.getText())
        };
    }
    public int parseIntOrZero(String text) {
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }
}
