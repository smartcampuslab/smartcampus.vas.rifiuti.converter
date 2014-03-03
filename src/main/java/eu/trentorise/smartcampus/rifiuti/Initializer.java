package eu.trentorise.smartcampus.rifiuti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.trentorise.smartcampus.rifiuti.kml.KMLData;
import eu.trentorise.smartcampus.rifiuti.kml.KMLReader;
import eu.trentorise.smartcampus.rifiuti.model.Rifiuti;

public class Initializer {

	private List<String> oneColumnAsMany;
	private Properties primaryKeys;
	
	Connection connection;
	
	Map<String, Collection<KMLData>> isole;
	Map<String, Collection<KMLData>> crm;

	public Initializer() throws Exception {
		String[] exSheets = new String[] { "TIPOLOGIA_RIFIUTO" };
		primaryKeys = new Properties();
		primaryKeys.load(new FileInputStream("src/main/resources/primary_keys.txt"));
		oneColumnAsMany = Arrays.asList(exSheets);
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:./src/main/resources/rifiuti.db");
		KMLReader kmlReader = new KMLReader();
		isole = kmlReader.readIsole();
		crm = kmlReader.readCRM();
	}

	public Rifiuti readExcel(String fileName) throws Exception {
		InputStream inp = new FileInputStream("src/main/resources/" + fileName);
		HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		ExcelExtractor extractor = new ExcelExtractor(wb);

		extractor.setFormulasNotResults(true);
		extractor.setIncludeSheetNames(false);

		Rifiuti rifiuti = new Rifiuti();

		Map<String, List<String>> singleColumn = new TreeMap<String, List<String>>();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			System.out.println(sheet.getSheetName());
			Thread.sleep(1000);
			if (sheet.getRow(0).getLastCellNum() == 1 && !oneColumnAsMany.contains(sheet.getSheetName())) {
			} else {
				List<Map<String, String>> result = getSheetMap(sheet);
				mapMap(rifiuti, sheet.getSheetName(), result);
			}
		}

