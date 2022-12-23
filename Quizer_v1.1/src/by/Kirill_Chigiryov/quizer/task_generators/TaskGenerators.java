package by.Kirill_Chigiryov.quizer.task_generators;

import by.Kirill_Chigiryov.quizer.Main;
import by.Kirill_Chigiryov.quizer.tasks.Tasks;

import java.util.*;

public class TaskGenerators {

    public static class ExpressionTaskGenerator implements Main.TaskGenerator {
        /**
         * @param minNumber              минимальное число
         * @param maxNumber              максимальное число
         * @param generateSum            разрешить генерацию с оператором +
         * @param generateDifference     разрешить генерацию с оператором -
         * @param generateMultiplication разрешить генерацию с оператором *
         * @param generateDivision       разрешить генерацию с оператором /
         */

        public ExpressionTaskGenerator(int minNumber,
                                       int maxNumber,
                                       boolean generateSum,
                                       boolean generateDifference,
                                       boolean generateMultiplication,
                                       boolean generateDivision) {
            this.minNumber = minNumber;
            this.maxNumber = maxNumber;
            this.generateDifference = generateDifference;
            this.generateDivision = generateDivision;
            this.generateSum = generateSum;
            this.generateMultiplication = generateMultiplication;
        }

        private void generator() {
            if (!generateDifference &&
                    !generateDivision &&
                    !generateMultiplication &&
                    !generateSum || (maxNumber < minNumber)) {
                System.out.println(Main.Result.INCORRECT_INPUT);
            }

            int signCount = 0;
            int firstNumber = 0;
            int secondNumber = 0;
            String sign;
            ArrayList<String> signs = new ArrayList<>();
            if (generateDifference) {
                signCount++;
                signs.add("-");
            }
            if (generateDivision) {
                signCount++;
                signs.add("/");
            }
            if (generateMultiplication) {
                signCount++;
                signs.add("*");
            }
            if (generateSum) {
                signCount++;
                signs.add("+");
            }

            sign = signs.get((int) (Math.random() * (signCount)));

            firstNumber = generateRandomNumber(minNumber, maxNumber);
            secondNumber = generateRandomNumber(minNumber, maxNumber);

            if (sign.equals("/") && secondNumber == 0) {
                if (1 <= maxNumber && 1 >= minNumber) {
                    secondNumber = 1;
                } else {
                    secondNumber = firstNumber;
                }
            }

            if (sign.equals("/") &&
                    Math.abs(firstNumber) <= Math.abs(secondNumber)) {
                int temp = firstNumber;
                firstNumber = secondNumber;
                secondNumber = temp;
            }

            switch (sign) {
                case "+" -> rightAnswer =
                        String.valueOf(firstNumber + secondNumber);
                case "-" -> rightAnswer =
                        String.valueOf(firstNumber - secondNumber);
                case "*" -> rightAnswer =
                        String.valueOf(firstNumber * secondNumber);
                case "/" -> {
                    int reminder = firstNumber % secondNumber;
                    if (reminder != 0) {
                        if ((firstNumber +
                                (secondNumber - reminder)) <= maxNumber) {
                            firstNumber = firstNumber +
                                    (secondNumber - reminder);
                        } else {
                            firstNumber -= reminder;
                        }
                    }
                    rightAnswer = String.valueOf(firstNumber / secondNumber);
                }
            }

            if (firstNumber < 0 && secondNumber > 0) {
                taskQuestion =
                        "(" + firstNumber + ")" + sign + secondNumber + "=?";
            } else if (firstNumber > 0 && secondNumber < 0) {
                taskQuestion =
                        firstNumber + sign + "(" + secondNumber + ")" + "=?";
            } else if (firstNumber < 0) {
                taskQuestion = "(" + firstNumber + ")" +
                        sign + "(" + secondNumber + ")" + "=?";
            } else {
                taskQuestion = firstNumber + sign + secondNumber + "=?";
            }

        }

        int generateRandomNumber(int minNumber, int maxNumber) {
            return minNumber +
                    (int) (Math.random() * (maxNumber - minNumber + 1));
        }

