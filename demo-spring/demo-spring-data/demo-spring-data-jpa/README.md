### Demo Spring Data Jpa
#### Project Metadata  
#### New & Noteworthy
#### Dependencies
Spring Data Bom
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-releasetrain</artifactId>
      <version>Lovelace-SR6</version>
      <scope>import</scope>
      <type>pom</type>
    </dependency>
  </dependencies>
</dependencyManagement>
```
Spring Data Jpa
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-jpa</artifactId>
  </dependency>
</dependencies>
```
#### 使用 Spring Data 存储库
Spring Data 存储库抽象的目标是显着减少为各种持久性存储实现数据访问层所需的样板代码量

##### 核心概念
Spring Data 存储库抽象的核心接口是 `Repository`, 它使用领域类以及领域类的 ID 类型作为类型参数进行管理
```
Repository <= CurdRepository <= PagingAndSortingRepository <= JpaRepository <= ...

```

##### 查询方法
标准 CRUD 功能存储库通常对底层数据存储进行查询; 使用 Spring Data, 完成以下四步可声明这些查询
- 声明一个继承 Repository 或者其子接口的接口, 并键入要处理的领域类和 ID 类型, 如以下示例
```java
interface PersonRepository extends Repository<Person, Long> { … }
```
- 在接口上声明查询方法
```java
interface PersonRepository extends Repository<Person, Long> {
  List<Person> findByLastname(String lastname);
}
```
- 启动 Spring 会为这些接口创建代理实例, 可以使用 JavaConfig 或 Xml 配置
  - 使用 Java 配置, 创建类似以下的类
  ```java
  import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

  @EnableJpaRepositories
  class Config {}    
  ```
  - 使用 XML 配置, 定义类似以下的 bean
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:jpa="http://www.springframework.org/schema/data/jpa"
     xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/data/jpa
       http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

     <jpa:repositories base-package="com.acme.repositories"/>

  </beans>
  ```
如果使用了其他存储, 需要将注解和命名空间修改为对应的存储库, 例如 `mongdb`
- 注入存储库实例并使用, 如以下示例
```java
class SomeClient {

  private final PersonRepository repository;

  SomeClient(PersonRepository repository) {
    this.repository = repository;
  }

  void doSomething() {
    List<Person> persons = repository.findByLastname("Matthews");
  }
}
```

##### 定义存储库接口
首先, 定义一个指定的领域类的存储库接口, 这个接口必须继承 `Repository` 并使用领域类和 ID 类的类型参数; 如果想为领域类暴露 CRUD 方法, 则继承 `CRUDRepository` 而不是 `Repository`

###### 调优存储库定义
通常, 自定义存储库接口可以继承 `Repository, CrudRepository, PagingAndSortingRepository` 等; 如果不想继承 Spring Data 接口, 你可以使用 `@RepositoryDefinition` 注解你的存储库接口; 继承 `CrudRepository` 会暴露所有可以操纵实体的方法; 如果你更偏好与选择一些方法来暴露, 可以将你想暴露的方法从 `CrudRepository` 中拷贝到你的领域类存储库接口中
```java
@NoRepositoryBean
interface MyBaseRepository<T, ID extends Serializable> extends Repository<T, ID> {

  Optional<T> findById(ID id);

  <S extends T> S save(S entity);
}

interface UserRepository extends MyBaseRepository<User, Long> {
  User findByEmailAddress(EmailAddress emailAddress);
}
```
以上示例, 你为你所有领域类存储库接口定义了一个公共基础接口, 其中的方法将会被路由到你使用的 Spring Data 存储的基础存储库实现的方法 (例如, 如果你使用 JPA, 实现时 `SimpleRepository`)

###### 存储库方法对 Null 的处理
在 Spring Data 2.0 中, 返回单个聚合实例的存储库 CRUD 方法使用 Java 8 的 `Optional` 来指示可能缺少值
**Nullability 注解**
- `@NonNullApi`
- `@NonNull`
- `@Nullable`

###### 使用具有多个 Spring Data 模块的存储库
在你的应用中使用唯一的 Spring Data 模块是很简单的, 因为定义域中的所有存储库接口都会绑定到这个 Spring Data 模块; 有时, 应用需要使用多个 Spring Data 模块; 在这种情况下, 存储库定义必须区分不懂的持久化技术; 当它探测到类路径中有多个存储库工厂类时, Spring Data 进入严格存储库配置模式; 严格配置降使用存储库或领域类上的细节来决定绑定为存储库定义的 Spring Data 模块
- 如果存储库定义 [继承了指定模块的存储库], 那么它是特定 Spring Data 模块的一个有效候选者
- 如果领域类 [注解了指定模块的类型注解], 那么它是特定 Spring Data 模块的一个有效候选者;

##### 定义查询方法
存储库代理有两种方法从方法名中派生出一个指定存储的查询
- 直接从方法名中派生出查询方法
- 使用一个手动定义查询
##### 查询查找策略
- `CREATE`
- `USE_DECLARED_QUERY`
- `CREATE_IF_NOT_FOUND`
###### 查询创建
TODO
###### 属性表达式
TODO
###### 特殊的参数处理
为了处理你查询中的参数, 定义方法参数正如在之前的示例中见到那样; 除此之外, 底层基础可以识别如 `Pageable` 和 `Sort` 等特定类型
###### 限制查询结果
查询方法结果可以使用 `first` 或 `top` 关键字来限制, 可选的数字值可以追加在 `first` 或 `top` 关键字之后用于指定返回的最大结果数
###### 流查询结果
可以使用 Java 8 的 `Stream<T>` 作为返回类型以递增方式处理查询方法的结果
###### 异步查询结果
使用 `Spring 的异步方法执行功能` 存储库查询可以异步执行

##### 创建存储库实例
在本节中, 你会为定义的存储库接口创建实例和 bean 定义
###### XML 配置
TODO
###### JavaConfig
TODO
###### 独立使用
你可以使用存储库基础在 Spring 容器之外

##### Spring Data 存储库的自定义实现
当一个查询方法需要不同的行为或者不能被查询派生实现时, 则有必要提供一个自定义实现; Spring Data 存储库允许你提供自定义存储库代码, 并将其与通用 CRUD 抽象和查询方法功能集成

######　自定义单独的存储库
使用自定义功能丰富存储库, 你必须先定义一个框架接口以及自定义功能的一个实现, 如以下示例所示
```java
// Interface for custom repository functionality
interface CustomizedUserRepository {
  void someCustomMethod(User user);
}