		return rifiuti;
	}

	private void getSheetList(Sheet sheet, Map<String, List<String>> map) {
		Row row = null;
		List<String> values = new ArrayList<String>();
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			String value = row.getCell(0).toString().replace("_", " ").trim();
			if (!value.isEmpty()) {
				values.add(value);
			}
		}

		map.put(sheet.getSheetName().trim(), values);
	}

	private List<Map<String, String>> getSheetMap(Sheet sheet) {
		Row row = sheet.getRow(0);
		List<String> keys = new ArrayList<String>();
		int firstRow = 1;
		if (row.getLastCellNum() != 1) {
			for (int j = 0; j < row.getLastCellNum(); j++) {
				String key = WordUtils.capitalizeFully(" " + getCellValue(row.getCell(j)).replace(' ', '_'), new char[] { '_' }).replace("_", "").trim();
				keys.add(key);
			}
		} else {
			firstRow = 0;
			keys.add("valore");
		}

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (int i = firstRow; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Map<String, String> map = new TreeMap<String, String>();
			boolean add = false;
			for (int j = 0; j < row.getLastCellNum(); j++) {
				if (j >= keys.size()) {
					continue;
				}
				if (row.getCell(j) != null) {
					String value = getCellValue(row.getCell(j)).replace("_", " ").trim();
					if (!value.isEmpty()) {
						add = true;
					}
					try {
						map.put(keys.get(j), value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					map.put(keys.get(j), "");
				}
			}
			if (add) {
				result.add(map);
			}
		}

		return result;
	}
	
	private String getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			String value;
			Calendar cal = new GregorianCalendar();
			cal.setTime(cell.getDateCellValue());
			if (cal.get(Calendar.YEAR) < 2014) {
				value = cal.get(Calendar.HOUR_OF_DAY) + ":" + (cal.get(Calendar.MINUTE) + "0").substring(0, 2);
			} else {
				value = cal.get(Calendar.DAY_OF_MONTH) + "/" + (1 + cal.get(Calendar.MONTH)) + "/" + cal.get(Calendar.YEAR);
			}
			return value;
		}
		return "";
	}

	private Rifiuti mapMap(Rifiuti rifiuti, String sheetName, List<Map<String, String>> data) throws Exception {
		String className = "eu.trentorise.smartcampus.rifiuti.model." + WordUtils.capitalizeFully(sheetName.replace(' ', '_'), new char[] { '_' }).replace("_", "").trim();
		String field = WordUtils.capitalizeFully(" " + sheetName.replace(' ', '_'), new char[] { '_' }).replace("_", "").trim();

		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			System.err.println("No class");
			return rifiuti;
		}

		ObjectMapper mapper = new ObjectMapper();
		List<Object> fields = new ArrayList<Object>();

		Multimap<String, Object> fieldsMap = ArrayListMultimap.create();

		for (Map<String, String> map : data) {
			Object o = mapper.convertValue(map, clazz);
			fields.add(o);
		}

		Method method2 = Rifiuti.class.getMethod("set" + WordUtils.capitalize(field), List.class);
		method2.invoke(rifiuti, fields);

		return rifiuti;
	}

	public List<String> writeCSV(Rifiuti rifiuti) throws Exception {
		List<String> result = new ArrayList<String>();
		Class clazz = Rifiuti.class;
		for (Field field : clazz.getDeclaredFields()) {
			String fieldName = field.getName();

			System.out.println(">" + fieldName);

			Method method = Rifiuti.class.getMethod("get" + WordUtils.capitalize(fieldName), null);
			List values = (List) method.invoke(rifiuti, null);

			CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/output/csv/" + fieldName + ".csv"), ',');

			List<String> keys = new ArrayList<String>();
			for (Field field2 : values.get(0).getClass().getDeclaredFields()) {
				keys.add(field2.getName());
			}

			writer.writeNext(keys.toArray(new String[keys.size()]));

			String sql = buildSQLCreate(fieldName, keys);
			result.add(sql);

			for (Object obj : values) {
				List<String> values2 = new ArrayList<String>();
				for (String key : keys) {
					Method method2 = obj.getClass().getMethod("get" + WordUtils.capitalize(key), null);
					String val = (String) method2.invoke(obj, null);
					values2.add(val);
				}

				if ("puntiRaccolta".equals(fieldName)) {
					List<List<String>> newValues2 = completePuntiRaccolta(values2);
					for (List<String> nv2 : newValues2) {
						writer.writeNext(nv2.toArray(new String[nv2.size()]));
					}
				} else {
					writer.writeNext(values2.toArray(new String[values2.size()]));
				}

			}

			writer.close();

		}
		return result;
	}

	private List<List<String>> completePuntiRaccolta(List<String> values) {
		String name;
		List<List<String>> result = new ArrayList<List<String>>();
		Map<String, Collection<KMLData>> kml;
		boolean isCrm = false;
		boolean hasDescription = values.get(4) == null || values.get(4).isEmpty();
		if ("crm".equals(values.get(1).toLowerCase())) {
			kml = crm;
			name = values.get(4).toLowerCase();
			isCrm = true;
		} else if ("isola ecologica".equals(values.get(1).toLowerCase())) {
			kml = isole;
			name = values.get(0).toLowerCase();
		} else {
			result.add(values);
			return result;
		}

		if (!kml.containsKey(name) || (isCrm && hasDescription == true)) {
			result.add(values);
			return result;
		}

		Collection<KMLData> data = kml.get(name);

		Iterator it = data.iterator();
		while (it.hasNext()) {
			List<String> line = new ArrayList<String>(values);
			KMLData next = (KMLData) it.next();
			line.set(3, next.getLat() + "," + next.getLon());
			if (!isCrm) {
				line.set(4, next.getDescription());
			}
			result.add(line);
		}

		return result;
	}
	
	public void convertFromCSVToDB() throws Exception {
		File[] files = new File("src/main/resources/output/csv/").listFiles();
		Rifiuti rifiuti = new Rifiuti();

		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);		
		
		String create = "CREATE TABLE \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')";
		statement.executeUpdate("DROP TABLE IF EXISTS \"android_metadata\"");
		System.err.println(create);
		statement.executeUpdate(create);		
		
		String insert = "INSERT INTO \"android_metadata\" VALUES ('en_US')";
		System.err.println(insert);
		statement.executeUpdate(insert);		
		
		for (File f : files) {
			if (!f.getName().endsWith("csv")) {
				continue;
			}
			String fieldName = f.getName().replace(".csv", "");
			String className = WordUtils.capitalize(fieldName);
			Class clazz = Class.forName("eu.trentorise.smartcampus.rifiuti.model." + className);

			System.out.println("Converting: " + fieldName);
			
			CSVReader reader = new CSVReader(new FileReader(f));

			List<String[]> lines = reader.readAll();

			reader.close();
			
			statement = connection.createStatement();
			
			boolean first = true;
			String[] keys = null;
			List objs = new ArrayList();
			for (String[] line : lines) {
				if (first) {
					keys = line;
					first = false;
					create = buildSQLCreate(fieldName, Arrays.asList(keys));
					statement.executeUpdate("DROP TABLE IF EXISTS " + fieldName);
					statement.executeUpdate(create);					
				} else {
					insert = buildSQLInsert(fieldName, Arrays.asList(line));
					statement.executeUpdate(insert);
				}
			}

			System.out.println("Done: " + fieldName);
			
		}
		
		System.out.println("All done");

	}
	
	private void initDB(Rifiuti rifiuti) throws Exception {
		List<String> result = new ArrayList<String>();
		Class clazz = Rifiuti.class;
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);		
		
		String create = "CREATE TABLE \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')";
		statement.executeUpdate("DROP TABLE IF EXISTS \"android_metadata\"");
		System.err.println(create);
		statement.executeUpdate(create);		
		
		String insert = "INSERT INTO \"android_metadata\" VALUES ('en_US')";
		System.err.println(insert);
		statement.executeUpdate(insert);		
		
		for (Field field : clazz.getDeclaredFields()) {
			String fieldName = field.getName();

			Method method = Rifiuti.class.getMethod("get" + WordUtils.capitalize(fieldName), null);
			List values = (List) method.invoke(rifiuti, null);

			List<String> keys = new ArrayList<String>();
			for (Field field2 : values.get(0).getClass().getDeclaredFields()) {
				keys.add(field2.getName());
			}		
			
			create = "CREATE TABLE \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')";
			
			
			create = buildSQLCreate(fieldName, keys);
			
			statement.executeUpdate("DROP TABLE IF EXISTS " + fieldName);
			System.err.println(create);
			statement.executeUpdate(create);
			
			for (Object obj : values) {
				List<String> values2 = new ArrayList<String>();
				for (String key : keys) {
					Method method2 = obj.getClass().getMethod("get" + WordUtils.capitalize(key), null);
					String val = (String) method2.invoke(obj, null);
					values2.add(val);
				}
				insert = buildSQLInsert(fieldName, values2);
				System.err.println(insert);
				statement.executeUpdate(insert);
			}
			}			
	}

	private String buildSQLCreate(String tableName, List<String> keys) {
		String pKeys = null;
		if (primaryKeys.containsKey(tableName)) {
			pKeys = primaryKeys.getProperty(tableName);
		}
		String s = "CREATE TABLE " + tableName + " (";
		for (int i = 0; i < keys.size(); i++) {
			s += keys.get(i) + " TEXT, ";
		}
		if (pKeys != null) {
			s = s + " PRIMARY KEY (" + pKeys + "))";
		} else {
			s = s.substring(0, s.length() - 2) + ")";
		}
//		System.err.println(s);
		return s;
	}
	
	private String buildSQLInsert(String tableName, List<String> values) {
		String s = "INSERT INTO " + tableName + " VALUES(";
		for (String value: values) {
			s += "\"" + value + "\",";
		}
		s = s.substring(0, s.length() - 1) + ")";
//		System.err.println(s);
		return s;
	}

	public Rifiuti readFromCSV() throws Exception {
		File[] files = new File("src/main/resources/output/csv/").listFiles();
		Rifiuti rifiuti = new Rifiuti();

		for (File f : files) {
			String fieldName = f.getName().replace(".csv", "");
			String className = WordUtils.capitalize(fieldName);
			Class clazz = Class.forName("eu.trentorise.smartcampus.rifiuti.model." + className);

			CSVReader reader = new CSVReader(new FileReader(f));

			List<String[]> lines = reader.readAll();

			reader.close();

			boolean first = true;
			String[] keys = null;
			List objs = new ArrayList();
			for (String[] line : lines) {
				String[] values;
				if (first) {
					keys = line;
					first = false;
				} else {
					Object obj = clazz.newInstance();
					for (int i = 0; i < keys.length; i++) {
						String key = keys[i];
						Method method = clazz.getMethod("set" + WordUtils.capitalize(key), String.class);
						method.invoke(obj, line[i]);
					}
					objs.add(obj);
				}
			}

			Method method2 = Rifiuti.class.getMethod("set" + WordUtils.capitalize(fieldName), List.class);
			method2.invoke(rifiuti, objs);
		}

		return rifiuti;
	}

}
