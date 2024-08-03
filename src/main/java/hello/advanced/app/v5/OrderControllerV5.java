package hello.advanced.app.v5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;

@RestController
public class OrderControllerV5 {
	private final OrderServiceV5 orderService;
	private final TraceTemplate template;
	
	// @AutoWired : 생성자가 하나일때는 생략 가능하다.
	public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
		this.orderService = orderService;
		this.template = new TraceTemplate(trace);
	}

	@GetMapping("/v5/request")
	public String request(@RequestParam("itemId") String itemId) {
		return template.execute("OrderController.request()", new TraceCallback<>() {

			@Override
			public String call() {
				orderService.orderItem(itemId);
				return "ok";
			}
		});
	}
}