package hello.proxy.pureproxy.decorator;

import org.junit.jupiter.api.Test;

import hello.proxy.pureproxy.decorator.code.Component;
import hello.proxy.pureproxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureproxy.decorator.code.MessageDecorator;
import hello.proxy.pureproxy.decorator.code.RealComponent;
import hello.proxy.pureproxy.decorator.code.TimeDecorator;

public class DecorationPatternTest {

	@Test
	void noDecorator() {
		Component component = new RealComponent();
		DecoratorPatternClient client = new DecoratorPatternClient(component);
		client.execute();
	}
	
	@Test
	void decorator() {
		Component component = new RealComponent();
		Component messageDecorator = new MessageDecorator(component);
		DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
		client.execute();
	}
	
	@Test
	void decorator2() {
		Component component = new RealComponent();
		Component messageDecorator = new MessageDecorator(component);
		Component timeDecorator = new TimeDecorator(messageDecorator);
		DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
		client.execute();
	}
}
