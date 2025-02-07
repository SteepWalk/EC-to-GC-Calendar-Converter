package com.calendarConverter.controller;

import com.calendarConverter.entity.Date;
import com.calendarConverter.exception.InvalidDate;
import com.calendarConverter.service.ConvertDate;
import com.calendarConverter.utility.*;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class AppFXML {

    @FXML
    private TextField DDEC, MMEC, YYEC, DDGC, MMGC, YYGC, dayInput;

    @FXML
    private TextField DDEC1, MMEC1, YYEC1, DDGC1, MMGC1, YYGC1;

    @FXML
    private Label dayOfWeek, invalidFieldInput;

    @FXML
    private Button performBtn;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton addOption, subOption;

    @Autowired
    private Utility utility;

    @Autowired
    private ECOperations ECOperations;

    @Autowired
    private GCOperations GCOperations;

    @Autowired
    private FieldParser tf;

    @Autowired
    private Date date;

    @Autowired
    private ConvertDate convertDate;

    private boolean affect = true;
    private SequentialTransition shakeTransition;
    private Timeline timeline;
    private PauseTransition pauseTransition;

    private int[] animate = {0,0,0,0,0,0};

    @FXML
    public void initialize() {
        //System.out.println("Controller initialized");
        setUpTextFormatters();
        setUpFocusedProperty();
        setUpListeners();
        initDate();
        performBtn.getStyleClass().add("btn");
        performBtn.setOnAction(e -> {
            addDate();
            dayInput.clear();
        });
    }

    private void setUpTextFormatters() {
        DDEC.setTextFormatter(createDayFormatter(Calendar.EC, MMEC, YYEC));
        DDGC.setTextFormatter(createDayFormatter(Calendar.GC, MMGC, YYGC));
        MMEC.setTextFormatter(createMonthFormatter(Calendar.EC, DDEC, YYEC));
        MMGC.setTextFormatter(createMonthFormatter(Calendar.GC, DDGC, YYGC));
        YYEC.setTextFormatter(createYearFormatter(Calendar.EC, DDEC, MMEC));
        YYGC.setTextFormatter(createYearFormatter(Calendar.GC, DDGC, MMGC));
        dayInput.setTextFormatter(createDayInputFormatter());
    }

    private void setUpListeners() {
        setUpFieldListener(DDGC, Calendar.GC);
        setUpFieldListener(MMGC, Calendar.GC);
        setUpFieldListener(YYGC, Calendar.GC);
        setUpFieldListener(DDEC, Calendar.EC);
        setUpFieldListener(MMEC, Calendar.EC);
        setUpFieldListener(YYEC, Calendar.EC);
    }

    private void setUpFieldListener(TextField field, Calendar calendar) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!affect) return;
            affect = false;

            int[] dateArr;
            if (calendar.equals(Calendar.EC)) {
                dateArr = new int[] {
                        tf.parseIntOrZero(DDEC.getText()),
                        tf.parseIntOrZero(MMEC.getText()),
                        tf.parseIntOrZero(YYEC.getText())
                };
                date.setDate(dateArr[0], dateArr[1], dateArr[2], calendar);
            } else {
                dateArr = new int[] {
                        tf.parseIntOrZero(DDGC.getText()),
                        tf.parseIntOrZero(MMGC.getText()),
                        tf.parseIntOrZero(YYGC.getText())
                };
                date.setDate(dateArr[0], dateArr[1], dateArr[2], calendar);
            }

            try {
                convertDate.convert(date.getDate(), calendar);
                setText(date.getDate(), calendar != Calendar.GC);
            } catch (InvalidDate e) {
                System.out.println(e.getMessage());
            }

            affect = true;
        });
    }

    void setUpFocusedProperty() {
        setFocused(DDEC, DDEC1);
        setFocused(MMEC, MMEC1);
        setFocused(YYEC, YYEC1);
        setFocused(DDGC, DDGC1);
        setFocused(MMGC, MMGC1);
        setFocused(YYGC, YYGC1);
    }
    public void setText(int[] newDate, boolean isGCDate) {
        if (isGCDate) {
            DDGC.setText(String.valueOf(newDate[0]));
            balancedRotateShakeTextField(DDGC,0);
            MMGC.setText(String.valueOf(newDate[1]));
            balancedRotateShakeTextField(MMGC,1);
            YYGC.setText(String.valueOf(newDate[2]));
            balancedRotateShakeTextField(YYGC,2);
            fancyLabelSetting(dayOfWeek, utility.setDayOfWeek(), Duration.millis(100));
            //dayOfWeek.setText(utility.setDayOfWeek());
        } else {
            DDEC.setText(String.valueOf(newDate[0]));
            balancedRotateShakeTextField(DDEC,0);
            MMEC.setText(String.valueOf(newDate[1]));
            balancedRotateShakeTextField(MMEC,1);
            YYEC.setText(String.valueOf(newDate[2]));
            balancedRotateShakeTextField(YYEC,2);
            fancyLabelSetting(dayOfWeek, utility.setDayOfWeek(), Duration.millis(100));
            //dayOfWeek.setText(utility.setDayOfWeek());
        }
    }

    public void addDate() {
        String check = dayInput.getText();
        if(check.isEmpty()) return;
        int dd = tf.parseIntOrZero(DDEC.getText());
        int mm = tf.parseIntOrZero(MMEC.getText());
        int yy = tf.parseIntOrZero(YYEC.getText());

        int toBeAdded = tf.parseIntOrZero(dayInput.getText());
        int[] localDate = {dd, mm, yy};

        localDate[0] = (addOption.isSelected()) ? dd+toBeAdded : dd-toBeAdded;
        ECOperations.formatter(localDate);
        DDEC.setText(String.valueOf(localDate[0]));
        MMEC.setText(String.valueOf(localDate[1]));
        YYEC.setText(String.valueOf(localDate[2]));

        balancedRotateShakeTextField(DDEC,3);
        balancedRotateShakeTextField(MMEC, 4);
        balancedRotateShakeTextField(YYEC, 5);
        //System.out.println("This is what I get After addition " + localDate[0] + " " + localDate[1] + " " + localDate[2]);

    }

    public void balancedRotateShakeTextField(TextField textField, int i) {
        // Create the 'rotate' transitions
        textField.setRotate(0);

        RotateTransition rotateClockwise = new RotateTransition(Duration.millis(200), textField);
        RotateTransition rotateBack = new RotateTransition(Duration.millis(200), textField);
        RotateTransition rotateCounterclockwise = new RotateTransition(Duration.millis(200), textField);

        rotateClockwise.setByAngle(10);
        rotateBack.setByAngle(10);
        rotateCounterclockwise.setByAngle(-20);


        // Stop previous animations if there are any
        if (shakeTransition != null & animate[i] == 1) {
            animate[i] = 0;
            shakeTransition.stop();
            //System.out.println("stopped");
        }
        animate[i] = 1;
        shakeTransition = new SequentialTransition(
                rotateClockwise, rotateCounterclockwise, rotateBack

        );
        //System.out.println("sequence endth.");
        // Play the animation
        shakeTransition.setOnFinished(e -> {
            animate[i] = 0;
            });
        shakeTransition.play();
    }

    public void fancyLabelSetting(Label label, String newWord, Duration delayPerLetter) {
        label.setText("");

        if (timeline != null) {
            timeline.stop();
            label.setText("");
        }
        timeline = new Timeline();

        for (int i=0; i<newWord.length(); i++) {
            final int index = i;

            KeyFrame keyFrame = new KeyFrame(
                    delayPerLetter.multiply(index+1),
                    event -> {
                        label.setText(label.getText()+newWord.charAt(index));
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }
    public void initDate() {
        java.util.Date currentDate = new java.util.Date();
        java.util.Calendar dateCalendar = java.util.Calendar.getInstance();
        dateCalendar.setTime(currentDate);
        int day = dateCalendar.get(java.util.Calendar.DAY_OF_MONTH);
        int month = dateCalendar.get(java.util.Calendar.MONTH);
        int year = dateCalendar.get(java.util.Calendar.YEAR);
        System.out.println("What the hell does it think the day is? " + day);
        DDGC.setText(String.valueOf(day));
        balancedRotateShakeTextField(DDGC,0);
        MMGC.setText(String.valueOf(month+1));
        balancedRotateShakeTextField(MMGC,1);
        YYGC.setText(String.valueOf(year));
        balancedRotateShakeTextField(YYGC,2);
        fancyLabelSetting(dayOfWeek, utility.setDayOfWeek(), Duration.millis(100));

    }

    public void setFocused(TextField textField, TextField underLine) {
        underLine.setStyle(
                "-fx-border-color: transparent transparent transparent transparent;" +
                "-fx-border-width: 0 0 0 0; -fx-background-color: transparent;"
        );
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal) {
                    underLine.setStyle(
                            "-fx-border-color: #333 transparent transparent transparent;" +
                                    "-fx-border-width: 3px 0 0 0; -fx-background-color: transparent;"
                    );

                } else {
                    underLine.setStyle(
                            "-fx-border-color: transparent transparent transparent transparent;" +
                                    "-fx-border-width: 0 0 0 0; -fx-background-color: transparent;"
                    );
                }
        }
        );
    }

    public void invalidFieldInput(TextField fieldEC, TextField fieldGC, Calendar calendar) {
        invalidFieldInput.getStyleClass().add("error-input");
        if (calendar.equals(Calendar.EC)) {
            fieldEC.getStyleClass().add("un-hide");
        } else {
            fieldGC.getStyleClass().add("un-hide");
        }

        pauseTransition = new PauseTransition(Duration.millis(500));

        pauseTransition.setOnFinished(e-> {
            invalidFieldInput.getStyleClass().remove("error-input");
            if (calendar.equals(Calendar.EC)) {
                fieldEC.getStyleClass().remove("un-hide");
            } else {
                fieldGC.getStyleClass().remove("un-hide");
            }
            pauseTransition = null;
        });

        pauseTransition.play();

    }



    public TextFormatter<String> createDayFormatter(Calendar calendar, TextField monthField, TextField yearField) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty() || newText.matches("\\d{0,2}")) {
                if (newText.isEmpty()){
                    return change;
                }

                int[] date = tf.parseDateFields(change.getText(), monthField, yearField);
                date[0] = tf.parseIntOrZero(newText);

                if(utility.validateDate(date, calendar)) {
                    return change;
                } else {
                    invalidFieldInput(DDEC, DDGC, calendar);
                    return null;
                }

            } else {
                System.out.println("number of calls");
                if(pauseTransition == null) invalidFieldInput(DDEC, DDGC, calendar);
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
                int[] date = tf.parseDateFields(dayField, change.getText(), yearField);
                date[1] = tf.parseIntOrZero(newText);
                if(utility.validateDate(date, calendar)) {
                    return change;
                } else {
                    invalidFieldInput(MMEC, MMGC, calendar);
                    return null;
                }
            } else {
                invalidFieldInput(MMEC, MMGC, calendar);
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
                int[] date = tf.parseDateFields(dayField, monthField, change.getText());
                date[2] = tf.parseIntOrZero(newText);
                if(date[2]< 10) {
                    invalidFieldInput(YYEC, YYGC, calendar);
                    return null;
                }
                if(utility.validateDate(date, calendar)) {
                    return change;
                } else {
                    invalidFieldInput(YYEC, YYGC, calendar);
                    return null;
                }
            } else {
                invalidFieldInput(YYEC, YYGC, calendar);
                return null;
            }
        });
    }

    public TextFormatter<String> createDayInputFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty() || newText.matches("\\d{0,6}")) {
                if(!newText.isEmpty()) {
                    performBtn.setDisable(false);
                }else {
                    performBtn.setDisable(true);
                }
                return change;
            } else {
                invalidFieldInput(dayInput, null, Calendar.EC);
                return null;
            }
        });
    }



}

