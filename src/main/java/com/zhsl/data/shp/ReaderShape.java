package com.zhsl.data.shp;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;

public class ReaderShape {
    public void readDBF(String path) {
        DbaseFileReader reader = null;
        try {
            reader = new DbaseFileReader(new ShpFiles(path), false, Charset.forName("GBK"));
            DbaseFileHeader header = reader.getHeader();
            int numFields = header.getNumFields();
            //迭代读取记录
            while (reader.hasNext()) {
                try {
                    Object[] entry = reader.readEntry();
                    for (int i=0; i<numFields; i++) {
                        String title = header.getFieldName(i);
                        Object value = entry[i];
                        System.out.println(title+"="+value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                //关闭
                try {reader.close();} catch (Exception e) {}
            }
        }
    }

    public static List<Map<String,Object>> readSHP(String path) {
        List<Map<String,Object>> list = new ArrayList<>();
        ShapefileDataStore shpDataStore = null;
        try{
            shpDataStore = new ShapefileDataStore(new File(path).toURI().toURL());
            shpDataStore.setCharset(Charset.forName("GBK"));
            String typeName = shpDataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();
            System.out.println(result.size());
            FeatureIterator<SimpleFeature> itertor = result.features();
            while(itertor.hasNext()){
                SimpleFeature feature = itertor.next();
                Collection<Property> p = feature.getProperties();
                Iterator<Property> it = p.iterator();
                while(it.hasNext()) {
                    Property pro = it.next();
                    Map<String,Object> map = new HashMap<>();
                    if (pro.getValue() instanceof com.vividsolutions.jts.geom.Point) {
                        com.vividsolutions.jts.geom.Point m = (com.vividsolutions.jts.geom.Point)pro.getValue();
                        System.out.println("X: " + m.getCoordinate().x);
                        System.out.println("Y: " + m.getCoordinate().y);
                        map.put("x",m.getCoordinate().x);
                        map.put("y",m.getCoordinate().y);
                        list.add(map);
                    }else if (pro.getValue() instanceof com.vividsolutions.jts.geom.MultiPolygon) {
                        com.vividsolutions.jts.geom.MultiPolygon m = (com.vividsolutions.jts.geom.MultiPolygon)pro.getValue();
                    }else {
                        System.out.println(pro.getName() + " = " + pro.getValue());
                    }
                }
            }
            itertor.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) { e.printStackTrace(); }
        return list;
    }
}
