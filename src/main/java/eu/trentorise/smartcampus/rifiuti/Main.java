package eu.trentorise.smartcampus.rifiuti;

import eu.trentorise.smartcampus.rifiuti.model.ProfiloUtente;
import eu.trentorise.smartcampus.rifiuti.model.Rifiuti;

public class Main {

	public static void main(String[] args) throws Exception {

		Initializer initializer = new Initializer();

		Rifiuti rifiuti = initializer.readExcel("ExcelModelloConcettuale_V0.26.xls");
		initializer.writeCSV(rifiuti);
		initializer.convertFromCSVToDB();
		
		ProfiloUtente pu = new ProfiloUtente();
		pu.setUtenza("utenza domestica");
		SQLQueries q = new SQLQueries();
		pu.setAree(q.getAree("Tione di Trento"));
		new SQLQueries().exec("SELECT DISTINCT puntiRaccolta.area, puntiRaccolta.tipologiaPuntiRaccolta,puntiRaccolta.tipologiaUtenza,puntiRaccolta.localizzazione,puntiRaccolta.indirizzo,puntiRaccolta.dettaglioIndirizzo, puntiRaccolta.gettoniera, puntiRaccolta.residuo, puntiRaccolta.imbCarta, puntiRaccolta.imbPlMet, puntiRaccolta.organico, puntiRaccolta.imbVetro, puntiRaccolta.indumenti, note FROM puntiRaccolta 	INNER JOIN raccolta ON puntiRaccolta.tipologiaPuntiRaccolta = raccolta.tipologiaPuntoRaccolta AND raccolta.tipologiaUtenza = puntiRaccolta.tipologiaUtenza  WHERE puntiRaccolta.area IN ('Madonna di Campiglio','Area Giudicarie') AND raccolta.area IN ('Madonna di Campiglio','Area Giudicarie') AND puntiRaccolta.indirizzo IN ('Madonna di Campiglio') AND puntiRaccolta.tipologiaUtenza = 'utenza domestica' AND raccolta.tipologiaRaccolta = 'Organico' AND ((raccolta.tipologiaRaccolta != 'Residuo') OR (puntiRaccolta.residuo = 'True' AND puntiRaccolta.tipologiaUtenza='utenza domestica') OR (puntiRaccolta.gettoniera = 'True' AND puntiRaccolta.tipologiaUtenza='utenza occasionale') OR (puntiRaccolta.residuo = '')) AND ((raccolta.tipologiaRaccolta != 'Carta, cartone e cartoni per bevande') OR (puntiRaccolta.imbCarta = 'True') OR (puntiRaccolta.imbCarta = '')) AND ((raccolta.tipologiaRaccolta != 'Imballaggi in plastica e metallo') OR (puntiRaccolta.imbPlMet = 'True') OR (puntiRaccolta.imbPlMet = '')) AND ((raccolta.tipologiaRaccolta != 'Imballaggi in vetro') OR (puntiRaccolta.imbVetro = 'True') OR (puntiRaccolta.imbVetro = '')) AND ((raccolta.tipologiaRaccolta != 'Indumenti usati') OR (puntiRaccolta.indumenti = 'True') OR (puntiRaccolta.indumenti = '')) AND ((raccolta.tipologiaRaccolta != 'Organico') OR (puntiRaccolta.organico = 'True') OR (puntiRaccolta.organico = ''))");
//		List<PuntiRaccolta> list = new SQLQueries().getPuntiDiRaccoltaPerTipoPuntoRaccolta("Isola Ecologica", pu);
//		System.err.println(list);
	}

}
