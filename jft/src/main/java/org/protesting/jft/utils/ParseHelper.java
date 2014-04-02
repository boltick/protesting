package org.protesting.jft.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Locale;

import sun.security.util.Resources;

/**
 * User: ab83625
 * Date: 14.02.2008
 * Time: 11:10:30
 */
public class ParseHelper {
    private static Log logger = LogFactory.getLog(ParseHelper.class);

    public static Document getDocument(String xmlFilename) {
        return getDocument(new File(xmlFilename));
    }


    public static Document getDocument(File xmlFile) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setValidating(false);

        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document dom = null;
        try {
            dom = db.parse(xmlFile);
        } catch (SAXException e) {
            logger.error(xmlFile.getName(), e);
        } catch (IOException e) {
            logger.error(xmlFile.getName(), e);
        }
        if (dom == null) {
            throw new IllegalStateException("File "+xmlFile.getName()+": incorrect DOM");
        }
        return dom;
    }

    public static Document getDocument(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (db == null) {
            throw new IllegalStateException("Document Builder ERROR.");
        }
        Document dom = null;
        try {
            dom = db.parse(inputStream);
        } catch (SAXException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        } finally{
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e);
            }
        }
        if (dom == null) {
            throw new IllegalStateException("DOM is not built for input stream");
        }
        return dom;
    }


    public static Document getDocument(URL url) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (db == null) {
            throw new IllegalStateException("Document Builder ERROR.");
        }
        Document dom = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
//            dbf.setNamespaceAware(true);
//            dbf.setValidating(true);
            dom = db.parse(urlConnection.getInputStream());
        } catch (SAXException e) {
            e.printStackTrace();
//            logger.error(xmlFile.getName(), e);
        } catch (IOException e) {
            e.printStackTrace();
//            logger.error(xmlFile.getName(), e);
        } finally{
            urlConnection.disconnect();
            urlConnection = null;
        }
        if (dom == null) {
            throw new IllegalStateException("URL "+url.toExternalForm()+": DOM is not built");
        }
        return dom;
    }



    public static Document getNewDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document dom = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
        } catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        if (dom == null) {
            throw new IllegalStateException("Document is not created");
        }
        return dom;
    }

    public static Element getConfigElement(Object object, String path) {
        ClassLoader cl = object.getClass().getClassLoader();
        InputStream stream = cl.getResourceAsStream(path);
        return ParseHelper.getDocument(stream).getDocumentElement();
    }

    public static ResourceBundle getResourceBundle(String path, Locale locale) {
        return Resources.getBundle(path, locale);
    }

    

}
