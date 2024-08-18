package hello.proxy.advisor;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdvisorTest {

	@Test
	void advisorTest1() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		
		// pointCut 과 advice 설정
		// Pointcut.TRUE는 모든 곳에 적용한다는 뜻이다.
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
		
		// 프록시 팩토리를 사용할 경우 advisor는 필수이다.
		// 하지만 addAdvice(어드바이스)를 해도 되는데 내부적으로 pointcut을 지정하지 않으면 Pointcut.TRUE가 들어간다.
		proxyFactory.addAdvisor(advisor);
		
		ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();
		
		proxy.save();
		proxy.find();
	}
	
	@Test
	@DisplayName("직접 만든 포인트컷")
	void advisorTest2() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		
		// pointCut 과 advice 설정
		// Pointcut.TRUE는 모든 곳에 적용한다는 뜻이다.
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
		
		// 프록시 팩토리를 사용할 경우 advisor는 필수이다.
		// 하지만 addAdvice(어드바이스)를 해도 되는데 내부적으로 pointcut을 지정하지 않으면 Pointcut.TRUE가 들어간다.
		proxyFactory.addAdvisor(advisor);
		
		ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();
		
		proxy.save();
		proxy.find();
	}
	
	static class MyPointcut implements Pointcut{

		@Override
		public ClassFilter getClassFilter() {
			return ClassFilter.TRUE;
		}

		@Override
		public MethodMatcher getMethodMatcher() {
			return new MyMethodMatcher();
		}
		
	}
	
	static class MyMethodMatcher implements MethodMatcher {
		
		private String matchName = "save";

		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			boolean result = method.getName().equals(matchName);
			log.info("포인트컷 호출 method = {}, class = {}", method.getName(), targetClass);
			log.info("포인트컷 결과 result = {}", result);
			return result;
		}

		@Override
		public boolean isRuntime() {
			// isRuntime이 true인 경우 아래에 잇는 matches를 호출하게 된다.
			// 아래 matches는 동적으로 넘어오는 매개변수를 판단 로직으로 사용가능하다.
			// 위에 matches는 정적이다.
			
			// false로 함으로써 이점은 정적 정보만 사용하기 때문에 스프링이 내부에서 캐싱을 통해 성능 향상이 가능하다.
			return false;
		}

		@Override
		public boolean matches(Method method, Class<?> targetClass, Object... args) {
			return false;
		}
		
	}
	
	@Test
	@DisplayName("스프링이 제공하는 포인트컷")
	void advisorTest3() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("save");
		
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
		proxyFactory.addAdvisor(advisor);
		
		ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();
		
		proxy.save();
		proxy.find();
	}
}
