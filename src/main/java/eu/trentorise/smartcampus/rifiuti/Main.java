package eu.trentorise.smartcampus.rifiuti;

import java.util.List;

import eu.trentorise.smartcampus.rifiuti.model.ProfiloUtente;
import eu.trentorise.smartcampus.rifiuti.model.PuntiRaccolta;
import eu.trentorise.smartcampus.rifiuti.model.Rifiuti;

public class Main {

	public static void main(String[] args) throws Exception {

		Initializer initializer = new Initializer();

		Rifiuti rifiuti = initializer.readExcel("ExcelModelloConcettuale_V0.8.xls");
		initializer.writeCSV(rifiuti);
		initializer.convertFromCSVToDB();
		
		ProfiloUtente pu = new ProfiloUtente();
		pu.setUtenza("utenza domestica");
		SQLQueries q = new SQLQueries();
		pu.setAree(q.getAree("Tione di Trento"));
		List<PuntiRaccolta> list = new SQLQueries().getPuntiDiRaccoltaPerTipoPuntoRaccolta("Isola Ecologica", pu);
		System.err.println(list);
	}

}
