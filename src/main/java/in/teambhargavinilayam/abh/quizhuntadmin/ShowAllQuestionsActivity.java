package in.teambhargavinilayam.abh.quizhuntadmin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowAllQuestionsActivity extends AppCompatActivity {

    ListView dataList;
    DBhelper myDB;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        myDB = new DBhelper(this);
        dataList = (ListView) findViewById(R.id.show_all_question_list);

        final ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.ShowAll();
        cv  = new ContentValues(data.getCount());   // sets the size corresponding to the no of elements in the DB
        String Content ;

        if (data.getCount() == 0){
            Toast.makeText(this, "No Questions", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, Integer.toString(data.getCount()) + " Questions", Toast.LENGTH_SHORT).show();
            int i = 0;  // for the list item id
            while (data.moveToNext()){
                cv.put(Integer.toString(i), data.getString(0)); //Connects the list item id and DB id
                i++;    // updates the list item id
                Content = data.getString(0) + ". " + data.getString(1) + "\n\t" + data.getString(2) + "\n\t" + data.getString(3) + "\n\t" + data.getString(4)
                        + "\n\t" + data.getString(5) + "\nCorrect : " + data.getString(6) + "\n";
                theList.add(Content);
                ListAdapter listAdapter = new ArrayAdapter<>(this,R.layout.show_all_questions,R.id.item_view,theList);
                dataList.setAdapter(listAdapter);
            }
        }

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String DBid = cv.getAsString(Integer.toString(position));
                Cursor data = myDB.ShowSpecificQuestion(DBid);
                EditText questField, optA, optB, optC, optD;
                Spinner rightOpt;
                questField = (EditText) findViewById(R.id.question_field);
                optA = (EditText) findViewById(R.id.optionA_field);
                optB = (EditText) findViewById(R.id.optionB_field);
                optC = (EditText) findViewById(R.id.optionC_field);
                optD = (EditText) findViewById(R.id.optionD_field);
                rightOpt = (Spinner) findViewById(R.id.right_option_list);

                data.moveToFirst();
                String[] args = {
                        data.getString(1),
                        data.getString(2),
                        data.getString(3),
                        data.getString(4),
                        data.getString(5)
                } ;
                int rightOptPosition = 0;
                switch (data.getString(6)){
                    case "A":
                        rightOptPosition = 1;
                        break;
                    case "B":
                        rightOptPosition = 2;
                        break;
                    case "C":
                        rightOptPosition = 3;
                        break;
                    case "D":
                        rightOptPosition = 4;
                        break;

                }
                Bundle params = new Bundle();
                params.putString("quest", args[0]);
                params.putString("optA", args[1]);
                params.putString("optB", args[2]);
                params.putString("optC", args[3]);
                params.putString("optD", args[4]);
                params.putInt("rightOptPosition",rightOptPosition);


                Intent intent = new Intent(ShowAllQuestionsActivity.this, EditQuestionActivity.class);
                intent.putExtras(params);
                startActivity(intent);
                finish();


            }
        });


    }

}
