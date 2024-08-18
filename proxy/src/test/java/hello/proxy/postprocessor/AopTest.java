package hello.proxy.postprocessor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

public class AopTest {

	@Test
	void basicConig() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicPostProcessorConfig.class);
		
		 A a = (A) applicationContext.getBean("beanA");
		 a.helloA();
		// B는 빈으로 등록되지 않았다.
		Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
	}
	
	@Configuration
	static class BasicPostProcessorConfig {
		@Bean(name = "beanA")
		public A a() {
			return new A();
		}
	}
	
	
	@Slf4j
	static class A {
		public void helloA() {
			log.info("hello A");
		}
	}
	
	@Aspect
	static class AopConfig {
		@Around("execution(* hello.proxy.postprocessor.A.*(..))")
		public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
			return joinPoint.proceed();
		}
	}

}
