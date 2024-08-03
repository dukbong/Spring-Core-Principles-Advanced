Java에서 기본적으로 제공하는 ThreadLocal은 동시성 문제를 해결하는 가장 기본적인 방법 중 하나입니다.
- ThreadLocal은 내부적으로 Map 자료 구조를 통해 각 Thread 별로 데이터를 저장하고 불러오기 때문에 각 Thread는 자신만의 데이터를 독립적으로 가질 수 있습니다.

---
### Thread와 ThreadPool
Thread는 하나의 프로세스 내에서 실행 단위를 뜻합니다.

Spring을 통해 웹 개발을 할 때 일반적으로 ThreadPool을 통해 Thread를 관리합니다.

이러한 상황에서 ThreadLocal을 사용할 경우, 몇 가지 주의해야 할 점이 있습니다.

---
### 주의사항 1. ThreadPool 재사용 문제
ThreadPool에서는 Thread가 응답 후 사라지는 것이 아니고 다시 ThreadPool로 반환됩니다.

예를 들어 사용자 A가 요청을 보낼 때 ThreadPool에서 Thread-A가 할당되어 작업을 수행하고 ThreadLocal을 통해 특정 값을 저장했다고 가정해 봅시다.

이 저장된 값은 오직 Thread-A에서만 조회 및 변경이 가능합니다.

그러나 작업이 완료된 후 Thread-A는 ThreadPool로 반환되고 ThreadLocal에 저장된 값이 제거되지 않은 상태로 남아 있습니다.

다음 사용자 B가 요청을 보낼 때 ThreadPool에서 다시 Thread-A가 할당된다면 사용자 B는 사용자 A가 ThreadLocal에 저장한 값을 보게 되는 문제가 발생할 수 있습니다.

또한 경우에 따라 사용자가 사용하지 않는 값이 ThreadLocal에 저장되어 있어서 객체에 저장되므로 메모리 누수 문제가 발생할 수 있습니다.

**예제**) ThreadLocal의 값을 제거하지 않은 경우
```java
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
				log.info("[{}] {}", Thread.currentThread().getName(), threadLocal.get());
			});
		}
		executorService.shutdown();
	}
}
```

**출력**) ThreadLocal의 값을 제거하지 않은 경우
```
[pool-2-thread-1] A
[pool-2-thread-2] B
[pool-2-thread-1] A
[pool-2-thread-2] B
```
이러한 문제를 방지하기 위해서 Thread 작업이 끝난 후 remove()를 통해 ThreadLocal 값을 명시적으로 제거해야 합니다.

**예제**) ThreadLocal의 값을 제거한 경우
```java
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
```
**출력**) ThreadLocal의 값을 제거한 경우
```
[pool-1-thread-1] A
[pool-1-thread-2] B
[pool-1-thread-1] C
[pool-1-thread-2] D
```
---
### 주의사항 2. 메모리 사용 문제
만약 ThreadPool을 사용하지 않고 매번 새로운 Thread를 생성하는 환경에서 ThreadLocal을 사용하게 되면 많은 양의 메모리를 소비하게 됩니다.

ThreadLocal에는 각 Thread마다 저장되기 때문에 Thread가 많아질수록 메모리 사용량이 급증하게 됩니다.

이는 곧 성능 저하 문제를 유발할 수 있습니다.

---
### 결론
ThreadLocal을 사용할 때는 반드시 사용이 끝난 후 remove() 메서드를 호출하여 데이터를 명시적으로 제거해야 합니다.

그렇지 않으면 메모리 누수와 사용자 데이터의 불필요한 공유 문제가 발생할 수 있습니다.

이를 통해 스레드 풀의 재사용으로 인한 데이터 누출과 메모리 낭비를 방지할 수 있습니다.
