package hello.advanced.trace.threadlocal;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalServiceTest {

	private ThreadLocalService threadLocalService = new ThreadLocalService();
	
	@Test
	void field() {
		log.info("main start");
		
		// 1. Thread가 실행할 로직 정의
		Runnable userA = () -> {
			threadLocalService.logic("userA");
		};
		Runnable userB = () -> {
			threadLocalService.logic("userB");
		};
		// 2. Thread 생성
		Thread threadA = new Thread(userA);
		threadA.setName("thread-A");
		Thread threadB = new Thread(userB);
		threadB.setName("thread-B");
		
		threadA.start();
		sleep(100); // 스레드 로컬을 통해 통시성 문제 해결
		threadB.start();
		sleep(3000); // 메인 쓰레드 종료 지연
		log.info("main exit");
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
