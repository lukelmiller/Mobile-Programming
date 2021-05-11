package edu.csce4623.lukelmiller.basiccalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import java.lang.Math;
import java.math.*;

//Developer: Luke Miller
//Project: Basic Calculator
//Class: Mobile Programming
//Date:9.11.20

//Main Activity
public class MainActivity extends AppCompatActivity {

    //General Vars to keep track of results/inputs/operation/Flag determines what sent equals() command
    static String inputStr = "";
    static String opperand = "";
    private double result = 0;
    static double right = 0;
    static boolean equalSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button Listeners
        findViewById(R.id.Button0).setOnClickListener(bListen);
        findViewById(R.id.Button1).setOnClickListener(bListen);
        findViewById(R.id.Button2).setOnClickListener(bListen);
        findViewById(R.id.Button3).setOnClickListener(bListen);
        findViewById(R.id.Button4).setOnClickListener(bListen);
        findViewById(R.id.Button5).setOnClickListener(bListen);
        findViewById(R.id.Button6).setOnClickListener(bListen);
        findViewById(R.id.Button7).setOnClickListener(bListen);
        findViewById(R.id.Button8).setOnClickListener(bListen);
        findViewById(R.id.Button9).setOnClickListener(bListen);
        findViewById(R.id.ButtonForClear).setOnClickListener(bListen);
        findViewById(R.id.ButtonForDeci).setOnClickListener(bListen);
        findViewById(R.id.ButtonForDiv).setOnClickListener(bListen);
        findViewById(R.id.ButtonForMinus).setOnClickListener(bListen);
        findViewById(R.id.ButtonForMulti).setOnClickListener(bListen);
        findViewById(R.id.ButtonForPlus).setOnClickListener(bListen);
        findViewById(R.id.ButtonForBack).setOnClickListener(bListen);
        findViewById(R.id.ButtonForNeg).setOnClickListener(bListen);
        findViewById(R.id.ButtonForEqual).setOnClickListener(bListen);


    }

    //Button Listener Function & Sorting Button Functions
    private View.OnClickListener bListen = new View.OnClickListener(){

        @Override
        public void onClick(View v) {



            //Switch to determine which button was pressed and take action
            switch(v.getId()){

                
                //Number Buttons

                //0 Case checks for a zero before and a length of atleast 1
                case R.id.Button0 :
                    if((inputStr.length() > 0) && (inputStr.charAt(0) != '0')) {
                        append("0");
                    }
                    else{
                        displayEdit("0");
                    }
                    break;
                case R.id.Button1 :
                    append("1");
                    break;
                case R.id.Button2 :
                    append("2");
                    break;
                case R.id.Button3 :
                    append("3");
                    break;
                case R.id.Button4 :
                    append("4");
                    break;
                case R.id.Button5 :
                    append("5");
                    break;
                case R.id.Button6 :
                    append("6");
                    break;
                case R.id.Button7 :
                    append("7");
                    break;
                case R.id.Button8 :
                    append("8");
                    break;
                case R.id.Button9 :
                    append("9");
                    break;
                    
                 //Operation buttons
                case R.id.ButtonForDiv :
                    setOpperand("/");
                    break;
                case R.id.ButtonForMinus :
                    setOpperand("-");
                    break;
                case R.id.ButtonForMulti :
                    setOpperand("*");
                    break;
                case R.id.ButtonForPlus :
                    setOpperand("+");
                    break;


                //Data Modifier Buttons

                //Button for Decimal
                //If there is not a decimal already in the string add one
                case R.id.ButtonForDeci :
                        if (inputStr.indexOf('.') == -1) {
                            append(".");
                        }

                    break;

                //Changes sign for number for both inputs and results
                case R.id.ButtonForNeg :
                    if(inputStr.length() != 0) {
                        if (inputStr.charAt(0) != '-') {
                            inputStr = "-" + inputStr;
                            displayEdit(inputStr);
                        } else {
                            inputStr = inputStr.substring(1);
                            displayEdit(inputStr);
                        }
                    }
                    else if(inputStr.length() == 0 && result!=0){
                        inputStr = Double.toString(result);
                        if (inputStr.charAt(0) != '-') {
                            inputStr = "-" + inputStr;
                            displayEdit(inputStr);
                        }
                        else {
                            inputStr = inputStr.substring(1);
                            displayEdit(inputStr);
                        }
                        parseInputStringIntoResult();
                        displayEdit(Double.toString(result));

                    }
                    break;
                //Button for DEL checks to see where it has a input or last result
                //Known bug: when deleting a result it starts with 0 after a double with .
                //ie 2 + 5 = 7, DEL, 7. will show
                case R.id.ButtonForBack :
                    if(inputStr.length() != 0){
                        inputStr = inputStr.substring(0, inputStr.length() - 1);
                        displayEdit(inputStr);
                    }else if(inputStr.length() == 0 && result!=0){

                        inputStr = Double.toString(result);
                        inputStr = inputStr.substring(0, inputStr.length() - 1);


                        displayEdit(inputStr);
                        result = 0;
                        right = 0;
                    }
                    break;
                // Button for Clear resets all variables
                case R.id.ButtonForClear :
                    opperand = "";
                    inputStr = "";
                    result = 0;
                    right = 0;
                    displayEdit(inputStr);
                    equalSent = false;
                    break;

                //Button for Equaling out the equation
                case R.id.ButtonForEqual :
                    equalSent = true;
                    equals();
                    break;

                default:
                    break;
            }
        }

        //Sets the operand and equals out the previous equation
        private void setOpperand(String op){

            if(result==0 && inputStr.length()!=0) {
                parseInputStringIntoResult();
            }
            else {
                equalSent = false;
                equals();
            }
            opperand = op;
        }

        //Takes the input and parses it into result error checks for a decimal followed by nothing
        private void parseInputStringIntoResult(){
            if(inputStr.charAt(inputStr.length()-1)=='.') {
                append("0");
            }
            result = Double.parseDouble(inputStr);
            inputStr = "";
        }

        //Appends numbers or decimals to the input
        private void append(String ap){
            if(equalSent == true && result != 0){
                result = 0;
                right = 0;
            }
            equalSent = false;
            if(inputStr.length()<10) {
                inputStr = inputStr + ap;
            }
            displayEdit(inputStr);
        }

        //Display for editable strings
        private void displayEdit(String displayStr){
            TextView tv= (TextView)findViewById(R.id.TextForResult);
            tv.setText(displayStr);

        }

        //Display for results (ie checks to makes sure they are formatted properly)
        private void displayResult(String displayStr){
            TextView tv= (TextView)findViewById(R.id.TextForResult);

            if(displayStr.length()!=0) {
                double dTemp = Double.parseDouble(displayStr);
                if (displayStr.indexOf(".") !=-1){
                    BigDecimal temp = new BigDecimal(displayStr);
                    temp = temp.stripTrailingZeros();
                    displayStr = temp.toString();

                }
                if(displayStr.length()>9){
                    String temp = String.format("%e",dTemp);
                    if(temp.indexOf("e+00")!=-1){
                        temp = temp.substring(0, temp.length()-4);
                        while(temp.charAt(temp.length()-1)=='0'||(temp.charAt(temp.length()-1)=='.')){
                            if(temp.charAt(temp.length()-1)=='.'){
                                temp = temp.substring(0, temp.length()-1);
                                break;
                            }
                            temp = temp.substring(0, temp.length()-1);
                        }


                    }
                    System.out.println(25-27.2);
                    tv.setText(temp);
                }
                else if(dTemp == (long) dTemp) {
                    tv.setText(String.format("%d", (long) dTemp));
                }
                else {
                    tv.setText(String.format("%s", dTemp));
                }
            }

            else{
                tv.setText(displayStr);
            }

        }
        //This is what runs if equals was pressed. Checks to see whether it was sent by equals or an operand
        private void equals(){

            //if there is an operand (ie they aren't just clicking = without equation)
            if (opperand.length() != 0) {

                //if there is an input put it in var named right
                if(inputStr.length() != 0) {
                    if(inputStr.charAt(inputStr.length()-1)=='.') {
                        inputStr = inputStr + "0";
                    }
                    right = Double.parseDouble(inputStr);
                }

                //if = was pressed and there is no input but there is a right from previous equation
                // OR
                //if there is an input
                if((equalSent == true && right!=0) || (inputStr.length() != 0)){
                    switch (opperand) {

                        case "+":
                            result = result + right;
                            displayResult(Double.toString(result));
                            break;
                        case "-":
                            result = result - right;
                            displayResult(Double.toString(result));
                            break;
                        case "*":
                            result = result * right;
                            displayResult(Double.toString(result));
                            break;
                        case "/":
                            //check for divide by 0 error
                            if(right != 0) {
                                result = result / right;
                                displayResult(Double.toString(result));
                            }
                            break;
                        default:
                            break;
                    }
                }

                //reset input
                inputStr="";


            }


        }
    };

}