package com.toplion.cplusschool.PhotoWall;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.PhotoWall.SelectPhoto.SelectPhotoActivity;
import com.toplion.cplusschool.PhotoWall.bean.PhotoInfoBean;
import com.toplion.cplusschool.QianDao.QianDaoActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SelectPicUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.ActionItem;
import com.toplion.cplusschool.widget.SendFlowerPopup;
import com.toplion.cplusschool.widget.TitlePopup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

    private List<PhotoInfoBean> dataList = new ArrayList<PhotoInfoBean>();//已加载图片列表
    private TextView tianjia;
    private static int SELECTIMG = 500;//选择图片
    private static int RELEASEPHOTO = 501;//发布照片
    private static int PHOTOWALLDETAIL = 503;
    private SharePreferenceUtils share;
    private CardSlidePanel slidePanel;
    private Button card_left_btn;
    private Button card_right_btn;
    private ImageView about_iv_return;
    private AbHttpUtil abHttpUtil;
    private int pageIndex = 1;//当前页数
    private PhotoInfoBean nowCard;//当前显示的照片
    private View popView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_layout, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        abHttpUtil = AbHttpUtil.getInstance(getActivity());
        share = new SharePreferenceUtils(getActivity());
        about_iv_return = (ImageView) rootView.findViewById(R.id.about_iv_return);
        slidePanel = (CardSlidePanel) rootView.findViewById(R.id.image_slide_panel);
        tianjia = (TextView) rootView.findViewById(R.id.tianjia);
        card_left_btn = (Button) rootView.findViewById(R.id.card_left_btn);
        card_right_btn = (Button) rootView.findViewById(R.id.card_right_btn);
        //实例化标题栏弹窗
        titlePopup = new TitlePopup(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        initData();
        prepareDataList(true);
        setListener();
    }

    /**
     * 加载照片信息
     *
     * @param bln
     */
    private void prepareDataList(final boolean bln) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("findPhotowallInfo") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("page", pageIndex);
        params.put("pageCount", 10);//每页显示多少条数据
        abHttpUtil.post(url, params, new CallBackParent(getActivity(), bln) {
            @Override
            public void Get_Result(String result) {
                try {
                    Log.e("pageIndex", pageIndex + "");
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    List<PhotoInfoBean> list = new ArrayList<PhotoInfoBean>();
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            PhotoInfoBean infoBean = new PhotoInfoBean();
                            infoBean.setPWBID(Function.getInstance().getInteger(jsonObject, "PWBID"));
                            infoBean.setNC(Function.getInstance().getString(jsonObject, "NC"));
                            infoBean.setPWBURL(Function.getInstance().getString(jsonObject, "PWBURL").replace("thumb/", ""));
                            infoBean.setPWBFLOWERSNUMBER(Function.getInstance().getInteger(jsonObject, "PWBFLOWERSNUMBER"));
                            infoBean.setSDS_NAME(Function.getInstance().getString(jsonObject, "SDS_NAME"));
                            infoBean.setTXDZ(Function.getInstance().getString(jsonObject, "TXDZ").replace("thumb/", ""));
                            infoBean.setXBM(Function.getInstance().getString(jsonObject, "XBM"));
                            list.add(infoBean);
                        }
                    }
                    dataList.addAll(list);
                    if (bln) {
                        if (dataList != null && dataList.size() > 0) {
                            slidePanel.fillData(dataList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //送鲜花
    private void giveFlowers(final int num) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("giveFlowers") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("ROLE_ID", ""));
        params.put("photoid", nowCard.getPWBID());//照片id
        params.put("goodid", "1");
        params.put("count", num);//每次赠送的数量
        abHttpUtil.post(url, params, new CallBackParent(getActivity(), getResources().getString(R.string.loading), "") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String msg = json.getString("msg");
                    int count = json.getInt("data");//现有鲜花数量
                    share.put("SBIFLOWERSNUMBER", count);
                    int index = dataList.indexOf(nowCard);
                    nowCard.setPWBFLOWERSNUMBER(nowCard.getPWBFLOWERSNUMBER() + num);
                    slidePanel.notifyData(index);//刷新当前卡片
                    ToastManager.getInstance().showToast(getActivity(), msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListener() {
        card_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
                popView = v;
            }
        });
        card_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowCard == null) {
                    ToastManager.getInstance().showToast(getActivity(), "暂无照片信息");
                    return;
                }
                Intent intent = new Intent(getActivity(), PhotoContent.class);
                intent.putExtra("cardItem", nowCard);
                startActivityForResult(intent, PHOTOWALLDETAIL);
            }
        });
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {
            @Override
            public void onShow(int index) {
                Log.d("CardFragment", "正在显示-" + dataList.get(index).getNC() + " index=" + index);
                nowCard = dataList.get(index);
                if ((dataList.size() - index) <= 5) {
                    pageIndex++;
                    prepareDataList(false);
                }
            }

            @Override
            public void onCardVanish(int index, int type) {
                Log.d("CardFragment", "正在消失-" + dataList.get(index).getNC() + " index=" + index);
                Log.d("CardFragment", "datalist长度" + dataList.size());
            }

            @Override
            public void onItemClick(View cardView, int index) {
                if (nowCard == null) {
                    ToastManager.getInstance().showToast(getActivity(), "暂无照片信息");
                    return;
                }
                Intent intent = new Intent(getActivity(), PhotoContent.class);
                intent.putExtra("cardItem", nowCard);
                startActivityForResult(intent, PHOTOWALLDETAIL);
            }
        };
        tianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(getActivity(), SelectPhotoActivity.class);
                        intent0.putExtra("maxImgCount", 1);//最大可选张数
                        startActivityForResult(intent0, SELECTIMG);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), MyHomeActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), FlowerRankListActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
        slidePanel.setCardSwitchListener(cardSwitchListener);
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void showPop() {
        if (nowCard == null) {
            ToastManager.getInstance().showToast(getActivity(), "暂无照片信息");
            return;
        }

        final FlowersPopWindow flowersPopWindow = new FlowersPopWindow(getActivity(), slidePanel);
        flowersPopWindow.setSendFlowerList(initFlowerData());
        final int nowFlowerNum = share.getInt("SBIFLOWERSNUMBER", 0);
        flowersPopWindow.setNowFlowersNumber(nowFlowerNum);
//        if (Integer.parseInt(flowersPopWindow.tv_number.getText().toString()) >= nowFlowerNum) {
//            flowersPopWindow.iv_jia.setEnabled(false);
//        } else {
//            flowersPopWindow.iv_jia.setEnabled(true);
//        }
        flowersPopWindow.iv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(flowersPopWindow.tv_number.getText().toString());
                if (i > 1) {
                    i--;
                    flowersPopWindow.tv_number.setText(i + "");
                }
            }
        });
        flowersPopWindow.iv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(flowersPopWindow.tv_number.getText().toString());
                if (i < nowFlowerNum) {
                    i++;
                    flowersPopWindow.tv_number.setText(i + "");
                }
            }
        });
        flowersPopWindow.setGiveFlowerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(flowersPopWindow.tv_number.getText().toString());
                if (n > 0) {
                    if (n <= nowFlowerNum) {
                        giveFlowers(n);//送花
                        flowersPopWindow.dismiss();
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "超出您拥有的的鲜花数量");
                    }
                } else {
                    ToastManager.getInstance().showToast(getActivity(), "至少送一朵鲜花");
                }
            }
        });

        flowersPopWindow.setZuanFlowerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QianDaoActivity.class);
                startActivity(intent);
                flowersPopWindow.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECTIMG) {
                List<String> imgList = data.getStringArrayListExtra("imgList");
                String imgPath = data.getStringExtra("imgPath");
                if (imgList != null && imgList.size() > 0) {
                    imgPath = imgList.get(0);
                }
                ImgCrop(imgPath);
            } else if (requestCode == SelectPicUtil.IMGCROP) {//裁剪完成后跳转发布界面
                Uri uri = SelectPicUtil.onActivityResultUri(requestCode, resultCode);
                if (uri != null && uri.getPath() != null) {
                    getUploadUrl(new File(uri.getPath()));
                }
            } else if (requestCode == RELEASEPHOTO) {
                Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                intent.putExtra("maxImgCount", 1);//最大可选张数
                startActivityForResult(intent, SELECTIMG);
            } else if (requestCode == PHOTOWALLDETAIL) {
                if (data != null) {
                    int n = data.getIntExtra("giveFlowerNum", 0);
//                    nowCard.likeNum = nowCard.likeNum + n;
                    nowCard.setPWBFLOWERSNUMBER(nowCard.getPWBFLOWERSNUMBER() + n);
                    int index = dataList.indexOf(nowCard);
                    slidePanel.notifyData(index);//刷新当前卡片
                }
            }
        }
    }

    private void getUploadUrl(final File file) {
        AbDialogUtil.showProgressDialog(getActivity(), 0, "图片正在上传...");
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getUploadUrl") + Constants.BASEPARAMS;
        abHttpUtil.post(url, new CallBackParent(getActivity(), false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    if (!TextUtils.isEmpty(data)) {
                        uploadImg(data, file);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "上传失败，请重试");
                        AbDialogUtil.removeDialog(getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(getActivity(), "上传失败，请重试");
                    AbDialogUtil.removeDialog(getActivity());
                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                ToastManager.getInstance().showToast(getActivity(), "上传失败，请重试");
                AbDialogUtil.removeDialog(getActivity());
            }
        });
    }

    //上传照片
    private String result;

    private void uploadImg(final String uploadUrl, final File file) {

        AbTask task = AbTask.newInstance();
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                String url = uploadUrl + "upload_image.php?ss=ss" + Constants.BASEPARAMS;
                result = HttpUtils.uploadFile(url, file);
                Log.e("result", result + ":" + file.getName());
            }

            @Override
            public void update() {
                super.update();
                try {
                    AbDialogUtil.removeDialog(getActivity());
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
//                        int imgId = json.getInt("IRIID");
                        String imgUrl = json.getString("IRIURL");
                        if (!TextUtils.isEmpty(imgUrl)) {
                            Intent intent = new Intent(getActivity(), PhotoReleaseActivity.class);
                            intent.putExtra("imgUri", imgUrl.replace("thumb/", ""));
                            startActivityForResult(intent, RELEASEPHOTO);
                        }
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "上传失败,请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(getActivity(), "上传失败,请重试");
                }
            }
        });
        task.execute(item);
    }

    private void ImgCrop(String imgPath) {
        Uri uri = Uri.fromFile(new File(imgPath));//将paht转换uri （切记必须这样转换，直接使用Uri.parse()报错）
        if (uri != null && uri.getPath() != null) {
            Intent intent = SelectPicUtil.goImgCrop(uri, BaseApplication.ScreenWidth, BaseApplication.ScreenWidth, 1, 1);
            startActivityForResult(intent, SelectPicUtil.IMGCROP);
        } else {
            ToastManager.getInstance().showToast(getActivity(), "换张图片试试" + uri.getPath());
        }
    }

    private List<Map<String, String>> initFlowerData() {
        List<Map<String, String>> mlist = new ArrayList<Map<String, String>>();
        //给标题栏弹窗添加子类
        Map<String, String> map = new HashMap<String, String>();
        map.put("count", "99");
        map.put("des", "天长地久");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "66");
        map.put("des", "六六大顺");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "50");
        map.put("des", "五彩缤纷");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "10");
        map.put("des", "十全十美");
        mlist.add(map);
        map = new HashMap<String, String>();
        map.put("count", "1");
        map.put("des", "一心一意");
        mlist.add(map);
        return mlist;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getActivity(), "我要发布", R.mipmap.photofabu));
        titlePopup.addAction(new ActionItem(getActivity(), "个人主页", R.mipmap.myfabu));
        titlePopup.addAction(new ActionItem(getActivity(), "排行榜单", R.mipmap.photopaihang));
    }
}
