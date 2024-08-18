package hello.proxy;

import hello.proxy.config.v1_proxy.ConcreteProxyConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV1;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;


// @Import(AppV1Config.class)
// @Import({AppV1Config.class, AppV2Config.class})
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(DynamicProxyFilterConfig.class)
//@Import(ProxyFactoryConfigV1.class)
@Import(ProxyFactoryConfigV2.class)
// 2. Bean을 수동 등록하는 곳은 hello.proxy.config 파일이기 떄문에 스캔하지 않지만 Bean을 등록하기 위해 강제로 읽게 시킨다.
@SpringBootApplication(scanBasePackages = "hello.proxy.app")
// 1. scanBasePackages를 설정하지 않은면 hello.proxy 하위 모든걸 스캔하지만 이렇게 지정해주면 지정된 곳 하위에 있는것들만 스캔한다.
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

}
