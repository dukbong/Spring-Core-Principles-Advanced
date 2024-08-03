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
	void threadLocalRemoveX() {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		AtomicInteger index = new AtomicInteger(0);
		for(int i = 0; i < 4; i++) {
			int currentIndex = index.getAndIncrement();
			executorService.submit(() -> {
				if(threadLocal.get() == null) {
					threadLocal.set(str[currentIndex]);
				}
				log.info("threadLocalRemoveX >> [{}] {}", Thread.currentThread().getName(), threadLocal.get());
			});
		}
		executorService.shutdown();
	}
	
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
				log.info("threadLocalRemoveO >> [{}] {}", Thread.currentThread().getName(), threadLocal.get());
				threadLocal.remove();
			});
		}
		executorService.shutdown();
	}
	
}
