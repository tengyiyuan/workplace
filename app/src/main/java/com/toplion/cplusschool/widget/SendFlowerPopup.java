package com.toplion.cplusschool.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.toplion.cplusschool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wang
 *         功能描述：送花（继承自PopupWindow）
 */
public class SendFlowerPopup extends PopupWindow {
    private View popView;
    private Context mContext;

    //列表弹窗的间隔
    protected final int LIST_PADDING = 10;

    //实例化一个矩形
    private Rect mRect = new Rect();

    //坐标的位置（x、y）
    private final int[] mLocation = new int[2];


    //判断是否需要添加或更新列表子类项
    private boolean mIsDirty;

    //位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;

    //弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;

    //定义列表对象
    private ListView mListView;

    //定义弹窗子类项列表
    private ArrayList<Map<String,String>> mActionItems = new ArrayList<Map<String,String>>();


    public SendFlowerPopup(Context context, List<Map<String,String>> maps,int width, int height) {
        this.mContext = context;
        //设置可以获得焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);

        //设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        //设置弹窗的布局界面
        popView = LayoutInflater.from(mContext).inflate(R.layout.send_flower_popup, null);
        setContentView(popView);
        mActionItems.addAll(maps);
        initUI();
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.title_list);
        populateActions();
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                //点击子类项后，弹窗消失
                dismiss();

                if (mItemOnClickListener != null)
                    mItemOnClickListener.onItemClick(mActionItems.get(index), index);
            }
        });
    }


    /**
     * 设置弹窗列表子项
     */
    private void populateActions() {
        mIsDirty = false;

        //设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.send_flower_item,null);
                    viewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
                    viewHolder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tv_count.setText(mActionItems.get(position).get("count"));
                viewHolder.tv_des.setText(mActionItems.get(position).get("des"));
                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return mActionItems.get(position);
            }

            @Override
            public int getCount() {
                return mActionItems.size();
            }
            class ViewHolder{
                private TextView tv_count;
                private TextView tv_des;

            }
        });
    }

    /**
     * 根据位置得到子类项
     */
    public Map<String, String> getAction(int position) {
        if (position < 0 || position > mActionItems.size())
            return null;
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu
     *         功能描述：弹窗子类项按钮监听事件
     */
    public interface OnItemOnClickListener {
        public void onItemClick(Map<String,String> item, int position);
    }
}
