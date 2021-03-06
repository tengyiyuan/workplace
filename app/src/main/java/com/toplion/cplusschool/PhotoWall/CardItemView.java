package com.toplion.cplusschool.PhotoWall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbImageUtil;
import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.toplion.cplusschool.PhotoWall.bean.PhotoInfoBean;
import com.toplion.cplusschool.R;

/**
 * 卡片View项
 *
 * @author tengyy
 */
@SuppressLint("NewApi")
public class CardItemView extends FrameLayout {
    private Spring springX, springY;
    public ImageView imageView;
    public View maskView;
    private TextView userNameTv;
    private TextView imageNumTv;
    private TextView likeNumTv;
    private TextView tvSchoolName;//学校
    private ImageView iv_sex;//性别
    private ImageView rentou;//头像
    private CardSlidePanel parentView;
    private View topLayout, bottomLayout;
    private Context context;

    public CardItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context=context;
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.card_item, this);
        imageView = (ImageView) findViewById(R.id.card_image_view);
        maskView = findViewById(R.id.maskView);
        userNameTv = (TextView) findViewById(R.id.card_user_name);
        imageNumTv = (TextView) findViewById(R.id.card_pic_num);
        tvSchoolName = (TextView) findViewById(R.id.card_other_description);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        rentou = (ImageView) findViewById(R.id.rentou);
        likeNumTv = (TextView) findViewById(R.id.card_like);
        topLayout = findViewById(R.id.card_top_layout);
        bottomLayout = findViewById(R.id.card_bottom_layout);
        this.context=context;
        initSpring();
    }

    private void initSpring() {
        SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(15, 20);
        SpringSystem mSpringSystem = SpringSystem.create();
        springX = mSpringSystem.createSpring().setSpringConfig(springConfig);
        springY = mSpringSystem.createSpring().setSpringConfig(springConfig);

        springX.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int xPos = (int) spring.getCurrentValue();
                setScreenX(xPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });

        springY.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                int yPos = (int) spring.getCurrentValue();
                setScreenY(yPos);
                parentView.onViewPosChanged(CardItemView.this);
            }
        });
    }

    public void fillData(PhotoInfoBean itemData) {
        ImageLoader.getInstance().displayImage(itemData.getPWBURL(), imageView);
//        Glide.with(context).load(itemData.imagePath).into(imageView);
        userNameTv.setText(itemData.getNC());
        imageNumTv.setText("");
        likeNumTv.setText(itemData.getPWBFLOWERSNUMBER()+"");
        tvSchoolName.setText(itemData.getSDS_NAME());
        if(itemData.getXBM().equals("2")){
            iv_sex.setImageResource(R.mipmap.girl);
        }else if(itemData.getXBM().equals("1")){
            iv_sex.setImageResource(R.mipmap.boy);
        }
        loadHead(itemData.getTXDZ());
    }
    /**
     * 加载头像
     * @param url
     */
    private void loadHead(String url) {
        Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.rentou);
        final int roundPx = bt.getWidth()/2;
        AbImageLoader.getInstance(context).download(url, bt.getWidth(), bt.getWidth(), new AbImageLoader.OnImageListener2() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                bitmap = AbImageUtil.toRoundBitmap(bitmap, roundPx);
                rentou.setImageBitmap(bitmap);
            }
        });
    }

    /**
     * 动画移动到某个位置
     */
    public void animTo(int xPos, int yPos) {
        setCurrentSpringPos(getLeft(), getTop());
        springX.setEndValue(xPos);
        springY.setEndValue(yPos);
    }

    /**
     * 设置当前spring位置
     */
    private void setCurrentSpringPos(int xPos, int yPos) {
        springX.setCurrentValue(xPos);
        springY.setCurrentValue(yPos);
    }

    public void setScreenX(int screenX) {
        this.offsetLeftAndRight(screenX - getLeft());
    }

    public void setScreenY(int screenY) {
        this.offsetTopAndBottom(screenY - getTop());
    }

    public void setParentView(CardSlidePanel parentView) {
        this.parentView = parentView;
    }

    public void onStartDragging() {
        springX.setAtRest();
        springY.setAtRest();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 兼容ViewPager，触点需要按在可滑动区域才行
            boolean shouldCapture = shouldCapture((int) ev.getX(), (int) ev.getY());
            if (shouldCapture) {
                parentView.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断(x, y)是否在可滑动的矩形区域内
     * 这个函数也被CardSlidePanel调用
     *
     * @param x 按下时的x坐标
     * @param y 按下时的y坐标
     * @return 是否在可滑动的矩形区域
     */
    public boolean shouldCapture(int x, int y) {
        int captureLeft = topLayout.getLeft() + topLayout.getPaddingLeft();
        int captureTop = topLayout.getTop() + topLayout.getPaddingTop();
        int captureRight = bottomLayout.getRight() - bottomLayout.getPaddingRight();
        int captureBottom = bottomLayout.getBottom() - bottomLayout.getPaddingBottom();

        return x > captureLeft && x < captureRight && y > captureTop && y < captureBottom;
    }
}
