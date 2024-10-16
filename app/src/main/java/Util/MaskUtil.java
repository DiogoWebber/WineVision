package Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class MaskUtil {

    public static void applyDateMask(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final String ddmmyyyy = "DDMMYYYY";
            private final Calendar cal = Calendar.getInstance();
            private boolean isUpdating = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return; // Evita chamadas recursivas

                String clean = s.toString().replaceAll("[^\\d]", "");

                // Limpa todo o campo se o texto estiver vazio
                if (clean.length() == 0) {
                    isUpdating = true;
                    editText.setText("");
                    current = "";
                    isUpdating = false;
                    return;
                }

                // Se o comprimento for menor que 8, preenche com a máscara
                if (clean.length() < 8) {
                    clean += ddmmyyyy.substring(clean.length());
                } else {
                    // Verifica a entrada de dia, mês e ano
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

                // Adiciona as barras da máscara
                String formatted = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                // Atualiza o campo com o texto formatado
                isUpdating = true; // Marca que estamos atualizando
                editText.setText(formatted);
                editText.setSelection(formatted.length()); // Mantenha o cursor no final
                current = formatted; // Atualiza o valor atual
                isUpdating = false; // Reseta a flag
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