// Implementation of custom repository functionality
class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

  public void someCustomMethod(User user) {
    // Your custom implementation
  }
}
```
实现不需要依赖 Spring Data, 可以是一个常规的 Spring bean; 所以, 你可以使用标准的依赖注入来注入其他 bean 的引用 (例如 `JdbcTemplate`), 加入切面等   
你可以让你新增的存储库接口继承片段接口, 如以下所示
```java
interface UserRepository extends CrudRepository<User, Long>, CustomizedUserRepository {

  // Declare query methods here
}
```
继承片段接口和你你的存储库接口可以联合 CRUD 和自定义功能让客户端使用  
###### 自定义基础存储库
为了改变所有存储库的行为, 你可以创建一个实现继承持久化的指定技术的存储库基类; 这个类作为存储库代理的一个自定义基类, 如以下代码所示
```java
class MyRepositoryImpl<T, ID extends Serializable>
  extends SimpleJpaRepository<T, ID> {

  private final EntityManager entityManager;

  MyRepositoryImpl(JpaEntityInformation entityInformation,
                          EntityManager entityManager) {
    super(entityInformation, entityManager);

    // Keep the EntityManager around to used from the newly introduced methods.
    this.entityManager = entityManager;
  }

  @Transactional
  public <S extends T> S save(S entity) {
    // implementation goes here
  }
}
```
首先要使 Spring Data 基础架构知道自定义存储库基类; 在 Java 配置中, 你可以使用 `@Enable${store}Repositories` 注解的 `repositoryBaseClass ` 属性, 如以下所示
```
@Configuration
@EnableJpaRepositories(repositoryBaseClass = MyRepositoryImpl.class)
class ApplicationConfiguration { … }
```
在 XML 命名空间中对应的属性, 如以下所示
```
<repositories base-package="com.acme.repository"
     base-class="….MyRepositoryImpl" />
```

##### 从聚合根发布事件
TODO

##### Spring Data 扩展
本节介绍了一组 Spring Data 扩展, 它们可以在各种上下文中使用 Spring Data; 目前大多数集成都针对 Spring MVC

#### JPA 存储库
本章节介绍关于 JPA 的特定存储库支持; 这是基于 [Working with Spring Data Repositories](https://docs.spring.io/spring-data/jpa/docs/2.1.6.RELEASE/reference/html/#repositories) 中阐述的核心存储支持之上构建的; 确保你已经对基础概念有一定的理解

#####　介绍
TODO

###### Spring 命名空间
###### 基于注解的配置
###### Bootstrap 模式
- DEFAULT:
- LAZY:
- DEFERRED:

##### 持久化实体
本节将会描述 Spring Data JPA 如何持久化 (保存) 实体
###### 保存实体
保存实体将会执行 `CrudRepository.save(…)` 方法; 它会使用底层 JPA `EntityManager` 来持久化或合并给定的实体; 如果实体还没有被持久化, Spring Data JPA 将会调用 `entityManager.persist(…)` 方法来保存实体， 否则调用 `entityManager.merge(…)` 方法
*实体状态探测策略*
Spring Data JPA 提供了以下策略来来探测一个实体是否是新的
- Id-Property
- 实现 `Persistable`
- 实现 `EntityInfomation`

##### 查询方法
本节叙述使用 Spring Data JPA 创建查询的各种方法
###### 查询查找策略
JPA 模块支持使用 String 手动定义一个查询或者从方法名派生

###### 查询创建
###### 使用 JPA 命名查询
###### 使用 `@Query`
###### 使用排序
###### 使用命名参数
###### 使用 SpEL 表达式
###### 更新查询
###### 应用查询 Hints
###### 配置 Fetch- 和 LoadGraphs
###### Projections

##### 存储过程
##### 规范
##### 查询示例
##### 事务
##### 锁
##### 审计
##### Miscellaneous Considerations

>**参考:**
- [Spring Data JPA - Reference Documentation](https://docs.spring.io/spring-data/jpa/docs/2.1.6.RELEASE/reference/html/#repositories.definition)
