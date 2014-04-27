package eu.trentorise.smartcampus.rifiuti.kml;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;

public class KMLReader {

	public static Map<String,Collection<KMLData>> readIsole() {
		Map<String,Integer> attributes = new HashMap<String, Integer>();
		attributes.put("riferiment", 5);
		attributes.put("GETTONIERA", 11);
		attributes.put("RESIDUO", 12);
		attributes.put("IMB_CARTA", 13);
		attributes.put("IMB_PL_MET", 14);
		attributes.put("ORGANICO", 15);
		attributes.put("IMB_VETRO", 16);
		attributes.put("INDUMENTI", 17);
		attributes.put("NOTE", 18);
		
		Set<String> keepFormat = Collections.singleton("NOTE");
		
		return read("src/main/resources/isole_estese.kml", "comune", attributes, keepFormat);
	}
	
	public static Map<String,Collection<KMLData>> readCRM() {
		return read("src/main/resources/crm.kml", "COMUNE",  null, null);
	}	

	public static Map<String,Collection<KMLData>> read(String file, String nameField, Map<String,Integer> attributes, Set<String> keepFormat) {
		Multimap<String, KMLData> multiResult = ArrayListMultimap.create(); 
		final Kml kml = Kml.unmarshal(new File(file));
		final Document document = (Document)kml.getFeature();
		Set<String> attrSet = new HashSet<String>();
		if (attributes != null) {
				attrSet = attributes.keySet();
		}
		System.out.println(document.getName());
		List<Feature> t = document.getFeature();
		for(Object o : t){
	        Folder f = (Folder)o;
	        List<Feature> tg = f.getFeature();
	        for(Object ftg : tg){
	            Placemark pm = (Placemark) ftg;
	            ExtendedData ext = pm.getExtendedData();
	            String name = "";
	            Map<Integer,String> attributeValues = new HashMap<Integer, String>();
	//            List<Data> data = ext.getData();
	            
	            for (SimpleData d: ext.getSchemaData().get(0).getSimpleData()) {
	            	if (nameField.equals(d.getName())) {
	            		name = WordUtils.capitalizeFully(d.getValue().toLowerCase());
	            		name = name.replace(" Di ", " di ");
	            	}
	            	if (attrSet.contains(d.getName())) {
	            		String value = d.getValue();
	            		if (keepFormat == null || !keepFormat.contains(d.getName())) {
	            			value = WordUtils.capitalizeFully(value.toLowerCase());
	            		} 
	            		attributeValues.put(attributes.get(d.getName()), value);
	            	}
	            }
	            Point point = (Point)pm.getGeometry();
	            System.out.println(name + " / " + point.getCoordinates());
	            
	            KMLData kd = new KMLData();
	            kd.setName(name);
	            kd.setLat(point.getCoordinates().get(0).getLatitude());
	            kd.setLon(point.getCoordinates().get(0).getLongitude());
	            kd.setAttributes(attributeValues);
	            multiResult.put(name.toLowerCase(),kd);
	        }
	    }
		return multiResult.asMap();
	}

}
