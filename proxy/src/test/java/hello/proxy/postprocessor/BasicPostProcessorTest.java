package hello.proxy.postprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.postprocessor.BasicPostProcessorTest.B;
import lombok.extern.slf4j.Slf4j;

public class BasicPostProcessorTest {

	@Test
	void basicConig() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicPostProcessorConfig.class);
		
		// beanA 이름으로 B객체가 빈으로 등록된다.
		B b = applicationContext.getBean("beanA", B.class);
		b.helloB();
		
		// B는 빈으로 등록되지 않았다.
		Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
	}
	
	@Slf4j
	@Configuration
	static class BasicPostProcessorConfig {
		@Bean(name = "beanA")
		public A a() {
			return new A();
		}
		
		@Bean
		public AToBPostProcessor helloPostProcessor() {
			return new AToBPostProcessor();
		}
	}
	
	
	@Slf4j
	static class A {
		
		public void helloA() {
			log.info("hello A");
		}
		
	}
	
	@Slf4j
	static class B {
		
		public void helloB() {
			log.info("hello B");
		}
		
	}
	
	@Slf4j
	static class AToBPostProcessor implements BeanPostProcessor{
		
		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			log.info("bean name = {}, bean = {}", beanName, bean);
			if(bean instanceof A) {
				return new B();
			}
			return bean;
		}
		
	}
}
