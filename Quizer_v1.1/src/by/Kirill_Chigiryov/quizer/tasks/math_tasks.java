package by.Kirill_Chigiryov.quizer.tasks;

import by.Kirill_Chigiryov.quizer.Main;

public class math_tasks {

    interface MathTask extends Main.Task {

    }

    abstract static class AbstractMathTask implements MathTask {

        AbstractMathTask(String taskQuestion, String rightAnswer) {
            this.taskQuestion = taskQuestion;
            this.rightAnswer = rightAnswer;
        }

        @Override
        public String getText() {
            return taskQuestion;
        }

        @Override
        public Main.Result validate(String answer) {
            if (answer.isEmpty() || !answer.matches("-?\\d+")) {
                return Main.Result.INCORRECT_INPUT;
            } else if (answer.equals(rightAnswer)) {
                return Main.Result.OK;
            } else {
                return Main.Result.WRONG;
            }
        }

        public void setTaskQuestion(String taskQuestion) {
            this.taskQuestion = taskQuestion;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        private String rightAnswer;
        private String taskQuestion;

    }

}
