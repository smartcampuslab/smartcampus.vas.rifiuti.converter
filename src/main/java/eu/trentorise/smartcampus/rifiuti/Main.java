package eu.trentorise.smartcampus.rifiuti;

import eu.trentorise.smartcampus.rifiuti.model.Rifiuti;

public class Main {

	public static void main(String[] args) throws Exception {

		Initializer initializer = new Initializer();

		Rifiuti rifiuti = initializer.readExcel("ExcelModelloConcettuale_V0.8.xls");
		initializer.writeCSV(rifiuti);
		initializer.convertFromCSVToDB();
	}

}
