package com.example.androidcalculator;// Vito Luong
// SID:41914256
// CS427
// I pledge that this submission is solely my work, and that I have neither given, nor received help from anyone.

import java.util.*;
import java.util.function.Function;

public class Calculator
{
    // left and right associates-constants for operators
    private static final int leftA  = 0;
    private static final int rightA = 1;

    // Operators and functions hashmap
    private static final Map<String, int[]> operators = new HashMap<String, int[]>();
    static
    {
        // Map<"token", []{precendence, associativity}>
        // prior 0
        operators.put("+", new int[] { 0, leftA });
        operators.put("-", new int[] { 0, leftA });
        // prior 5
        operators.put("*", new int[] { 5, leftA });
        operators.put("/", new int[] { 5, leftA });
        operators.put("^", new int[] { 8, leftA });

        // prior 10
        operators.put("sin", new int[] { 10, leftA });
        operators.put("cos", new int[] { 10, leftA });
        operators.put("tan", new int[] { 10, leftA });
        operators.put("cot", new int[] { 10, leftA });
        operators.put("asin", new int[] { 10, leftA });
        operators.put("acos", new int[] { 10, leftA });
        operators.put("atan", new int[] { 10, leftA });
        operators.put("acot", new int[] { 10, leftA });
        operators.put("log", new int[] { 10, leftA });
        operators.put("ln", new int[] { 10, leftA });

    }



    // Test if token is an operator
    private static boolean isOperator(String token)
    {
        boolean state = operators.containsKey(token);
        return state;
    }

    // Test if is function
    private static boolean isFunction(String token)
    {
        boolean state = token.equals("sin") ||
                token.equals("cos") ||
                token.equals("tan") ||
                token.equals("cot") ||
                token.equals("asin") ||
                token.equals("acos") ||
                token.equals("atan") ||
                token.equals("acot") ||
                token.equals("log") ||
                token.equals("ln") ;
        return state;
    }

    // check if token is associated left or right
    private static boolean isAssociative(String token, int type)
    {
        if (!isOperator(token))
        {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        int stateType = operators.get(token)[1];
        if (stateType == type) {
            return true;
        }
        return false;
    }

    // Compare precedence of operators.    
    private static final int comparePrecedence(String token1, String token2)
    {
        if (!isOperator(token1) || !isOperator(token2))
        {
            throw new IllegalArgumentException("Invalid tokens: " + token1
                    + " " + token2);
        }
        int state1 = operators.get(token1)[0];
        int state2 = operators.get(token2)[0];
        return state1 - state2;
    }

    // Convert infix expression format into Reverse Polish Notation
    public static String[] infixToRPN(String[] inputTokens)
    {
        ArrayList<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();

        // For each token
        for (int i = 0; i < inputTokens.length; i++){
            String token =inputTokens[i];


            // If token is an operator
            if (isOperator(token))
            {
                // Dealing with multiple operators
                // While stack not empty AND stack top element 
                // is an operator
                while (!stack.empty() && isOperator(stack.peek()))
                {
                    if ((isAssociative(token, leftA)         &&
                            comparePrecedence(token, stack.peek()) <= 0) ||
                            (isAssociative(token, rightA)        &&
                                    comparePrecedence(token, stack.peek()) < 0))
                    {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }


                // Push the new operator on the stack
                stack.push(token);
            }
            // If token is a left bracket '('
            else if (token.equals("("))
            {
                stack.push(token);  // 
            }
            // If token is a right bracket ')'
            else if (token.equals(")"))
            {
                while (!stack.empty() && !stack.peek().equals("("))
                {
                    out.add(stack.pop());
                }
                stack.pop();
            }
            // If token is a number
            else
            {
                out.add(token);
            }
        }
        while (!stack.empty())
        {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static double evaluateRPN(String[] tokens)
    {
        Stack<String> stack = new Stack<String>();

        // For each token 
        for (String token : tokens)
        {
            // If the token is a value push it onto the stack

            if (!isOperator(token) && !isFunction(token))
            {
                if(token.equals("pi")){
                    token = String.valueOf(Math.PI);
                }
                if(token.equals("e")){
                    token = String.valueOf(Math.E);
                }

                stack.push(token);
            }
            // If token is function: pop one entry
            else if (isFunction(token)){

                double d = Double.valueOf( stack.pop() );
                Double result = token.compareTo("sin") == 0 ? Math.sin(d) :
                        token.compareTo("cos") == 0 ? Math.cos(d) :
                                token.compareTo("tan") == 0 ? Math.tan(d) :
                                        token.compareTo("cot") == 0 ? (1/Math.tan(d)) :
                                                token.compareTo("asin") == 0 ? Math.asin(d) :
                                                        token.compareTo("acos") == 0 ? Math.acos(d) :
                                                                token.compareTo("acos") == 0 ? Math.atan(d) :
                                                                        token.compareTo("acot") == 0 ? Math.atan(1/d) :
                                                                                token.compareTo("log") == 0 ? Math.log10(d) :
                                                                                        Math.log(d);



                stack.push( String.valueOf( result ));
            } else {
                // Token is an operator: pop top two entries
                Double d2 = Double.valueOf( stack.pop() );
                Double d1 = Double.valueOf( stack.pop() );

                // Get the result
                Double result = token.compareTo("+") == 0 ? d1 + d2 :
                        token.compareTo("-") == 0 ? d1 - d2 :
                                token.compareTo("*") == 0 ? d1 * d2 :
                                        token.compareTo("/") == 0 ? d1 / d2 :
                                                Math.pow(d1, d2);

                // Push result onto stack
                stack.push( String.valueOf( result ));
            }
        }

        return Double.valueOf(stack.pop());
    }

    // evaluate by convert from expression to Reverse polish notation then evaluate the answer
    public double evaluate(String inp)
    {
        String[] input = inp.split(" ");
        input = StringProcessor(input);
        String[] output = infixToRPN(input);

        return evaluateRPN( output );
    }

    // String processor to add "-" if user did not.
    private static String[] StringProcessor(String[] inp) {
        ArrayList<String> temp = new ArrayList<String>();

        for (int i = 0; i < inp.length; i++){

            if (inp[i].equals("-")) {
                if (i==0 || inp[i-1].equals("(")){
                    temp.add("0");
                    temp.add("-");
                }else{
                    temp.add("-");
                }
            }else{
                temp.add(inp[i]);
            }
        }
        String[] result = temp.toArray(new String[0]);

        return result;
    }

    // DEMO
    public static void main(String args[])
    {
        String input = "acot ( pi / 2 )";
        Calculator parser = new Calculator();

        double output = parser.evaluate(input);
        System.out.println(output);

    }
}

