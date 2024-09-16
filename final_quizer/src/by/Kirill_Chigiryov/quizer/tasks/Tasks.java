package by.Kirill_Chigiryov.quizer.tasks;

import by.Kirill_Chigiryov.quizer.Main;

public class Tasks {

    public static class ExpressionTask extends math_tasks.AbstractMathTask {
        public ExpressionTask(String taskQuestion, String rightAnswer) {
            super(taskQuestion, rightAnswer);
        }
    }

    public static class EquationTask extends math_tasks.AbstractMathTask {
        public EquationTask(String taskQuestion, String rightAnswer) {
            super(taskQuestion, rightAnswer);
        }
    }

    /**
     * Задание с заранее заготовленным текстом.
     * Можно использовать {@link PoolTaskGenerator}, чтобы задавать задания такого типа.
     */
    public static class TextTask implements Main.Task {
        /**
         * @param text   текст задания
         * @param answer ответ на задание
         */
        public TextTask(String text, String answer) {
            rightAnswer = answer;
            taskText = text;
        }

        @Override
        public String getText() {
            return taskText;
        }

        @Override
        public Main.Result validate(String answer) {
            if (answer.isEmpty() || !answer.matches("\\d+")) {
                return Main.Result.INCORRECT_INPUT;
            } else if (answer.equals(rightAnswer)) {
                return Main.Result.OK;
            } else {
                return Main.Result.WRONG;
            }
        }

        private final String rightAnswer;
        private final String taskText;
    }

}

