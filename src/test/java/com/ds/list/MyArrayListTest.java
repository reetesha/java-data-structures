import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyArrayListTest {

    @Test
    void testAddAndGet() {
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(10);
        list.add(20);

        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void testRemove() {
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);

        list.remove(1);

        assertEquals(2, list.size());
        assertEquals(30, list.get(1));
    }

    @Test
    void testInsert() {
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(10);
        list.add(30);

        list.insert(1, 20);

        assertEquals(3, list.size());
        assertEquals(20, list.get(1));
    }
}
