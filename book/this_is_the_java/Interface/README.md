## 인터페이스
### 인터페이스의 역할
* 객체의 사용 방법을 정의한 타입이다.
* 객체의 교환성(여러가지 타입을 대입할 수 있음)을 높여주기 때문에 다형성을 구현하는 매우 중요한 역할을 한다.
* 자바8에서 람다식은 함수적 인터페이스의 구현 객체를 생성한다. 
* 주로 매개값으로 많이 쓰이는데, 예를 들어 RemoteControl 인터페이스 변수에 Television와 Audio 중 어느 객체가 대입되느냐에 따라 결과가 달라질 수 있다. (같은 이름의 메소드지만 매개값으로 넘어온 구현 객체의 메소드가 어떻게 구현되었냐에 따라 달라진다.)
    * 아래 매개변수의 다형성 참고

### 매개변수의 다형성 
다음과 같이 Vehicle 인터페이스와 Driver 클래스가 있을 때
```$xslt
public class Driver {
    public void drive(Vehicle vehicle){
        vehicle.run();
    }
}
```

```$xslt
public interface Vehicle {
    public void run();
}
```

driver의 drive시() 메소드를 호출시 Bus 객체를 생성해서 매개 값으로 줄 수 있다.
```$xslt
Driver driver = new Driver();

Taxi taxi = new Taxi();
Bus bus = new Bus();

drvier.drive(bus);
driver.drive(taxi);
....
```
Bus 뿐만 아니라 어떤 구현 객체든 올 수 있다. 그리고 어떤 구현 객체냐에 따라 메소드의 실행 결과가 달라질 수 있다.

<br>

### 디폴트 메소드의 필요성
기존 인터페이스를 확장해서 새로운 기능을 추가하기 위해 필요하다.
예를 들어 myInterface라는 인터페이스와 이를 구현한 myClass 클래스가 있는데, myInterface에 기능을 추가하고자 추상 메소드를 추가하면 myClass에서 오류가 발생한다. myClass를 수정할 수 없는 상황이라면 이것이 문제가 된다.  
이때 myInterface에 `디폴트 메소드`로 새로운 기능을 추가한다면 myClass에도 문제가 생기지 않고 새로운 기능도 추가할 수 있다.  
➕ 그리고 다른 구현 클래스에서도 그 새로운 기능이 필요하다면 오버라이드 하면 된다.  
➕ 자식 인터페이스에서 그 디폴트 메소드를 다시 추상화 시킬 수 도 있다.    

#### ❓ 왜 직접 객체의 메소드를 호출하지 않고, 인터페이스를 중간에 둘까?
개발 코드를 수정하지 않고, 사용하는 객체를 변경할 수 있도록 하기 위함.  
ex) I 인터페이스로 프로그램을 개발하고, I 인터페이스를 구현할 클래스로 A 클래스를 선택했다.  
    추후 A 클래스에 문제가 생겨 다른 클래스를 사용하려고 할 때, I 인터페이스로 구현한 B 클래스를 만들고 단 한줄만 수정하면 된다.  
    `I i = new A();` => `I i = new B();`   
    

#### ❓ 상속과 뭐가 다를까?
* 인터페이스는 `다중 상속`이 가능하다. (인터페이스 내에서 extends로 상속 받고, 구현 클래스에서는 iplements로 구현)
* 상속이 `Animal animal = new Cat()`과 같이 `클래스 = 클래스`로 선언하고 `cat.sound()` 와 같이 메소드를 직접 사용한다면,  
  인터페이스는 `RemoteControl rc = new Television()`과 같이 `인터페이스 변수에 클래스 객체를 대입`하고, `rc.setVolume(10)`과 같이 메소드를 간접적으로 사용한다.  
* 상속은 같은 종류의 하위 클래스를 만드는 기술이고, 인터페이스는 사용 방법이 동일한 클래스를 만드는 기술
    * **결국 둘 다 다형성을 구현하는 기술이다.**

### 기타
* 상속과 많은 부분이 비슷하다. 
* 강제 타입 변환시에는 꼭 `instanceof` 연산자를 이용해서 객체 타입을 확인하자.
* 인터페이스에는 `상수`와 `메소드`만 온다. 그러므로 상수에 반드시 **초기값을 대입**해야 한다. 
    * 따로 `public static final`을 명시하지 않아도 컴파일 과정에서 붙는다.
    * 메소드도 `public`이 기본이다. 즉 명시하지 않아도 컴파일 과정에서 붙는다.
* `추상 메소드`, `디폴트 메소드`, `정적 메소드`가 올 수 있다.
* 인터페이스에 있는 추상 메소드를 하위 클래스에서 구현하거나, 그렇지 않으면 하위 클래스에 abstract를 선언해서 추상 클래스로 만들어야 한다.
* 디폴트 메소드는 인스턴트 메소드 이므로 반드시 구현 객체를 생성하고 사용할 수 있다.