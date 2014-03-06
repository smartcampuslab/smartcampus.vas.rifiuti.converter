package eu.trentorise.smartcampus.rifiuti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;

import eu.trentorise.smartcampus.rifiuti.model.Aree;
import eu.trentorise.smartcampus.rifiuti.model.ProfiloUtente;
import eu.trentorise.smartcampus.rifiuti.model.PuntiRaccolta;

public class SQLQueries {

	private Connection connection;
	private BeanProcessor bp;

	public SQLQueries() throws Exception {
		Class.forName("org.sqlite.JDBC");
		// change db location
		connection = DriverManager.getConnection("jdbc:sqlite:./src/main/resources/rifiuti.db");
		bp = new BeanProcessor();
	}

	public List<Aree> getAree(String area) throws Exception {
		String name = area;
		List<Aree> result = new ArrayList<Aree>();
		String query = "SELECT * FROM aree WHERE nome = \"" + area + "\"";
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		rs.next();
		Aree start = bp.toBean(rs, Aree.class);
		result.add(start);

		String parent = start.getParent();
		boolean next = false;
		do {
			query = "SELECT * FROM aree WHERE nome = \"" + parent + "\"";
			rs = statement.executeQuery(query);
			next = rs.next();
			if (next) {
				Aree aree = bp.toBean(rs, Aree.class);
				result.add(aree);
				parent = aree.getParent();
			}
		} while (parent != null && !parent.isEmpty());

		return result;
	}

	public List<Aree> getAree() throws Exception {
		List<Aree> result = new ArrayList<Aree>();
		String query = "SELECT * FROM aree";
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			result.add(bp.toBean(rs, Aree.class));
		}
		return result;
	}

	public String getTipoRifiuto(String nome, ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		System.out.println(aree);
		String query = "SELECT tipologiaRifiuto FROM riciclabolario WHERE nome = \"" + nome + "\" AND area in " + aree;
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<String> result = new ArrayList<String>();
		if (rs.next()) {
			return rs.getString("tipologiaRifiuto");
		}
		return null;
	}	

	// punti di raccolta 1/2
	public List<String> getTipiDiPuntiRaccolta(ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		String query = "SELECT DISTINCT tipologiaPuntoRaccolta FROM raccolta WHERE tipologiaUtenza = \"" + pu.getUtenza() + "\" AND area IN " + aree;
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("tipologiaPuntoRaccolta"));
		}
		return result;
	}

	// punti di raccolta 2/2
	public List<PuntiRaccolta> getPuntiDiRaccoltaPerTipoPuntoRaccolta(String tipoPuntiRaccolta, ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		String query = "SELECT * FROM puntiRaccolta WHERE tipologiaUtenza = \"" + pu.getUtenza() + "\" AND area IN " + aree + " AND tipologiaPuntiRaccolta = \"" + tipoPuntiRaccolta + "\"";
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<PuntiRaccolta> result = bp.toBeanList(rs, PuntiRaccolta.class);
		return result;
	}

	// ???
	public List<String> getTipoRaccoltaPerTipoPuntoRaccolta(String tipoRaccolta, ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		String query = "SELECT * FROM raccolta WHERE tipologiaUtenza = \"" + pu.getUtenza() + "\" AND area IN " + aree + " AND tipologiaPuntoRaccolta = \"" + tipoRaccolta + "\"";
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("tipologiaRaccolta"));
		}
		return result;
	}

	// tipi di raccolta 1/2
	public List<String> getTipiDiRaccolta() throws Exception {
		String query = "SELECT DISTINCT nome FROM tipologiaRaccolta";
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("nome"));
		}
		return result;
	}

	// tipo di raccolta 2/2
	public List<PuntiRaccolta> getPuntiDiRaccoltaPerTipoRaccolta(String tipoRaccolta, ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		Statement statement = connection.createStatement();
		String query = "SELECT puntiRaccolta.* FROM puntiRaccolta INNER JOIN raccolta ON puntiRaccolta.tipologiaPuntiRaccolta = raccolta.tipologiaPuntoRaccolta AND raccolta.tipologiaUtenza = puntiRaccolta.tipologiaUtenza WHERE raccolta.tipologiaRaccolta = \"" + tipoRaccolta + "\" AND puntiRaccolta.area IN " + aree + " AND raccolta.area IN " + aree + " AND puntiRaccolta.tipologiaUtenza = \"" + pu.getUtenza() + "\"";
		System.out.println(query);
		ResultSet rs = statement.executeQuery(query);

		List<PuntiRaccolta> result = bp.toBeanList(rs, PuntiRaccolta.class);
		
		return result;
	}
	
	// tipi di rifiuti 1/3
	public List<String> getTipiDiRifiuti() throws Exception {
		String query = "SELECT DISTINCT valore FROM tipologiaRifiuto";
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("valore"));
		}
		return result;
	}	
	
	// tipi di rifiuti 2/3
	public List<String> getRifiutoPerTipoDiRifiuti(String tipoRifiuto, ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		System.out.println(aree);
		String query = "SELECT nome FROM riciclabolario WHERE tipologiaRifiuto = \"" + tipoRifiuto + "\" AND area in " + aree;
		System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		List<String> result = new ArrayList<String>();
		while (rs.next()) {
			result.add(rs.getString("nome"));
		}
		return result;
	}
	
	public List<PuntiRaccolta> getPuntiDiRaccoltaPerTipoRifiuto(String tipoRifiuto, ProfiloUtente pu) throws Exception {
		String aree = getAreeForQuery(pu);
		Statement statement = connection.createStatement();
		String query = "SELECT puntiRaccolta.* FROM puntiRaccolta INNER JOIN raccolta ON puntiRaccolta.tipologiaPuntiRaccolta = raccolta.tipologiaPuntoRaccolta AND raccolta.tipologiaUtenza = puntiRaccolta.tipologiaUtenza WHERE raccolta.tipologiaRifiuto = \"" + tipoRifiuto + "\" AND puntiRaccolta.area IN " + aree + " AND raccolta.area IN " + aree + " AND puntiRaccolta.tipologiaUtenza = \"" + pu.getUtenza() + "\"";
		System.out.println(query);
		ResultSet rs = statement.executeQuery(query);

		List<PuntiRaccolta> result = bp.toBeanList(rs, PuntiRaccolta.class);
		
		return result;
	}
	

	private String getAreeForQuery(ProfiloUtente pu) {
		String aree = "(";
		for (Aree area : pu.getAree()) {
			aree += "\"" + area.getNome() + "\",";
		}
		aree = aree.substring(0, aree.length() - 1) + ")";
		return aree;
	}
	
}
