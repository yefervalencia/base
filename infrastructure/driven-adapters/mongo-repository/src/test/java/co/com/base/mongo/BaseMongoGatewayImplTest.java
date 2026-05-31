package co.com.base.mongo;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseMongoGatewayImplTest {

    @SuppressWarnings("unchecked")
    private final ReactiveMongoRepository<TestDocument, String> repository =
            mock(ReactiveMongoRepository.class);

    private final TestGateway gateway = new TestGateway(repository);

    @Test
    void saveShouldMapAndReturnDomain() {
        TestDomain domain = new TestDomain("1", "name");
        TestDocument document = new TestDocument("1", "name");

        when(repository.save(document)).thenReturn(Mono.just(document));

        StepVerifier.create(gateway.save(domain))
                .expectNextMatches(result -> "1".equals(result.id()) && "name".equals(result.name()))
                .verifyComplete();
    }

    @Test
    void findByIdShouldReturnMappedDomain() {
        TestDocument document = new TestDocument("1", "name");

        when(repository.findById("1")).thenReturn(Mono.just(document));

        StepVerifier.create(gateway.findById("1"))
                .expectNextMatches(result -> "1".equals(result.id()) && "name".equals(result.name()))
                .verifyComplete();
    }

    @Test
    void deleteByIdShouldComplete() {
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(gateway.deleteById("1"))
                .verifyComplete();
    }

    private static final class TestGateway extends BaseMongoGatewayImpl<TestDomain, TestDocument, ReactiveMongoRepository<TestDocument, String>> {

        private TestGateway(ReactiveMongoRepository<TestDocument, String> repository) {
            super(repository);
        }

        @Override
        protected TestDomain toDomain(TestDocument document) {
            return new TestDomain(document.id(), document.name());
        }

        @Override
        protected TestDocument toDocument(TestDomain domain) {
            return new TestDocument(domain.id(), domain.name());
        }
    }

    private record TestDomain(String id, String name) {
    }

    private record TestDocument(String id, String name) {
    }
}
