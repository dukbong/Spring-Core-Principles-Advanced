package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


// @Import(AppV1Config.class)
@Import({AppV1Config.class, AppV2Config.class})
// 2. Bean을 수동 등록하는 곳은 hello.proxy.config 파일이기 떄문에 스캔하지 않지만 Bean을 등록하기 위해 강제로 읽게 시킨다.
@SpringBootApplication(scanBasePackages = "hello.proxy.app")
// 1. scanBasePackages를 설정하지 않은면 hello.proxy 하위 모든걸 스캔하지만 이렇게 지정해주면 지정된 곳 하위에 있는것들만 스캔한다.
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}


}
