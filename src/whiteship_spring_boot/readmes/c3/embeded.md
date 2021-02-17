# 내장 웹 서버
### 내장 웹 서버 이해

스프링부트 메인메소드에서 Run 하면 웹 애플리케이션이 실행되니까, 이 자체가 웹서버인줄 오해할 수 있는데,   
**스프링부트 자체는 웹서버가 아니다!**

- 커스텀할때 이미 웹서버가 아닌체로 스프링부트를 실행해본 적이 있다.
- 스프링 부트는 내장 서블릿 컨테이너를 쉽게 사용할 수 있도록 해주는 툴일 뿐이다.  
> Tomcat, Netty, Undertow 등이 서버(서블릿 컨테이너)다.  
  의존성을 보면 톰캣이 들어와있다. (자바 코드로 직접 서버를 만들 수 있는 기능을 제공함)

_main 메소드에 작성해본 것과 비슷한(코드 참고)_ **톰캣 설정이 어딨길래, 스프링부트가 서블릿 컨테이너를 띄워주는 것인가?**
- 자동 설정과 관련이 있다.
- 자동 설정 덕분에 스프링부트 애플리케이션을 실행하면 톰캣이 만들어지고 (디스패처)서블릿이 추가가 되고, 웹앱도 뜨고 스프링 MVC 도 설정이 되는 것이다.
- `ServletWebServerFactoryAutoConfiguration` 가 서블릿 **웹 서버** 생성/등록을 한다.

**서블릿**은 어디서 등록하나?
- 서블릿 기반의 mvc 라면 `디스패처 서블릿`을 만들어야 한다.
- 디스패처 서블릿은 http 서블릿을 상속해서 만든 스프링 mvc 의 핵심 클래스다.
- `DispatcherServletAutoConfiguration` 는 디스패처 서블릿을 만든 후 _(어떤 서블릿 컨테이너든 상관없이)_ 서블릿 컨테이너에 등록하는 설정을 한다.

서블릿 웹서버 생성과 디스패처 서블릿 등록이 분리된 이유는?
- pom.xml 에 해주는 설정에 따라 서블릿 컨테이너는 달라질 수 있다.
- 서블릿은 변하지 않는다. 그래서 분리.

### 내장 웹 서버 응용 : 컨테이너와 포트

- 기본적으로 서블릿 기반 웹mvc 개발할 때 톰캣 사용. 의존성에 톰캣이 들어있음. (? 자동성절에 의해서.)
- 다른 컨테이너를 쓰려면?
    - 기존 톰캣 의존성을을 먼저 뺀다.
    - `jetty` 의존성을 추가한다 (다른 was 쓰려면 다른 의존성 추가)
      - 추가하지 않으면 웹 애플리케이션이 아니므로, 한번 실행하고 종료됌.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

**랜덤포트 주는법**
- properties 에서 `server.port=0` 으로 준다.
- 랜덤포트를 획득하는 best way는 `ApplicationListener<ServletWebServerInitializedEvent>` 를 빈으로 만든다
  - 웹서버가 생성이 되면 이벤트 리스너가 호출된다.
  - 이벤트에서 context 를 꺼내고 웹서버를 꺼내고 그를 통해 포트를 알 수 있다.


### 내장 웹 서버 응용 : HTTPS(+ SSL), HTTP2 적용

#### HTTPS (SSL)
- [SSL 인증서를 생성한다.](https://gist.github.com/keesun/f93f0b83d7232137283450e08a53c4fd)
  - HTTPS 로 접근 가능해진다.
  - HTTP 는 이제 사용할 수 없다.
- 그러나 HTTP 를 위한 커넥터를 따로 만들 수도 있다. (코드 참고)
  - properties 에 설정된 포트는 HTTPS 전용이고,
  - 새로 생성한 커넥터 빈에 있는 포트는 HTTP 전용이다.
  
#### HTTP2 
- **SSL 이 기본으로 적용되어 있어야 한다.**
- properties 에서 `server.http2.enabled=true` 만 하면 된다.
  - 그러나 컨테이너에 따라 제약사항이 다르다.
  - 예를들어 톰캣 8.x 버전은 설정이 많다.
  - 그러나 undertow / 톰캣 9에 자바9 면 설정이 필요없다.
  - 설정이 많은 경우 사용을 권장하지 않고, 설정이 필요없는 환경을 만드는 것을 권장한다.
- 컨테이너를 undertow 로 하고, curl 로 리퀘스트를 보내보면 HTTP2 로 응답이 오는 것을 볼 수 있다.

--- 
[생활코딩 HTTPS, SSL 강의 참고](https://opentutorials.org/course/228/4894)  
**HTTP** 
- Hypertext Transfer Protocol. 
- HTML 을 전송하기 위해 만들어진 통신 규약
- 스프링부트에서 톰켓이 사용하는 커넥터는 하나만 제공되는데 여기에 SSL 을 적용해준다. 
  - 모든 요청은 HTTPS 를 붙여서 해야 한다.
  - http 로 요청하면 bad request 뜬다.
    - 브라우저가 서버에 요청보내면 서버가 아까 만든 인증서를 보낸다. (키스토어에 들어있음)
    - 그러나 브라우저는 인증서의 pub-key 를 모르기 때문에 뜬다.
  - https 로 요청하면 빨간화면으로 한번 물어본다 (진짜 들어갈거냐)
    - 왜냐면 우리가 만든 인증서는 공인된 인증서가 아니기 때문에 브라우저가 모르는 인증서다.

**HTTPS(+ SSL)**
- 복잡한 메커니즘으로 동작하는 정보보호 수단.
- 따라하는건 어렵지 않지만, 문제가 생겼을 때 이것을 해결하기 어려울 수 있다.
- S는  Over Secure Socket Layer 의 약자. --> HTTP 보다 보안장치가 더 결합되어 있는 형태의 통신방식
  - 스니핑/스푸핑을 방지
  
- SSL 위에 HTTPS 가 있는 것이다. 
  - 인터넷 위에 웹이 있다.
  - 컴퓨터와 윈도우와 웹브라우저가 서로 계층적인 관계인 것과 같다.
  - SSL 통신 방법 위에 동작하는 서비스 중 하나가 HTTP 이고, HTTP 가 SSL 을 이용하면 HTTPS
--- 

**curl 옵션**
- -k : SSL 연결 및 서버의 인증서 확인.  
- -I (i) : 응답의 헤더만 가져온다.

---

## 질문
- 크롬  https  무시하고 넘어가기 안뜸.
> 크롬의경우 localhost에서 로드된 유효하지않은 인증서를 허용하는 방식으로 해결할 수 있습니다.  
크롬브라우저의경우 아래의 url을 입력하신후  
chrome://flags/#allow-insecure-localhost  
젤위에 Allow invalid cetificates for resources loaded from localhost를 Disabled에서 Enabled로 변경해주시면 됩니다.  
