import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    Stack<String> numS = new Stack<>();
    Stack<String> opS = new Stack<>();

    Stack<String> undos = new Stack<>();
    Stack<String> redos = new Stack<>();

    public float cal(float a, float b, char op) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                break;
        }
        return 0;
    }

    public String ScanInput(String input) {
        int n = input.length();
        int i = 0;
        while (i < n) {
            char e = input.charAt(i);
            if (Character.isDigit(e)) {
                String num = String.valueOf(e);
                while ((i + 1) < n && (Character.isDigit(input.charAt(i + 1)) || input.charAt(i + 1) == '.')) {
                    num += input.charAt(i + 1);
                    i++;
                }
                float fnum = Float.valueOf(Math.round(Float.parseFloat(num) * 10000)) / 10000;
                numS.add(String.valueOf(fnum));
            } else if (e == '+' || e == '-') {
                while (opS.size() > 0 && !opS.peek().equals("(") && !opS.peek().equals(")")) {
                    String a = numS.pop();
                    String b = numS.pop();
                    String op = opS.pop();
                    numS.add(String.valueOf(cal(Float.valueOf(b), Float.valueOf(a), op.charAt(0))));
                }
                opS.add(String.valueOf(e));
            } else if (e == '*' || e == '/') {
                while (opS.size() > 0 && (opS.peek().equals("*") || opS.peek().equals("/"))) {
                    String a = numS.pop();
                    String b = numS.pop();
                    String op = opS.pop();
                    numS.add(String.valueOf(cal(Float.valueOf(b), Float.valueOf(a), op.charAt(0))));
                }
                opS.add(String.valueOf(e));
            } else if (e == '(') {
                opS.add(String.valueOf(e));
            } else if (e == ')') {
                while (opS.size() > 0) {
                    String op = opS.pop();
                    if (op.equals("("))
                        break;
                    String a = numS.pop();
                    String b = numS.pop();

                    numS.add(String.valueOf(cal(Float.valueOf(b), Float.valueOf(a), op.charAt(0))));
                }
            }

            i++;
        }
        float result = 0f;
        while (opS.size() > 0) {
            String a = numS.pop();
            String b = numS.pop();
            String op = opS.pop();
            numS.add(String.valueOf(cal(Float.valueOf(b), Float.valueOf(a), op.charAt(0))));
        }

        return numS.pop();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();
        String tempLine = "";
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("exit")) {
                break;
            }
            if (line.equals("undo")) {

                if (calculator.undos.size() > 0) {
                    String a = calculator.undos.pop();
                    String b = calculator.undos.size()>0?calculator.undos.peek():"";

                    System.out.println(b);
                    if(b.length()>0){
                        calculator.redos.push(a);
                        System.out.println(calculator.ScanInput(b));
                    }
                }

                continue;
            }

            if (line.equals("redo")) {
                if (calculator.redos.size() > 0) {
                    String a = calculator.redos.pop();
                    System.out.println(a);
                    System.out.println(calculator.ScanInput(a));
                    calculator.undos.push(a);
                }

                continue;
            }

            if (line.length() > 0) {
                System.out.println(calculator.ScanInput(line));
                calculator.undos.push(new String(line));
            }

        }
    }
}
