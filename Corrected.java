/*
ИСПРАВЛЕННЫЙ РЕЗУЛЬТАТ
Expected: 100000000
Result: 100000000
*/

import java.util.concurrent.atomic.AtomicInteger;

// Класс для инкрементирования общей переменной
class InterferenceThread extends Thread {
    private final InterferenceExample checker;
    private static final AtomicInteger i = new AtomicInteger(); // i теперь AtomicInteger

    InterferenceThread(InterferenceExample checker) {
        this.checker = checker;
    }

    private void increment() {
        i.incrementAndGet(); // АТОМАРНАЯ ОПЕРАЦИЯ
    }

    int getI() {
        return i.get(); // возврат значения общей переменной
    }

    public void run() {
        while (!checker.stop()) {
            increment();
        }
    }
}

// Пример совместного использования ресурсов
class InterferenceExample {
    private static final int HUNDRED_MILLION = 100_000_000;
    private AtomicInteger counter = new AtomicInteger();

    boolean stop(){
        return counter.incrementAndGet() > HUNDRED_MILLION;
    }

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
public class Corrected {
    public static void main(String[] args) throws InterruptedException {
        new InterferenceExample().example();
    }
}