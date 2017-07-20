package in.teambhargavinilayam.abh.quizhuntadmin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditQuestionActivity extends AppCompatActivity {

    String rightOption;
    EditText questField, optA, optB, optC, optD;
    Spinner rightOpt;
    FloatingActionButton saveData;
    DBhelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Toast.makeText(this, "Type all data in Malayalam", Toast.LENGTH_LONG).show();

        myDB = new DBhelper(this);
        questField = (EditText) findViewById(R.id.question_edit_field);
        optA = (EditText) findViewById(R.id.optionA_edit_field);
        optB = (EditText) findViewById(R.id.optionB_edit_field);
        optC = (EditText) findViewById(R.id.optionC_edit_field);
        optD = (EditText) findViewById(R.id.optionD_edit_field);
        rightOpt = (Spinner) findViewById(R.id.edit_right_option_list);
        saveData = (FloatingActionButton) findViewById(R.id.updateQuestion);

        final List<String> rightOptList = new ArrayList<String>();
        rightOptList.add("Select Right Option..");
        rightOptList.add("A");
        rightOptList.add("B");
        rightOptList.add("C");
        rightOptList.add("D");

        ArrayAdapter<String> CourseListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, rightOptList);
        CourseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightOpt.setAdapter(CourseListAdapter);

        final String id = setFieldsEditing();
        rightOpt.setSelection(Integer.parseInt(id));

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
                    Toast.makeText(EditQuestionActivity.this, "All Fields are necessary", Toast.LENGTH_SHORT).show();
                }
                else {

                    boolean isInserted = myDB.UpdateQuestion(id, questField.getText().toString(),
                            optA.getText().toString().trim(),
                            optB.getText().toString().trim(),
                            optC.getText().toString().trim(),
                            optD.getText().toString().trim(),
                            rightOption.trim());
                    if (isInserted){
                        Toast.makeText(EditQuestionActivity.this, "Question Added", Toast.LENGTH_SHORT).show();

                        questField.setText("");
                        optA.setText("");
                        optB.setText("");
                        optC.setText("");
                        optD.setText("");
                        rightOpt.setSelection(0);
                    }
                    else {
                        Toast.makeText(EditQuestionActivity.this, "Process Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String setFieldsEditing(){
        Bundle params = getIntent().getExtras();
        questField.setText(params.getString("quest"));
        optA.setText(params.getString("optA"));
        optB.setText(params.getString("optB"));
        optC.setText(params.getString("optC"));
        optD.setText(params.getString("optD"));

        return Integer.toString(params.getInt("rightOptPosition"));
    }

}
