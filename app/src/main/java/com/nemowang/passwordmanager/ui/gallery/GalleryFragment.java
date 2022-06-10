package com.nemowang.passwordmanager.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.databinding.FragmentGalleryBinding;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private String dateFormatString = "yyyy/MM/dd HH:mm";
    private static DateTime workingOn10 = new DateTime(2022, 6, 2, 0, 0, 0);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ImageView coco_img = root.findViewById(R.id.img_coco);

//        TextView wpg_tv = root.findViewById(R.id.text_wpg_date);
//        TextView wpg_tv2 = root.findViewById(R.id.text_wpg_date2);
//
//        TextView sh_tv = root.findViewById(R.id.text_sh_date);
//        TextView sh_tv2 = root.findViewById(R.id.text_sh_date2);
//
//        TextView do_thing_tv = root.findViewById(R.id.text_do_thing);

//        coco_img.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                freshTime(root);
//                return false;
//            }
//        });

        coco_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freshTime(root);
            }
        });

        freshTime(root);

        /**********************************/
//        LocalDateTime wpg_dt = LocalDateTime.now();
//        int wpg_hour = wpg_dt.getHour();
//        DayOfWeek wpg_week = wpg_dt.getDayOfWeek();
//        String wpg_week_str = wpg_week.getDisplayName(TextStyle.FULL, Locale.CHINESE);
//
//        DateTimeFormatter dtFormatObj = DateTimeFormatter.ofPattern(dateFormatString);
//
//        wpg_tv.setText(wpg_dt.format(dtFormatObj));
//        if(wpg_hour < 5)
//            wpg_tv2.setText(wpg_week_str + ", " + "凌晨, " + String.valueOf(wpg_hour) + "点");
//        else if(wpg_hour < 12)
//            wpg_tv2.setText(wpg_week_str + ", " + "上午, " + String.valueOf(wpg_hour) + "点");
//        else if(wpg_hour < 19)
//            wpg_tv2.setText(wpg_week_str + ", " + "下午, " + String.valueOf(wpg_hour - 12) + "点");
//        else
//            wpg_tv2.setText(wpg_week_str + ", " + "晚上, " + String.valueOf(wpg_hour - 12) + "点");
//
//        /**********************************/
//        DateTimeZone sh_zone = DateTimeZone.forID("Asia/Shanghai");
//        DateTime dt_sh = new DateTime(sh_zone);
//        DateTime workingOn10 = new DateTime(2022, 6, 2, 0, 0, 0, sh_zone);
//
//        org.joda.time.format.DateTimeFormatter dtFormatObjSH = DateTimeFormat.forPattern(dateFormatString);
//        int sh_hour = dt_sh.getHourOfDay();
//        DateTime sh_t_span = dt_sh.minus(workingOn10.getMillis());
//
//        sh_tv.setText(dtFormatObjSH.print(dt_sh));
//        if(sh_hour < 5)
//            sh_tv2.setText(wpg_week_str + ", " + "凌晨, " + String.valueOf(sh_hour) + "点");
//        else if(sh_hour < 12)
//            sh_tv2.setText(wpg_week_str + ", " + "上午, " + String.valueOf(sh_hour) + "点");
//        else if(sh_hour < 19)
//            sh_tv2.setText(wpg_week_str + ", " + "下午, " + String.valueOf(sh_hour - 12) + "点");
//        else
//            sh_tv2.setText(wpg_week_str + ", " + "晚上, " + String.valueOf(sh_hour - 12) + "点");
//
//        if(sh_t_span.getDayOfYear() % 2 == 0)
//            do_thing_tv.setText("今天10点做事？" + "<Yes>");
//        else
//            do_thing_tv.setText("今天10点做事？" + "<No>");

        return root;
    }

    private void freshTime(View root){
        TextView wpg_tv = root.findViewById(R.id.text_wpg_date);
        TextView wpg_tv2 = root.findViewById(R.id.text_wpg_date2);

        TextView sh_tv = root.findViewById(R.id.text_sh_date);
        TextView sh_tv2 = root.findViewById(R.id.text_sh_date2);

        TextView do_thing_tv = root.findViewById(R.id.text_do_thing);

        /**********************************/
        LocalDateTime wpg_dt = LocalDateTime.now();
        int wpg_hour = wpg_dt.getHour();
        DayOfWeek wpg_week = wpg_dt.getDayOfWeek();
        String wpg_week_str = wpg_week.getDisplayName(TextStyle.FULL, Locale.CHINESE);

        DateTimeFormatter dtFormatObj = DateTimeFormatter.ofPattern(dateFormatString);

        wpg_tv.setText(wpg_dt.format(dtFormatObj));
        if(wpg_hour < 5)
            wpg_tv2.setText(wpg_week_str + ", " + "凌晨, " + String.valueOf(wpg_hour) + "点");
        else if(wpg_hour < 12)
            wpg_tv2.setText(wpg_week_str + ", " + "上午, " + String.valueOf(wpg_hour) + "点");
        else if(wpg_hour < 19)
            wpg_tv2.setText(wpg_week_str + ", " + "下午, " + String.valueOf(wpg_hour - 12) + "点");
        else
            wpg_tv2.setText(wpg_week_str + ", " + "晚上, " + String.valueOf(wpg_hour - 12) + "点");

        /**********************************/
        DateTimeZone sh_zone = DateTimeZone.forID("Asia/Shanghai");
        DateTime dt_sh = new DateTime(sh_zone);
        DateTime workingOn10 = new DateTime(2022, 6, 2, 0, 0, 0, sh_zone);

        org.joda.time.format.DateTimeFormatter dtFormatObjSH = DateTimeFormat.forPattern(dateFormatString);
        int sh_hour = dt_sh.getHourOfDay();
        DateTime sh_t_span = dt_sh.minus(workingOn10.getMillis());

        sh_tv.setText(dtFormatObjSH.print(dt_sh));
        if(sh_hour < 5)
            sh_tv2.setText(wpg_week_str + ", " + "凌晨, " + String.valueOf(sh_hour) + "点");
        else if(sh_hour < 12)
            sh_tv2.setText(wpg_week_str + ", " + "上午, " + String.valueOf(sh_hour) + "点");
        else if(sh_hour < 19)
            sh_tv2.setText(wpg_week_str + ", " + "下午, " + String.valueOf(sh_hour - 12) + "点");
        else
            sh_tv2.setText(wpg_week_str + ", " + "晚上, " + String.valueOf(sh_hour - 12) + "点");

        if(sh_t_span.getDayOfYear() % 2 == 0)
            do_thing_tv.setText("今天10点做事？" + "<Yes>");
        else
            do_thing_tv.setText("今天10点做事？" + "<No>");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}