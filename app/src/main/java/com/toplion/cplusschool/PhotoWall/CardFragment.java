package com.toplion.cplusschool.PhotoWall;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.widget.ActionItem;
import com.toplion.cplusschool.widget.TitlePopup;


/**
 * 卡片Fragment
 *
 * @author tengyy
 */
@SuppressLint({"HandlerLeak", "NewApi", "InflateParams"})
public class CardFragment extends Fragment {

    private CardSlidePanel.CardSwitchListener cardSwitchListener;
    //定义标题栏弹窗按钮
    private TitlePopup titlePopup;
    private String imagePaths[] = {
            "http://img1.gtimg.com/edu/pics/hv1/119/44/1870/121608089.jpg",
            "http://souky.eol.cn/HomePage/adminupload/schoolpic/2013-04-03/20130403161835680DSCN6897.JPG",
            "http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037193_1687.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
            "http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg"
    }; // 24个图片资源名称

    private String names[] = {"郭富城", "刘德华", "张学友", "李连杰", "成龙", "谢霆锋", "李易峰",
            "霍建华", "胡歌", "曾志伟"}; // 24个人名

    private List<CardDataItem> dataList = new ArrayList<CardDataItem>();
    private TextView tianjia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_layout, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        CardSlidePanel slidePanel = (CardSlidePanel) rootView
                .findViewById(R.id.image_slide_panel);
        tianjia = (TextView) rootView.findViewById(R.id.tianjia);
        //实例化标题栏弹窗
        titlePopup = new TitlePopup(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        initData();
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {
            @Override
            public void onShow(int index) {
                Log.d("CardFragment", "正在显示-" + dataList.get(index).userName);
            }

            @Override
            public void onCardVanish(int index, int type) {
                Log.d("CardFragment", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
            }

            @Override
            public void onItemClick(View cardView, int index) {
                Log.d("CardFragment", "卡片点击-" + dataList.get(index).userName);
                Intent intent = new Intent();
                intent.putExtra("imagePath",dataList.get(index).imagePath);
                intent.setClass(getActivity(), PhotoContent.class);
                getActivity().startActivity(intent);
            }
        };
        tianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });
        slidePanel.setCardSwitchListener(cardSwitchListener);
        prepareDataList();
        slidePanel.fillData(dataList);
    }

    private void prepareDataList() {
        int num = imagePaths.length;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < num; i++) {
                CardDataItem dataItem = new CardDataItem();
                dataItem.userName = names[i];
                dataItem.imagePath = imagePaths[i];
                dataItem.likeNum = (int) (Math.random() * 10);
                dataItem.imageNum = (int) (Math.random() * 6);
                dataList.add(dataItem);
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getActivity(), "我要发布", R.mipmap.photofabu));
        titlePopup.addAction(new ActionItem(getActivity(), "我的发布", R.mipmap.myfabu));
        titlePopup.addAction(new ActionItem(getActivity(), "排行榜单", R.mipmap.photopaihang));
    }
}
