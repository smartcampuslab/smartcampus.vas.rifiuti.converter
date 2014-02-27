package eu.trentorise.smartcampus.rifiuti.model;


public class Aree {

	private String istituzione;
	
	private String nome;
	private String parent;
	private String gestore;
	private String descrizione;
	
	public String getIstituzione() {
		return istituzione;
	}

	public void setIstituzione(String istituzione) {
		this.istituzione = istituzione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getGestore() {
		return gestore;
	}

	public void setGestore(String gestore) {
		this.gestore = gestore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