        /**
         * return задание типа {@link ExpressionTask}
         */
        public Tasks.ExpressionTask generate() {
            generator();
            return new Tasks.ExpressionTask(taskQuestion, rightAnswer);
        }

        private String rightAnswer;
        private String taskQuestion;
        private final int minNumber;
        private final int maxNumber;
        private final boolean generateSum;
        private final boolean generateDifference;
        private final boolean generateMultiplication;
        private final boolean generateDivision;

    }

    public static class EquationTaskGenerator implements Main.TaskGenerator {
        /**
         * @param minNumber              минимальное число
         * @param maxNumber              максимальное число
         * @param generateSum            разрешить генерацию с оператором +
         * @param generateDifference     разрешить генерацию с оператором -
         * @param generateMultiplication разрешить генерацию с оператором *
         * @param generateDivision       разрешить генерацию с оператором /
         */
        public EquationTaskGenerator(int minNumber,
                                     int maxNumber,
                                     boolean generateSum,
                                     boolean generateDifference,
                                     boolean generateMultiplication,
                                     boolean generateDivision) {
            this.minNumber = minNumber;
            this.maxNumber = maxNumber;
            this.generateDifference = generateDifference;
            this.generateDivision = generateDivision;
            this.generateSum = generateSum;
            this.generateMultiplication = generateMultiplication;
        }

        private void generator() {
            int signCount = 0;
            int xPosition;
            int firstNumber = generateRandomNumber(minNumber, maxNumber);
            int secondNumber = generateRandomNumber(minNumber, maxNumber);

            String sign;
            ArrayList<String> signs = new ArrayList<String>();
            if (generateDifference) {
                signCount++;
                signs.add("-");
            }
            if (generateDivision) {
                signCount++;
                signs.add("/");
            }
            if (generateMultiplication) {
                signCount++;
                signs.add("*");
            }
            if (generateSum) {
                signCount++;
                signs.add("+");
            }

            int randomSign = (int) (Math.random() * (signCount));

            sign = signs.get(randomSign);

            xPosition = (int) (Math.random() * 2);

            if (sign.equals("/")) {
                if (firstNumber == 0) {
                    firstNumber = secondNumber;
                }
                if (secondNumber == 0) {
                    if (1 <= maxNumber && 1 >= minNumber) {
                        secondNumber = 1;
                    } else {
                        secondNumber = firstNumber;
                    }
                }
            }

            if (sign.equals("+")) {
                rightAnswer = String.valueOf(secondNumber - firstNumber);
            }

            if (sign.equals("-") && xPosition == 1) {
                rightAnswer = String.valueOf(firstNumber - secondNumber);
            } else if (sign.equals("-") && xPosition == 0) {
                rightAnswer = String.valueOf(firstNumber + secondNumber);
            }

            if (sign.equals("*")) {
                if (firstNumber < secondNumber) {
                    int temp = firstNumber;
                    firstNumber = secondNumber;
                    secondNumber = temp;
                }
                int reminder = secondNumber % firstNumber;
                if (reminder != 0) {
                    if ((secondNumber +
                            (firstNumber - reminder)) <= maxNumber) {
                        secondNumber = secondNumber + (firstNumber - reminder);
                    } else {
                        secondNumber -= reminder;
                    }
                }
                rightAnswer = String.valueOf(secondNumber / firstNumber);
//                if (secondNumber % firstNumber != 0) {
//                    while (secondNumber % firstNumber != 0) {
//                        firstNumber++;
//                    }
//                }
//                rightAnswer = String.valueOf(secondNumber / firstNumber);
            }

            if (sign.equals("/") && xPosition == 1) {
                if (secondNumber < firstNumber) {
                    int temp = secondNumber;
                    secondNumber = firstNumber;
                    firstNumber = temp;
                }
                int reminder = firstNumber % secondNumber;
                if (reminder != 0) {
                    if ((firstNumber +
                            (secondNumber - reminder)) <= maxNumber) {
                        firstNumber = firstNumber + (secondNumber - reminder);
                    } else {
                        firstNumber -= reminder;
                    }
                }
                rightAnswer = String.valueOf(firstNumber / secondNumber);
//                if (firstNumber % secondNumber != 0) {
//                    while (firstNumber % secondNumber != 0) {
//                        secondNumber++;
//                    }
//                }
//                rightAnswer = String.valueOf(firstNumber / secondNumber);
            } else if (sign.equals("/") && xPosition == 0) {
                rightAnswer = String.valueOf(firstNumber * secondNumber);
            }

            if (firstNumber < 0 && secondNumber > 0 && xPosition == 0) {
                taskQuestion =
                        "x" + sign + "(" + firstNumber + ")=" + secondNumber;
            } else if (firstNumber < 0 && secondNumber < 0 && xPosition == 0) {
                taskQuestion = "x" + sign +
                        "(" + firstNumber + ")=(" + secondNumber + ")";
            } else if (firstNumber > 0 && secondNumber < 0 && xPosition == 0) {
                taskQuestion =
                        "x" + sign + firstNumber + "=(" + secondNumber + ")";
            } else if (firstNumber > 0 && secondNumber > 0 && xPosition == 0) {
                taskQuestion = "x" + sign + firstNumber + "=" + secondNumber;
            }

            if (firstNumber < 0 && secondNumber > 0 && xPosition == 1) {
                taskQuestion =
                        "(" + firstNumber + ")" + sign + "x=" + secondNumber;
            } else if (firstNumber < 0 && secondNumber < 0 && xPosition == 1) {
                taskQuestion = "(" + firstNumber + ")" +
                        sign + "x=(" + secondNumber + ")";
            } else if (firstNumber > 0 && secondNumber < 0 && xPosition == 1) {
                taskQuestion = firstNumber + sign + "x=(" + secondNumber + ")";
            } else if (firstNumber > 0 && secondNumber > 0 && xPosition == 1) {
                taskQuestion = firstNumber + sign + "x=" + secondNumber;
            }
        }

