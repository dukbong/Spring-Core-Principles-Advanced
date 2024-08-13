package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();

        TimeInvocastionHandler hanler = new TimeInvocastionHandler(target);

        // 어디에 생성될지, 어떤 인터페이스 기반으로 Proxy를 만들지, 핸들러(로직)
        // JDK 동적 프록시는 인터페이스 기반이기 때문에 인터페이스가 필수다.
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, hanler);

        proxy.call();
        // TimeInvocastionHandler에 있는 invoke 실행
        
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();

        TimeInvocastionHandler hanler = new TimeInvocastionHandler(target);

        // 어디에 생성될지, 어떤 인터페이스 기반으로 Proxy를 만들지, 핸들러(로직)
        // JDK 동적 프록시는 인터페이스 기반이기 때문에 인터페이스가 필수다.
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, hanler);

        proxy.call();
        // TimeInvocastionHandler에 있는 invoke 실행

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }
}
