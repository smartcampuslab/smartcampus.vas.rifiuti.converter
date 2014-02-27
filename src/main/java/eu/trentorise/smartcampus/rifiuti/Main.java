package eu.trentorise.smartcampus.rifiuti;

import eu.trentorise.smartcampus.rifiuti.model.Rifiuti;

public class Main {

	public static void main(String[] args) throws Exception {

		Initializer initializer = new Initializer();

		// excel file to read
		Rifiuti rifiuti = initializer.readExcel("");
		initializer.writeCSV(rifiuti);
		initializer.convertFromCSVToDB();
	}

}
