package com.toplion.cplusschool.Map;


import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.ab.util.AbJsonUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.Mapbean;
import com.toplion.cplusschool.Bean.ParentMap;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.AMapUtil;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;


import java.util.ArrayList;
import java.util.List;

/**
 * AMapV1地图中简单介绍显示定位小蓝点
 */
public class LocationSourceActivity extends BaseActivity implements LocationSource, OnMapClickListener,
        AMapLocationListener, AMap.OnMarkerClickListener, RouteSearch.OnRouteSearchListener {
    private final int ROUTE_TYPE_WALK = 3;
    private WalkRouteResult mWalkRouteResult;//步行路线规划
    private RouteSearch mRouteSearch;//路线规划的注册类
    private LatLonPoint mStartPoint;//起点，
    private LatLonPoint mEndPoint;//终点，
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLng latlng = new LatLng(36.658126, 117.131828);
    private LatLng dl = new LatLng(36.658035, 117.1305381);
    private LatLng fools = new LatLng(36.658126, 117.131828);
    private LinearLayout jiaoxuelou, shangdian, sushe, canting, yingxin;
    private LinearLayout pop_layout;
    private Marker locationMarker; // 选择的点
    private Marker detailMarker;
    private Marker mlastMarker;
    private myPoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据
    private TextView mapname, maptel, mapaddress, maptext;
    private LinearLayout lay_yuan;
    private Marker pubmaker;
    private TextView shoptext, sushetext, cantingtext, loutext, xintext;
    private ImageView shopimg, susheimg, cantingimg, louimg, yingxinimg;
    private ImageView repair_question_info_return;
    private ParentMap listBean;
    private ImageView leftimg;
    //图片下载类
    private AbImageLoader mAbImageLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationsource_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }


    /**
     * 初始化AMap对象
     */
    protected void init() {
        //图片的下载
        mAbImageLoader = AbImageLoader.getInstance(this);
        poiItems = new ArrayList<PoiItem>();
        repair_question_info_return = (ImageView) findViewById(R.id.repair_question_info_return);
        shoptext = (TextView) findViewById(R.id.shoptext);
        sushetext = (TextView) findViewById(R.id.sushetext);
        cantingtext = (TextView) findViewById(R.id.cantingtext);
        loutext = (TextView) findViewById(R.id.loutext);
        xintext = (TextView) findViewById(R.id.xinetext);
        lay_yuan = (LinearLayout) findViewById(R.id.lay_yuan);
        mapname = (TextView) findViewById(R.id.mapname);
        maptel = (TextView) findViewById(R.id.maptel);
        leftimg = (ImageView) findViewById(R.id.leftimg);
        maptext = (TextView) findViewById(R.id.maptext);
        mapaddress = (TextView) findViewById(R.id.mapaddress);
        pop_layout = (LinearLayout) findViewById(R.id.pop_layout);
        jiaoxuelou = (LinearLayout) findViewById(R.id.jiaoxuelou);
        shangdian = (LinearLayout) findViewById(R.id.shangdian);
        sushe = (LinearLayout) findViewById(R.id.sushe);
        canting = (LinearLayout) findViewById(R.id.canting);
        yingxin = (LinearLayout) findViewById(R.id.yingxin);
        shopimg = (ImageView) findViewById(R.id.shopimg);
        susheimg = (ImageView) findViewById(R.id.susheimg);
        cantingimg = (ImageView) findViewById(R.id.cantingimg);
        louimg = (ImageView) findViewById(R.id.louimg);
        yingxinimg = (ImageView) findViewById(R.id.yingxinimg);
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        shangdian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmenubgColor(1);
                getData(3);
            }
        });
        sushe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmenubgColor(2);
                getData(1);
            }
        });
        canting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmenubgColor(3);
                getData(4);
            }
        });
        jiaoxuelou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmenubgColor(4);
                getData(2);

            }
        });
        yingxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmenubgColor(5);
                getData(5);
            }
        });
        lay_yuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartPoint == null) {
                    ToastManager.getInstance().showToast(LocationSourceActivity.this, "没有定位到您的位置，请重新定位！");
                    return;
                }
                whetherToShowDetailInfo(false);
                mEndPoint = new LatLonPoint(pubmaker.getPosition().latitude, pubmaker.getPosition().longitude);
                aMap.addMarker(new MarkerOptions()
                        .position(AMapUtil.convertToLatLng(mStartPoint))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
                aMap.addMarker(new MarkerOptions()
                        .position(AMapUtil.convertToLatLng(mEndPoint))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
            }
        });
        repair_question_info_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.mapzhezhao));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()));
        // aMap.setMyLocationType()
        // aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        //aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnMapClickListener(this);
        // aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        //aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式


    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mStartPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());//起点，
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        whetherToShowDetailInfo(false);
        if (mlastMarker != null) {
            resetlastmarker();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.mapzhezhao));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

