# 독립적으로 실행 가능한 JAR
우리는 IDE 에서 웹서버 실행하지만, **도커 이미지로 만들거나 배포할 때 jar 로 패키징하고 실행하는게 유용**하다.  
- 이것도 스프링부트의 유용한 특징중 하나다.  

#### maven을 이용해서 jar 패키징 하는 방법
- mvn clean  
- mvn package [--D skipTests]  
- java -jar target/spring-boot-lecture-0.0.1-SNAPSHOT.jar  

#### 수 많은 의존성들이 어디로 갔을까?  
패키징해서 생긴 jar 파일안에 모두 들어있다.
- jar 를 복사해서 다른 폴더에 놓고 압축을 풀어보자.
    - unzip -q spring-boot-lecture-0.0.1-SNAPSHOT.jar
    - `/classes/lib` 안에 필요한 라이브러리가 모두 들어있는 것을 확인할 수 있다.

#### 문제는 자바에는 jar 안에 있는 jar 를 읽을 표준적인 방법이 없다.
과거에는 `uber jar` 를 사용하여 라이브러리를 모두 한곳에 풀어 하나의 jar 로 만들어 놓았다.
  - 무슨 라이브러리를 썼는지 알 수 없다.
  - 동명의 서로 다른 라이브러리가 있다면...?
스프링부트는 jar 안에 jar 로 넣고, 읽을 수 있는 파일로 만들어 놓았다.
- `org.springframework.boot.loader.jar.JarFile` 을 사용해서 내장 JAR 읽음
- `org.springframework.boot.loader.Launcher `를 사용해서 실행한다.
- `/META-INF/MANIFEST.MF` 를 확인하면 `MainClass`, `StartClass` 를 통해 확인할 수 있다.


이런 과정이 있어서 JAR 파일 하나로도 동작을 하는구나, 하나에 묶어놨구나 정도만 이해하면 된다.  
- 이처럼 독립가능한 애플리케이션이 스프링부트의 주요 goal 중 하나다.  
- 내장 톰캣도 이것을 위한 일부분이다. (절대 스프링부트가 서버가 아니다.) 
- 독립가능한 애플리케이션을 만들기 위해서는 **jar 하나로 앱을 실행할 수 있다**는게 중요하다.  

<br>

한줄 요약
- 스프링부트 메이븐 플러그인이 패키징을 해주고, 매니패스트를 읽어들여서 내장 Jar를 읽고 런처가 실행된다.