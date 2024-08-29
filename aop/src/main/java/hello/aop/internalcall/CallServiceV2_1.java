package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2_1 {

    private final ApplicationContext applicationContext;

    public CallServiceV2_1(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void external() {
        log.info("call external");
        CallServiceV2_1 callServiceV2_1 = applicationContext.getBean(CallServiceV2_1.class);
        callServiceV2_1.internal();
    }

    public void internal(){
        log.info("call internal");
    }
}
