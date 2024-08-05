package hello.advanced.trace.strategy;

import org.junit.jupiter.api.Test;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextV1Test {
	@Test
	void strategyV0() {
		logic1();
		logic2();
	}

	private void logic1() {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		log.info("비즈니스 로직1 실행");
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime = {}", resultTime);
	}

	private void logic2() {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		log.info("비즈니스 로직2 실행");
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime = {}", resultTime);
	}
	
	/**
	 * 전략 패턴 사용
	 */
	@Test
	void strategyV1() {
		StrategyLogic1 strategyLogic1 = new StrategyLogic1();
		ContextV1 context1 = new ContextV1(strategyLogic1);
		context1.execute();
		StrategyLogic2 strategyLogic2 = new StrategyLogic2();
		ContextV1 context2 = new ContextV1(strategyLogic2);
		context2.execute();
	}
	/**
	 * 전략 패턴 사용 ( 익명 클래스 )
	 */
	@Test
	void strategyV2() {
		Strategy strategyLogic1 = new Strategy() {
			
			@Override
			public void call() {
				log.info("비즈니스 로직 1 실행");
			}
		};
		ContextV1 context1 = new ContextV1(strategyLogic1);
		context1.execute();
		Strategy strategyLogic2 = new Strategy() {
			
			@Override
			public void call() {
				log.info("비즈니스 로직 2 실행");
			}
		};
		ContextV1 context2 = new ContextV1(strategyLogic2);
		context2.execute();
		Strategy strategyLogic3 = () -> {
			log.info("비즈니스 로직 3 실행");
		};
		ContextV1 context3 = new ContextV1(strategyLogic3);
		context3.execute();
	}
	/**
	 * 전략 패턴 사용 ( 익명 클래스 )
	 */
	@Test
	void strategyV3() {
		ContextV1 context1 = new ContextV1(new Strategy() {
			@Override
			public void call() {
				log.info("비즈니스 로직 1 실행");
			}
		});
		context1.execute();
		
		ContextV1 context2 = new ContextV1(new Strategy() {
			@Override
			public void call() {
				log.info("비즈니스 로직 2 실행");
			}
		});
		context2.execute();
		
		ContextV1 context3 = new ContextV1(() -> {
			log.info("비즈니스 로직 3 실행");
		});
		context3.execute();
	}
	/**
	 * 전략 패턴 사용 ( 람다식 )
	 */
	@Test
	void strategyV4() {
		ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직 1 실행"));
		context1.execute();
		ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직 2 실행"));
		context2.execute();
	}
}
