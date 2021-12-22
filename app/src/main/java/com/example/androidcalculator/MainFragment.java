package com.example.androidcalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcalculator.databinding.FragmentFirstBinding;

public class MainFragment extends Fragment {

    View fragmentFirstLayout;
    TextView showCountTextView;
    TextView showInputTextView;
    private FragmentFirstBinding binding;
    private static final String LETTER_SPACING = " ";
    private String myPreviousText;
    private EditText myEditText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        // Get the count text view
        // Inflate the layout for this fragment
        fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        // Get the count text view
        showCountTextView = fragmentFirstLayout.findViewById(R.id.textview_first);
        myEditText = (EditText) fragmentFirstLayout.findViewById(R.id.text_box);


        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing here
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                // Only update the EditText when the user modify it -> Otherwise it will be triggered when adding spaces
                if (!text.equals(myPreviousText)) {
                    // Remove spaces
                    text = text.replace(" ", "");
                    // Add space between each character
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        if (i == text.length() - 1) {
                            // Do not add a space after the last character -> Allow user to delete last character
                            newText.append(Character.toLowerCase(text.charAt(text.length() - 1)));
                        }
                        else {
                            if(isOperator(text.charAt(i+1)) || isOperator(text.charAt(i)) && (Character.isDigit(text.charAt(i+1)) || Character.isAlphabetic(text.charAt(i+1)))){
                                newText.append(Character.toLowerCase(text.charAt(i)) + LETTER_SPACING);
                            }else{
                                newText.append(Character.toLowerCase(text.charAt(i)));
                            }
                        }
                    }

                    myPreviousText = newText.toString();
                    // Update the text with spaces and place the cursor at the end
                    myEditText.setText(newText);
                    myEditText.setSelection(newText.length());
                }
            }
        });


        myEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_REFRESH)){
                    calculate();
                    return true;
                }
                return false;
            }
        });

        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.calculate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
        view.findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInput();
            }
        });

        // Inflate the layout for this fragment




    }

    private void calculate() {
        try{
            showInputTextView = fragmentFirstLayout.findViewById(R.id.text_box);
            String inp = showInputTextView.getText().toString();
            Calculator cal = new Calculator();
            Double output = cal.evaluate(inp);

            showCountTextView.setText(String.valueOf(output));
        }catch(Exception e){
            showCountTextView.setText("Invalid Input");

        }


    }

    private void clearInput() {
        showInputTextView = fragmentFirstLayout.findViewById(R.id.text_box);
        showInputTextView.setText("");
    }

    private void randomMe(View view) {
        Integer random =  (int)(Math.random()*1000);
        showCountTextView.setText(random.toString());
    }

    public static boolean isNumeric(char charNum) {
        if (String.valueOf(charNum) == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(String.valueOf(charNum));
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static boolean isOperator(char inp)
    {
        String token = String.valueOf(inp);
        boolean state = token.equals("+") ||
                token.equals("-") ||
                token.equals("*") ||
                token.equals("/") ||
                token.equals("^") ||
                token.equals("(") ||
                token.equals(")") ;
        return state;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}