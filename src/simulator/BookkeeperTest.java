package simulator;

import org.junit.Test;

public class BookkeeperTest {
    Bookkeeper bk = new Bookkeeper(5, 10, 10, "TestDisease");

    @Test
    public void test() {
        bk.updateSimulation();
    }
}

