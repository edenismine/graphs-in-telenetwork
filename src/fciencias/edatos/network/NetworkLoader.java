package fciencias.edatos.network;

import fciencias.edatos.util.Graph;
import fciencias.edatos.xml.ToXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class NetworkLoader implements ToXML<Graph<Station>> {
    @Override
    public Graph<Station> read(String fileName) throws ParserConfigurationException, SAXException, IOException {
        return null;
    }

    @Override
    public boolean write(Graph<Station> object, String fileName) throws ParserConfigurationException, TransformerException {
        return false;
    }
}
