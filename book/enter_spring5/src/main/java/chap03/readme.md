# DI (Dependency Injection)
- DI는 의존 주입

### 의존이란? 
- 객체간의 의존을 말한다. 
    - A라는 클래스의 메소드 내에서 B라는 클래스의 메소드를 사용하면?
    - A 클래스가 B 클래스에 의존한다.
    - B 클래스에 변화가 생기면 A 클래스에도 변화가 생긴다.
### 의존하는 대상을 구하는 방법은?
- 가장 쉬운 방법은 직접 의존하는 대상 객체를 생성하는 일이다.  
`B b = new B();` `b.method();`
- 그러나 유지보수 관점에서 문제가 발생할 수 있다.  
- 스프링에서는 `DI`를 통한 의존 처리가 가능하다.

### DI ?  
- 직접 객체를 생성하지 않고, **전달받는 방식을 사용한다.**
- 생성자나 setter를 통해 객체를 `주입`받는다.
- 과정이 더 복잡해지는데 사용하는 이유는 `변경의 유연함` 때문이다.

- ex)
    - 유저 db에 접근하는 UserDao가 있다.
    - userDao에 의존하는 클래스 A, B, C가 있다.
    - userDao을 확장시킨 UpgradeUserDao를 만들어서, 클래스 A, B, C의 의존을 변경해줘야 한다.
    - 만약, 직접 생성하는 방식을 사용하고 있다면
    세 개의 클래스에서 `UserDao dao = new UserDao()`를 `UserDao dao = new UpgradeUserDao()`로 변경해야 한다.  
    - 세 개가 아니라... 훨씬 많다면 ..? ..... 노가다가 발생한다.  
- 만약 DI를 사용하면 코드 변경이 확연하게 줄어든다.
    - DI를 사용하면 실제로 A, B, C를 호출하는 곳에서 UserDao도 생성하게 될텐데
    - 그 UserDao 실제 객체를 생성하는 곳에서 한번만 수정하면 된다.
    
**dao객체를 생성하는 클래스 - 의존 변경 전**
```java
UserDao dao = new UserDao();
A a = new A(dao);
B b = new B(dao);
C c = new C(dao);
```
**dao객체를 생성하는 클래스 - 의존 변경 후**
```java
UserDao dao = new UpgradeUserDao();
// ...이하 변경없음
```

**A, B, C 클래스의 생성자**
- 의존이 변경되어도 수정할 코드가 없다.
```
public class A {
    UserDao dao;
    public A (UserDao dao) {
        this.dao = dao;
    }
}
```

> **요약**  
> 직접 의존 대상을 생성할 때는 관리 대상이 흩어져 있었지만 (A, B, C 클래스 각각)  
> DI를 사용하고 나서 부터는 dao 객체 생성 지점 한곳으로 관리 대상이 집중되었다.

### 객체 조립기 (assembler)
- 위에서 DI를 사용하면 의존 대상 객체를 생성하는 곳만 관리하면 된다고 했는데, 그 생성하는 곳과 관련된 내용이다.
- 메인 메서드에서 해도 되지만, 객체를 생성하고 의존객체를 주입하는 클래스를 따로 작성하는 것이 더 좋다.
    - 의존 객체를 변경하려면, Assember의 생성자에 있는 코드만 변경하면 된다.
```java
public class Assembler {
  private MemberDao memberDao;
  private MemberRegisterService regSvc;
  private ChangePasswordService pwdSvc;

  // 의존하는 객체를 변경하고 싶다면, 이 생성자 안에서 초기화하는 부분만 바꾸면 된다.
  public Assembler() {
    memberDao = new MemberDao(); // ex) .. -> new CachedMemberDao();
    regSvc = new MemberRegisterService(memberDao);
    pwdSvc = new ChangePasswordService();
    pwdSvc.setMemberDao(memberDao);
  }
}
```

## 스프링의 DI
- 위의 `Assembler` 클래스로 조립기를 구현한 것은 스프링 예제는 아니다.
    - 객체를 생성하고 의존 주입을 이용해 객체를 서로 연결해주는 `조립기`에 대해 알아본 것이다. (스프링을 이해하기 위함)
    - **_스프링이 DI를 지원하는 조립기이다._**
