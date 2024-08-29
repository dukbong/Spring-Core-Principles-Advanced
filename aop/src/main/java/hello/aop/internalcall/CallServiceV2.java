package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

    private final ObjectProvider<CallServiceV2> callServiceV1Provider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceV1Provider) {
        this.callServiceV1Provider = callServiceV1Provider;
    }

    public void external() {
        log.info("call external");
        CallServiceV2 callServiceV1 = callServiceV1Provider.getObject();
        callServiceV1.internal();
    }

    public void internal(){
        log.info("call internal");
    }
}
