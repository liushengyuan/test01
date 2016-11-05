package com.enation.app.base.core.dic;

import org.xml.sax.helpers.DefaultHandler;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
public class ConfigXmlParser extends DefaultHandler {

	private Map<String, String> configItems = new HashMap<String, String>();

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void error(SAXParseException e) throws SAXException {

	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {

	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if(!"item".equals(name))
			return;
		String configItemName = null;
		String configItemVal = null;
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String n = attributes.getQName(i);
				String v = attributes.getValue(n);
				if ("name".equalsIgnoreCase(n)) {
					configItemName = v;
				}
				if ("value".equalsIgnoreCase(n)) {
					configItemVal = v;
				}
			}
		}
		if (configItemName != null && configItemVal != null) {
			this.configItems.put(configItemName, configItemVal);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {

	}

	public void doParse(InputStream ins) {
		try {

			SAXParserFactory spFactory = SAXParserFactory.newInstance();
			spFactory.setFeature("http://xml.org/sax/features/validation",
					false);
			spFactory.setValidating(false);
			SAXParser sParser = spFactory.newSAXParser();
			XMLReader xr = sParser.getXMLReader();
			xr.setErrorHandler(this);

			xr.setContentHandler(this);
			xr.parse(new InputSource(new InputStreamReader(ins, "UTF-8")));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Map<String, String> getConfigItems() {
		return configItems;
	}

}
