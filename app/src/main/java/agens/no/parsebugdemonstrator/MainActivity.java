package agens.no.parsebugdemonstrator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {


    private static final String LOG_TAG = "MainActivity";

    private View.OnClickListener signupOla = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signup("ola", "kari4ever", "ole@agens.no");
        }
    };


    private View.OnClickListener loginOla = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginUser("ola");
        }
    };

    private View.OnClickListener signupKari = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signup("kari", "ola4ever", "kari@agens.no");
        }
    };

    private View.OnClickListener loginKari = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginUser("kari");
        }
    };

    private View.OnClickListener loginAlex = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loginUser("alex");
        }
    };

    private View.OnClickListener signupAlex = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signup("alex", "alex", "alex@agens.no");
        }
    };

    private View.OnClickListener query = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            query();
        }
    };

    private View.OnClickListener forgotPasswordAlex = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            forgotPassword("alex@agens.no");
        }
    };


    private View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            logout();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.signup_ola).setOnClickListener(signupOla);

        findViewById(R.id.login_ola).setOnClickListener(loginOla);

        findViewById(R.id.signup_kari).setOnClickListener(signupKari);

        findViewById(R.id.login_kari).setOnClickListener(loginKari);

        findViewById(R.id.signup_alex).setOnClickListener(signupAlex);

        findViewById(R.id.login_alex).setOnClickListener(loginAlex);

        findViewById(R.id.query).setOnClickListener(query);

        findViewById(R.id.forgot_password_alex).setOnClickListener(forgotPasswordAlex);

        findViewById(R.id.log_out).setOnClickListener(logout);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    private void signup(String userName, String password, String email) {
        final ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);

        user.put("dateOfBirth", new Date(1000000000000L));
        user.put("telephone", "55555555");

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(false);
        defaultACL.setPublicWriteAccess(false);
        user.setACL(defaultACL);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    toast("Hooray! " + user.getUsername() + " signed up");
                } else {
                    toast("signup failed " + user.getUsername() + "\n" + e.toString());
                }
            }
        });

    }

    private void loginUser(final String userName) {

        getPassword(new PasswordCallback() {
            @Override
            public void password(String password) {
                ParseUser.logInInBackground(userName, password, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            toast("Hooray! " + user.getUsername() + " logged in");
                        } else {
                            toast("login failed\n" + e.toString());
                        }
                    }
                });
            }
        });

    }


    private void toast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    private void query() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    toast("Retrieved " + scoreList.size() + " scores");
                } else {
                    toast("Error: " + e.getMessage());
                }
            }
        });

    }

    private void forgotPassword(final String emailAddress) {
        ParseUser.requestPasswordResetInBackground(emailAddress,
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            toast("Hooray! email sent to " + emailAddress);
                        } else {
                            toast("Error: " + e.getMessage());
                        }
                    }
                });

    }


    private interface PasswordCallback {
        void password(String password);
    }

    private void getPassword(final PasswordCallback passwordCallback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Password");
        alert.setMessage("Password");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                passwordCallback.password(value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    private void logout() {
        ParseUser.logOut();
    }

}
