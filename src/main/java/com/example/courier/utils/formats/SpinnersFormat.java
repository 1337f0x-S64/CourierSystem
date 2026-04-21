package com.example.courier.utils.formats;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class SpinnersFormat {
    public static SpinnerValueFactory<Integer> FormatTime (int start, int end, int initialValue) {
        SpinnerValueFactory<Integer> formatTime = new SpinnerValueFactory.IntegerSpinnerValueFactory(start, end, initialValue);

        formatTime.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return String.format("%02d", integer);
            }

            @Override
            public Integer fromString(String s) {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });
        return formatTime;
    }
}
