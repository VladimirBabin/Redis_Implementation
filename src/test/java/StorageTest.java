import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageTest {

    Storage storage = new Storage();
    List<Integer> listOfIntegers = new ArrayList<>();
    List<String> listOfStrings = new ArrayList<>();

    Map<Object, Object> expectedMap = new HashMap<>();


    @Before
    public void setUp() throws Exception {
        // заполняем storage некоторыми данными с помощью метода set
        storage.set("server:name", "fido");

        listOfIntegers.add(1);
        listOfIntegers.add(2);
        listOfIntegers.add(3);

        listOfStrings.add("one");
        listOfStrings.add("two");
        listOfStrings.add("three");

        storage.set(listOfIntegers, listOfStrings);

        storage.set(123, "one-two-three");

        // заполняем словарь такими же данными

        expectedMap.put("server:name", "fido");
        expectedMap.put(listOfIntegers, listOfStrings);
        expectedMap.put(123, "one-two-three");
    }

    @After
    public void cleanTheStorage() {
        storage.deleteAll();
    }

    @Test
    public void get() {
        // проверяем что метод get работает правильно при наличии ключа и в случае приемлемого содержимого
        Assert.assertEquals(storage.get(123), "one-two-three");
        Assert.assertEquals(storage.get(listOfIntegers), listOfStrings);
        Assert.assertEquals(storage.get("server:name"), "fido");
    }

    @Test
    public void get_NOT_NULL() {
        Assert.assertEquals("Key should not be null", storage.get(null));
    }

    @Test
    public void get_NOT_BLANK() {
        Assert.assertEquals("Key should not be empty/blank", storage.get(" "));
        Assert.assertEquals("Key should not be empty/blank", storage.get(""));
    }

    @Test
    public void get_NOT_EXISTS() {
        Assert.assertEquals("nil", storage.get("foo"));
    }

    @Test
    public void set_IF_EXISTS() {
        // проверяем что данные в storage и expectedMap равны
        Assert.assertEquals(expectedMap, Storage.storage);
        // проверяем правильную работу метода если ключ уже существует
        Assert.assertEquals("Key overwritten", storage.set(123, "one and two and three"));
        Assert.assertTrue(storage.storage.containsKey(123));
    }

    @Test
    public void set_IF_NOT_EXISTS() {
        Assert.assertEquals("OK", storage.set(345, "three-four-five"));
        Assert.assertTrue(storage.storage.containsKey(345));
    }

    @Test
    public void set_NOT_NULL() {
        Assert.assertEquals("Key should not be null", storage.set(null, null));
    }

    @Test
    public void set_NOT_BLANK() {
        Assert.assertEquals("Key should not be empty/blank", storage.set("", ""));
        Assert.assertEquals("Key should not be empty/blank", storage.set(" ", " "));
    }

    @Test
    public void delete() {
        // проверяем что метод delete удаляет данные из storage и возвращает последнее значение по ключу
        Assert.assertEquals(storage.get("server:name"), storage.delete("server:name"));
        Assert.assertFalse(storage.storage.containsValue("fido"));
        Assert.assertEquals(storage.get(123), storage.delete(123));
        Assert.assertFalse(storage.storage.containsValue("one-two-three"));
        Assert.assertEquals(storage.get(listOfIntegers), storage.delete(listOfIntegers));
        Assert.assertFalse(storage.storage.containsValue(listOfStrings));
        Assert.assertTrue(storage.storage.isEmpty());
    }
    @Test
    public void delete_NOT_NULL() {
        Assert.assertEquals("Key should not be null", storage.delete(null));
    }

    @Test
    public void delete_NOT_BLANK() {
        Assert.assertEquals("Key should not be empty/blank", storage.delete(" "));
        Assert.assertEquals("Key should not be empty/blank", storage.delete(""));
    }
    @Test
    public void delete_NOT_EXISTS() {
        Assert.assertEquals("nil", storage.delete("foo"));
    }

    @Test
    public void keys() {
        // проверяем что метод keys возвращает искомый сет значений по ключу
        Assert.assertEquals(expectedMap.keySet(), storage.keys());
    }

    @Test
    public void keys_NOT_NULL() {
        storage.deleteAll();
        Assert.assertNotNull(storage.keys());
    }

    @Test
    public void deleteAll() {
        // проверяем что storage не пустой, потом все из него удаляем и проверяем что он пустой
        Assert.assertFalse(storage.storage.isEmpty());
        storage.deleteAll();
        Assert.assertTrue(storage.storage.isEmpty());
    }
}