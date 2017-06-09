package fciencias.edatos.xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * The root interface in the xml parser/writer (XMLPW) hierarchy. An XMLPW
 * represents an object that can be used to parse and write objects of type T to
 * valid xml files representing such structures. In addition to an XMLPW, a
 * developer that wants to implement this interface will need to create a valid
 * <a href="https://en.wikipedia.org/wiki/Document_type_definition">Document
 * Type Definition</a> file.
 *
 * @param <T> The type of the elements the XMLPW is able to parse and write.
 * @author Luis Daniel Aragon Bermudez 416041271
 */
public interface ToXML<T> {
    /**
     * This method parses a valid xml (that represents an object or structure)
     * and returns an object of type {@code T} with the attributes described by
     * the specified file.
     *
     * @param fileName the relative path to the file that should be read.
     * @return The object specified by the valid xml file.
     * @throws ParserConfigurationException if the creation of a
     *                                      {@link javax.xml.parsers.DocumentBuilder} fails.
     * @throws SAXException                 if the file cannot be parsed.
     * @throws IOException                  if the file cannot be found or read.
     */
    public T read(String fileName) throws ParserConfigurationException, SAXException, IOException;

    /**
     * This method writes a valid xml that represents the provided object and
     * returns true if it's successful.
     *
     * @param object   the object to be written to an xml file.
     * @param fileName the relative path to the file to which the object should be
     *                 written.
     * @return true if the object was written to an xml file successfully.
     * @throws ParserConfigurationException if the creation of a
     *                                      {@link javax.xml.parsers.DocumentBuilder} fails.
     * @throws TransformerException         if the creation of a {@link javax.xml.transform.Transformer}
     *                                      fails or if the xml cannot be created with the properties
     *                                      specified by this method.
     */
    public boolean write(T object, String fileName) throws ParserConfigurationException, TransformerException;
}