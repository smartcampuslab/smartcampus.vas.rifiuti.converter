package eu.trentorise.smartcampus.rifiuti.kml;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.WordUtils;

import de.micromata.opengis.kml.v_2_2_0.Data;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;

public class KMLReader {

	public static Map<String,KMLData> readIsole() {
		Map<String, KMLData> result = new TreeMap<String, KMLData>();
		final Kml kml = Kml.unmarshal(new File("src/main/resources/isole.kml"));
    final Document document = (Document)kml.getFeature();
    System.out.println(document.getName());
    List<Feature> t = document.getFeature();
    for(Object o : t){
        Folder f = (Folder)o;
        List<Feature> tg = f.getFeature();
        for(Object ftg : tg){
            Placemark pm = (Placemark) ftg;
            ExtendedData ext = pm.getExtendedData();
            String name = "";
            String descr = "";
            List<Data> data = ext.getData();
            
            for (SimpleData d: ext.getSchemaData().get(0).getSimpleData()) {
            	if ("comune".equals(d.getName())) {
            		name = WordUtils.capitalizeFully(d.getValue().toLowerCase());
            	}
            	if ("riferiment".equals(d.getName())) {
            		descr = WordUtils.capitalizeFully(d.getValue().toLowerCase());
            	}            	
            }
            Point point = (Point)pm.getGeometry();
            System.out.println(name + " / " + descr + " / " + point.getCoordinates());
            
            KMLData kd = new KMLData();
            kd.setName(name);
            kd.setDescription(descr);
            kd.setLat(point.getCoordinates().get(0).getLatitude());
            kd.setLon(point.getCoordinates().get(0).getLongitude());
            result.put(name.toLowerCase(),kd);
        }
    }
    return result;
	}
	
	public static Map<String,KMLData> readCRM() {
		Map<String, KMLData> result = new TreeMap<String, KMLData>();
		final Kml kml = Kml.unmarshal(new File("src/main/resources/crm.kml"));
    final Document document = (Document)kml.getFeature();
    System.out.println(document.getName());
    List<Feature> t = document.getFeature();
    for(Object o : t){
        Folder f = (Folder)o;
        List<Feature> tg = f.getFeature();
        for(Object ftg : tg){
            Placemark pm = (Placemark) ftg;
            ExtendedData ext = pm.getExtendedData();
            String name = "";
            String descr = "";
            List<Data> data = ext.getData();
            
            for (SimpleData d: ext.getSchemaData().get(0).getSimpleData()) {
            	if ("COMUNE".equals(d.getName())) {
            		name = WordUtils.capitalizeFully(d.getValue().toLowerCase());
            	}
            	if ("DESCR".equals(d.getName())) {
            		descr = WordUtils.capitalizeFully(d.getValue().toLowerCase());
            	}            	
            }
            Point point = (Point)pm.getGeometry();
            System.out.println(name + " / " + descr + " / " + point.getCoordinates());
            
            KMLData kd = new KMLData();
            kd.setName(name);
            kd.setDescription(descr);
            kd.setLat(point.getCoordinates().get(0).getLatitude());
            kd.setLon(point.getCoordinates().get(0).getLongitude());
            result.put(name.toLowerCase(),kd);
        }
    }
    return result;
	}	

}
