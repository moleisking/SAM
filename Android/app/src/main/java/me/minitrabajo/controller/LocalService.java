package me.minitrabajo.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.minitrabajo.R;
import me.minitrabajo.model.Conversations;
import me.minitrabajo.model.UserAccount;

/**
 * Created by Scott on 21/10/2016.
 */
public class LocalService extends Service {
    // Binder given to clients
    public final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();
    public ResponseMSG messageDelegate = null;
    private Timer timer = null;
    private Handler messageHandler= new Handler();
    public static final long NOTIFY_INTERVAL = 10 * 1000;
    private Conversations conversations;
    private UserAccount userAccount;
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
       public LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int getRandomNumber() {
        int i = mGenerator.nextInt(100);
        Log.v("LocalService:getRandom",String.valueOf(i));
        return i;
    }

    public void startMessageService()
    {
        if(timer != null)
        {
            timer.cancel();
        }
        else
        {
            // recreate new
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
                        Log.v("MessagePullTask", "Call message pull");
                        getMessages();
                        //getDateTime();
                    }

                });
            }
            catch (Exception ex)
            {
                Log.v("MessagePullTask", ex.getMessage());
            }
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

        private void getMessages()
        {
            userAccount = new UserAccount(context);
            userAccount.loadFromFile();
            Log.v("LocalService:getMes","GET");
            String url = getResources().getString(R.string.url_get_messages);
            String parameters = "";
            Log.v("LocalService:Para ",parameters );
            GetAPI asyncTask =new GetAPI(context);
            asyncTask.delegate = this;
            asyncTask.execute(url,parameters,userAccount.getToken());
        }

        @Override
        public void processFinish(String output)
        {
            Log.w("LocalService:processF", output);

            try
            {
                //Get JSON and add to object
                if (output.contains("readalllasts"))
                {
                    conversations = new Conversations(context);
                    conversations.loadFromJSON(output, userAccount.getEmail());
                    conversations.print();
                    //conversations.saveToFile();
                    messageDelegate.updateConversations(conversations);
                }
                else if (output.contains("read"))
                {
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
                Log.w("MessagesService:Pro:Err", ex.getMessage());
            }
        }



    }
}