### 스프링을 이용한 객체 생성과 사용 방법
`ApplicationContext`인터페이스를 사용하는데, chapter02의 내용과 같다.

### DI방식 1: 생성자 방식
> `MemberListPrinter` 참고
### DI방식 2: 세터(setter) 방식
> `MemberInfoPrinter` 참고  
- 자바빈 규칙에 따라 작성한다. [자바빈](bit.ly/22Rj2Ar)
  - 메서드 이름이 `set` 으로 시작
  - set 다음 첫 글자는 _대문자로 시작_
  - 파라미터가 **1개**
  - 리턴 타입이 `void`

#### 생성자 방식과 세터 방식의 장단점 비교
상황에 따라 둘을 혼용해서 쓰는 것이 좋다.
- 생성자 방식의 장단점
  - 장점  
    - 빈 객체를 생성하는 시점에 모두 갖추고 시작한다.
    - 그러므로 `NullPointerException`이 발생하지 않는다.
  - 단점
- 파라미터가 많을 때, 각 인자가 어떤 의존 객체를 설정하는지 생성자의 코드를 확인해야 한다.
```java
@Bean
public MemberListPrinter listPrinter() {
return new MemberListPrinter(memberDao(), memberPrinter()); // 첫번째 인자, 두번째 인자에 어떤 의존 객체가 오는지 단번에 알 수 없다.
}
```
- 세터 방식의 장단점
  - 장점
    - 세터 메서드의 이름을 통해 어떤 의존 객체가 주입되는지 알 수 있다.
    - 파라미터가 많아도 어떤 의존 객체를 설정
```java
@Bean
public MemberInfoPrinter infoPrinter() {
    MemberInfoPrinter infoPrinter =  new MemberInfoPrinter();
    infoPrinter.setMemDao(memberDao()); // IDE 자동완성 기능으로 어떤 의존 객체를 설정해야 하는지도 알 수 있다.
    infoPrinter.setPrinter(memberPrinter());
    return infoPrinter;
}
```
  - 단점
    - 깜빡하고 의존 객체를 전달하지 않아도 빈 객체가 생성되기 때문에, 객체를 사용하는 시점에 `NullPointerException이` 발생할 수 있다.

<br>

> **@Configuration 설정 클래스에 @Bean 설정과 싱글톤**  
> @configuration 설정 클래스 내부에 있는 @Bean 메소드들 중에 memberDao()를 실행하는 메소드가 몇개 있다.  
> 매번 새로 객체를 생성해서 리턴하므로 서로 다른 dao가 리턴되는 것 아닌가 생각할 수 있다.  
> 그러나 초반에 언급했듯 빈은 싱글톤 객체기 때문에 같은 객체를 리턴한다.  
> 설정 클래스는 내부적으로 설정 클래스를 상속한 새로운 설정 클래스를 만들고, 한번 생성한 객체를 보관하고 있다가 같은 객체를 재사용한다. 

> **두 개 이상의 설정파일**
> 스프링으로 웹 앱을 개발하면 수십~수백개의 빈을 생성할 수 있는데, 하나의 설정파일에 몰아넣으면 관리가 힘들기 때문에 영역별로 나누는게 좋다.  
> 스프링은 한 개 이사의 설정 파일을 이용해 컨테이너를 생성할 수 있다.  
> `ctx = new AnnotationConfigApplicationContext(AppConf1.class, AppConf2.class);`
> `AnnotationConfigApplicationContext`은 가변인자를 받으므로 필요한 만큼 인자를 추가하면 된다. 


### @Autowired, @Configuration, 빈
- `@Autowired` 어노테이션을 붙이면, 알아서 같은 타입의 빈을 찾아 할당받는다. (= 알아서 주입받는다.)
- 메소드를 호출하는 것 대신, 할당받은 객체를 사용하면 된다.
```java
@Configuration
public class AppCtx2 {
  // AppCtx1에 설정된 빈을 @Autowired 필드에 주입한다.
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private MemberPrinter memberPrinter;
  
  @Bean
  public MemberRegisterService memberRegSvc() {
      return new MemberRegisterService(memberDao); // 주입받은 빈 객체를 사용한다.
    }
}
```
<br>

