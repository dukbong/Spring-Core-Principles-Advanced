package hello.advanced.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalRemoveTest {

	private ThreadLocal<String> threadLocal = new ThreadLocal<>();
	private String[] str = {"A", "B", "C", "D"};
	
	@Test
	void threadLocalRemoveO() {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		AtomicInteger index = new AtomicInteger(0);
		for(int i = 0; i < 4; i++) {
			int currentIndex = index.getAndIncrement();
			executorService.submit(() -> {
				if(threadLocal.get() == null) {
					threadLocal.set(str[currentIndex]);
				}
				log.info("[{}] {}", Thread.currentThread().getName(), threadLocal.get());
				threadLocal.remove(); // 사용 완료 후 ThreadLocal에 저장된 값 제거
			});
		}
		executorService.shutdown();
	}
}
