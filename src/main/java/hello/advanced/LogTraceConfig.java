package hello.advanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.advanced.trace.logtrace.FieldLogTrace;
import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.logtrace.ThreadLocalLogTrace;

@Configuration
public class LogTraceConfig {

	@Bean
	public LogTrace logTrace() {
		// return new FieldLogTrace(); // 인스턴스 변수를 사용 ( 동시성 문제 O ) 
		return new ThreadLocalLogTrace(); // ThreadLocal 사용 ( 동시성 문제 X )
	}
}
