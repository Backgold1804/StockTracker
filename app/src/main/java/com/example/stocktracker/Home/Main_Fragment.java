package com.example.stocktracker.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Main_Fragment extends Fragment {

    public Main_Fragment() {
        // Required empty public constructor
    }

    private TextView toptext, secondtext, thirdlinetext;
    private ImageView thirdlineimage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView RV = v.findViewById(R.id.RV);
        RV.setLayoutManager(new LinearLayoutManager(getActivity()));
        RV.setAdapter(new M1RVadapter(getActivity(), new Data()));

        toptext = v.findViewById(R.id.textView7);
        secondtext = v.findViewById(R.id.textView5);
        thirdlineimage = v.findViewById(R.id.imageView);
        thirdlinetext = v.findViewById(R.id.textView);

        return v;

    }

    public void onSortClicked(View v){
        // 데이터 형태 정립후 정렬
    }

    public void setTopText(String s){
        toptext.setText(s);
    }

    public void setSecondtext(String s){
        secondtext.setText(s);
    }

    public void setThirdline(long l){

        double num;
        if (l < 0){ //음수
            num = l * (double)-1; //양수로 변경

            int downID = 0; //리소스 아이디 대체
            thirdlineimage.setImageResource(downID); //내려가는 화살표 이미지
            String text = l + "추가 데이터"; //3번째 라인 텍스트 설정
            thirdlinetext.setText(text);
        } else if (l >0) { //양수
            int upID = 0; //리소스 아이디 대체
            thirdlineimage.setImageResource(upID); //올라가는 화살표 이미지
            String text = l + "추가 데이터"; //3번째 라인 텍스트 설정
            thirdlinetext.setText(text);
        } else { //0
            int stableID = 0; //리소스 아이디 대체
            thirdlineimage.setImageResource(stableID); //변동없는 이미지
            String text = l + "추가 데이터"; //3번째 라인 텍스트 설정
            thirdlinetext.setText(text);
        }
    }

}