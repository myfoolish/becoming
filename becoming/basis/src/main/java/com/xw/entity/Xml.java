package com.xw.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/6/18 15:21
 */
public class Xml {

    private Element rootElement;
    private Element currentElement;
    private Document document;
    private boolean remPrefixFlag = false;

    public Xml() {
    }
    public static Xml newInstance(String eleName) {
        Xml ele = new Xml();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ele.document = builder.newDocument();
            ele.document.setXmlStandalone(true);
            Element element = ele.document.createElement(eleName);
            if(ele.rootElement == null) {
                ele.document.appendChild(element);
                ele.rootElement = element;
            }
            ele.currentElement = element;
        }catch (Exception e) {
            System.err.println("初始化Xml失败");
        }
        return ele;
    }

    /**
     * 	去除xml声明部分
     * @return
     */
    public Xml needPrefix(boolean flag) {
        remPrefixFlag = flag;
        return this;
    }

    /**
     * 	去除声明前缀
     * @return
     */
    private String remPrefix(String xmlStr) {
        final String regex = "(.*\\?>)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(xmlStr);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount();) {
                xmlStr = xmlStr.replace(matcher.group(i), "");
                break;
            }
        }
        return xmlStr;
    }

    @Override
    public String toString() {
        String xmlString = "";
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            // xml transform String
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transformer.transform(domSource, new StreamResult(bos));
            xmlString = bos.toString();
            if(remPrefixFlag) {
                xmlString = remPrefix(xmlString);
            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return xmlString;
    }

    /**
     * 	节点设置内容
     * @param element 节点
     * @param content	节点内容
     */
    public void setTextContent(Element element, String content) {
        if(null != content)	element.setTextContent(content);
    }

    /**
     * 	往指定节点添加子节点，记录当前节点
     * @param subEname
     * @param content
     * @return
     */
    public Xml putInner(Element parentElement, String subEname,String content) {
        Element subElement = document.createElement(subEname);
        subElement.setTextContent(content);
        parentElement.appendChild(subElement);
        currentElement = subElement;
        return this;
    }


    public Element getRootElement() {
        return rootElement;
    }

    public Element getCurrentElement() {
        return currentElement;
    }

    /**
     * 	工具测试方法
     <list>
     <row>
     <transferFlowNo>ERP企业流水号</transferFlowNo>
     <payAcc>付款账号</payAcc>
     <payAccName>付款账号名称</payAccName>
     <rcvAcc>收款账号</rcvAcc>
     <rcvAccName>收款账号名称</rcvAccName>
     <rcvBank>收款方开户行名称</rcvBank>
     <comitrNo>收款方开户行号</comitrNo>
     <cry>币种（01人民币）</cry>
     <tranAmt>金额</tranAmt>
     <rem>备注</rem>
     </row>
     </list>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Xml xml = Xml.newInstance("xml");
        xml.putInner(xml.getRootElement(), "data", "content...")
                .putInner(xml.getRootElement(), "data1", null)
                .putInner(xml.getCurrentElement(), "sub_data", "子节点内容");
        System.out.println(xml);

        Xml ele = Xml.newInstance("list");
        Element currentElement = null;
        for (int i = 0; i < 2; i++) {
            ele = ele.putInner(ele.getRootElement(), "row", null);
            currentElement = ele.getCurrentElement();//防止需要插入的节点改变
            Element tempElement = null;//当有多个子节点中含子节点，可以抽取出当前节点作为标记
            ele.putInner(currentElement, "transferFlowNo", "ERP企业流水号")
                    .putInner(currentElement, "payAcc", "付款账号")
                    .putInner(currentElement, "rem", "备注")
                    .putInner(ele.getCurrentElement(), "remData", null)
                    .putInner(ele.getCurrentElement(), "remDataData", "rem>>remData>>remDataData内容")
                    .putInner(currentElement, "data", null)
                    .putInner((tempElement = ele.getCurrentElement()), "data1", null)
                    .putInner(ele.getCurrentElement(), "data2", null)
                    .putInner(ele.getCurrentElement(), "data3", null)
                    .putInner(ele.getCurrentElement(), "data4", "data4内容")
                    .putInner(tempElement, "data11", "跟data1同级的内容，演示增加临时节点记录");
        }
        System.out.println(ele.needPrefix(true) );
    }
    /*public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        stringBuffer.append("<requestdata>");
        stringBuffer.append("<!-- 消息头 -->");
        stringBuffer.append("<reqcode>请求流水号</reqcode>");
        stringBuffer.append("<cmdcode>消息命令字</cmdcode>");
        stringBuffer.append("<user>用户名</user>");
        stringBuffer.append("<passwrd>密码</passwrd>");
        stringBuffer.append("<timestamp>时间戳</timestamp>");
        stringBuffer.append("<skey>签名</skey>");
        stringBuffer.append("<!-- 消息体 -->");
        stringBuffer.append("</requestdata>");
        System.out.println(stringBuffer);
    }*/
}
