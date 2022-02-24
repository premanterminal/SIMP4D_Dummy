package com.dispenda.report;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class ReportModule {
	public String GetNPWPDFormatByDot(String npwpd)
	{
		String retValue = npwpd.substring(0, 1) + "." + npwpd.substring(1, 8) + "." + npwpd.substring(8, 10) + 
				"." + npwpd.substring(10, 12);
		return retValue;
	}
	
	public String getTerbilang(Double value){
		DecimalFormat df = new DecimalFormat("#.##");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_DOWN);
		
		value = Double.parseDouble(df.format(value).replace(',', '.'));
		String description = "-x: Minus >>;\n"
				+ "x.x: << Koma >>;\n"
				+ "Nol; Satu; Dua; Tiga; Empat; Lima; Enam; Tujuh; Delapan; Sembilan;"
				+ "Sepuluh; Sebelas; Dua Belas; Tiga Belas; Empat Belas; Lima Belas; Enam Belas; Tujuh Belas; Delapan Belas; Sembilan Belas;\n"
				+ "20: Dua Puluh[ >>];"
				+ "30: Tiga Puluh[ >>];"
				+ "40: Empat Puluh[ >>];"
				+ "50: Lima Puluh[ >>];"
				+ "60: Enam Puluh[ >>];"
				+ "70: Tujuh Puluh[ >>];"
				+ "80: Delapan Puluh[ >>];"
				+ "90: Sembilan Puluh[ >>];"
				+ "100: Seratus[ >>];"
				+ "200: Dua Ratus[ >>];"
				+ "300: Tiga Ratus[ >>];"
				+ "400: Empat Ratus[ >>];"
				+ "500: Lima Ratus[ >>];"
				+ "600: Enam Ratus[ >>];"
				+ "700: Tujuh Ratus[ >>];"
				+ "800: Delapan Ratus[ >>];"
				+ "900: Sembilan Ratus[ >>];"
				+ "1,000: Seribu[ >>];"
				+ "2,000: << Ribu[ >>];"
				+ "1,000,000: << Juta[ >>];"
				+ "1,000,000,000: << Milyar [>>];\n"
				+ "1,000,000,000,000: =#,##0=;\n";
		RuleBasedNumberFormat ruleNumberFormat = new RuleBasedNumberFormat(description);
		String text = ruleNumberFormat.format(value)+" Rupiah.";
		return text;
	}
}
