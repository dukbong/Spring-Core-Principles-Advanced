package hello.proxy.advisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;

public class MultiadviosrTest {

	@Test
	@DisplayName("여러 프록시")
	void multiAdvisorTest1() {
		// client -> proxy2(advisor2) -> proxy1(advisor1) -> target
		
		// 프록시 1 생성
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory1 = new ProxyFactory(target);
		DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
		proxyFactory1.addAdvisor(advisor1);
		ServiceInterface proxy1 = (ServiceInterface)proxyFactory1.getProxy();
		
		// 프록시 2 생성
		ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
		DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
		proxyFactory2.addAdvisor(advisor2);
		ServiceInterface proxy2 = (ServiceInterface)proxyFactory2.getProxy();
		
		// 실행
		proxy2.save();
	}
	
	@Slf4j
	static class Advice1 implements MethodInterceptor{

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			log.info("advice1 호출");
			return invocation.proceed();
		}
		
	}
	
	@Slf4j
	static class Advice2 implements MethodInterceptor{
		
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			log.info("advice2 호출");
			return invocation.proceed();
		}
		
	}
	
	@Test
	@DisplayName("하나의 프록시 여러개의 어드바이저")
	void multiAdvisorTest2() {
		// client -> proxy -> advisor2 -> advisor1 -> target
		DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
		DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
		
		// 프록시 1 생성
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		
		// 순서대로 작성해줘야한다.
		proxyFactory.addAdvisor(advisor2);
		proxyFactory.addAdvisor(advisor1);
		
		ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
		
		proxy.save();
		
	}
}
