## 스프링부트 시작 (1부 대략적인 대용 요약 : 앞으로 공부할 내용)
- `@SpringBootApplication`과 main 에 있는 `SpringApplication.run()` 메소드 만으로 어떻게 톰캣이 실행되는걸까?
- 추가한 의존성은 얼마 없는데 어떻게 수 많은 의존성이 들어왔는가?
- `Dispatcher Servlet`과 리스너에 어떤 `WebApplicationContext`를 사용할지, `Bean` 등 설정을 하나도 안했다.   
  - `@EnableAutoConfiguration`과 관련) 
- `mvn package`를 실행하면 패키지를 패키징한다.   
  - 이 경우 웹 프로젝트가 아닌, 메이븐 기반의 **자바 프로젝트**가 되기 때문에 `jar` 파일이 생기고, _이것을 실행하면_ 비로소 웹 애플리케이션이 동작한다.

#### 스프링부트 프로젝트 구조
- 스프링부트 프로젝트 구조는 메이븐 기반 자바 기본 프로젝트와 동일하다.
- 작업하는 최상위 패키지에 `@SpringBootApplication` 붙인 클래스(메인 클래스)를 위치시키는 것이 좋다.
    - `@ComponentScan`을 여기서부터 하는데, 이 아래로 부터 Bean 을 등록한다.
    - 만약 java 디렉토리 바로 밑에 두면 모든 패키지를 스캔해버리게 된다. 
      - 원하지않는 패키지까지 스캔하여 불필요한 스캔이 발생한다.
--- 
### [의존성 관리](https://github.com/soongjamm/java-study/blob/whiteship-spring-boot-lecture/src/whiteship_spring_boot/readmes/c3/dependency_management.md)
### [자동 설정](https://github.com/soongjamm/java-study/blob/whiteship-spring-boot-lecture/src/whiteship_spring_boot/readmes/c3/autoconfigure.md) 
코드
- [Starter & AutoConfigure](https://github.com/soongjamm/java-study/commit/f8156f380dbb209d43d87e4cc3f431ce09987ccc#diff-1b67087e602bd2d09edd84e069362056fe8df50c6347012362f32d866bc37844)
- [@ConfigurationProperties](https://github.com/soongjamm/java-study/commit/ee794a5e530e5725bce171d2cf781df152a8bfa5#diff-1b67087e602bd2d09edd84e069362056fe8df50c6347012362f32d866bc37844)
### [내장 웹 서버](https://github.com/soongjamm/java-study/blob/whiteship-spring-boot-lecture/src/whiteship_spring_boot/readmes/c3/embeded.md) 
코드
- [서블릿 컨테이너와 포트 바꾸기, HTTPS/SSL 적용, HTTP 컨넥션 생성](https://github.com/soongjamm/java-study/commit/8d74d626dbc46584f9661fcfcef4b49299351a05#diff-1b67087e602bd2d09edd84e069362056fe8df50c6347012362f32d866bc37844)
- [HTTP2 적용](https://github.com/soongjamm/java-study/commit/c59f84d74fd8bcfec17fa8194a479fd89d20d823#diff-1b67087e602bd2d09edd84e069362056fe8df50c6347012362f32d866bc37844)
### [독립적으로 실행가능한 JAR](https://github.com/soongjamm/java-study/blob/whiteship-spring-boot-lecture/src/whiteship_spring_boot/readmes/c3/JAR_independent.md)