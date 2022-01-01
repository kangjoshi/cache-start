### Cache
- 동일한 요청에 동일한 결과를 반환하는 메서드에 캐시를 적용하여 작업을 매번 수행해서 결과를 만드는 대신 캐시에 보관 해뒀던 기존 결과를 반환하여 성능을 높일수 있다.

#### 애노테이션을 이용한 캐시
- 스프링에서는 추상화된 캐시 기능을 제공한다. 애노테이션을 이용하여 지정하고 AOP 되어 메소드 실행 과정에 투명하게 적용된다.
- 또한 특정 캐시 서비스 기술에 종속되지 않도록 추상화 서비스를 제공하여, 환경이 바뀌거나 적용할 기술을 변경하여 캐시 서비스 종류가 달라져도 코드에는 영향이 없다.
- 애노테이션을 이용한 캐시 기능을 사용하기 위해 `@EnableCaching` 선언을 추가해야 한다.

##### @Cacheable
```java
@Cacheable("product") // 캐시명 지정
public Product bestProduct(String productNo) {
    
}
```
- 하나의 캐시에는 키가 다른 여러 개의 오브젝트를 넣을 수 있다. 위 메서드에서는 파라미터인 `productNo`가 키가 된다.
- 메서드의 파라미터가 없는 경우, 기본 키 값 생성 구현에 의해 0이라는 키가 지정된다. 즉 모든 요청에 동일한 결과를 리턴한다.
- 메서드의 파라미터 값이 여러개인 경우는 별도 설정 하지 않으면 모든 파라미터의 hashCode() 값을 조합해서 키로 만든다. 해시 코드의 값이 키로서 유효하다면 그대로 사용하면 되지만
아닌 경우 설정을 통해 지정도 가능하다.
```java
@Cacheable(value = "product", key = "#productNo") // productNo 값만 키로 사용
public Product bestProduct(String productNo, Date datetime, Category category) {
    
}
```
- condition 엘리먼트를 이용하면 파라미터 값에 따라서 선택적으로 캐시 적용도 가능하다.
```java
@Cacheable(value = "product", condition = "#category.name == 'IT'") // category의 name 값이 IT 경우만 캐시 적용
public Product bestProduct(String productNo, Date datetime, Category category) {
    
}
```
- unless 속성을 이용하면 해당 조건에 맞는 결과 캐싱하지 않는다.
```java
@Cacheable(value = "product", unless = "#result == null") // 결과가 null이라면 캐싱하지 않음.
public Product bestProduct(String productNo) {
    
}
```

##### @CacheEvict
- 캐시는 동일한 결과가 보장되는 동안에만 사용 되어야하고 결과가 캐시 값과 달라지는 순간 제거되어야 한다.
- 캐시 제거에 사용될 메서드에 `@CacheEvict` 애노테이션을 붙이면 해당 메서드가 실행될 때 캐시의 내용이 제거된다.
```java
@CacheEvict(value = "product", key = "#product?.productNo")
public void refreshBestProducts(Product product) {
    
}
```
- 캐시에 저장된 값을 모두 제거하려면 allEntries 엘리먼트를 true로 지정한다.
```java
@CacheEvict(value = "product", allEntries = true)
public void refreshAllBestProducts() {

}
```

#### cache manager
- 적용할 캐시 기술을 지원하는 cache maanger를 빈으로 등록한다.
- 스프링 3.1에서는 기본적으로 다섯가지 cache manager 구현 클래스를 제공한다.
1. ConcurrentMapCacheManager
   - ConcurrentHashMap을 이용하여 캐시 기능을 구현한 간단한 캐시
   - 별다른 설정이 필요 없다는 장점이 있지만 사용하기엔 기능이 빈약하여, 테스트 용도로 사용하자.
2. SimpleCacheManager
   - 프로퍼티를 이용하여 사용할 캐시를 직접 등록해야 한다. 테스트 용도로 사용하자.
3. EhCacheCacheManager
   - EhCache를 지원하는 캐시 매니저
4. CompositeCacheManager, NoOpCacheManager
   - CompositeCacheManager는 하나 이상의 cache manager를 사용하도록 지원하는 혼합 cache manager
   - NoOpCacheManager는 캐시가 지원되지 않는 환경에서 동작할 때 관련 설정을 제거하지 않아도 에러가 나지 않게 해주는 기능
   
---
### Reference
이일민. _토비의스프링 3.1 vol2_. 에이콘  