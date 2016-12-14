package com.toplion.cplusschool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toplion.cplusschool.Bean.GradeInfoBean;
import com.toplion.cplusschool.R;

import java.util.List;

/**
 * Created by wang
 * on 2016/5/26.
 * 成绩列表适配器
 */
public class GradeListAdapter extends BaseAdapter{
    private ViewHolder viewHolder;
    private Context mcontext;
    private List<GradeInfoBean> mlist;
    public List<GradeInfoBean> getMlist() {
        return mlist;
    }

    public void setMlist(List<GradeInfoBean> mlist) {
        this.mlist = mlist;
    }
    public GradeListAdapter(Context context, List<GradeInfoBean> list){
        this.mcontext = context;
        this.mlist = list;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.gradelist_item,null);
            viewHolder.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            viewHolder.tv_subject_type = (TextView) convertView.findViewById(R.id.tv_subject_type);
            viewHolder.tv_subject_grade = (TextView) convertView.findViewById(R.id.tv_subject_grade);
            viewHolder.tv_credit = (TextView) convertView.findViewById(R.id.tv_credit);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * PJKG 如果大于0不需要判断，为0时判断查看状态（CKZT）是否为0 ，为0时可以查看成绩 , 大于0时查找评教状态（PJZT），
         评教状态为0表示未评教，大于0已评教
         */
        if(mlist.get(position).getPJKG()==0&&mlist.get(position).getCKZT()>0&&mlist.get(position).getPJZT()==0){
            viewHolder.tv_subject.setText(mlist.get(position).getKCM()+"");
            viewHolder.tv_subject_type.setText(mlist.get(position).getKCXZMC()+"");
            viewHolder.tv_subject_grade.setText("未评教");
            viewHolder.tv_credit.setText(mlist.get(position).getXF()+"分");
            viewHolder.tv_subject_grade.setTextColor(mcontext.getResources().getColor(R.color.red));
        }else{
            viewHolder.tv_subject.setText(mlist.get(position).getKCM()+"");
            viewHolder.tv_subject_type.setText(mlist.get(position).getKCXZMC()+"");
            double grade = mlist.get(position).getZCJ();
            if(grade < 10){ //成绩小于10的时候，为了布局美观补两个空格，对其布局
                viewHolder.tv_subject_grade.setText(grade+"  ");
            }else{
                viewHolder.tv_subject_grade.setText(grade+"");
            }
            if(grade<60){//成绩小于60分 显示红色
                viewHolder.tv_subject_grade.setTextColor(mcontext.getResources().getColor(R.color.red));
            }else{
                viewHolder.tv_subject_grade.setTextColor(mcontext.getResources().getColor(R.color.black));
            }
            viewHolder.tv_credit.setText(mlist.get(position).getXF()+"分");
        }
        return convertView;
    }

    class ViewHolder{
        private TextView tv_subject;//考试科目
        private TextView tv_subject_type;//课程性质
        private TextView tv_subject_grade;//成绩
        private TextView tv_credit;//学分
    }
}
