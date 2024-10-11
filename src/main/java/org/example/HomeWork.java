package org.example;

import java.util.*;

public class HomeWork {
    /**
     * <h1>Задание 1.</h1>
     * Требуется реализовать метод, который по входной строке будет вычислять математические выражения.
     * <br/>
     * Операции: +, -, *, / <br/>
     * Функции: sin, cos, sqr, pow <br/>
     * Разделители аргументов в функции: , <br/>
     * Поддержка скобок () для описания аргументов и для группировки операций <br/>
     * Пробел - разделитель токенов, пример валидной строки: "1 + 2 * ( 3 - 4 )" с результатом -1.0 <br/>
     * <br/>
     * sqr(x) = x^2 <br/>
     * pow(x,y) = x^y
     */

    private static final Map<String, Integer> PRIORITY_MAP = Map.of(
            "+", 1,
            "-", 1,
            "*", 2,
            "/", 2
    );
    private static final List<String> FUNCTIONS = List.of("sin", "cos", "sqr", "pow");


    private static final String OPERATOR_DELIMITER = " ";
    private static final String FUNCTION_PARAM_DELIMITER = ",";

    private static final Character OPENING_BRACKET = '(';
    private static final String CLOSING_BRACKET = ")";


    double calculate(String expr) {
        //получаем значения в виде обратной польской записи
        var rpn = getRPN(expr);
        //Считаем выражение и возвращаем результат
        return calculateRPN(rpn);
    }

    private List<String> getRPN(String expr) {
        var input = expr.split(OPERATOR_DELIMITER);
        List<String> output = new ArrayList<>();
        Deque<String> stack = new LinkedList<>();
        for (String token : input) {
            //Если это функция, то она обладает наивысшим приоритетом, по этому сразу считаем результат ее выполнения
            if (isFunc(token)) token = calcFunc(token);

            //Если это число - кладем в результат
            if (isNumber(token)) output.add(token);

            //Если это операция - смотрим на стек, если он не пуст,
            //и последний элемент не открывающаяся скобка
            //и приоритет того что лежит в стеке выше текущей чем у текущей операции
            //забираем операцию из стека в результат
            if (PRIORITY_MAP.containsKey(token)) {
                while (!stack.isEmpty()
                        && !Objects.equals(stack.peek(), String.valueOf(OPENING_BRACKET))
                        && PRIORITY_MAP.get(stack.peek()) >= PRIORITY_MAP.get(token)
                ) {
                    output.add(String.valueOf(stack.pop()));
                }
                stack.push(token);
            }

            //Если открывающая скобка - кладем в стек
            if (Objects.equals(token, String.valueOf(OPENING_BRACKET))) stack.push(token);

            //Если закрывающая скобка, пробегаем по стеку до открывающей скобки и кладем в результат все
            if (Objects.equals(token, CLOSING_BRACKET)) {
                while (!stack.isEmpty()
                        && !Objects.equals(stack.peek(), String.valueOf(OPENING_BRACKET))) {
                    output.add(stack.pop());
                }
                if (stack.isEmpty()) throw new RuntimeException("Mismatched '(' brackets");
                stack.pop();
            }
        }

        //Докладываем все в результат
        while (!stack.isEmpty()) {
            if (Objects.equals(stack.peek(), String.valueOf(OPENING_BRACKET)))
                throw new RuntimeException("Mismatched ')' brackets");
            output.add(stack.pop());
        }
        return output;
    }

    private String calcFunc(String input) {
        //Расчет значения для функций. Находим все числовые значения, и производим расчет
        for (String func : FUNCTIONS) {
            if (input.startsWith(func)) {
                var values = new ArrayList<Double>();
                var value = "";
                for (Character ch : input.toCharArray()) {
                    if ((((ch >= '0') && (ch <= '9')) || ch == '.')) {
                        value = value + ch;
                        continue;
                    }
                    if ((Objects.equals(String.valueOf(ch), CLOSING_BRACKET)
                            || Objects.equals(String.valueOf(ch), FUNCTION_PARAM_DELIMITER))
                    ) {
                        values.add(Double.parseDouble(value));
                        value = "";
                    }
                }
                switch (func) {
                    case "sin":
                        return String.valueOf(Math.sin(values.get(0)));
                    case "cos":
                        return String.valueOf(Math.cos(values.get(0)));
                    case "sqr":
                        return String.valueOf(Math.pow(values.get(0), 2));
                    case "pow":
                        return String.valueOf(Math.pow(values.get(0), values.get(1)));
                }
            }
        }
        return input;
    }

    private boolean isNumber(String s) {
        if (s.length() == 0) return false;
        for (Character ch : s.toCharArray()) {
            if (!(((ch >= '0') && (ch <= '9')) || ch == '.')) return false;
        }
        return true;
    }

    private boolean isFunc(String s) {
        for (String func : FUNCTIONS) {
            if (s.startsWith(func)) return true;
        }
        return false;
    }

    public static Double calculateRPN(List<String> elements) {
        Stack<String> stack = new Stack<>();
        for (String element : elements) {
            if (element.matches("\\b[+-]?\\d+(?:\\.\\d+)?\\b")) {
                // Число, добавляем в стек
                stack.push(element);
            } else {
                // Операция, вытаскиваем два последних числа из стека
                var b = Double.parseDouble(stack.pop());
                var a = Double.parseDouble(stack.pop());

                switch (element) {
                    case "+":
                        stack.push(String.valueOf(a + b));
                        break;
                    case "-":
                        stack.push(String.valueOf(a - b));
                        break;
                    case "*":
                        stack.push(String.valueOf(a * b));
                        break;
                    case "/":
                        stack.push(String.valueOf(a / b));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + element);
                }
            }
        }
        // Возвращаем результат, который должен быть последним элементом в стеке
        return Double.parseDouble(stack.peek());
    }
}
