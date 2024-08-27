package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

public class ArgsTest {

    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getDeclaredMethod("hello", String.class);
    }

    private AspectJExpressionPointcut poincut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void args() {
        Assertions.assertThat(poincut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("args(Object)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("args()")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(poincut("args(..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("args(*)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("args(String, ..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsVsExecution() {
        Assertions.assertThat(poincut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("args(java.io.Serializable)") // Serializable는 String이 내부에서 구현한 인터페이스이다.
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("args(Object)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();

        // Execution
        Assertions.assertThat(poincut("execution(* *(String))")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(poincut("execution(* *(java.io.Serializable))")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(poincut("execution(* *(Object))")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
