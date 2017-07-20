package in.teambhargavinilayam.abh.quizhuntadmin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageAcoountFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    public ManageAcoountFragment() {
        // Required empty public constructor
    }

    private ImageView userPic;
        private LinearLayout userDetailsView;
        private TextView userName, userEmail;
        private TextView WelcomeTXT;
        private Button logoutBtn;
        private DBhelper myDB;
        private SignInButton signInButton;
        private final int REQ_CODE = 9001;
    MainActivity m = new MainActivity();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_acoount, container, false);

        myDB = new  DBhelper(view.getContext());
        userPic = (ImageView) view.findViewById(R.id.user_pic);
        userDetailsView = (LinearLayout) view.findViewById(R.id.loggedInDetails);
        userName = (TextView) view.findViewById(R.id.user_name);
        userEmail = (TextView) view.findViewById(R.id.user_email);
        WelcomeTXT = (TextView) view.findViewById(R.id.user_name_view_main_aactivity);
        logoutBtn = (Button) view.findViewById(R.id.logout_btn);
        signInButton = (SignInButton) view.findViewById(R.id.signInButton);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        if (m.apiClient == null) {
            m.apiClient = new GoogleApiClient.Builder(view.getContext()).enableAutoManage(getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        }

        SignIn();
        logoutBtn.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        return view;
    }


    @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.logout_btn:
                    SignOut();
                    break;
                case R.id.signInButton:
                    SignIn();
                    break;
            }

        }

    public void SignIn(){
        Intent intent;
        if (((MainActivity)getActivity()).IsNetworkAvailable()){
            intent = Auth.GoogleSignInApi.getSignInIntent(m.apiClient);
            startActivityForResult(intent, REQ_CODE);
        }
        else
            Toast.makeText(getContext(), "Check your Network Connection", Toast.LENGTH_SHORT).show();

    }

    public void SignOut(){
        Auth.GoogleSignInApi.signOut(m.apiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                UpdateUI(false);
            }
        });

        boolean isInserted = myDB.InsertUserData("ADMIN","","");

        userPic.setImageResource(R.drawable.account_round);

    }

    public void handleResult(GoogleSignInResult signInResult){

        if (signInResult.isSuccess()){
            GoogleSignInAccount account = signInResult.getSignInAccount();
            String Name = account.getDisplayName();
            String Email = account.getEmail();
            String Img_Url = "";

            if (account.getPhotoUrl() != null){
                Img_Url = account.getPhotoUrl().toString();
                Glide.with(this).load(Img_Url).into(userPic);
                UpdateUI(true);
            }
            userName.setText(Name);
            userEmail.setText(Email);


            boolean isInserted = myDB.InsertUserData(Name,Email,Img_Url);
        }
        else
            UpdateUI(false);
    }

    public void UpdateUI(boolean isLoggedIn){

        if (isLoggedIn){
            signInButton.setVisibility(View.GONE);
            userDetailsView.setVisibility(View.VISIBLE);
        }
        else {
            signInButton.setVisibility(View.VISIBLE);
            userDetailsView.setVisibility(View.GONE);
        }

    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE){
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(signInResult);
        }
    }

}





