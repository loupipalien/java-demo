package com.ltchen.demo.spring.framework.expression;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author: 01139983
 */
public class Evaluation {

    public static void main(String[] args) {
        evaluateTheLiteralStringExpression(); printlnSeparatorLine();
        retrieveTheNamePropertyFromInstance(); printlnSeparatorLine();
        typeConversionInEvaluationContext(); printlnSeparatorLine();
        parserConfiguration(); printlnSeparatorLine();
        compilerConfiguration(); printlnSeparatorLine();
    }

    public static void printlnSeparatorLine() {
        System.out.println("===========================================");
    }

    public static void evaluateTheLiteralStringExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        // parser
        Expression expression = parser.parseExpression("'Hello World!'");
        String message = (String) expression.getValue();
        System.out.println(message);
        // invoke method
        expression = parser.parseExpression("'Hello World'.concat('!')");
        message = (String) expression.getValue();
        System.out.println(message);
        // invokes javabean property
        expression = parser.parseExpression("'Hello World!'.bytes");
        byte[] bytes = (byte[]) expression.getValue();
        System.out.println(bytes);
        // invokes nested property
        expression = parser.parseExpression("'Hello World!'.bytes.length");
        int length = (Integer) expression.getValue();
        System.out.println(length);
        // invokes constructor
        expression = parser.parseExpression("new String('hello world!').toUpperCase()");
        message = expression.getValue(String.class);
        System.out.println(message);
    }

    public static void retrieveTheNamePropertyFromInstance() {
        class Inventor {
            public String name;
            public Date birthday;
            public String nationality;

            public Inventor(String name, Date birthday, String nationality) {
                this.name = name;
                this.birthday = birthday;
                this.nationality = nationality;
            }
        }

        // Create and set a calendar
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(1856, 7, 9);

        // The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", calendar.getTime(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("name");
        String name = (String) expression.getValue(tesla);
        System.out.println(name);

        // name == "Nikola Tesla"
        expression = parser.parseExpression("name == 'Nikola Tesla'");
        boolean result = expression.getValue(tesla, Boolean.class);
        // result == true
        System.out.println(result);
    }

    public static void typeConversionInEvaluationContext() {
        class Simple {
            public List<Boolean> booleanList = new ArrayList<Boolean>();
        }

        Simple simple = new Simple();
        simple.booleanList.add(true);

        EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();
        // "false" is passed in here as a String. SpEL and the conversion service
        // will recognize that it needs to be a Boolean and convert it accordingly.
        parser.parseExpression("booleanList[0]").setValue(context, simple, "false");

        // flag is false
        Boolean flag = simple.booleanList.get(0);
        System.out.println(flag);
    }

    public static void parserConfiguration() {
        class Demo {
            public List<String> list;
        }

        // Turn on:
        // - auto null reference initialization
        // - auto collection growing
        SpelParserConfiguration config = new SpelParserConfiguration(true,true);

        ExpressionParser parser = new SpelExpressionParser(config);
        Expression expression = parser.parseExpression("list[3]");
        Demo demo = new Demo();
        Object obj = expression.getValue(demo);

        // demo.list will now be a real collection of 4 entries
        // Each entry is a new empty String
        System.out.println(demo.list);
    }

    public static void compilerConfiguration() {
        class Message {
            public Object payload;
        }

        SpelParserConfiguration configuration = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, Evaluation.class.getClassLoader());

        SpelExpressionParser parser = new SpelExpressionParser(configuration);
        Expression expr = parser.parseExpression("payload");

        Message message = new Message();
        Object payload = expr.getValue(message);
        System.out.println(payload);
    }


}
