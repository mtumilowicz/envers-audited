# envers-audited
The main goal of this project is to explore basic features of `@Audited` from `Hibernate Envers`.

_Reference_: [UserGuide documentation](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#envers)  
_Reference_: [JBoss documentation](https://docs.jboss.org/envers/docs/)  
_Reference_: [Hibernate envers tutorial](https://www.thoughts-on-java.org/hibernate-envers-getting-started/)  
_Reference_: [Spring Boot rest integration testing](http://www.springboottutorial.com/integration-testing-for-spring-boot-rest-services)

# preface
You just need to add the `hibernate-envers.jar` file to the classpath of your application and annotate 
your entities with `@Audited`. Hibernate will then create a new revision for each transaction and create 
a new record in the audit table for each **create**, **update** or **delete** operation performed on an 
audited entity.

You can then retrieve and query historical data without much effort. Basically, one transaction is one 
revision. As the revisions are global, having a revision number, you can query for various entities at that 
revision, retrieving a (partial) view of the database at that revision. You can find a revision number 
having a date, and the other way round, you can get the date at which a revision was commited.

# manual
* `pom.xml`
    ```
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-envers</artifactId>
        <version>5.3.2.Final</version>
    </dependency>
    ```
    
* annotate entity with `@Audited`
    ```
    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Audited
    @Builder
    public class Customer implements Serializable {
        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String firstName;
        private String lastName;
    }
    ```
    Instead of annotating the whole class and auditing all properties, you can annotate only some 
    persistent properties with `@Audited`. This will cause only these properties to be audited.
* for above entity will be auto-generated audit table:
    ```
    create table Customer_AUD (
        id bigint not null,
        REV integer not null,
        REVTYPE tinyint,
        firstName varchar(255),
        lastName varchar(255),
        primary key (id, REV)
    )
    ```
    The `REVTYPE` column value is taken from the `RevisionType` `Enum`.
    
        | Database column value | RevisionType
        | ----------------------|-------------
        | 0                     | ADD
        | 1                     | MOD
        | 2                     | DEL
        
    * **ADD** - indicates that the entity was added (persisted) at that revision.
    * **DEL** - indicates that the entity was deleted (removed) at that revision.
    * **MOD** - indicates that the entity was modified (one or more of its fields) at that revision.
    
* accessing history  
    You can access the audit (history) of an entity using the `AuditReader` interface, which you can 
    obtain when having an open `EntityManager`:
    ```
    AuditReader reader = AuditReaderFactory.get(entityManager);
    Customer oldCustomer = auditReader.find(Customer.class, customerId, rev)
    ```
    it returns an entity with the given primary key, with the data it contained at the given revision. 
    If the entity didn't exist at this revision, `null` is returned.
    
* get revisions
    You can also get a list of revisions at which an entity was modified:
    ```
    List<Number> revisions = auditReader.getRevisions(Customer.class, customerId);
    ```
    as well as retrieve the date, at which a revision was created using the `getRevisionDate` method:
    ```
    Map<Number, Date> revisionDatesMap = revisions.stream().collect(Collectors.toMap(Function.identity(), auditReader::getRevisionDate));
    ```
    
* get `Customer` with `id` at revision `rev`
    ```auditReader.find(Customer.class, id, rev)```
    
# tests
Tests are divided in three sections:
* junit (`Spock`)
    * `Controller`
    * `Service`
* functional (`Spock`):
    * `Controller` <-> `Mock(Service)`
    * `Service` <-> `Mock(Repository)`
* health-check (`TestRestTemplate`, `jUnit`)
    * `Controller`
* integration (`TestRestTemplate`, `jUnit`)
    * `Controller` <-> `database`
    
**Coverage**: `95%`