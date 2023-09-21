package com.simplejcode.commons.misc.util;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import jakarta.xml.bind.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public final class XMLUtils {

    private XMLUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        return createDocumentBuilder(true);
    }

    public static DocumentBuilder createDocumentBuilder(boolean namespaceAware) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(namespaceAware);
        return documentBuilderFactory.newDocumentBuilder();
    }

    public static Document normalize(Document doc) {
        doc.getDocumentElement().normalize();
        doc.normalizeDocument();
        return doc;
    }

    public static Document readXmlByURI(String uri) throws Exception {
        return normalize(createDocumentBuilder().parse(uri));
    }

    public static Document readXml(InputStream is) throws Exception {
        return normalize(createDocumentBuilder().parse(is));
    }

    public static Document readXml(String source) throws Exception {
        return readXml(source, true);
    }

    public static Document readXml(String source, boolean namespaceAware) throws Exception {
        return normalize(createDocumentBuilder(namespaceAware).parse(new InputSource(new StringReader(source))));
    }

    //-----------------------------------------------------------------------------------

    public static String getValue(Node node, String... key) {
        return getValue(node, 0, key);
    }

    public static String getValue(Node node, int i, String... key) {
        if (i == key.length) {
            return node.getChildNodes().getLength() == 0 ? null : node.getChildNodes().item(0).getNodeValue();
        }
        NodeList list = getNodesByTagName(node, key[i]);
        return list == null || list.getLength() == 0 ? null : getValue(list.item(0), i + 1, key);
    }

    public static Node getNode(Node node, String... key) {
        return getNode(node, 0, key);
    }

    public static Node getNode(Node node, int i, String... key) {
        if (i == key.length) {
            return node;
        }
        NodeList list = getNodesByTagName(node, key[i]);
        return list == null || list.getLength() == 0 ? null : getNode(list.item(0), i + 1, key);
    }

    public static Document createEmptyDocument() throws ParserConfigurationException {
        return createDocumentBuilder().newDocument();
    }

    public static String objectToXml(JAXBContext context, JAXBElement obj) {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(obj, baos);
            baos.flush();
            String responseStr = new String(baos.toByteArray());
            baos.close();
            return responseStr;
        } catch (Exception ex) {
            throw new RuntimeException("Internal error: invalid xml");
        }
    }

    public static String toString(Node node) throws TransformerException {
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();

        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(node);
        trans.transform(source, result);
        return sw.toString();

    }

    public static NodeList getNodesByTagName(Node node, String tagName) {
        final List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (child.getNodeName().equals(tagName)) {
                nodes.add(child);
            }
        }
        return new NodeList() {
            public Node item(int index) {
                return nodes.get(index);
            }

            public int getLength() {
                return nodes.size();
            }
        };
    }

}
