### AOP 사용 시 중요한 사항
- Advice는 기본적으로 순서를 보장하지 않는다.
- 순서를 지정하고 싶다면 @Aspect 적용 단위로 @Order를 적용해야한다.
- @Order는 Advice단위가 아닌 클래스 단위로 적용할 수 있다.
- 만약 하나의 @Aspect 안에서 여러 Advice가 있다면 순서를 보장 받을 수 없다.
- 순서를 보장 받기 위해서는 별도로 분리가 필요하다.


### Pointcut
- execution(접근제어자? 반환타입 선언타입?메서드이름(파라미트) 예외?)
    - ?는 생략할 수 있다.
    - 선언타입은 패키지와 클래스명을 포함한다.
    - .  : 정확한 패키지 (하위 패키지는 신경쓰지 않는다.)
    - .. : 해당 패키지 및 하위 패키지까지 신경쓴다.

- within
    - 주의사항 : 부모타입을 지정하면 안되고 정확한 타입을 지정해줘야한다.

---
### args와 execution의 차이
execution은 메서드의 시그니처로 판단 (정적)
args는 런타임에 전달된 인수로 판단 (동적)
- args의 경우 부모타입으로도 찾을 수 있다.

---
### @target, @within
@target으로 자식 클래스에 적용하면 부모 클래스도 같이 어드바이스를 적용한다.
@within으로 자식 클래스에 적용하면 자식 클래스만 어드바이스를 적용한다.

### annotation
메소드가 주어진 annotation을 가지고 있는 조인 포인트를 매칭한다.
 
### bean
spring 전용 포인트 컷 지시자, 빈의 이름으로 지정한다.

### this : 스프링 컨테이너에 등록된 프록시 객체를 전달 받는다.
### target : 실제 대상 객체를 전달 받는다.
### @target, @within : 타입의 어노테이션을 전달 받는다.
### @annotation : 메소드의 어노테이션을 전달 받으며 value() 메서드를 상요하여 값을 추출 할 수 있다.