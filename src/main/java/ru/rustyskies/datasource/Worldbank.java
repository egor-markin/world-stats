package ru.rustyskies.datasource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.rustyskies.beans.Country;
import ru.rustyskies.tools.ProxyUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * @author Egor Markin
 * @since 29.04.2018
 */
@Slf4j
@UtilityClass
public class Worldbank {

    // Based on https://datahelpdesk.worldbank.org/knowledgebase/topics/125589-developer-information
    // https://data.worldbank.org/indicator/NY.GDP.MKTP.CD?year_high_desc=true
    // https://datahelpdesk.worldbank.org/knowledgebase/articles/898590-api-country-queries
    // https://datahelpdesk.worldbank.org/knowledgebase/articles/898599-api-indicator-queries
    // http://api.worldbank.org/v2/countries/rus/indicators/NY.GDP.MKTP.CD?date=2017:2017
    // http://api.worldbank.org/v2/countries/rus/indicators/NY.GDP.MKTP.CD

    private static final String BASE_URL = "https://api.worldbank.org/v2/";

    private double getIndicatorValue(Country country, String indicator) {
        String url = BASE_URL + "countries/" + country.iso2Code + "/indicators/" + indicator;

        String xml;
        try {
            xml = IOUtils.toString(new URI(url), Charset.forName("UTF-8"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        NodeList nodes = document.getElementsByTagName("wb:data");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                Node childNode = node.getChildNodes().item(j);
                if ("wb:value".equals(childNode.getNodeName())) {
                    String value = childNode.getTextContent();
                    if (value != null && !value.trim().equals("")) {
                        try {
                            return Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            log.warn("Unable to parse double value: " + value);
                        }
                    }
                }
            }
        }
        return 0;
    }

    /** Nominal Gross Domestic Product ($USD) */
    public double getGdp(Country country) {
        return getIndicatorValue(country, "NY.GDP.MKTP.CD");
    }

    /** Nominal Gross Domestic Product per Capita ($USD) */
    public double getGdppc(Country country) {
        return getIndicatorValue(country, "NY.GDP.PCAP.CD");
    }

    public static void main(String[] args) {
        ProxyUtils.enableSocksProxy();
        System.out.println(getGdppc(Country.Russia));
    }

}
