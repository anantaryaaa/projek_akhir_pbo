package com.example.dompetmahasiswa;

import java.text.DecimalFormat;

public class FormatUangRupiah {
    public static String formatRupiah(double uang) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return "Rp " + decimalFormat.format(uang);
    }
}
