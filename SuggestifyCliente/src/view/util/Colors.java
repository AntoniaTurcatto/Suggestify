package view.util;

public class Colors {
     public String rgbToHex(int r, int g, int b) {
        // Converte os valores RGB para uma string hexadecimal
        return String.format("#%02X%02X%02X", r, g, b);
    }
}
