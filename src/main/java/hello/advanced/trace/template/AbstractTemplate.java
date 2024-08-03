package hello.advanced.trace.template;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;

public abstract class AbstractTemplate <T> {
	
	private final LogTrace trace;
	
	public AbstractTemplate(LogTrace trace) {
		this.trace = trace;
	}
	
	// 반환 타입이 다 다르기 때문에 제네릭으로 처리
	// - 반환 타입을 객체 생성 시점까지 미루고 객체 생성시 지정되도록 만든다.
	public T execute(String message) {
		TraceStatus status = null;
		try {
			status = trace.begin(message);
			
			T result = call();
			
			trace.end(status);
			return result;
		} catch(Exception e) {
			trace.exception(status, e);
			throw e;
		}
	}

	protected abstract T call();
}
