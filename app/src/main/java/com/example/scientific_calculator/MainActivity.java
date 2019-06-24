package com.example.scientific_calculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends Activity {

    TextView calculation, solution;
    String mathCalculation = "", finalAnswer = "", first_num = "", second_num = "", operator = "", old_ans = "";
    String RorD = "RAD", function;
    Double Result = 0.0, firstNum = 0.0, secondNum = 0.0, temp = 0.0;
    Boolean currentDot = false, numberAccepted = true, currentRoot = false, invert_allow = true, currentPower = false;
    Boolean currentFactor = false, currentConstant = false, currentFunction = false, invertedValue = false;
    NumberFormat format, longFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculation = findViewById(R.id.solve);
        calculation.setMovementMethod(new ScrollingMovementMethod());
        solution = findViewById(R.id.value);

        format = new DecimalFormat("#.####");
        longFormat = new DecimalFormat("0.#E0");


        final Button btn_RorD = findViewById(R.id.btn_RorD);
        btn_RorD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RorD = btn_RorD.getText().toString();
                RorD = RorD.equals("RAD") ? "DEG" : "RAD";
                btn_RorD.setText(RorD);
            }
        });

    }

    public void onClickNumber(View v) {
        if (numberAccepted) {
            Button bn = (Button) v;
            mathCalculation += bn.getText();
            first_num += bn.getText();
            firstNum = Double.parseDouble(first_num);

            if (currentFunction) {
                calculateFunction(function);
                return;
            }
            if (currentRoot) {
                firstNum = Math.sqrt(firstNum);
            }
            switch (operator) {

                case "":
                    if (currentPower) {
                        temp = Result + Math.pow(secondNum, firstNum);
                    } else {
                        temp = Result + firstNum;
                    }
                    break;

                case "+":
                    if (currentPower) {
                        temp = Result + Math.pow(secondNum, firstNum);
                    } else {
                        temp = Result + firstNum;
                    }
                    break;

                case "-":
                    if (currentPower) {
                        temp = Result - Math.pow(secondNum, firstNum);
                    } else {
                        temp = Result - firstNum;
                    }
                    break;

                case "x":
                    if (currentPower) {
                        temp = Result * Math.pow(secondNum, firstNum);
                    } else {
                        temp = Result * firstNum;
                    }
                    break;

                case "/":
                    try {
                        if (currentPower) {
                            temp = Result / Math.pow(secondNum, firstNum);
                        } else {
                            temp = Result / firstNum;
                        }
                    } catch (Exception e) {
                        finalAnswer = e.getMessage();
                    }
                    break;

            }
            finalAnswer = format.format(temp).toString();
            updateCalculation();
        }
    }

    public void onClickOprator(View v) {
        Button ob = (Button) v;
        if (finalAnswer != "") {
            if (operator != "") {
                char c = getcharfromLast(mathCalculation, 2);
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    mathCalculation = mathCalculation.substring(0, mathCalculation.length() - 3);
                }
            }
            mathCalculation = mathCalculation + "\n" + ob.getText() + " ";
            first_num = "";
            Result = temp;
            operator = ob.getText().toString();
            updateCalculation();
            second_num = "";
            secondNum = 0.0;
            currentDot = false;
            numberAccepted = true;
            currentRoot = false;
            invert_allow = true;
            currentPower = false;
            currentFactor = false;
            currentConstant = false;
            currentFunction = false;
            invertedValue = false;
        }

    }

    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }

    public void onClickClear(View v) {
        cleardata();
    }

    public void cleardata() {
        mathCalculation = "";
        finalAnswer = "";
        operator = "";
        first_num = "";
        second_num = "";
        old_ans = "";
        Result = 0.0;
        firstNum = 0.0;
        secondNum = 0.0;
        temp = 0.0;
        updateCalculation();
        currentDot = false;
        numberAccepted = true;
        currentRoot = false;
        invert_allow = true;
        currentPower = false;
        currentFactor = false;
        currentFunction = false;
        currentConstant = false;
        invertedValue = false;
    }

    public void updateCalculation() {
        calculation.setText(mathCalculation);
        solution.setText(finalAnswer);
    }

    public void onDotClick(View view) {
        if (!currentDot) {
            if (first_num.length() == 0) {
                first_num = "0.";
                mathCalculation += "0.";
                finalAnswer = "0.";
                currentDot = true;
                updateCalculation();
            } else {
                first_num += ".";
                mathCalculation += ".";
                finalAnswer += ".";
                currentDot = true;
                updateCalculation();
            }
        }

    }

    public void onClickEqual(View view) {
        showresult();
    }

    public void showresult() {
        if (finalAnswer != "" && finalAnswer != old_ans) {
            mathCalculation += "\n= " + finalAnswer + "\n----------\n" + finalAnswer + " ";
            first_num = "";
            second_num = "";
            secondNum = 0.0;
            firstNum = 0.0;
            Result = temp;
            old_ans = finalAnswer;
            updateCalculation();
            currentDot = true;
            currentPower = false;
            numberAccepted = false;
            currentFactor = false;
            currentConstant = false;
            currentFunction = false;
            invertedValue = false;
        }
    }

    public void onModuloClick(View view) {
        if (finalAnswer != "" && getcharfromLast(mathCalculation, 1) != ' ') {
            mathCalculation += "% ";
            switch (operator) {
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = Result + ((Result * firstNum) / 100);
                    break;
                case "-":
                    temp = Result - ((Result * firstNum) / 100);
                    break;
                case "x":
                    temp = Result * (firstNum / 100);
                    break;
                case "/":
                    try {
                        temp = Result / (firstNum / 100);
                    } catch (Exception e) {
                        finalAnswer = e.getMessage();
                    }
                    break;
            }
            finalAnswer = format.format(temp).toString();
            if (finalAnswer.length() > 9) {
                finalAnswer = longFormat.format(temp).toString();
            }
            Result = temp;
            showresult();

        }
    }

    public void onPorMClick(View view) {
        if (invert_allow) {
            if (finalAnswer != "" && getcharfromLast(mathCalculation, 1) != ' ') {
                firstNum = firstNum * (-1);
                first_num = format.format(firstNum).toString();
                switch (operator) {
                    case "":
                        temp = firstNum;
                        mathCalculation = first_num;
                        break;
                    case "+":
                        temp = Result + firstNum;
                        removeuntilchar(mathCalculation, ' ');
                        mathCalculation += first_num;
                        break;
                    case "-":
                        temp = Result - firstNum;
                        removeuntilchar(mathCalculation, ' ');
                        mathCalculation += first_num;
                        break;
                    case "*":
                        temp = Result * firstNum;
                        removeuntilchar(mathCalculation, ' ');
                        mathCalculation += first_num;
                        break;
                    case "/":
                        try {
                            temp = Result / firstNum;
                            removeuntilchar(mathCalculation, ' ');
                            mathCalculation += first_num;
                        } catch (Exception e) {
                            finalAnswer = e.getMessage();
                        }
                        break;
                }
                finalAnswer = format.format(temp).toString();
                invertedValue = invertedValue ? false : true;
                updateCalculation();
            }
        }
    }

    public void removeuntilchar(String str, char chr) {
        char c = getcharfromLast(str, 1);
        if (c != chr) {
            str = removechar(str, 1);
            mathCalculation = str;
            updateCalculation();
            removeuntilchar(str, chr);
        }
    }

    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - i);
        if (c == '.' && !currentDot) {
            currentDot = false;
        }
        if (c == '^') {
            currentPower = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }

    public void onRootClick(View view) {
        Button root = (Button) view;
        if (finalAnswer == "" && Result == 0 && !currentRoot && !currentFunction) {
            mathCalculation = root.getText().toString();
            currentRoot = true;
            invert_allow = false;
            updateCalculation();
        } else if (getcharfromLast(mathCalculation, 1) == ' ' && operator != "" && !currentRoot) {
            mathCalculation += root.getText().toString();
            currentRoot = true;
            invert_allow = false;
            updateCalculation();
        }
    }

    public void onPowerClick(View view) {
        Button power = (Button) view;
        if (mathCalculation != "" && !currentRoot && !currentPower && !currentFunction) {
            if (getcharfromLast(mathCalculation, 1) != ' ') {
                mathCalculation += power.getText().toString();
                second_num = first_num;
                secondNum = firstNum;
                first_num = "";
                currentPower = true;
                updateCalculation();
            }
        }
    }

    public void onSquareClick(View view) {
        if (mathCalculation != "" && finalAnswer != "") {
            if (!currentRoot && !currentFunction && !currentPower && getcharfromLast(mathCalculation, 1) != ' ' && getcharfromLast(mathCalculation, 1) != ' ') {
                firstNum = firstNum * firstNum;
                first_num = format.format(firstNum).toString();
                if (operator == "") {
                    if (first_num.length() > 9) {
                        first_num = longFormat.format(firstNum);
                    }
                    mathCalculation = first_num;
                    temp = firstNum;
                } else {
                    switch (operator) {
                        case "+":
                            temp = Result + firstNum;
                            break;
                        case "-":
                            temp = Result - firstNum;
                            break;
                        case "x":
                            temp = Result * firstNum;
                            break;
                        case "/":
                            try {
                                temp = Result / firstNum;
                            } catch (Exception e) {
                                finalAnswer = e.getMessage();
                            }
                            break;
                    }
                    removeuntilchar(mathCalculation, ' ');
                    if (first_num.length() > 9) {
                        first_num = longFormat.format(firstNum);
                    }
                    mathCalculation += first_num;
                }
                finalAnswer = format.format(temp);
                if (finalAnswer.length() > 9) {
                    finalAnswer = longFormat.format(temp);
                }
                updateCalculation();
            }
        }
    }


    public void onClickPIorE(View view) {
        Button btn_PIorE = (Button) view;
        numberAccepted = false;
        if (!currentRoot && !currentDot && !currentPower && !currentFactor && !currentConstant && !currentFunction) {
            String str_PIorE = btn_PIorE.getText().toString() + " ";
            if (!str_PIorE.equals("e ")) {
                str_PIorE = "\u03A0" + " ";
            }
            if (mathCalculation == "") {
                first_num = str_PIorE;
                if (str_PIorE.equals("e ")) {
                    firstNum = Math.E;
                } else {
                    firstNum = Math.PI;
                }
                temp = firstNum;
            } else {
                if (str_PIorE.equals("e ")) {
                    firstNum = getcharfromLast(mathCalculation, 1) == ' ' ? Math.E : Double.parseDouble(first_num) * Math.E;
                } else {
                    firstNum = getcharfromLast(mathCalculation, 1) == ' ' ? Math.PI : Double.parseDouble(first_num) * Math.PI;
                }
                switch (operator) {

                    case "":
                        temp = Result + firstNum;
                        break;

                    case "+":
                        temp = Result + firstNum;
                        break;

                    case "-":
                        temp = Result - firstNum;
                        break;

                    case "x":
                        temp = Result * firstNum;
                        break;

                    case "/":
                        try {
                            temp = Result / firstNum;
                        } catch (Exception e) {
                            finalAnswer = e.getMessage();
                        }
                        break;
                }
            }
            mathCalculation += str_PIorE;
            finalAnswer = format.format(temp).toString();
            updateCalculation();
            currentConstant = true;
        }
    }

    public void onClickFunction(View view) {
        Button func = (Button) view;
        function = func.getHint().toString();
        if (!currentFunction && !currentRoot && !currentPower && !currentFactor && !currentDot) {
            calculateFunction(function);

        }
    }

    public void calculateFunction(String function) {
        currentFunction = true;
        if (operator != "" && getcharfromLast(mathCalculation, 1) == ' ') {
            switch (function) {
                default:
                    mathCalculation += function + "(";
                    break;
            }
            updateCalculation();
        } else {
            switch (operator) {
                case "":
                    if (mathCalculation.equals("")) {
                        switch (function) {
                            default:
                                mathCalculation += function + "( ";
                                break;
                        }
                    } else {
                        switch (function) {
                            case "sin":
                                if (RorD.equals("DEG")) {
                                    firstNum = Math.toDegrees(firstNum);
                                }
                                temp = Result + Math.sin(firstNum* Math.PI / 180);
                                mathCalculation = "sin( " + first_num;
                                break;


                            case "cos":
                                if (RorD.equals("DEG")) {
                                    firstNum = Math.toDegrees(firstNum);
                                }
                                temp = Result + Math.cos(firstNum * Math.PI / 180);
                                mathCalculation = "cos( " + first_num;
                                break;

                            case "tan":
                                if (RorD.equals("DEG")) {
                                    firstNum = Math.toDegrees(firstNum);
                                }
                                temp = Result + Math.tan(firstNum * Math.PI / 180);
                                mathCalculation = "tan( " + first_num;
                                break;

                        }
                    }
                    finalAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "+":
                    removeuntilchar(mathCalculation, ' ');
                    switch (function) {
                        case "log":
                            temp = Result + Math.log10(firstNum);
                            mathCalculation += "log(" + first_num;
                            break;

                        case "ln":
                            temp = Result + Math.log(firstNum);
                            mathCalculation += "ln(" + first_num;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result + Math.sin(firstNum * Math.PI / 180);
                            mathCalculation += "sin(" + first_num;
                            break;


                        case "cos":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result + Math.cos(firstNum * Math.PI / 180);
                            mathCalculation += "cos(" + first_num;
                            break;


                        case "tan":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result + Math.tan(firstNum * Math.PI / 180);
                            mathCalculation += "tan(" + first_num;
                            break;

                    }
                    finalAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "-":
                    removeuntilchar(mathCalculation, ' ');
                    switch (function) {

                        case "sin":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result - Math.sin(firstNum * Math.PI / 180);
                            mathCalculation += "sin(" + first_num;
                            break;

                        case "cos":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum );
                            }
                            temp = Result - Math.cos(firstNum * Math.PI / 180);
                            mathCalculation += "cos(" + first_num;
                            break;

                        case "tan":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result - Math.tan(firstNum * Math.PI / 180);
                            mathCalculation += "tan(" + first_num;
                            break;
                    }
                    finalAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "x":
                    removeuntilchar(mathCalculation, ' ');
                    switch (function) {

                        case "sin":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result * Math.sin(firstNum * Math.PI / 180);
                            mathCalculation += "sin(" + first_num;
                            break;


                        case "cos":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result * Math.cos(firstNum * Math.PI / 180);
                            mathCalculation += "cos(" + first_num;
                            break;


                        case "tan":
                            if (RorD.equals("DEG")) {
                                firstNum = Math.toDegrees(firstNum);
                            }
                            temp = Result * Math.tan(firstNum * Math.PI / 180);
                            mathCalculation += "tan(" + first_num;
                            break;

                    }
                    finalAnswer = temp.toString();
                    updateCalculation();
                    break;

                case "/":
                    removeuntilchar(mathCalculation, ' ');
                    switch (function) {

                        case "sin":
                            try {
                                if (RorD.equals("DEG")) {
                                    firstNum = Math.toDegrees(firstNum);
                                }
                                temp = Result / Math.sin(firstNum * Math.PI / 180);
                                mathCalculation += "sin(" + first_num;
                            } catch (Exception e) {
                                finalAnswer = e.getMessage();
                            }
                            break;


                        case "cos":
                            try {
                                if (RorD.equals("DEG")) {
                                    firstNum = Math.toDegrees(firstNum);
                                }
                                temp = Result / Math.cos(firstNum * Math.PI / 180);
                                mathCalculation += "cos(" + first_num;
                            } catch (Exception e) {
                                finalAnswer = e.getMessage();
                            }
                            break;

                        case "tan":
                            try {
                                if (RorD.equals("DEG")) {
                                    firstNum = Math.toDegrees(firstNum);
                                }
                                temp = Result / Math.tan(firstNum * Math.PI / 180);
                                mathCalculation += "tan(" + first_num;
                            } catch (Exception e) {
                                finalAnswer = e.getMessage();
                            }
                            break;

                    }
                    finalAnswer = temp.toString();
                    updateCalculation();
                    break;
            }
        }
    }

    public void onClickDelete(View view) {
        if (currentFunction) {
            DeleteFunction();
            return;
        }
        if (currentRoot) {
            removeRoot();
            return;
        }
        if (currentPower) {
            removePower();
            return;
        }
        if (finalAnswer != "") {
            if (getcharfromLast(mathCalculation, 1) != ' ') {
                if (first_num.length() < 2 && operator != "") {
                    first_num = "";
                    temp = Result;
                    finalAnswer = format.format(Result).toString();
                    mathCalculation = removechar(mathCalculation, 1);
                    updateCalculation();
                } else {
                    switch (operator) {
                        case "":
                            if (invertedValue) {
                                finalAnswer = finalAnswer.substring(1, finalAnswer.length());
                                mathCalculation = mathCalculation.substring(1, finalAnswer.length());
                                updateCalculation();
                            }
                            if (mathCalculation.length() < 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(mathCalculation, 1) == '.') {
                                    currentDot = false;
                                }
                                first_num = removechar(first_num, 1);
                                firstNum = Double.parseDouble(first_num);
                                temp = firstNum;
                                mathCalculation = first_num;
                                finalAnswer = first_num;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (invertedValue) {
                                firstNum = firstNum * (-1);
                                first_num = format.format(firstNum).toString();
                                temp = Result + firstNum;
                                finalAnswer = format.format(temp).toString();
                                removeuntilchar(mathCalculation, ' ');
                                mathCalculation += first_num;
                                updateCalculation();
                                invertedValue = invertedValue ? false : true;
                            }
                            if (getcharfromLast(mathCalculation, 1) == '.') {
                                currentDot = false;
                            }
                            first_num = removechar(first_num, 1);
                            if (first_num.length() == 1 && first_num == ".") {
                                firstNum = Double.parseDouble(first_num);
                            }
                            firstNum = Double.parseDouble(first_num);
                            temp = Result + firstNum;
                            finalAnswer = format.format(temp).toString();
                            mathCalculation = removechar(mathCalculation, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (invertedValue) {
                                firstNum = firstNum * (-1);
                                first_num = format.format(firstNum).toString();
                                temp = Result - firstNum;
                                finalAnswer = format.format(temp).toString();
                                removeuntilchar(mathCalculation, ' ');
                                mathCalculation += first_num;
                                updateCalculation();
                                invertedValue = invertedValue ? false : true;
                            }
                            if (getcharfromLast(mathCalculation, 1) == '.') {
                                currentDot = false;
                            }
                            first_num = removechar(first_num, 1);
                            if (first_num.length() == 1 && first_num == ".") {
                                firstNum = Double.parseDouble(first_num);
                            }
                            firstNum = Double.parseDouble(first_num);
                            temp = Result - firstNum;
                            finalAnswer = format.format(temp).toString();
                            mathCalculation = removechar(mathCalculation, 1);
                            updateCalculation();
                            break;

                        case "x":
                            if (invertedValue) {
                                firstNum = firstNum * (-1);
                                first_num = format.format(firstNum).toString();
                                temp = Result * firstNum;
                                finalAnswer = format.format(temp).toString();
                                removeuntilchar(mathCalculation, ' ');
                                mathCalculation += first_num;
                                updateCalculation();
                                invertedValue = invertedValue ? false : true;
                            }
                            if (getcharfromLast(mathCalculation, 1) == '.') {
                                currentDot = false;
                            }
                            first_num = removechar(first_num, 1);
                            if (first_num.length() == 1 && first_num == ".") {
                                firstNum = Double.parseDouble(first_num);
                            }
                            firstNum = Double.parseDouble(first_num);
                            temp = Result * firstNum;
                            finalAnswer = format.format(temp).toString();
                            mathCalculation = removechar(mathCalculation, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (invertedValue) {
                                    firstNum = firstNum * (-1);
                                    first_num = format.format(firstNum).toString();
                                    temp = Result / firstNum;
                                    finalAnswer = format.format(temp).toString();
                                    removeuntilchar(mathCalculation, ' ');
                                    mathCalculation += first_num;
                                    updateCalculation();
                                    invertedValue = invertedValue ? false : true;
                                }
                                if (getcharfromLast(mathCalculation, 1) == '.') {
                                    currentDot = false;
                                }
                                first_num = removechar(first_num, 1);
                                if (first_num.length() == 1 && first_num == ".") {
                                    firstNum = Double.parseDouble(first_num);
                                }
                                firstNum = Double.parseDouble(first_num);
                                temp = Result / firstNum;
                                finalAnswer = format.format(temp).toString();
                                mathCalculation = removechar(mathCalculation, 1);
                            } catch (Exception e) {
                                finalAnswer = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }

    public void removePower() {
        if (finalAnswer != "" && mathCalculation != "") {
            switch (operator) {
                case "":
                    if (getcharfromLast(mathCalculation, 1) == '^') {
                        mathCalculation = removechar(mathCalculation, 1);
                        first_num = second_num;
                        firstNum = Double.parseDouble(first_num);
                        second_num = "";
                        secondNum = 0.0;
                        updateCalculation();
                    } else if (getcharfromLast(mathCalculation, 2) == '^') {
                        first_num = "";
                        firstNum = 0.0;
                        temp = secondNum;
                        finalAnswer = format.format(temp).toString();
                        mathCalculation = removechar(mathCalculation, 1);
                        updateCalculation();
                    } else {
                        if (getcharfromLast(mathCalculation, 1) == '.') {
                            currentDot = false;
                        }
                        first_num = removechar(first_num, 1);
                        firstNum = Double.parseDouble(first_num);
                        temp = Math.pow(secondNum, firstNum);
                        finalAnswer = format.format(temp).toString();
                        mathCalculation = removechar(mathCalculation, 1);
                        updateCalculation();
                    }
                    break;
            }
        }
    }

    public void removeRoot() {
        if (getcharfromLast(mathCalculation, 1) != ' ') {
            if (String.valueOf(getcharfromLast(mathCalculation, 1)).equals("\u221A")) {
                mathCalculation = removechar(mathCalculation, 1);
                currentRoot = false;
                invert_allow = true;
                updateCalculation();
            }
            if (finalAnswer != "") {
                if (first_num.length() < 2 && operator != "") {
                    first_num = "";
                    firstNum = Result;
                    temp = Result;
                    finalAnswer = format.format(Result).toString();
                    mathCalculation = removechar(mathCalculation, 1);
                    updateCalculation();
                } else {
                    switch (operator) {
                        case "":
                            if (mathCalculation.length() <= 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(mathCalculation, 1) == '.') {
                                    currentDot = false;
                                }
                                first_num = removechar(first_num, 1);
                                firstNum = Double.parseDouble(first_num);
                                firstNum = Math.sqrt(firstNum);
                                temp = firstNum;
                                finalAnswer = format.format(temp).toString();
                                mathCalculation = "\u221A" + first_num;
                                updateCalculation();
                            }
                            break;

                        case "+":
                            if (getcharfromLast(mathCalculation, 1) == '.') {
                                currentDot = false;
                            }
                            first_num = removechar(first_num, 1);
                            if (first_num.length() == 1 && first_num == ".") {
                                firstNum = Double.parseDouble(first_num);
                            }
                            firstNum = Double.parseDouble(first_num);
                            firstNum = Math.sqrt(firstNum);
                            temp = Result + firstNum;
                            finalAnswer = format.format(temp).toString();
                            mathCalculation = removechar(mathCalculation, 1);
                            updateCalculation();
                            break;

                        case "-":
                            if (getcharfromLast(mathCalculation, 1) == '.') {
                                currentDot = false;
                            }
                            first_num = removechar(first_num, 1);
                            if (first_num.length() == 1 && first_num == ".") {
                                firstNum = Double.parseDouble(first_num);
                            }
                            firstNum = Double.parseDouble(first_num);
                            firstNum = Math.sqrt(firstNum);
                            temp = Result - firstNum;
                            finalAnswer = format.format(temp).toString();
                            mathCalculation = removechar(mathCalculation, 1);
                            updateCalculation();
                            break;

                        case "x":
                            if (getcharfromLast(mathCalculation, 1) == '.') {
                                currentDot = false;
                            }
                            first_num = removechar(first_num, 1);
                            if (first_num.length() == 1 && first_num == ".") {
                                firstNum = Double.parseDouble(first_num);
                            }
                            firstNum = Double.parseDouble(first_num);
                            firstNum = Math.sqrt(firstNum);
                            temp = Result * firstNum;
                            finalAnswer = format.format(temp).toString();
                            mathCalculation = removechar(mathCalculation, 1);
                            updateCalculation();
                            break;

                        case "/":
                            try {
                                if (getcharfromLast(mathCalculation, 1) == '.') {
                                    currentDot = false;
                                }
                                first_num = removechar(first_num, 1);
                                if (first_num.length() == 1 && first_num == ".") {
                                    firstNum = Double.parseDouble(first_num);
                                }
                                firstNum = Double.parseDouble(first_num);
                                firstNum = Math.sqrt(firstNum);
                                temp = Result + firstNum;
                                finalAnswer = format.format(temp).toString();
                                mathCalculation = removechar(mathCalculation, 1);
                            } catch (Exception e) {
                                finalAnswer = e.getMessage();
                            }
                            updateCalculation();
                            break;
                    }
                }
            }
        }
    }
            public void DeleteFunction () {
                if (operator == "") {
                    if (getcharfromLast(mathCalculation, 1) == ' ') {
                        cleardata();
                    } else if (getcharfromLast(mathCalculation, 2) == ' ') {
                        cleardata();
                    } else {
                        mathCalculation = removechar(mathCalculation, 1);
                        first_num = removechar(first_num, 1);
                        firstNum = Double.parseDouble(first_num);
                        calculateFunction(function);
                    }
                    updateCalculation();
                } else {
                    if (getcharfromLast(mathCalculation, 1) == '(') {
                        removeuntilchar(mathCalculation, ' ');
                        currentFunction = false;
                    } else if (getcharfromLast(mathCalculation, 2) == '(') {
                        mathCalculation = removechar(mathCalculation, 1);
                        first_num = "";
                        temp = Result;
                        finalAnswer = format.format(Result).toString();
                    } else {
                        mathCalculation = removechar(mathCalculation, 1);
                        first_num = removechar(first_num, 1);
                        firstNum = Double.parseDouble(first_num);
                        calculateFunction(function);
                    }
                    updateCalculation();
                }
            }
        }

