import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class Test {
    private static int niveau;

    public static String pathToXml(String path) {

        File dossier = new File(path);
        String xmlstring = "\n" + "<directory name = " + "\"" + dossier.getName() + "\"" + ">";
        File[] liste = dossier.listFiles();
        for (File df : liste) {
            if (df.isFile()) {
                xmlstring = xmlstring + "\n" + "\t" + "<file name = " + "\"" + df.getName() + "\"" + "/>";
            } else if (df.isDirectory()) {
                xmlstring = xmlstring + pathToXml(df.getAbsolutePath());
            }
        }

        xmlstring = xmlstring + "\n" + "</directory>" + "\n";
        return xmlstring;

    }

    public static Composant insertion(Element e) {
        Dossier home = new Dossier(e.getAttribute("name"), niveau);
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nodes.item(i);
                if (el.getNodeName().equals("file")) {
                    Composant myFile;
                    myFile = new Fichier(el.getAttribute("name"), home.getNiveau() + 1);
                    home.ajouter(myFile);
                } else if (el.getNodeName().equals("directory")) {
                    niveau = home.getNiveau() + 1;
                    home.ajouter(insertion(el));
                    niveau--;
                }
            }
        }
        return home;
    }

    public static Composant xmlToDoc(String xmlstring) throws ParserConfigurationException, SAXException, IOException {
        String xmlStr = "<?xml version=\"1.0\"?>" + xmlstring;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(xmlStr);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
        Document doc = builder.parse(input);
        Element element = doc.getDocumentElement();
        return insertion(element);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        // String path = "c:\\users\\hp\\onedrive\\bureau\\java\\composite";

        // Dossier dossier = new Dossier(path, 0);
        // dossier.afficher();

        String xmlString1 = pathToXml("c:\\users\\hp\\onedrive\\bureau\\java\\composite");
        System.out.println(xmlString1);

        // System.out.println("**************************************************************");

        String xmlString2 = pathToXml("c:\\users\\hp\\onedrive\\bureau\\java\\composite");
        Composant root = xmlToDoc(xmlString2);
        root.afficher();
    }
}