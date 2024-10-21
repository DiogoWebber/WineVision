package Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class MaskUtil {

    // Method to apply a date mask (DD/MM/YYYY)
    public static void applyDateMask(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final String ddmmyyyy = "DDMMYYYY";
            private final Calendar cal = Calendar.getInstance();
            private boolean isUpdating = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return; // Prevent recursive calls

                String clean = s.toString().replaceAll("[^\\d]", "");

                // Clear the field if the text is empty
                if (clean.length() == 0) {
                    isUpdating = true;
                    editText.setText("");
                    current = "";
                    isUpdating = false;
                    return;
                }

                // If length is less than 8, fill with the mask
                if (clean.length() < 8) {
                    clean += ddmmyyyy.substring(clean.length());
                } else {
                    // Validate day, month, and year
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int month = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    month = Math.min(12, month);
                    cal.set(Calendar.MONTH, month - 1);

                    year = Math.max(1900, Math.min(2100, year));
                    cal.set(Calendar.YEAR, year);

                    day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                    clean = String.format("%02d%02d%02d", day, month, year);
                }

                // Add the slashes of the mask
                String formatted = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                // Update the field with the formatted text
                isUpdating = true; // Mark that we are updating
                editText.setText(formatted);
                editText.setSelection(formatted.length()); // Keep cursor at the end
                current = formatted; // Update current value
                isUpdating = false; // Reset the flag
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // New method to apply CEP mask (#####-###)
    public static void applyCepMask(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private boolean isUpdating = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return; // Prevent recursive calls

                String clean = s.toString().replaceAll("[^\\d]", "");

                // Clear the field if the text is empty
                if (clean.length() == 0) {
                    isUpdating = true;
                    editText.setText("");
                    current = "";
                    isUpdating = false;
                    return;
                }

                // Format the string based on the CEP mask
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < clean.length(); i++) {
                    if (i == 5) {
                        formatted.append('-'); // Add dash after the fifth character
                    }
                    formatted.append(clean.charAt(i));
                }

                // Update the field with the formatted text
                isUpdating = true; // Mark that we are updating
                editText.setText(formatted.toString());
                editText.setSelection(formatted.length()); // Keep cursor at the end
                current = formatted.toString(); // Update current value
                isUpdating = false; // Reset the flag
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
