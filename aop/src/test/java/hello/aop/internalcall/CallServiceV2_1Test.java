package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV2_1Test {

    @Autowired
    private CallServiceV2_1 CallServiceV2_1;

    @Test
    void external() {
        // log.info("target = {}", callServiceV0.getClass());
        CallServiceV2_1.external();
    }

}