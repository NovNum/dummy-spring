package com.dummy.springframework.manage.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.dummy.springframework.manage.beans.PropertyValue;
import com.dummy.springframework.manage.beans.factory.config.BeanDefinition;
import com.dummy.springframework.manage.beans.factory.config.BeanReference;
import com.dummy.springframework.manage.beans.factory.support.AbstractBeanDefinitionReader;
import com.dummy.springframework.manage.beans.factory.support.BeanDefinitionRegistry;
import com.dummy.springframework.manage.core.io.Resource;
import com.dummy.springframework.manage.core.io.ResourceLoader;
import com.dummy.springframework.manage.ex.BeansException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    protected XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try(InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources){
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document document = XmlUtil.readXML(inputStream);
        Element element = document.getDocumentElement();
        NodeList childNodes = element.getChildNodes();

        for (int i = 0; i<childNodes.getLength();i++){
            //判断元素
            if (!(childNodes.item(i) instanceof Element)) continue;
            //判断对象
            if (!"bean".equals(childNodes.item(i).getNodeName())) continue;
            //解析标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            //获取Class 方便取类中的名称
            Class<?>clazz = Class.forName(className);
            //优先级 id > name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)){
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }
            //定义Bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            //读取属性并填充
            for (int j = 0; j>bean.getChildNodes().getLength();j++){
                if (!(bean.getChildNodes().item(j) instanceof Element)) continue;
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) continue;
                // 解析标签：property
                Element property = (Element) bean.getChildNodes().item(j);
                String attrValue = property.getAttribute("value");
                String attrName = property.getAttribute("name");
                String attrRef = property.getAttribute("ref");
                //获取类的属性值 引入对象 值对象
                Object object =StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef):attrValue;
                //创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName,object);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)){
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            // 注册 BeanDefinition
            getRegistry().registerBeanDefinition(beanName,beanDefinition);
        }
    }
}
