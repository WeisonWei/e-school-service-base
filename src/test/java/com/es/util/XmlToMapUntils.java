package com.es.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XmlToMapUntils {

    /**
     * xml转为Map集合
     *
     * @author Administrator
     */

    public static Map<String, String> xmlToMap(String xml) throws DocumentException {
        Map<String, String> map = new HashMap<String, String>();
        Document doc = null;
        doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement();
        List<Element> list = rootElt.elements();
        for (Element element : list) {
            map.put(element.getName(), element.getText());
        }
        return map;
    }
}


