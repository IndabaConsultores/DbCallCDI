![Travis-CI](https://travis-ci.org/IndabaConsultores/DbCallCDI.svg?branch=master) ![Sonarcloud](https://sonarcloud.io/api/badges/gate?key=es.indaba:DbCallCDI:sonar)

DB Call CDI Extension (LGPL)
=============

A CDI Extension to call stored procedures or database functions declaratively using hibernate.

Feel free to use this library as you wish, make sure to quote the LGPL in all used sources.

## Using this project

Include Maven dependency on your pom.xml
```xml
<dependency>
	<groupId>es.indaba</groupId>
	<artifactId>DbCallCDI</artifactId>
	<version>1.7</version>
</dependency>
```

Define your CDI Bean as an interface. The implementation will be provided by the CDI extension:

```java
@ApplicationScoped
public interface DBTester {

  @StoredProcedure("CALL echoProc(?,?)")
  @StoredProcedureResult({ @FieldResult(name = "value", position = 2) })
  @DatabaseCall
  public ProcedureResult<String> callEchoAsProcedure(@StoredProcedureParameter(1) String name) throws Exception;

}
```

The extension interprets the following annotations in order to build a call to a database stored procedures.

* **DatabaseCall** defines that this method should be processed as a stored procedure call
* **StoredProcedure** defines the database call expression 
* **StoredProcedureResult** defines the mapping of the output values retrieved from the stored procedure call and the returning object properties
* **StoredProcedureParameter** identifies the stored procedure input parameters


Executing db call:

The interface is injected as a normal CDI bean

```java
@Inject
private DBTester dbTester;
...
ProcedureResult<String> result = dbTester.callEchoAsProcedure(testVal);
```

The returning type looks like this

```java
public class ProcedureResult<T> {

	private T value;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
```

### Using EntityManager Qualifier

```java
@ApplicationScoped
public interface DBTester {

  @StoredProcedure("CALL echoProc(?,?)")
  @StoredProcedureResult({ @FieldResult(name = "value", position = 2) })
  @DatabaseCall(qualifier=MyDatabase.class)
  public ProcedureResult<String> callEchoAsProcedure(@StoredProcedureParameter(1) String name) throws Exception;

}
```
Check tests for detailed use.

## References
* JPA 2.1 Stored Procedure Support - http://docs.oracle.com/javaee/7/api/javax/persistence/StoredProcedureQuery.html
* JDBC CallableStatement - https://docs.oracle.com/javase/7/docs/api/java/sql/CallableStatement.html
* CDI Extension - https://docs.jboss.org/weld/reference/latest/en-US/html/extend.html
* DeltaSpike - https://deltaspike.apache.org/index.html
