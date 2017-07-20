package in.teambhargavinilayam.abh.quizhuntadmin;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    Button addQuestion, showAllQuestion, deleteQuestion;
    Intent intent;
    DBhelper myDB;
    TextView NavHeaderTxt, WelcomeTxt;
    ImageView NavHeaderImage;
    String UserName = "Admin", UserEmail, UserPicURL = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        myDB = new DBhelper(view.getContext());
        addQuestion = (Button) view.findViewById(R.id.new_question);
        showAllQuestion = (Button) view.findViewById(R.id.show_all_question);
        deleteQuestion = (Button) view.findViewById(R.id.delete_question);
        WelcomeTxt = (TextView) view.findViewById(R.id.user_name_view_main_aactivity);
        NavHeaderTxt = (TextView) view.findViewById(R.id.nav_header_txt);
        NavHeaderImage = (ImageView) view.findViewById(R.id.nav_header_img);

        UpdateNavDrawerIcons();


        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).SetActionBarTitle("Sign In");
                AddQuestionFragment homeFragment = new AddQuestionFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_layout,homeFragment);
                ft.commit();
            }
        });

        showAllQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),ShowAllQuestionsActivity.class);
                startActivity(intent);
            }
        });
        deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(),DeleteQuestion.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public void UpdateNavDrawerIcons(){
        myDB = new DBhelper(getContext());
        Cursor data = myDB.ShowUserData();
        if (data.getCount() == 0) {

            UserName = "ADMIN";
            UserPicURL = "";
            UserEmail = "";
        }
        else {
            data.moveToFirst();
            UserName = data.getString(1);
            UserEmail = data.getString(2);
            UserPicURL = data.getString(3);
        }

        String text = "";
        if (NavHeaderTxt != null && NavHeaderImage != null) {
            SetNavDrawer(UserName);
            if (!(UserPicURL.trim().equals("USER"))) {
                Glide.with(this).load(UserPicURL).into(NavHeaderImage);
            }
        }
        String[] splitUserName = UserName.split("");
        for (int i = 0 ; i < splitUserName.length; i++){
            if (splitUserName[i].equals(" ")) {
                text = "Welcome " + text;
                WelcomeTxt.setText(text);
                return;
            }
            else {
                text += splitUserName[i];
                text += " ";
            }
        }
        text = "Welcome " + text;
        WelcomeTxt.setText(text);
    }
    public void SetNavDrawer(String text){
        NavHeaderTxt.setText(text);
    }


}
