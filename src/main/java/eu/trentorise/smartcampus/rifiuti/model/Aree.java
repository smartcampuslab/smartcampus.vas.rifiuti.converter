package eu.trentorise.smartcampus.rifiuti.model;


public class Aree {

	private String istituzione;
	
	private String nome;
	private String parent;
	private String gestore;
	private String descrizione;
	private String comune;
	private String localita;
	private String via;
	private String numero;
	
	private String utenzaDomestica;
	private String utenzaNonDomestica;
	private String utenzaOccasionale;
	
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

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getUtenzaDomestica() {
		return utenzaDomestica;
	}

	public void setUtenzaDomestica(String utenzaDomestica) {
		this.utenzaDomestica = utenzaDomestica;
	}

	public String getUtenzaNonDomestica() {
		return utenzaNonDomestica;
	}

	public void setUtenzaNonDomestica(String utenzaNonDomestica) {
		this.utenzaNonDomestica = utenzaNonDomestica;
	}

	public String getUtenzaOccasionale() {
		return utenzaOccasionale;
	}

	public void setUtenzaOccasionale(String utenzaOccasionale) {
		this.utenzaOccasionale = utenzaOccasionale;
	}
	
}
