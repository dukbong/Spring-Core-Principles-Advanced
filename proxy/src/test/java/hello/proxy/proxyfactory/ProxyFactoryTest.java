package hello.proxy.proxyfactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyFactoryTest {

	@Test
	@DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
	void interfaceProxy() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.addAdvice(new TimeAdvice());
		ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();
		
		log.info("targetClass = {}", target.getClass());
		log.info("proxyzClass = {}", proxy.getClass());
		
		proxy.save();
		
		// ProxyFactory를 만들면 검증 할 수 있는 것들
		// 1. Proxy가 맞는지?
		Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		// 2. JDK 동적 프록시가 맞는지?
		Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
		// 3. CGLIB가 맞는지?
		Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
	}
	
	@Test
	@DisplayName("구체 클래스만 있으면 CGLIB 사용") 
	void concreteProxy() {
		ConcreteService target = new ConcreteService();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.addAdvice(new TimeAdvice());
		ConcreteService proxy = (ConcreteService)proxyFactory.getProxy();
		
		log.info("targetClass = {}", target.getClass());
		log.info("proxyzClass = {}", proxy.getClass());
		
		proxy.call();
		
		// ProxyFactory를 만들면 검증 할 수 있는 것들
		// 1. Proxy가 맞는지?
		Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		// 2. JDK 동적 프록시가 맞는지?
		Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
		// 3. CGLIB가 맞는지?
		Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
	}
	
	@Test
	@DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
	void proxyTargetClass() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		
		proxyFactory.setProxyTargetClass(true);
		// true라면 인터페이스가 있어도 CGLIB로 만든다.
		// 최근 스프링 부트에서는 true가 기본값으로 되어있다.
		
		proxyFactory.addAdvice(new TimeAdvice());
		ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();
		
		log.info("targetClass = {}", target.getClass());
		log.info("proxyzClass = {}", proxy.getClass());
		
		proxy.save();
		
		// ProxyFactory를 만들면 검증 할 수 있는 것들
		// 1. Proxy가 맞는지?
		Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		// 2. JDK 동적 프록시가 맞는지?
		Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
		// 3. CGLIB가 맞는지?
		Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
	}
}