//
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        mEndPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
//        aMap.addMarker(new MarkerOptions()
//                .position(AMapUtil.convertToLatLng(mStartPoint))
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
//        aMap.addMarker(new MarkerOptions()
//                .position(AMapUtil.convertToLatLng(mEndPoint))
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
//        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
//    }
//
//    @Override
//    public void onMapLoaded() {
//        // 设置所有maker显示在当前可视区域地图中
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(dl)
//                .include(latlng).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
//    }

    private void whetherToShowDetailInfo(boolean isToShow) {
        if (isToShow) {
            pop_layout.setVisibility(View.VISIBLE);
            lay_yuan.setVisibility(View.VISIBLE);

        } else {
            pop_layout.setVisibility(View.GONE);
            lay_yuan.setVisibility(View.GONE);
        }
    }

    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        } else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed)));
        }
        mlastMarker = null;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getObject() != null) {
            whetherToShowDetailInfo(true);
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                if (mlastMarker == null) {
                    mlastMarker = marker;
                } else {
                    // 将之前被点击的marker置为原来的状态
                    resetlastmarker();
                    mlastMarker = marker;
                }
                detailMarker = marker;
                pubmaker = marker;
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.mipmap.popdian)));
                setPoiItemDisplayContent(mCurrentPoi);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            whetherToShowDetailInfo(false);
            resetlastmarker();
        }
        return true;
    }


    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        int item = Integer.parseInt(mCurrentPoi.getTitle()) - 1;
        mapname.setText(listBean.getData().get(item).getMAINAME());
        mapaddress.setText(listBean.getData().get(item).getSDSNAME());
        mAbImageLoader.display(leftimg, listBean.getData().get(item).getMAIICONURL());
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(List<Mapbean> maplist) {
        for (int i = 0; i < maplist.size(); i++) {
            LatLonPoint lat = new LatLonPoint(maplist.get(i).getMAILATITUDE(), maplist.get(i).getMAILONGITUDE());
            PoiItem poiItem = new PoiItem("", lat, (i + 1) + "", "");
            poiItems.add(poiItem);
        }
        if (poiItems != null && poiItems.size() > 0) {
            //清除POI信息显示
            whetherToShowDetailInfo(false);
            //并还原点击marker样式
            if (mlastMarker != null) {
                resetlastmarker();
            }
            //清理之前搜索结果的marker
            if (poiOverlay != null) {
                poiOverlay.removeFromMap();
            }
            aMap.clear();
            poiOverlay = new myPoiOverlay(aMap, poiItems);
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan();


//            aMap.addMarker(new MarkerOptions()
//                    .anchor(0.5f, 0.5f)
//                    .icon(BitmapDescriptorFactory
//                            .fromResource(R.mipmap.location_marker))
//                    .position(new LatLng(mStartPoint.getLatitude(), mStartPoint.getLongitude())));

        }

    }


    protected void getData(int type) {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("type", type);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showMapAnnotationInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在查询数据") {
            @Override
            public void Get_Result(String result) {
                listBean = AbJsonUtil.fromJson(result, ParentMap.class);
                if (listBean.getData().size() > 0) {
                    deactivate();
                    aMap.clear();
                    poiItems.clear();
                    addMarkersToMap(listBean.getData());
                }
            }
        });
    }

    /**
     * 绘制系统默认的1种marker背景图片
     */
    public void drawMarkers() {
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("好好学习")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        // marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastManager.getInstance().showToast(this, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastManager.getInstance().showToast(this, "终点未设置");
            return;
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
        mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;

                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.setNodeIconVisibility(false);
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    // mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
//                    mRotueTimeDes.setText(des);
//                    mRouteDetailDes.setVisibility(View.GONE);
//                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext,
//                                    WalkRouteDetailActivity.class);
//                            intent.putExtra("walk_path", walkPath);
//                            intent.putExtra("walk_result",
//                                    mWalkRouteResult);
//                            startActivity(intent);
//                        }
//                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastManager.getInstance().showToast(this, "未找到合适路线");
                }

            } else {
                ToastManager.getInstance().showToast(this, "未找到合适路线");
            }
        } else {
            ToastManager.getInstance().showToast(this, errorCode);
        }

    }

    private int[] markers = {R.mipmap.poi_marker_1,
            R.mipmap.poi_marker_2,
            R.mipmap.poi_marker_3,
            R.mipmap.poi_marker_4,
            R.mipmap.poi_marker_5,
            R.mipmap.poi_marker_6,
            R.mipmap.poi_marker_7,
            R.mipmap.poi_marker_8,
            R.mipmap.poi_marker_9,
            R.mipmap.poi_marker_10
    };

    @Override
    public void onMapClick(LatLng latLng) {
        whetherToShowDetailInfo(false);
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }


    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public myPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         *
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         *
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         *
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            } else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed));
                return icon;
            }
        }
    }

    private void setmenubgColor(int mun) {
        switch (mun) {
            case 1:
                shopimg.setImageResource(R.mipmap.shop1);
                shoptext.setTextColor(getResources().getColor(R.color.logo_color));


                susheimg.setImageResource(R.mipmap.sushe);
                sushetext.setTextColor(getResources().getColor(R.color.mapcoltext));
                cantingimg.setImageResource(R.mipmap.canting);
                cantingtext.setTextColor(getResources().getColor(R.color.mapcoltext));
                louimg.setImageResource(R.mipmap.lou);
                loutext.setTextColor(getResources().getColor(R.color.mapcoltext));
                yingxinimg.setImageResource(R.mipmap.yingxin);
                xintext.setTextColor(getResources().getColor(R.color.mapcoltext));
                break;
            case 2:
                susheimg.setImageResource(R.mipmap.sushe1);
                sushetext.setTextColor(getResources().getColor(R.color.logo_color));


                shopimg.setImageResource(R.mipmap.shop);
                shoptext.setTextColor(getResources().getColor(R.color.mapcoltext));
                cantingimg.setImageResource(R.mipmap.canting);
                cantingtext.setTextColor(getResources().getColor(R.color.mapcoltext));
                louimg.setImageResource(R.mipmap.lou);
                loutext.setTextColor(getResources().getColor(R.color.mapcoltext));
                yingxinimg.setImageResource(R.mipmap.yingxin);
                xintext.setTextColor(getResources().getColor(R.color.mapcoltext));


                break;
            case 3:
                cantingimg.setImageResource(R.mipmap.canting1);
                cantingtext.setTextColor(getResources().getColor(R.color.logo_color));


                shopimg.setImageResource(R.mipmap.shop);
                shoptext.setTextColor(getResources().getColor(R.color.mapcoltext));
                susheimg.setImageResource(R.mipmap.sushe);
                sushetext.setTextColor(getResources().getColor(R.color.mapcoltext));
                louimg.setImageResource(R.mipmap.lou);
                loutext.setTextColor(getResources().getColor(R.color.mapcoltext));
                yingxinimg.setImageResource(R.mipmap.yingxin);
                xintext.setTextColor(getResources().getColor(R.color.mapcoltext));
                break;
            case 4:
                louimg.setImageResource(R.mipmap.lou1);
                loutext.setTextColor(getResources().getColor(R.color.logo_color));

                shopimg.setImageResource(R.mipmap.shop);
                shoptext.setTextColor(getResources().getColor(R.color.mapcoltext));
                susheimg.setImageResource(R.mipmap.sushe);
                sushetext.setTextColor(getResources().getColor(R.color.mapcoltext));
                cantingimg.setImageResource(R.mipmap.canting);
                cantingtext.setTextColor(getResources().getColor(R.color.mapcoltext));
                yingxinimg.setImageResource(R.mipmap.yingxin);
                xintext.setTextColor(getResources().getColor(R.color.mapcoltext));

                break;
            case 5:
                yingxinimg.setImageResource(R.mipmap.yingxin1);
                xintext.setTextColor(getResources().getColor(R.color.logo_color));


                shopimg.setImageResource(R.mipmap.shop);
                shoptext.setTextColor(getResources().getColor(R.color.mapcoltext));
                susheimg.setImageResource(R.mipmap.sushe);
                sushetext.setTextColor(getResources().getColor(R.color.mapcoltext));
                cantingimg.setImageResource(R.mipmap.canting);
                cantingtext.setTextColor(getResources().getColor(R.color.mapcoltext));
                louimg.setImageResource(R.mipmap.lou);
                loutext.setTextColor(getResources().getColor(R.color.mapcoltext));
                break;

        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(pop_layout.isShown()){
                whetherToShowDetailInfo(false);
                return true;
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

