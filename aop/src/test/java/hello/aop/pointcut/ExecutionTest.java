package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // method name, param type
    }

    @Test
    void printMethod() {
        // helloMethod = public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod = {}", helloMethod);
    }

    @Test
    @DisplayName("정확한 포인트컷으로 찾기")
    void exactMatch() {
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("최대한 생략하여 찾기")
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드이름을 지정하여 찾기")
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패턴 형식으로 메서드이름을 지정하여 찾기")
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패턴 형식으로 메서드이름을 지정하여 찾기")
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("없는 이름으로 찾아보기")
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("정확한 패키지 명으로 찾기")
    void packageExactMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("*을 사용하기")
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 매칭 실패하는 경우 [그냥 작성 시 서브 패키지를 확인하지 않는다.]")
    void packageExactMatchFalse() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        // 실패하는 이유
        // MemberServiceImpl class는 hello.aop.member 패키지 안에 있다.
        // 포인트컷은 서브 패키지는 이런식으로 작성시 서브 패키지를 확인하지 않는다.
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("서브 패키지 확인하는 경우")
    void packageExactMatchSubPackage1() {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("서브 패키지 확인하는 경우")
    void packageExactMatchSubPackage2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 타입 매칭")
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 타입 매칭")
    void typeExactMatchSupperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); // 부모타입
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); // 자식타입
    }

    @Test
    @DisplayName("타입 타입 매칭")
    void typeExactMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.internal(..))"); // 부모타입
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class); // 부모에 없는 메소드
        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue(); // 자식타입
    }

    @Test
    @DisplayName("타입 타입 매칭")
    void typeMatchNoSupperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.internal(..))"); // 부모타입
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class); // 부모에 없는 메소드
        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse(); // 자식타입
        // superType으로 매칭을 할때는 부모 타입에 있는 메소드만 포인트컷으로 잡힌다.
    }

    @Test
    @DisplayName("파라미터 매칭 [파라미터가 String이다.]")
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); // 자식타입
    }

    @Test
    @DisplayName("파라미터 매칭 [파라미터 없다]")
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse(); // 자식타입
    }

    @Test
    @DisplayName("파라미터 매칭 [파라미터가 1개 있다. 하지만 모든 타입 허용]")
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); // 자식타입
    }

    @Test
    @DisplayName("파라미터 매칭 [없거나, 1개 거나, n개 거나 그러면서 모든 타입 허용]")
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); // 자식타입
    }

    @Test
    @DisplayName("파라미터 매칭 [String 타입으로 시작하고, 개수는 무관하고, 모든 타입 허용]")
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); // 자식타입
    }
}
