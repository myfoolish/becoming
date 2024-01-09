package com.xw.spring.ioc.context;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/4
 */
public class ClassPathXmlApplicationContext implements ApplicationContext{
    private Map iocContainer = new HashMap<>();

    public ClassPathXmlApplicationContext(String configLocation) {
        try {
            // 得到文件的路径
            String filePath = this.getClass().getResource(configLocation).getPath();
            // 若filePath包含中文可能会出现路径找不到，所以使用url的解码
            filePath = URLDecoder.decode(filePath, "UTF-8");
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(filePath));
            // 获取bean
            List<Node> beans = document.getRootElement().selectNodes("bean");
            for (Node bean : beans) {
                Element e = (Element) bean;
                String id = e.attributeValue("id");
                String aClass = e.attributeValue("class");
                // 通过反射
                Class<?> className = Class.forName(aClass);
                Object o = className.newInstance();
                iocContainer.put(id, o);

                // 设置属性
                List<Node> properties = e.selectNodes("property");
                for (Node property : properties) {
                    Element el = (Element) property;
                    String propertyName = el.attributeValue("name");
                    String propertyValue = el.attributeValue("value");
                    // 通过setter方法注入数据
                    String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    System.out.println("准备执行" + setterMethodName + "方法注入数据");
                    Method setterMethod = className.getMethod(setterMethodName, String.class);
                    setterMethod.invoke(o, propertyValue);
                }
            }
            System.out.println("ioc容器初始化完毕");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getBean(String name) {
        return iocContainer.get(name);
    }
}
