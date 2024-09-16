package by.Kirill_Chigiryov.quizer;

import by.Kirill_Chigiryov.quizer.task_generators.*;
import by.Kirill_Chigiryov.quizer.tasks.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    /**
     * Class, который описывает один тест
     */
    static class Quiz {
        /**
         * @param generator генератор заданий
         * @param taskCount количество заданий в тесте
         */
        Quiz(TaskGenerator generator, int taskCount) {
            this.taskcount = taskCount;
            tasks = new Task[taskCount];
            for (int i = 0; i < taskCount; i++) {
                tasks[i] = generator.generate();
            }
            isnext = false;
            curtask = 0;
            correctanswers = 0;
            incorrectanswers = 0;
            incorrectinputs = 0;
        }

        /**
         * @return задание, повторный вызов вернет слелующее
         * @see Task
         */
        Task nextTask() {
            if (isnext) {
                isnext = false;
                curtask++;
            } else {
                isnext = true;
            }
            return tasks[curtask];
        }

        /**
         * Предоставить ответ ученика. Если результат {@link Result#INCORRECT_INPUT}, то счетчик неправильных
         * ответов не увеличивается, а {@link #nextTask()} в следующий раз вернет тот же самый объект {@link Task}.
         */
        Result provideAnswer(String answer) {
            if (tasks[curtask].validate(answer) == Result.WRONG) {
                incorrectanswers++;
                isnext = true;
            } else if (tasks[curtask].validate(answer) == Result.OK) {
                correctanswers++;
                isnext = true;
            } else if (tasks[curtask].validate(answer) ==
                    Result.INCORRECT_INPUT) {
                isnext = false;
                incorrectinputs++;
            }
            return tasks[curtask].validate(answer);
        }

        /**
         * @return завершен ли тест
         */
        boolean isFinished() {
            return curtask == taskcount;
        }

        /**
         * @return количество правильных ответов
         */
        int getCorrectAnswerNumber() {
            return correctanswers;
        }

        /**
         * @return количество неправильных ответов
         */
        int getWrongAnswerNumber() {
            return incorrectanswers;
        }

        /**
         * @return количество раз, когда был предоставлен неправильный ввод
         */
        int getIncorrectInputNumber() {
            return incorrectinputs;
        }

        /**
         * @return оценка, которая является отношением количества правильных ответов к количеству всех вопросов.
         * Оценка выставляется только в конце!
         */
        double getMark() {
            return (double) correctanswers * 10 / taskcount;
        }

        public void quizHandler() {
            while (!this.isFinished()) {
                System.out.println(this.nextTask().getText());
                Scanner scanner = new Scanner(System.in);
                while (!scanner.hasNextLine()) {
                    System.out.println("Ошибка ввода. Попробуйте еще раз.");
                }
                String answer = scanner.nextLine();
                this.provideAnswer(answer);
                if (curtask == tasks.length - 1) {
                    curtask++;
                }
            }
            System.out.println("Количество ошибок ввода: " + getIncorrectInputNumber());
            System.out.println("Количество неправильных ответов:  " + getWrongAnswerNumber());
            System.out.println("Количество правильных ответов: " + getCorrectAnswerNumber());
            System.out.println("Итоговая оценка: " + getMark());
        }

        private final Task[] tasks;
        private final int taskcount;
        private int curtask;

        private int correctanswers;

        private int incorrectanswers;

        private int incorrectinputs;
        private boolean isnext;

    }

    /**
     * @return тесты в {@link Map}, где
     * ключ     - название теста {@link String}
     * значение - сам тест       {@link Quiz}
     */
    static Map<String, Quiz> getQuizMap() {
        Quiz expressionQuiz =
                new Quiz(new TaskGenerators.ExpressionTaskGenerator(-50, -5,
                        false, false, false, true), 10);
        Quiz equationQuiz =
                new Quiz(new TaskGenerators.EquationTaskGenerator(-100, 100,
                        true, true, true, true), 10);
        Quiz groupQuiz = new Quiz(new TaskGenerators.GroupTaskGenerator(
                new TaskGenerators.ExpressionTaskGenerator(-50, -5,
                        false, false, false, true),
                new TaskGenerators.EquationTaskGenerator(-50, -5,
                        false, false, false, true),
                new TaskGenerators.ExpressionTaskGenerator(-50, -5,
                        false, false, false, true),
                new TaskGenerators.EquationTaskGenerator(-50, -5,
                        false, false, false, true)), 4);
        Collection<Main.TaskGenerator> taskCollection = new ArrayList<>();
        taskCollection.add(new TaskGenerators.ExpressionTaskGenerator(-50, -5,
                false, false, false, true));
        taskCollection.add(new TaskGenerators.EquationTaskGenerator(-50, -5,
                false, false, false, true));
        taskCollection.add(new TaskGenerators.ExpressionTaskGenerator(-50, -5,
                false, false, false, true));
        taskCollection.add(new TaskGenerators.EquationTaskGenerator(-50, -5,
                false, false, false, true));
        Quiz groupCollectionQuiz = new Quiz(new TaskGenerators.GroupTaskGenerator(taskCollection), 4);
        Quiz poolQuiz = new Quiz(new TaskGenerators.PoolTaskGenerator(true,
                new Tasks.TextTask("В магазине было 6 ящиков с яблоками. " +
                        "На следующий день они получили пополнение в кол-ве 13 ящиков с яблоками." +
                        "Сколько ящиков с яблоками стало в магазине после пополнения?", "19"),
                new Tasks.ExpressionTask("5*13=?", "65"),
                new Tasks.EquationTask("x+45=23", "-22")), 3);
        Collection<Task> tasks = new ArrayList<>();
        tasks.add(new Tasks.EquationTask("x+45=23", "-22"));
        tasks.add(new Tasks.ExpressionTask("5*13=?", "65"));
        tasks.add(new Tasks.TextTask("В магазине было 6 ящиков с яблоками. " +
                "На следующий день они получили пополнение в кол-ве 13 ящиков с яблоками." +
                "Сколько ящиков с яблоками стало в магазине после пополнения?", "19"));
        Quiz poolQuizCollection = new Quiz(new TaskGenerators.PoolTaskGenerator(false, tasks), 3);

        Map<String, Quiz> quizMap = new HashMap<>();
        quizMap.put("Quiz 1", expressionQuiz);
        quizMap.put("Quiz 2", equationQuiz);
        quizMap.put("Quiz 3", groupQuiz);
        quizMap.put("Quiz 4", groupCollectionQuiz);
        quizMap.put("Quiz 5", poolQuiz);
        quizMap.put("Quiz 6", poolQuizCollection);

        return quizMap;
    }

    static void wrongInput() throws InterruptedException {
        System.out.println("Вы ввели неправильное название квиза.");
        TimeUnit.SECONDS.sleep(1);
        System.out.print("Введите название квиза: ");
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        StringBuilder quizName;
        boolean rightInput = false;
        Quiz quiz1 = null;
        System.out.println("Список квизов:");
        for (Map.Entry<String, Quiz> pair : getQuizMap().entrySet()) {
            String key = pair.getKey();
            System.out.println(key);
        }
        System.out.print("Введите название квиза: ");
        while (!rightInput) {
            while (!scanner.hasNextLine()) {
                wrongInput();
            }
            quizName = new StringBuilder(scanner.nextLine());
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