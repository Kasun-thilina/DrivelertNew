package com.example.safe.drivelert;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.safe.drivelert.Utility.Const;
import com.example.safe.drivelert.Utility.TinyDB;

import java.text.DateFormat;
import java.util.Date;

import br.com.bloder.magic.view.MagicButton;

public class monitor_menu extends Fragment {
    MagicButton b;
    SeekBar s;
    TextView ttv;


    private String key_2 = "safe's project";
    private String key_4 = "senstivity";

    SeekBar mSpeedLimitSeekbar;
    TextView mSpeedLimitTextView;
    TinyDB tinyDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root;
        root = inflater.inflate(R.layout.activity_monitor_menu,container,false);
        init(root);
        return root;

    }

    private void init(final View root)
    {
         tinyDB = new TinyDB(getActivity());

        b = (MagicButton) root.findViewById(R.id.magic_button);
        s = (SeekBar)root.findViewById(R.id.seekBar2);
        ttv = (TextView)root.findViewById(R.id.textView21);
        mSpeedLimitTextView = (TextView)root.findViewById(R.id.tv_speedLimit);
        mSpeedLimitSeekbar = root.findViewById(R.id.seekBar);

        int saved_speed = tinyDB.getInt(Const.SPEED_LIMIT);
        if(saved_speed > 0)
        {
            mSpeedLimitTextView.setText(Integer.toString(saved_speed) + " km/h");
            mSpeedLimitSeekbar.setProgress(saved_speed);
        }

        if(s.getProgress() == 0)
        {
            ttv.setText("0.5 second");
        }
        else if(s.getProgress() == 1)
        {
            ttv.setText("0.75 second");
        }
        else if(s.getProgress() == 2)
        {
            ttv.setText("1 seconds");
        }
        else if(s.getProgress() == 3)
        {
            ttv.setText("1.25 seconds");
        }
        else if(s.getProgress() == 4)
        {
            ttv.setText("1.5 seconds");
        }
        else if(s.getProgress() == 5)
        {
            ttv.setText("1.75 seconds");
        }
        else if(s.getProgress() == 6)
        {
            ttv.setText("2 seconds");
        }
        else if(s.getProgress() == 7)
        {
            ttv.setText("2.25 seconds");
        }
        else
        {
            ttv.setText("2.5 seconds");
        }
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(s.getProgress() == 0)
                {
                    ttv.setText("0.5 second");
                }
                else if(s.getProgress() == 1)
                {
                    ttv.setText("0.75 second");
                }
                else if(s.getProgress() == 2)
                {
                    ttv.setText("1 seconds");
                }
                else if(s.getProgress() == 3)
                {
                    ttv.setText("1.25 seconds");
                }
                else if(s.getProgress() == 4)
                {
                    ttv.setText("1.5 seconds");
                }
                else if(s.getProgress() == 5)
                {
                    ttv.setText("1.75 seconds");
                }
                else if(s.getProgress() == 6)
                {
                    ttv.setText("2 seconds");
                }
                else if(s.getProgress() == 7)
                {
                    ttv.setText("2.25 seconds");
                }
                else
                {
                    ttv.setText("2.5 seconds");
                }


            }
        });

        b.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(root.getContext(),FaceTrackerActivity.class);
                i.putExtra(key_4,""+s.getProgress());
                i.putExtra(key_2,DateFormat.getDateTimeInstance().format(new Date()));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();

            }
        });

        mSpeedLimitSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               int progress =  seekBar.getProgress();
               mSpeedLimitTextView.setText(Integer.toString(progress) + " km/h");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tinyDB.putInt(Const.SPEED_LIMIT , seekBar.getProgress());
            }
        });
    }
}
