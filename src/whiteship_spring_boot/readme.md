## 스프링부트 시작 (1부 대략적인 대용 요약)
- `@SpringBootApplication`과 main에 `SpringApplication.run()` 메소드 하나로 어떻게 톰캣이 실행되는걸까?
    - 추가한 의존성은 얼마 없는데 어떻게 수 많은 의존성이 들어왔는가?
    - `Dispatcher Servlet`, 리스너에 어떤 `WebApplicationContext`를 사용할지, `Bean` 등 설정을 하나도 안했다. (`@EnableAutoConfiguration`과 관련) 
- `mvn package`를 실행하면 패키지를 패키징한다. 웹 프로젝트가 아닌 메이븐 기반의 자바 프로젝트기 때문에 `jar` 파일이 생기고, 이것을 실행하면 웹 애플리케이션이 동작한다.

#### 스프링부트 프로젝트 구조
- 스프링부트 프로젝트 구조는 메이븐 기반 자바 기본 프로젝트와 동일하다.
- 작업하는 최상위 패키지에 @SpringBootApplication 붙인 클래스(메인 클래스)를 위치시키는 것이 좋다.
    - `@ComponentScan`을 여기서부터 하는데, 이 아래로 부터 Bean을 등록한다.
    - 만약 java 디렉토리 바로 밑에 두면 모든 패키지를 스캔해버리게 된다. -> 불필요한 스캔 발생
  
--- 
## 스프링 부트 원리
### 의존성 관리 
### 자동 설정 
### 내장 웹 서버 