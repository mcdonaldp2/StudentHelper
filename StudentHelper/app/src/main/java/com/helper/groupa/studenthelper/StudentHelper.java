package com.helper.groupa.studenthelper;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by willsteiner on 9/26/15.
 *
 * A Singleton class that allows a single request queue to exist throughout the life cycle of
 * the application. Simply build a HTTP request and add to the queue, let Volley handle the rest.
 *  ex:
 *      // Fetch helper
 *      private final StudentHelper helper = StudentHelper.getInstance();
 *      // Build request ( String, JSON, ect.)
 *      request = ........
 *      // Add to queue
 *      helper.add(request)
 *
 * A similar implementation with detailed request examples is
 * available at https://developer.android.com/training/volley/request.html
 *
 */
public class StudentHelper extends Application {

    public static final String debugHeading = "S H StudentHelper";

    // Set up request queue to be used cross-activity
    private RequestQueue mRequestQueue;
    private static StudentHelper mInstance;
    public static final String TAG = StudentHelper.class.getName();
    // Allows user object to be saved and accessed across multiple fragments/activities
    private static User user;

    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        refreshUser();

    }

    // Returns application instance
    public static synchronized StudentHelper getInstance()
    {
        return mInstance;
    }

    // Returns singleton queue
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    // Add HTTP request to Queue
    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // Cancel HTTP request
    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }

    // Retrieve User object from StudentHelper.getUser()
    public static User getUser()
    {
        return user;
    }



    // Update the Student Helper User
    public static void setUser (User curUser)
    {
        Log.v(debugHeading, "\nCurrent  User :\n"+curUser.toString());
        user = curUser;
    }


    public void refreshUser()
    {
        // Initialize User object
        final User user = new User();
        // Set url to fetch user
        String url ="http://steiner.solutions/studentHelper/fetchUser/"+user.getPhoneNumber();
        // A user object will be returned encoded into a json string
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Declare user Obj
                        JSONObject userObj;
                        try {
                            // Parse string into JSON object and assign respective object fields
                            // For new users, the server is responding with "{jsonObject}"
                            // instead of a normal {jsonObject}
                            // Check first char
                            Character fC = response.toString().charAt(0);
                            if(response.startsWith("\""))
                            {
                                // remove first and last characters
                                int n = response.length();
                                response = response.substring(1,n-1);
                                // now a valid json object
                                Log.v(debugHeading, "New User");
                            }
                            userObj = new JSONObject(response);
                            user.setUserId(userObj.getInt("user_id"));
                            Log.v(debugHeading, "User " + user.getUserId() + " fetched from DB");
                            user.setUserName(userObj.getString("userName"));
                            if(!userObj.getString("groups").isEmpty())
                                user.setGroupsArray(userObj.getJSONArray("groups"));
                            StudentHelper.setUser(user);

                        }
                        catch(JSONException e)
                        {
                            //TODO JSON ERROR HANDLING
                            Log.v(debugHeading, " JSON Error \n "+ e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO Failed response from server
                Log.v(debugHeading, "Volley Error \n "+ error.toString());
            }
        });
        // Add the request to the RequestQueue.
        this.getRequestQueue().add(stringRequest);
    }

}
