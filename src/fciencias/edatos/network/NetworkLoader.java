package fciencias.edatos.network;

import fciencias.edatos.util.UnstableGraphException;
import fciencias.edatos.xml.ToXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;


/**
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public class NetworkLoader implements ToXML<Network> {
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static NetworkLoader newInstance() {
        return new NetworkLoader();
    }

    @Override
    public Network read(String fileName) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        Element root = doc.getDocumentElement();
        Network network = new Network();
        try {
            // If the root element is not Network, raise exception.
            if (!root.getTagName().equals("Network"))
                throw new IllegalArgumentException();
            // log.println("We're now parsing the string stack in " + fileName);

            // Extract Station tags.
            NodeList stationTags = doc.getElementsByTagName("Station");
            // Traverse the list of Station tags.
            for (int i = 0; i < stationTags.getLength(); i++) {
                // Reset tmp variables.
                Station station = null;
                HashSet<Client> clients = new HashSet<>();
                // Get actual Station tag.
                Node stationTag = stationTags.item(i);
                if (stationTag.getNodeType() == Node.ELEMENT_NODE) { // Formality, if .xml is valid this is redundant.
                    // Casting to Element so we can use getElementsByTagName.
                    Element stationElement = (Element) stationTag;
                    // Getting the station's attributes.
                    int areaCode = Integer.parseInt(stationElement.getAttribute("code"));
                    String stationName = stationElement.getAttribute("name");
                    // Extract Client tags.
                    NodeList clientTags = stationElement.getElementsByTagName("Client");
                    // Traverse the list of Client tags.
                    for (int j = 0; j < clientTags.getLength(); j++) {
                        // Get actual Client tag.
                        Node clientTag = clientTags.item(j);
                        if (clientTag.getNodeType() == Node.ELEMENT_NODE) { // Again, this is pure formality.
                            // Cast to Element so we can access getAttribute.
                            Element clientElement = (Element) clientTag;
                            // Getting the client's attributes.
                            String name = clientElement.getAttribute("name");
                            int phone = Integer.parseInt(clientElement.getAttribute("phone"));
                            // Adding a new client to this station's set of clients.
                            clients.add(new Client(name, phone, areaCode));
                        }
                    }
                    // After adding all clients, create station.
                    station = new Station(stationName, areaCode, clients);
                }

                // log.println("Found following station:\n" + station);
                // If everything went well, add the newly created station.
                if (station != null)
                    network.addVertex(station.getAreaCode(), station);
                else throw new SAXException("Invalid network .xml file. Found invalid station tag.");
            }

            // Extract Link tags.
            NodeList linkTags = doc.getElementsByTagName("Link");
            int stationACode, stationBCode;
            // Traverse the list of Link tags.
            for (int i = 0; i < linkTags.getLength(); i++) {
                // Reset tmp variables.
                stationACode = stationBCode = -1;
                // Get actual Link tag.
                Node linkTag = linkTags.item(i);
                if (linkTag.getNodeType() == Node.ELEMENT_NODE) { // Formality, if .xml is valid this is redundant.
                    // Casting to Element so we can use getElementsByTagName.
                    Element stationElement = (Element) linkTag;
                    // Getting the link's attributes.
                    stationACode = Integer.parseInt(stationElement.getAttribute("stationACode"));
                    stationBCode = Integer.parseInt(stationElement.getAttribute("stationBCode"));
                }
                // After parsing link, create edge. (Fails if tag is not element node)
                // log.println(String.format("Found following link:\t{%d, %d}%n", stationACode, stationBCode));
                network.addEdge(stationACode, stationBCode);
            }

            // log.println("[END at " + timestamp() + "] SUCCESS: read terminated successfully.");
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                throw new SAXException("Invalid network .xml file. Something other than an integer found in numeric field.");
            throw new SAXException("Invalid network .xml file.");


            // log.println("[END at " + timestamp() + "] ERROR: read failed. Invalid xml format, "
            //        + "this file doesn't construct a valid network. "
            //        + "Check resources/Network.dtd for more information on valid input. "
            //        + "You may have entered unparsable numbers or strings in one of the required fields.");

        } catch (DuplicateAreaCodeException e) {
            throw new SAXException("Invalid network .xml file. Duplicate area code found.");
        } catch (UnstableGraphException e) {
            throw new SAXException("Invalid network .xml file, it doesn't construct an simple undirected labeled graph.");
        }

        return network;
    }

    @Override
    public boolean write(Network object, String fileName) throws ParserConfigurationException, TransformerException {
        throw new UnsupportedOperationException("Network modification must be done manually. Support for Network saving underway.");
    }
}