        int generateRandomNumber(int minNumber, int maxNumber) {
            return minNumber +
                    (int) (Math.random() * (maxNumber - minNumber + 1));
        }

        /**
         * return задание типа {@link EquationTask}
         */
        public Tasks.EquationTask generate() {
            generator();
            return new Tasks.EquationTask(taskQuestion, rightAnswer);
        }

        private String rightAnswer;
        private String taskQuestion;
        private final int minNumber;
        private final int maxNumber;
        private final boolean generateSum;
        private final boolean generateDifference;
        private final boolean generateMultiplication;
        private final boolean generateDivision;

    }


    public static class GroupTaskGenerator implements Main.TaskGenerator {
        /**
         * Конструктор с переменным числом аргументов
         *
         * @param generators генераторы, которые в конструктор передаются через запятую
         */
        public GroupTaskGenerator(Main.TaskGenerator... generators) {
            tasks = new ArrayList<>();
            for (int i = 0; i < generators.length; i++) {
                tasks.add(generators[i].generate());
                taskIDs.add(i);
            }
            taskID = (int) (Math.random() * generators.length);
        }

        /**
         * Конструктор, который принимает коллекцию генераторов
         *
         * @param generators генераторы, которые передаются в конструктор в Collection (например, {@link ArrayList})
         */
        public GroupTaskGenerator(Collection<Main.TaskGenerator> generators) {
            List<Main.TaskGenerator> taskGenerators = new ArrayList<>(generators.stream().toList());
            tasks = new ArrayList<>();
            for (int i = 0; i < generators.size(); ++i) {
                taskIDs.add(i);
                tasks.add(taskGenerators.get(i).generate());
            }
            taskID = (int) (Math.random() * taskIDs.size());
        }

        /**
         * @return результат метода generate() случайного генератора из списка.
         * Если этот генератор выбросил исключение в методе generate(), выбирается другой.
         * Если все генераторы выбрасывают исключение, то и тут выбрасывается исключение.
         */
        public Main.Task generate() {
            if (taskIDs.size() == 1) {
                int ID = taskIDs.get(0);
                taskIDs.remove(0);
                return tasks.get(ID);
            }
            int thisTaskID = taskID;
            if (thisTaskID == taskIDs.size()) {
                thisTaskID--;
            }
            Main.Task task = tasks.get(taskIDs.get(thisTaskID));
            taskIDs.remove(thisTaskID);
            taskID = (int) (Math.random() * taskIDs.size());
            return task;
        }

