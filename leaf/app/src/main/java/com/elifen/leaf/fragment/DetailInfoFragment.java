package com.elifen.leaf.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elifen.leaf.R;
import com.elifen.leaf.UIActivity.FavoriteActivity;
import com.elifen.leaf.UIActivity.MyfansActivity;
import com.elifen.leaf.UIActivity.Person_informationActivity;
import com.elifen.leaf.entity.CurrentUser;
import com.elifen.leaf.view.CoustomImageView;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("ALL")
public class DetailInfoFragment extends Fragment implements View.OnClickListener {
    private TextView tv_setInfo;
    private TextView tv_name;
    private ImageView iv_sex;
    private TextView tv_userId;
    private TextView tv_description;

    private CoustomImageView image_head;
    private CurrentUser currentUser;
    private String userName;
    private String sex;
    private String description;
    private int userId;
    private RelativeLayout favorite;
    private RelativeLayout fans;
    public DetailInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);
        initView(view);
        initListenner();
        return view;
    }

    private void initListenner() {
        tv_setInfo.setOnClickListener(this);
        favorite.setOnClickListener(this);
        fans.setOnClickListener(this);
    }
    private void initView(View view) {
        tv_setInfo = (TextView) view.findViewById(R.id.tv_setInfo);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        tv_userId = (TextView) view.findViewById(R.id.tv_userId);
        tv_description = (TextView) view.findViewById(R.id.description);
        favorite = (RelativeLayout) view.findViewById(R.id.rl_favorite);
        fans = (RelativeLayout) view.findViewById(R.id.rl_fans);

        //得到用户数据
        currentUser = CurrentUser.getInstance();
        //currentUser.readFromFile(getActivity());
        this.userName = currentUser.getUserName();
        this.sex = currentUser.getSex();
        this.description = currentUser.getDescription();
        this.userId = currentUser.getUserId();
        //将用户数据显示
        image_head = (CoustomImageView) view.findViewById(R.id.image_head);
        if(currentUser.getHeadString()!= null) {
            image_head.setImageBitmap(currentUser.getHeadBitmapFromdatabase(currentUser.getHeadString())); //设置头像
        }
        if(userName !=null)
        tv_name.setText(userName);    //设置昵称

        if(sex != null&&sex.equals("男")){
            iv_sex.setImageResource(R.drawable.male);
        }else
            iv_sex.setImageResource(R.drawable.female);

        tv_userId.setText("ID:"+userId);
        if (description==null){
            tv_description.setText("说点什么吧！");
        }else
            tv_description.setText(description);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_setInfo:
                Intent intent = new Intent(getActivity(), Person_informationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.rl_favorite:
                Intent intent1 = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_fans:
                 Intent intent2 = new Intent(getActivity(), MyfansActivity.class);
                 startActivity(intent2);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                currentUser = CurrentUser.getInstance();
                image_head.setImageBitmap(currentUser.getHeadBitmapFromdatabase(currentUser.getHeadString()));
                tv_name.setText(currentUser.getUserName());
                if(currentUser.getSex().equals("男")){
                    iv_sex.setImageResource(R.drawable.male);
                }else
                    iv_sex.setImageResource(R.drawable.female);
                if (currentUser.getDescription().equals("")||description==null){
                    tv_description.setText("说点什么吧！");
                }else
                    tv_description.setText(currentUser.getDescription());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
