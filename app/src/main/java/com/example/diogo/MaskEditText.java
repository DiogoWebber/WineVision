package com.example.diogo;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskEditText {

    public static String unmask(String s) {
        return s.replaceAll("[^\\d]", "");
    }

    public static TextWatcher insert(final String mask, final EditText editText) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = unmask(s.toString());
                String masked = "";

                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }

                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        masked += m;
                        continue;
                    }
                    try {
                        masked += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }

                isUpdating = true;
                editText.setText(masked);
                editText.setSelection(masked.length());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }
}