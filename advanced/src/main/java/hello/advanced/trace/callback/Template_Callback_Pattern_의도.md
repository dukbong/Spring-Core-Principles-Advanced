### Template Callback Pattern 정의
Strategy Pattern에서 Context가 Template 역할(변하지 않는 부분)을 하고 Strategy가 Callback(변하는 부분)으로 전달되는 것입니다.
- Spring에서 이름이 XXXTemplate 이런 식이라면 Template Callback Pattern을 사용하는 것이다.

### Callback 정의
Callback은 다른 코드의 매개변수로 전달되는 실행 가능한 코드를 의미합니다. 
- 주로 람다, 인터페이스 구현 등을 통해 전달되며 특정 시점에 호출되어 작업을 수행합니다.
