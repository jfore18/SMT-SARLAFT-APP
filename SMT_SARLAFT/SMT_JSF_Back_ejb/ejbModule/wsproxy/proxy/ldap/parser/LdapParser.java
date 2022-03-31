package wsproxy.proxy.ldap.parser;

import wsproxy.proxy.ldap.core.LDAPUserMessage;
import wsproxy.proxy.ldap.core.LDAPDominioMessage;
import wsproxy.proxy.ldap.core.LDAPException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import java.io.ByteArrayInputStream;

import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Collection;


import org.w3c.dom.Document;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class LdapParser {

    public static LDAPDominioMessage parseDominios(String xml) throws LDAPException {
        LDAPDominioMessage mensaje = new LDAPDominioMessage();
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LDAPException("Error analizando respuesta de LDAP");
        }
        InputSource is = new InputSource(bais);
        DOMParser parser = new DOMParser();
        try {
            parser.parse(is);
            Document doc = parser.getDocument();
            Node root = doc.getFirstChild();
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node nodo = nl.item(i);
                if (nodo.getNodeName().equals("proceso")) {
                    mensaje.setProceso(nodo.getFirstChild().getNodeValue());
                } else if (nodo.getNodeName().equals("mensaje")) {
                    mensaje.setMensaje(nodo.getFirstChild().getNodeValue());
                } else if (nodo.getNodeName().equals("dominio")) {
                    mensaje.addDominio(nodo.getFirstChild().getNodeValue().replaceAll(" ", ""));
                }

            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new LDAPException("Error analizando respuesta de LDAP");
        } catch (IOException e) {
            e.printStackTrace();
            throw new LDAPException("Resultado de consulta de LDAP no exitoso");
        }
        return mensaje;
    }


    public static LDAPUserMessage parseUser(String xml) throws LDAPException {

        LDAPUserMessage mensaje = new LDAPUserMessage();
        ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LDAPException("Error analizando respuesta de LDAP");
        }
        InputSource is = new InputSource(bais);
        DOMParser parser = new DOMParser();
        try {
            parser.parse(is);
            Document doc = parser.getDocument();
            Node root = doc.getFirstChild();
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node nodo = nl.item(i);
                if (nodo.getNodeName().equals("proceso")) {
                    mensaje.setProceso(nodo.getFirstChild().getNodeValue());

                    //   }else if(nodo.getNodeName().equals("user")){
                } else if (nodo.getNodeName().equals("mensaje")) {
                    mensaje.setUser(nodo.getFirstChild().getNodeValue());
                }

            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new LDAPException("Error analizando respuesta de LDAP");
        } catch (IOException e) {
            e.printStackTrace();
            throw new LDAPException("Resultado de consulta de LDAP no exitoso");
        }
        return mensaje;
    }


}
