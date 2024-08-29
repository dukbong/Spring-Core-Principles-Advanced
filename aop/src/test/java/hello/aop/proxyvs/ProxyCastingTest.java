package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory factory = new ProxyFactory(target);
        factory.setProxyTargetClass(false); // JDK 동적 프록시

        // Proxy를 interface로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) factory.getProxy();

        // JDK 동적 프록시는 interface를 기반으로 만들기 때문에 MemberService의 자식인 MembmerServiceImpl로 캐스팅 할 경우 오류가 발생한다 (다형성)
        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });

    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory factory = new ProxyFactory(target);
        factory.setProxyTargetClass(true); // JDK 동적 프록시

        // Proxy를 interface로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) factory.getProxy();

        // CGLIB는 구현 클래스를 기반으로 만들어지기 때문에 가능하다.
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
