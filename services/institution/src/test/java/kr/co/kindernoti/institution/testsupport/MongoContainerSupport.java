package kr.co.kindernoti.institution.testsupport;


import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest
public abstract class MongoContainerSupport {
    @Container
    @ServiceConnection
    static MongoDBContainer container = new MongoDBContainer("mongo:7.0.2");

}
