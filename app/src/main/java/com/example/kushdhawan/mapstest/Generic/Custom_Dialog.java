package com.example.kushdhawan.mapstest.Generic;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.kushdhawan.mapstest.Abstract.AsyncTaskListener;
import com.example.kushdhawan.mapstest.Enums.TaskType;
import com.example.kushdhawan.mapstest.R;

public class Custom_Dialog extends Activity  implements AsyncTaskListener {


    String rating = "1.0";



    public void showDialog(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);

        TextView text = (TextView) dialog.findViewById(R.id.dialog_result);
        text.setText(msg);

        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // activity.finish();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

   /* public void showDialog_forPayment(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom_payment);
        TextView text = (TextView) dialog.findViewById(R.id.dialog_result);
        text.setText(msg);
        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //  activity.finish();
              //  activity.
                dialog.dismiss();
                //new Intent(getBaseContext(),Activity.class)
               Intent i = new Intent(activity,WebViewPayment_Activity.class);
                Log.e("We Are Here",activity.getPackageName().toString());
               // startActivity(i);
            }
        });
        dialog.show();
    }*/




    @Override
    public void onTaskCompleted(String result, TaskType taskType) {



    }

    @Override
    public void onTaskCompleted(Activity activity, String result, TaskType taskType) {

//        if(taskType.equals(TaskType.USER_RATING)){
//            String Result_to_Show = null;
//            Result_to_Show = Ratings_Json.Rating_Parse(result);
//            showDialog(activity,Result_to_Show);
//
//        }else if(taskType.equals(TaskType.PARK_USER)){
//            String Result_to_Show = null;
//            Result_to_Show = Manager_Json.Parse_PArkME(result);
//            showDialog(activity,Result_to_Show);
//        }else if(taskType.equals(TaskType.PARK_OUT_USER)){
//            String Result_to_Show = null;
//            Result_to_Show = Manager_Json.Parse_ParkOut(result);
//            showDialog(activity,Result_to_Show);
//        }
//        else{
//            showDialog(activity,"Something went wrong.");
//        }
    }


}