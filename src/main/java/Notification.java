
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Notification {

    public void WriteNotificationXml(String data){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //root elements
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("root");
            document.appendChild(rootElement);
            // staff elements
            Element staff = document.createElement("msg-list");
            rootElement.appendChild(staff);

            // set attribute to staff element
            /*Attr attr = document.createAttribute("id");
            attr.setValue("1");
            staff.setAttributeNode(attr);*/

            // shorten way
            // staff.setAttribute("id", "1");

            // firstname elements
            Element firstname = document.createElement("msg");
            firstname.appendChild(document.createTextNode("Purchase with card 966796******6093 on 11.11.2018 14:34, amount 45.99  usd."));
            staff.appendChild(firstname);


            firstname.appendChild(document.createTextNode("Withdrawal with card 8237******105879 on 11.12.2018 09:56, amount 10.00 eur."));
            staff.appendChild(firstname);

            // salary elements
            //Element salary = document.createElement("salary");
            //salary.appendChild(document.createTextNode("100000"));
           // staff.appendChild(salary);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("src/file.xml"));

            // Output to console for testing
            //StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

}
