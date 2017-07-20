package in.teambhargavinilayam.abh.quizhuntadmin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DeleteQuestion extends AppCompatActivity {

    ListView dataList;
    DBhelper myDB;
    ContentValues cv;
    int[] isSelected;
    int noOfitemSelected = 0;
    TextView selectedPosition;
    FloatingActionButton deleteBtn;
    ArrayList<String> theList = new ArrayList<>();
    Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDB = new DBhelper(this);
        deleteBtn = (FloatingActionButton) findViewById(R.id.deleteBtn);
        dataList = (ListView) findViewById(R.id.delete_question_list);

        data = myDB.ShowAll();
        cv  = new ContentValues(data.getCount());   // sets the size corresponding to the no of elements in the DB
        ViewAll();


        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = (TextView) view.findViewById(R.id.item_view);
                if (isSelected[position] != 0) {
                    isSelected[position] = 0;
                    noOfitemSelected--;
                    selectedPosition.setBackgroundColor(Color.WHITE);
                    Toast.makeText(DeleteQuestion.this, Integer.toString(isSelected[position])+ " " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                }
                else {
                    isSelected[position] = 1;
                    noOfitemSelected++;
                    selectedPosition.setBackgroundColor(Color.DKGRAY);
                    Toast.makeText(DeleteQuestion.this, Integer.toString(isSelected[position])+ " " + Integer.toString(position), Toast.LENGTH_SHORT).show();


                }

            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DBid;

                int position = 0;
                for (position = 0; position < noOfitemSelected; position++) {
                    if (isSelected[position] != 0) {

                        DBid = cv.getAsString(Integer.toString(position));

                        boolean isDeleted = myDB.DeleteSpecificQuestion(DBid);
                        noOfitemSelected--;
                        isSelected[position] = 0;
                        Toast.makeText(DeleteQuestion.this, "Deleted " + cv.getAsString(Integer.toString(position)), Toast.LENGTH_SHORT).show();

                    }


                }
                ViewAll();


            }
        });


    }

    void ViewAll(){
        myDB = new DBhelper(this);
        data = myDB.ShowAll();
        theList = new ArrayList<>();
        isSelected = new int[data.getCount()];
        for(int i =0; i< data.getCount(); i++)
            isSelected[i] = 0;

        String Content ;

        if (data.getCount() == 0){
            Toast.makeText(this, "No Questions", Toast.LENGTH_SHORT).show();
        }
        else {
            int i = 0;  // for the list item id
            while (data.moveToNext()){
                cv.put(Integer.toString(i), data.getString(0)); //Connects the list item id and DB id
                i++;    // updates the list item id
                Content = data.getString(0) + ". " + data.getString(1) + "\n\t" + data.getString(2) + "\n\t" + data.getString(3) + "\n\t" + data.getString(4)
                        + "\n\t" + data.getString(5) + "\nCorrect : " + data.getString(6) + "\n";
                theList.add(Content);

            }
            ListAdapter listAdapter = new ArrayAdapter<>(this,R.layout.show_all_questions,R.id.item_view,theList);
            dataList.setAdapter(listAdapter);
        }
    }


}
