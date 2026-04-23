package com.example.courier.utils.formats;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class FormatDate {
    public static void formatCalendar(DatePicker datePicker) {
        TextField editor = datePicker.getEditor();
        editor.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String c = event.getCharacter();
            if (!c.equals("\b") && !c.equals("\u007F")) {
                event.consume();
            }
        });
        editor.setEditable(false);
    }
}
