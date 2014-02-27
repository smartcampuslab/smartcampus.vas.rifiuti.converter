package eu.trentorise.smartcampus.rifiuti.model;

import java.util.List;

public class ProfiloUtente {

	private String utenza;
	private String comune;
	private String indirizzo;
	private List<Aree> aree;
	
	public String getUtenza() {
		return utenza;
	}
	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public List<Aree> getAree() {
		return aree;
	}
	public void setAree(List<Aree> aree) {
		this.aree = aree;
	}


	
	
}
