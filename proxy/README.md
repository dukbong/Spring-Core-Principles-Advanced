### Proxy 

Client가 요청한 결과를 Server에 직접 요청하는 것이 아니라 대리자(Proxy)를 통해 간접적으로 서버에 요청할 수 있다.
- Clinet 입장에서 Server를 호출하는 것인지 Proxy를 호출하는 것인지 몰라야 한다.

#### Proxy의 주요 기능
1. 접근 제어 ( Proxy Pattern )
   - 권한에 따른 접근 차단
   - 캐싱
   - 지연 로딩
3. 부가 기능 추가 ( Decorator Pattern )
   - 원래 Server가 제공하는 기능에 더해서 부가 기능 수행
   - 요청 값 혹은 응답 값을 중간에서 변형

#### Proxy 적용 시 중요한 사항
Spring Container에 실제 객체가 아닌 Proxy 객체를 등록해서 관리해야 하며, Proxy 객체는 내부에서 실제 객체를 참조하고 있어야한다.
