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
### 의존성 관리 이해
#### 목표 : 스프링부트가 어떻게 수많은 의존성을 가져왔는지, 왜 좋은지 이해
의존성을 2개, 버전도 명시하지 않았다. (이것이 스프링부트가 제공해주는 의존성 관리 기능)
- `<parent>` 를 타고 들어가면 의존성들이 나오고 artifactId를 타고 들어가보면 버전이 명시되어 있다.
  - 직접 관리해야할 의존성의 수가 줄어든다. (관리해야할 일이 줄어든다.)
  - 만약 일일이 다 관리해주어야 하면, 수 많은 의존성이 어떤 의존성과 호환되는지 알 수 없다.
- 그러나 스프링부트에서 관리되지 않는 의존성들은 버전을 명시해야 한다.

### 의존성 관리 응용
#### 목표 : 스프링부트가 지원하는 의존성 관리기능 활용 방법 이해
의존성을 추가하는 방법
- SpringDataJPA 추가 - starter 에서 제공
    - 왼쪽 라인넘버에 화살표가 나온다. 관리되는 의존성이라는 뜻
    - 화살표가 안뜨면 관리되고 있는 의존성이 아니라는 뜻
> 의존성을 검색하고 싶을 땐 mvn repository 라는 웹사이트에서.
 
스프링부트에서 관리해주는 의존성도 버전을 명시할 수 있다.
- 스프링 버전을 바꿔보자. (현재 5.3.3)
    - parent -> dependencies 를 타고 들어가면 <properties>에 스프링 버전이 일괄적으로 명시되어 있다.
    - `<spring-framework.version>5.3.3</spring-framework.version>`
    - 이것을 pom.xml에 properties로 재정의한다.
> 자바 버전도 마찬가지로 properties에 등록해서 바꿀 수 있다.   
> 다른 설정도 dependencies에서 찾아서 마찬가지로 바꿀 수 있다.

<br> 
<br>

### 자동 설정 이해
#### 실행하면 웹 애플리케이션이 여러가지 설정이 읽히며 뜬다. 그 이유는?
- 중 하나는 `@SpringBootApplication` 에 숨어있는 `@EnableAutoConfiguration` 때문이다.
- `@SpringBootApplication`은 다음으로 바꿀 수 있다.
```java
@SpringBootConfiguration
@ComponentScan
@EnableAutoConfiguration
```
사실상 빈을 2번 등록한다.
- `@ComponentScan `으로 등록하고 
- `@EnableAutoConfiguration `으로 읽어온 빈들을 다시 등록한다. 
- 즉 자동설정을 하지 않을 거라면 `@EnableAutoConfiguration` 을 지워도 스프링 부트는 동작한다.
- (`@SpringBootConfiguration` 도 `@Configuration` 나 마찬가지다.)
  
> @EnableAutoConfiguration 에서 자동 생성해주는 빈(ServletWebServerFactory)을 못 만들었기 때문에 현재 실행이 안된다.  
> 실행을 위해선 설정을 바꿔야 한다.  
> SpringApplication을 스태틱이 아닌, 인스턴스 만들면 커스텀이 가능해진다.
> 타입을 NONE으로 주면 된다. (그러나 웹 애플리케이션은 아니다.)
```
SpringApplication application = new SpringApplication(Application.class);
application.setWebApplicationType(WebApplicationType.NONE);
application.run(args);
```

`@ComponentScan` 은 `@Component` 어노테이션이 붙은 클래스들을 스캔해서 빈으로 등록한다.
    - 하위 패키지에 있는 클래스들만. (다른 패키지는 읽지 않음)
    - 자기 자신에 @Configuration 을 붙이면 마찬가지로 빈이 된다.
`@EnableAutoConfiguration` 은 다음을 설정한다.
    - ~-autoconfigure 의존성 -> META-INF -> spring.factories 에서 확인할 수 있다. (키 값이 정의되어 있다.)
    - 키 값을 따라가서 확인해보면 마찬가지로 @Configuration 이 붙은 설정 클래스들이다.
    - Conditional로 시작하는 어노테이션이 많은데, 조건에 따라 설정이 달라진다.

정리
- `@EnableAutoConfiguration` 하나로 `spring.factories` 에 있는 설정들이 조건에 따라 적용되어 빈들이 생성된다.  
- 내장 톰캣을 사용해서 웹 애플리케이션이 구동된다.  
  (서블릿 컨테이너에서 서블릿 애플리케이션 하나가 돌아가게 된다.)


### 자동 설정 구현 1 (Starter와 AutoConfigure)
- Spring-Boot-**Autoconfigure** 모듈 : 자동 설정
- Spring-Boot-**Starter** 모듈 : 필요한 의존성 정의
자동설정도 스타터에 넣어서 하나만 만든다. (새로운 프로젝트 생성)
  
#### pom.xml
- spring-boot-autoconfigure 
- spring-boot-autoconfigure-processor
- 둘의 버전관리를 위해 `<dependencyManagement>` 

#### 과정
- Holoman 클래스와 이것을 자동설정 해주는 클래스 생성   
  (원래 둘은 다른 프로젝트에 있는게 흔하지만 편의상 하나의 프로젝트로)
- `META-INF/spring.factories`를 resources 안에 생성  
  (스프링 부트에 특화된 파일이 아니라, 스프링에 있는 여러 용도로 활용되는 것)
- 그 안에 자동 설정파일 추가   
  추가한 키가 @EnableAutoConfiguration 에 의해 읽혀서 빈도 추가 될 것
- 이 프로젝트를 빌드하고 인스톨해서 다른 프로젝트에서 사용할 수 있도록 해야한다. 
    `mvn install` : 프로젝트를 빌드해서 jar 파일 생성된 것을 다른 메이븐 프로젝트에서도 사용할 수 있또록 로컬 레포지토리에 설치한다.
- 이제 사용하는 쪽에서 다음을 의존성 추가하면 된다.
```
<groupId>org.example</groupId>
<artifactId>me.soongjamm</artifactId>
<version>1.0-SNAPSHOT</version>
```
- 추가한 Holoman 이라는 빈이 있는지 확인하기 위해 Runner 를 만들어준다.
아규먼트 인자 받아서 무언가 하고싶을 때. 스프링부트 애플리케이션이 만들어지고 띄웠을 때 자동으로 실행되는 빈을 만들고 싶을 때 ApplicationRunner 를 구현할 수 있다.
  

- 문제가 있는데 명시적으로 빈을 등록하면 무시된다.
    - 스프링 부트에서 빈을 등 두번.
    - 컴포넌트 스캔으로 등록하는게 먼저다.
    - 두번째가 오토 컨피규레이션 (그래서 이게 덮어쓴다.)