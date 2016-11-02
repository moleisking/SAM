package me.minitrabajo.controller;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.minitrabajo.R;
import me.minitrabajo.model.Conversation;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.Message;
import me.minitrabajo.model.UserAccount;

/**
 * Created by Scott on 20/10/2016.
 * https://developer.android.com/guide/components/bound-services.html
 * https://xjaphx.wordpress.com/2012/07/07/create-a-service-that-does-a-schedule-task/
 */
public class MessageService extends Service  {

    // Binder given to clients
    public ResponseMSG messageDelegate = null;
    private Timer timer = null;
    private Handler messageHandler= new Handler();
    private final IBinder binder = new MessageBinder();
    private UserAccount userAccount;
    private Conversations conversations;
    public static final long NOTIFY_INTERVAL = 10 * 1000;

    /*public MessageService()
    {
        Log.v("MessageService","UserAccount");
        userAccount.loadFromFile();
        userAccount.print();
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

   /* @Override
    public void onCreate() {
        Log.v("MessageService:onCreate","Loaded");
        // cancel if already existed
        startMessageService();
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("MessageService","onStartCommand");
        startMessageService();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Log.v("MessageService","onDestroy");
        stopMessageService();
        super.onDestroy();
    }

    public void startMessageService()
    {
        if(timer != null)
        {
            timer.cancel();
        }
        else
        {
            timer = new Timer();
        }
        // schedule task
        timer.scheduleAtFixedRate(new MessageTask(this), 0, NOTIFY_INTERVAL);
    }

    public void stopMessageService()
    {
        if(timer != null)
        {
            timer.cancel();
        }
    }

    /**
     * Class used for to ge client message
     */

    class MessageTask extends TimerTask implements ResponseAPI {

        private Context context;

        public MessageTask(Context context)
        {
            this.context = context;
        }

        @Override
        public void run() {
            // run on another thread
            try {
                messageHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.v("MessageTask", "pull messages");
                        getMessages();
                        //getDateTime();
                    }

                });
            }
            catch (Exception ex)
            {
                Log.v("MessageTask:run:Err", ex.getMessage());
            }
        }

        private void getMessages()
        {
            userAccount = new UserAccount(context);
            userAccount.loadFromFile();
            Log.v("MessageService","getMessages");
            String url = getResources().getString(R.string.url_get_messages);
            GetAPI asyncTask =new GetAPI(context);
            asyncTask.delegate = this;
            asyncTask.execute(url,"",userAccount.getToken());
        }

        @Override
        public void processFinish(String output)
        {
            Log.w("MessageService:pFinish", output);

            try
            {
                //Get user account
                userAccount = new UserAccount(context);
                userAccount.loadFromFile();

                //Get JSON and add to object
                if (output.contains("readalllasts"))
                {
                    Log.v("MessageService","Real latest");
                    conversations = new Conversations(context);
                    conversations.loadFromJSON(output,userAccount.getEmail());
                    conversations.print();

                    //Pass new conversations to message fragment where they can be added to the list and file
                    messageDelegate.updateConversations(conversations);
                }
                else if (output.contains("read"))
                {
                    Log.v("MessageService","Read");
                    conversations.loadFromJSON(output, userAccount.getEmail());
                }
                else if (output.contains("err"))
                {
                    Log.v("MessageService","Message send error");
                }
                else
                {
                    Log.v("MessageService","No results");
                }
            }
            catch (Exception ex)
            {
                Log.w("Messages:Process:Err", ex.getMessage());
            }
        }



    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */

    public class MessageBinder extends Binder {
        public MessageService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MessageService.this;
        }
    }

}
