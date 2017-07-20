package in.teambhargavinilayam.abh.quizhuntadmin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    String rightOption;
    TextView questionNum;
    EditText questField, optA, optB, optC, optD;
    Spinner rightOpt;
    FloatingActionButton saveData;
    DBhelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Toast.makeText(this, "Type all data in Malayalam", Toast.LENGTH_LONG).show();
        myDB = new DBhelper(this);
        questionNum = (TextView) findViewById(R.id.q_no);
        questField = (EditText) findViewById(R.id.question_field);
        optA = (EditText) findViewById(R.id.optionA_field);
        optB = (EditText) findViewById(R.id.optionB_field);
        optC = (EditText) findViewById(R.id.optionC_field);
        optD = (EditText) findViewById(R.id.optionD_field);
        rightOpt = (Spinner) findViewById(R.id.right_option_list);
        saveData = (FloatingActionButton) findViewById(R.id.saveQuestion);

        UpdateQuestionNum();

        final List<String> rightOptList = new ArrayList<String>();
        rightOptList.add("Select Right Option..");
        rightOptList.add("A");
        rightOptList.add("B");
        rightOptList.add("C");
        rightOptList.add("D");

        ArrayAdapter<String> CourseListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, rightOptList);
        CourseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightOpt.setAdapter(CourseListAdapter);

        rightOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rightOption = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rightOption = "";
            }
        });



        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questField.getText().toString().isEmpty() ||
                        optA.getText().toString().isEmpty() ||
                        optB.getText().toString().isEmpty() ||
                        optC.getText().toString().isEmpty() ||
                        optD.getText().toString().isEmpty() ||
                        rightOption.isEmpty() ||
                        rightOption.equals("Select Right Option..") ||
                        rightOption.equals("")){
                    Toast.makeText(AddQuestionActivity.this, "All Fields are necessary", Toast.LENGTH_SHORT).show();
                }
                else {

                    boolean isInserted = myDB.InsertQuestion(questField.getText().toString(),
                            optA.getText().toString().trim(),
                            optB.getText().toString().trim(),
                            optC.getText().toString().trim(),
                            optD.getText().toString().trim(),
                            rightOption.trim());
                    if (isInserted){
                        Toast.makeText(AddQuestionActivity.this, "Question Added", Toast.LENGTH_SHORT).show();

                        questField.setText("");
                        optA.setText("");
                        optB.setText("");
                        optC.setText("");
                        optD.setText("");
                        rightOpt.setSelection(0);
                        UpdateQuestionNum();
                    }
                    else {
                        Toast.makeText(AddQuestionActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void UpdateQuestionNum(){
        int qno = myDB.GetLastQuestionIndex();
        String qust_no = Integer.toString(qno + 1) + ".";
        questionNum.setText(qust_no);
    }


}
