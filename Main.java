/*
Суть: переменная должна инкрементироваться 100 миллионов раз
и по логике в конце должна равняться 100 миллионам, однако при каждом новом запуске программы
результат меньше 100 миллионов и всегда разный
*/

import java.util.concurrent.atomic.AtomicInteger;

// Класс для инкрементирования общей переменной
class InterferenceThread extends Thread {
    private final InterferenceExample checker;
    private static int i; // общая перемнная

    InterferenceThread(InterferenceExample checker) {
        this.checker = checker;
    }

    private void increment() {
        i++; /* инкремент общей переменной, ЗДЕСЬ ТЕРЯЕТСЯ РЕЗУЛЬТАТ, тк операция НЕ АТОМАРНАЯ,
        потоки могут брать старные значения переменной!
        */
    }

    int getI() {
        return i; // возврат значения общей переменной
    }

    public void run() {
        while (!checker.stop()) {
            increment(); // переменная инкрементируется 100 миллионов раз
        }
    }
}

// Пример совместного использования ресурсов
class InterferenceExample {
    private static final int HUNDRED_MILLION = 100_000_000;
    private AtomicInteger counter = new AtomicInteger();

    boolean stop(){
        return counter.incrementAndGet() > HUNDRED_MILLION; /* AtomicInteger защищает только счётчик остановки,
        а не инкрементирование общей переменной!
        */
    }
    // создание и работа 2 потоков, которые лезут к одной переменной
    void example() throws InterruptedException {
        InterferenceThread thread1 = new InterferenceThread(this);
        InterferenceThread thread2 = new InterferenceThread(this);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("Expected: " + HUNDRED_MILLION);
        System.out.println("Result: " + thread1.getI());
    }
}

// Лаунчер
public class Main {
    public static void main(String[] args) throws InterruptedException {
        new InterferenceExample().example(); // запуск программы
    }
}