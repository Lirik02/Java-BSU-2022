package by.Kirill_Chigiryov.quizer.task_generators;

import by.Kirill_Chigiryov.quizer.Main;

public class math_task_generators {

    interface MathTaskGenerator extends Main.Task {
        int getMinNumber(); // получить минимальное число

        int getMaxNumber(); // получить максимальное число

        /**
         * @return разница между максимальным и минимальным возможным числом
         */
        default int getDiffNumber() {
            return 0;
        }
    }

}
