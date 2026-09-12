package bg.unisofia.fmi.mjt.nikevelik.foodanalyzer.service.storage;

import org.junit.jupiter.api.BeforeEach;

class InMemoryStorageTest extends StorageTest{

    @Override
    @BeforeEach
    void setUp() {
        cache = new InMemoryStorage<String>();
    }

}
