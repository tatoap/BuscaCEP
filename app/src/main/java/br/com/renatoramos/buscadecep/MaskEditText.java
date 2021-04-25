package br.com.renatoramos.buscadecep;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MaskEditText {

    public static final String FORMAT_CEP = "#####-###";

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param editText
     * @param mask
     * @return
     */

    public static TextWatcher mask(final EditText editText, final String mask){
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = MaskEditText.unmask(s.toString());
                String mascara = "";

                if (isUpdating){
                    old  = str;
                    isUpdating = false;
                    return;
                }

                int i = 0;

                for (final char m : mask.toCharArray()){

                    if (m != '#' && str.length() > old.length()){
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e){
                        break;
                    }

                    i++;
                }

                if (mascara.length() > 0){
                    char last_char = mascara.charAt(mascara.length() - 1);
                    boolean hadSign = false;

                    while (isASign(last_char) && str.length() == old.length()){
                        mascara = mascara.substring(0, mascara.length() - 1);
                        last_char = mascara.charAt(mascara.length() - 1);
                        hadSign = true;
                    }

                    if (mascara.length() > 0 && hadSign){
                        mascara = mascara.substring(0, mascara.length() - 1);
                    }
                }

                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        };
    }

    public static String unmask(final String s){
        return s.replaceAll("[.]","").replaceAll("[-]","")
                .replaceAll("[/]","").replaceAll("[(]", "")
                .replaceAll("[:]", "").replaceAll("[)]","");
    }

    public static boolean isASign(char c){
        if (c == '.' || c == '-' || c == '/' || c == '(' || c == ':' || c == ')' ){
            return true;
        } else {
            return false;
        }
    }
}
