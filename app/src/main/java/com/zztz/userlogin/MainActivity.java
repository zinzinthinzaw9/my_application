package com.zztz.userlogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import static com.google.android.gms.common.Scopes.*;

public class MainActivity extends AppCompatActivity {
    private static final int RC_GET_AUTH_CODE = 9003;
   private SignInButton sign_in_btn;
   public static GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_in_btn = findViewById(R.id.sign_in_button);
        final String serverClientId = "";
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(DRIVE_APPFOLDER))
                        .requestServerAuthCode(serverClientId)
                        .requestIdToken(serverClientId)
                        .requestEmail()
                        .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sign_in_btn.setSize(SignInButton.SIZE_STANDARD);
        sign_in_btn.setColorScheme(SignInButton.COLOR_LIGHT);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_AUTH_CODE) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("SignInActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    public void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            String idToken = account.getIdToken();
            String serverAuth=account.getServerAuthCode();

           Intent intent=new  Intent(this, logoutActivity.class);
           startActivity(intent);

        } else {
            Toast.makeText(this,"sign in failed!",Toast.LENGTH_LONG).show();
        }
    }

    }