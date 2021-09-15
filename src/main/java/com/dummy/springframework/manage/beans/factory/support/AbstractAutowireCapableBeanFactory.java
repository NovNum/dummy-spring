package com.dummy.springframework.manage.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.dummy.springframework.manage.beans.PropertyValue;
import com.dummy.springframework.manage.beans.PropertyValues;
import com.dummy.springframework.manage.beans.factory.config.BeanDefinition;
import com.dummy.springframework.manage.beans.factory.config.BeanReference;
import com.dummy.springframework.manage.ex.BeansException;

import java.lang.reflect.Constructor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    public Object createBean(String beanName , BeanDefinition beanDefinition,Object[] args) throws BeansException {
        Object bean;
        try {
            bean =createBeanInstance(beanDefinition,beanName,args);
            applyPropertyValues(beanName,bean,beanDefinition);
        } catch (Exception e ) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingletonBean(beanName,bean);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition,String beanName,Object[] objects){
        Constructor constructor = null;
        Class<?> clazz = beanDefinition.getBeanClass();
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor ctor : constructors){
            if (objects != null && ctor.getParameterTypes().length == objects.length){
                constructor = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition,beanName,constructor,objects);
    }

    /**
     * Bean 设置属性 填充
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues (String beanName,Object bean,BeanDefinition beanDefinition){
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()){
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference){
                    BeanReference  beanReference = (BeanReference) value;
                   value = getBean(beanReference.getBeanName());
                }
                BeanUtil.setFieldValue(bean,name,value);
            }
        } catch (BeansException e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

}
