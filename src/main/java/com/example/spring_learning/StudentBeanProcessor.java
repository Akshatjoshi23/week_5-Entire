
package com.example.spring_learning;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(
            Object bean, String beanName) throws BeansException {

        if (beanName.equals("studentLifecycleService")) {
            System.out.println("BEFORE INIT: " + beanName);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean, String beanName) throws BeansException {

        if (beanName.equals("studentLifecycleService")) {
            System.out.println("AFTER INIT: " + beanName);
        }

        return bean;
    }
}
