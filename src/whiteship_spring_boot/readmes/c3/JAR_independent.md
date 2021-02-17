# 독립적으로 실행 가능한 JAR
우리는 IDE 에서 웹서버 실행하지만,  
도커 이미지로 만들거나 배포할 때 jar 로 패키징하고 실행하는게 유용하다.  
-->  이것도 스프링부트의 유용한 특징중 하나다.  


- mvn clean  
- mvn package [--D skipTests] // 패키징
- java -jar target/spring-boot-lecture-0.0.1-SNAPSHOT.jar // 실행 

수 많은 의존성들이 어떻게 됬을까?  
- 패키징해서 생긴 jar 파일안에 모두 들어있다.
- jar 를 복사해서 다른 폴더에 놓고 압축을 풀어보자.
    - unzip -q spring-boot-lecture-0.0.1-SNAPSHOT.jar
    - ....../classes/lib 안에 다 있다.

문제는 자바에는 jar 안에 있는 jar 를 읽을 표준적인 방법이 없다.
- 과거에는 uber jar 를 사용해서 모두 다 풀어서 하나의 jar 로 풀어놓음
    - 무슨 라이브러리를 썼는지 알 수 없음
    - 동명의 서로 다른 라이브러리가 있다면...?
- 스프링부트는 jar 안에 jar 로 넣고, 읽을 수 있는 파일로 만들어 놓았따.
- org.springframework.boot.loader.jar.JarFile 을 사용해서 내장 JAR 읽음
- org.springframework.boot.loader.Launcher 를 사용해서 실행한다.


이런게 있어서 JAR 파일 하나로도 동작을 하는구나, 하나에 묶어놨구나 정도만 이해하면 된다.  
- 이것이 스프링부트의 주요 goal 중 하나. (독립가능한 애플리케이션)  
- 내장 톰캣도 이것을 위한 일부분. 
- 독립가능한 애플리케이션을 만들기 위해서는 jar 하나로 앱을 실행할 수 있다는게 중요하다.  

스프링부트 메이븐 플러그인이 패키징을 이렇게 해주고, 매니패스트를 읽어들여서 런처가 실행되고... 