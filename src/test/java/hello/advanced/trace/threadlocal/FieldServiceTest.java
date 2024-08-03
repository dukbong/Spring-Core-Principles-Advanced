package hello.advanced.trace.threadlocal;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldServiceTest {

	private FieldService fieldService = new FieldService();
	
	@Test
	void field() {
		log.info("main start");
		
		// 1. Thread가 실행할 로직 정의
		Runnable userA = () -> {
			fieldService.logic("userA");
		};
		Runnable userB = () -> {
			fieldService.logic("userB");
		};
//		람다 표현식을 풀어서 쓴것.
//		Runnable userAA = new Runnable() {
//			
//			@Override
//			public void run() {
//				fieldService.logic("userAA");
//			}
//			
//		};
		// 2. Thread 생성
		Thread threadA = new Thread(userA);
		threadA.setName("thread-A");
		Thread threadB = new Thread(userB);
		threadB.setName("thread-B");
		
		threadA.start();
		// sleep(2000); // 동시성 문제 X 
		sleep(100); // 동시성 문제 O
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
