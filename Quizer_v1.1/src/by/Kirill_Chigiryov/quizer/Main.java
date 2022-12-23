package by.Kirill_Chigiryov.quizer;

import java.util.Map;
import java.util.Scanner;

import static by.Kirill_Chigiryov.quizer.MyQuiz.getQuizMap;
import static by.Kirill_Chigiryov.quizer.MyQuiz.wrongInput;

public class Main {

    /**
     * Enum, который описывает результат ответа на задание
     */
    public enum Result {
        OK, // Получен правильный ответ
        WRONG, // Получен неправильный ответ
        INCORRECT_INPUT // Некорректный ввод. Например, текст, когда ожидалось число
    }

    /**
     * Interface, который описывает одно задание
     */
    public interface Task {
        /*
         @return текст задания
         */
        String getText();

        /*
         * Проверяет ответ на задание и возвращает результат
         *
         * @param  answer ответ на задание
         * @return        результат ответа
         * @see           Result
         */
        Result validate(String answer);
    }

    /**
     * Interface, который описывает один генератор заданий
     */
    public interface TaskGenerator {
        /*
         * Возвращает задание. При этом новый объект может не создаваться, если класс задания иммутабельный
         *
         * @return задание
         * @see    Task
         */
        Task generate();
    }


    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String quizName;
        boolean rightInput = false;
        MyQuiz.Quiz quiz1 = null;
        System.out.println("Список квизов:");
        for (Map.Entry<String, MyQuiz.Quiz> pair : getQuizMap().entrySet()) {
            String key = pair.getKey();
            System.out.println(key);
        }
        System.out.print("Введите название квиза: ");
        while (!rightInput) {
            while (!scanner.hasNextLine()) {
                wrongInput();
            }
            quizName = scanner.nextLine();
            if (!getQuizMap().containsKey(quizName.toString())) {
                wrongInput();
            } else {
                rightInput = true;
                quiz1 = getQuizMap().get(quizName.toString());
            }
        }
        quiz1.quizHandler();
    }
}