        private final List<Main.Task> tasks;
        private int taskID;
        private final List<Integer> taskIDs = new ArrayList<>();

    }


    public static class PoolTaskGenerator implements Main.TaskGenerator {
        /**
         * Конструктор с переменным числом аргументов
         *
         * @param allowDuplicate разрешить повторения
         * @param tasks          задания, которые в конструктор передаются через запятую
         */
        public PoolTaskGenerator(boolean allowDuplicate, Main.Task... tasks) {
            this.tasks = new ArrayList<>();
            Collections.addAll(this.tasks, tasks);
            if (!allowDuplicate) {
                boolean isRepeated = false;
                for (int i = 0; i < tasks.length; i++) {
                    for (int j = 0; j < i; j++) {
                        if (tasks[i].getText().equals(tasks[j].getText())) {
                            isRepeated = true;
                            break;
                        }
                    }
                    if (!isRepeated) {
                        this.tasks.add(tasks[i]);
                    } else {
                        isRepeated = false;
                    }
                }
            }
            for (int i = 0; i < this.tasks.size(); ++i) {
                taskIDs.add(i);
            }
        }

        /**
         * Конструктор, который принимает коллекцию заданий
         *
         * @param allowDuplicate разрешить повторения
         * @param tasks          задания, которые передаются в конструктор в Collection (например, {@link LinkedList})
         */
        public PoolTaskGenerator(boolean allowDuplicate,
                                 Collection<Main.Task> tasks) {
            this.tasks = new ArrayList<>(tasks);
            if (!allowDuplicate) {
                this.tasks = this.tasks.stream().distinct().toList();
            }
            for (int i = 0; i < this.tasks.size(); ++i) {
                taskIDs.add(i);
            }
        }

        /**
         * @return случайная задача из списка
         */
        public Main.Task generate() {
            if (taskIDs.size() == 1) {
                int ID = taskIDs.get(0);
                taskIDs.remove(0);
                return tasks.get(ID);
            }
            int thisTaskID = taskID;
            if (thisTaskID == taskIDs.size()) {
                thisTaskID--;
            }
            if (thisTaskID == -1) {
                thisTaskID++;
            }
            Main.Task task = tasks.get(taskIDs.get(thisTaskID));
            taskIDs.remove(thisTaskID);
            taskID = (int) (Math.random() * taskIDs.size());
            return task;
        }

        private List<Main.Task> tasks;
        private int taskID;
        private final List<Integer> taskIDs = new ArrayList<>();

    }

//    public static void main(String[] args) throws InterruptedException {
//        PoolTaskGenerator poolQuiz = new TaskGenerators.PoolTaskGenerator(true,
//                new Tasks.TextTask("В магазине было 6 ящиков с яблоками. " +
//                        "На следующий день они получили пополнение в кол-ве 13 ящиков с яблоками." +
//                        "Сколько ящиков с яблоками стало в магазине после пополнения?", "19"),
//                new Tasks.ExpressionTask("5*13=?", "65"),
//                new Tasks.EquationTask("x+45=23", "-22"));
//        Collection<Main.Task> tasks = new ArrayList<>();
//        tasks.add(new Tasks.EquationTask("x+45=23", "-22"));
//        tasks.add(new Tasks.ExpressionTask("5*13=?", "65"));
//        tasks.add(new Tasks.TextTask("В магазине было 6 ящиков с яблоками. " +
//                "На следующий день они получили пополнение в кол-ве 13 ящиков с яблоками." +
//                "Сколько ящиков с яблоками стало в магазине после пополнения?", "19"));
//        PoolTaskGenerator poolQuiz1 = new TaskGenerators.PoolTaskGenerator(false, tasks);
//        Main.Task[] arr = new Main.Task[3];
//        for (int i = 0; i < 3; i++) {
//            arr[i] = poolQuiz1.generate();
//        }
//    }
}
