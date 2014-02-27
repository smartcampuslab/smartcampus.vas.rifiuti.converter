package eu.trentorise.smartcampus.rifiuti.model;

import java.util.List;

public class Rifiuti {

	private List<Aree>aree;
	private List<Gestori>gestori;
	private List<Istituzioni>istituzioni;
	private List<Profili>profili;
	private List<PuntiRaccolta>puntiRaccolta;
	private List<TipologiaRifiuto> TipologiaRifiuto;
	private List<Riciclabolario>riciclabolario;
	private List<Raccolta>raccolta;
	private List<TipologiaPuntiRaccolta>tipologiaPuntiRaccolta;
	private List<TipologiaRaccolta>tipologiaRaccolta;
	
//	private Map<String, List<String>> singleColumns;

	public List<Aree>getAree() {
		return aree;
	}

	public void setAree(List<Aree>aree) {
		this.aree = aree;
	}

	public List<Gestori>getGestori() {
		return gestori;
	}

	public void setGestori(List<Gestori>gestori) {
		this.gestori = gestori;
	}

	public List<Istituzioni>getIstituzioni() {
		return istituzioni;
	}

	public void setIstituzioni(List<Istituzioni>istituzioni) {
		this.istituzioni = istituzioni;
	}

	public List<Profili>getProfili() {
		return profili;
	}

	public void setProfili(List<Profili>profili) {
		this.profili = profili;
	}

	public List<PuntiRaccolta>getPuntiRaccolta() {
		return puntiRaccolta;
	}

	public void setPuntiRaccolta(List<PuntiRaccolta>puntiRaccolta) {
		this.puntiRaccolta = puntiRaccolta;
	}

	public List<TipologiaRifiuto> getTipologiaRifiuto() {
		return TipologiaRifiuto;
	}

	public void setTipologiaRifiuto(List<TipologiaRifiuto> tipologiaRifiuto) {
		TipologiaRifiuto = tipologiaRifiuto;
	}

	public List<Riciclabolario>getRiciclabolario() {
		return riciclabolario;
	}

	public void setRiciclabolario(List<Riciclabolario>riciclabolario) {
		this.riciclabolario = riciclabolario;
	}

	public List<Raccolta>getRaccolta() {
		return raccolta;
	}

	public void setRaccolta(List<Raccolta>raccolta) {
		this.raccolta = raccolta;
	}

	public List<TipologiaPuntiRaccolta> getTipologiaPuntiRaccolta() {
		return tipologiaPuntiRaccolta;
	}

	public void setTipologiaPuntiRaccolta(List<TipologiaPuntiRaccolta> tipologiaPuntiRaccolta) {
		this.tipologiaPuntiRaccolta = tipologiaPuntiRaccolta;
	}

	public List<TipologiaRaccolta>getTipologiaRaccolta() {
		return tipologiaRaccolta;
	}

	public void setTipologiaRaccolta(List<TipologiaRaccolta>tipologiaRaccolta) {
		this.tipologiaRaccolta = tipologiaRaccolta;
	}

//	public Map<String, List<String>> getSingleColumns() {
//		return singleColumns;
//	}
//
//	public void setSingleColumns(Map<String, List<String>> singleColumns) {
//		this.singleColumns = singleColumns;
//	}

}