- 스프링 빈에 의존하는 다른 빈을 자동으로 주입하고 싶을 때 사용한다.
  - 아래 코드에서는 스프링 빈(memberDao)에 의존하는 다른 빈(infoPrinter)을 자동으로 주입
```java
public class MemberInfoPrinter {

  @Autowired
  private MemberDao memDao;
  @Autowired
  private MemberPrinter printer;
    ...
}
```
```java
@Configuration
public class AppCtx2 {
  @Bean
  public MemberInfoPrinter infoPrinter() {
    MemberInfoPrinter infoPrinter = new MemberInfoPrinter();
    // 세터 메서드로 의존 주입을 하지 않아도
    // 스프링 컨테이너가 @Autowired를 붙인 필드에
    // 자동으로 해당 타입의 빈 객체를 주입한다.
    return infoPrinter;
  }
}
```

> 헷갈릴 수 있는게, `AppCtx2`에 있는 필드에도 `@Autowired`가 붙어있다.  
> 필드에 붙은 @Autowired는 생성자로 DI를 하는 빈을 위한 것이고,  
> `MemberInfoPrinter`에 붙은 `@Autowired`는 세터로 DI를 하는 빈을 위한 것이다.

- 스프링 컨테이너는 `@Configuration` 설정 클래스도 빈 객체로 등록한다. (이 부분은 다시 이해하자)
```java
// @Configuration 설정 클래스도 빈으로 등록함
AppCtx1 appCtx1 = ctx.getBean(AppCtx1.class);
System.out.println(appCtx1 != null); // true 출력
```

### @Import 애노테이션 사용
- 두 개 이상의 설정 파일을 사용하는 또 다른 방법이다.   
(첫번째는 `AnnotationConfigApplicationContext`를 생성할 때 인자를 추가하는 방법 )  
> 예제에서는 `AppConfImport`에 `AppConf2`를 import했다. 
- `@Import`에 배열을 통해 두 개 이상의 설정 클래스도 지정할 수 있다.
- import 된 설정파일에 또 `@Import`를 할 수도 있다.

### getBean() 메소드 사용
- getBean()으로 빈을 가져올 때, 지금까지 `ctx.getBean(빈 이름, 빈 타입)` 으로 사용했는데,   
만약 ApplicationContext 객체에 해당 타입의 빈 객체가 하나만 존재한다면 이름 없이 메소드를 호출할 수 있다.   
  - ex) `ctx.getBean(빈 타입)`
  - 만약 해당 타입의 빈 객체가 존재하지 않으면 `NoSuchBeanDefinitionException` 발생
  - 만약 해당 타입의 빈 객체가 두개 이상이라면 `NoUniqueBeanDefinitionException` 발생
> `ctx.getBean(빈 이름, 빈 타입)` 으로 사용시에
> - bean 이름을 잘못 지정하면 `NoSuchBeanDefinitionException` 발생
> - bean 타입을 잘못 지정하면 `BeanNotOfRequiredTypeException` 발생

--- 
#### 주입 대상 객체를 모두 빈 객체로 설정해야 하나?
빈 객체로 설정하지 않는다는 것은 일반 객체로 생성해서 주입하는 경우다.
```java
public class AppCtxNoMemberPrinterBean {
  private MemberPrinter printer = new MemberPrinter(); // 빈 아님
  
  @Bean
  public .....{..}
}
```
빈 객체로 설정하는것과 하지 않는 것의 차이는 _스프링 컨테이너가 객체를 관리하는지 여부_ 이다.  빈으로 등록하지 않으면 스프링 컨테이너가 객체를 관리할 수 없다.  
스프링 컨테이너는 자동 주입, 라이프사이클 관리등의 단순 객체 생성  및 객체 관리를 해주는데, 빈으로 등록된 객체에만 제공되는 기능이다.  
만약 그 기능들이 필요없다면 빈으로 등록하지 않아도 된다.  

하지만 의존 자동 주입 기능을 프로젝트 전반에 사용하는 추세라 스프링 빈 등록하는게 보통이라고 한다.
