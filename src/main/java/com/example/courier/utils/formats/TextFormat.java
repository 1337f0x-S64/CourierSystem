package com.example.courier.utils.formats;

import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

public class TextFormat {
    public static UnaryOperator<TextFormatter.Change> textLength(long maxLength) {
        return change -> change.getControlNewText().length() <= maxLength ? change : null;
    }

}