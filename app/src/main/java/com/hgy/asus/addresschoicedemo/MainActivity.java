package com.hgy.asus.addresschoicedemo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.llAddressChoice)
    LinearLayout rl_choose_diqu;
    @Bind(R.id.rlAttestation)
    RelativeLayout ll_parent;
    @Bind(R.id.tvAddress)
    TextView tvAddrssChoice;
    private PopupWindow popupWindow;

    private TextView tv_sheng;//pop标题上的省
    private TextView tv_shi;//pop标题上的市
    private TextView tv_qu;//pop标题上的区
    private TextView line;//下划线
    private ListView listview;//pop中的listView
    private LinearLayout ll_line;
    private Context context;
    private ShengShiQuAdapter adapter;
    private int shi_parent_id;
    private int qu_parent_id;
    private int sheng_id;
    private int shi_id;
    private int qu_id;
    private float fromX;
    private float toX;
    private float to1;
    private float to2;
    private float to3;


    private int index_sheng;
    private int index_shi;
    private int index_qu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        initPopupWind();
    }

    private void initPopupWind() {
        popupWindow = new PopupWindow();
        View popView = LayoutInflater.from(context).inflate(R.layout.popup_choose_diqu, null);
        ImageView iv_chahao = (ImageView) popView.findViewById(R.id.iv_chahao);
        tv_sheng = (TextView) popView.findViewById(R.id.sheng);
        tv_sheng = (TextView) popView.findViewById(R.id.sheng);
        tv_shi = (TextView) popView.findViewById(R.id.shi);
        tv_qu = (TextView) popView.findViewById(R.id.qu);
        line = (TextView) popView.findViewById(R.id.line);
        ll_line = (LinearLayout) popView.findViewById(R.id.ll_line);
        listview = (ListView) popView.findViewById(R.id.listview);
        tv_sheng.setText("请选择");
        popupWindow.setContentView(popView);
        tv_shi.setVisibility(View.GONE);
        tv_qu.setVisibility(View.GONE);
        tv_sheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sheng.setText("请选择");
                tv_sheng.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tv_sheng.getMeasuredWidth() - 60, 6);
                        lp.leftMargin = 30;
                        lp.rightMargin = 30;
                        line.setLayoutParams(lp);
                        return true;
                    }
                });
                tv_shi.setText("");
                tv_qu.setText("");
                tv_shi.setClickable(false);
                tv_qu.setClickable(false);

                to1 = 0;
                toX = to1;
                setLineAnimation(fromX, toX);
                getProvinces(0);
            }
        });

        tv_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_shi.setText("请选择");
                tv_shi.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tv_shi.getMeasuredWidth() - 60, 6);
                        lp.leftMargin = 30;
                        lp.rightMargin = 30;
                        line.setLayoutParams(lp);
                        return true;
                    }
                });

                tv_qu.setText("");
                tv_qu.setClickable(false);
                to2 = tv_sheng.getMeasuredWidth();
                toX = to2;
                setLineAnimation(fromX, toX);
                getAddressInfo(shi_parent_id, 1);
            }
        });

        tv_qu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_qu.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tv_qu.getMeasuredWidth() - 60, 6);
                        lp.leftMargin = 30;
                        lp.rightMargin = 30;
                        line.setLayoutParams(lp);
                        return true;
                    }
                });
                to3 = tv_sheng.getMeasuredWidth() + tv_shi.getMeasuredWidth();
                toX = to3;
                setLineAnimation(fromX, toX);
                getAddressInfo(qu_parent_id, 2);
            }
        });

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                AppUtils.backgroundAlpha(MainActivity.this, 1f);
            }
        });

        /**
         * pop 标题上的×
         */
        iv_chahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                fromX = 0;
            }
        });
    }


    /**
     * 下划线的动画
     *
     * @param fX
     * @param toX
     */
    public void setLineAnimation(float fX, float toX) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fX, toX, 0, 0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        ll_line.setAnimation(translateAnimation);
        fromX = toX;
    }

    @OnClick({R.id.llAddressChoice})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddressChoice:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    getProvinces(0);//获得省的数据
                    popupWindow.showAtLocation(ll_parent, Gravity.BOTTOM, 0, 0);
                    AppUtils.backgroundAlpha(this, 0.5f);
                    tv_sheng.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tv_sheng.getMeasuredWidth() - 60, 6);//
                            lp.leftMargin = 10;
                            lp.rightMargin = 10;
                            line.setLayoutParams(lp);
                            return true;
                        }
                    });
                }
                break;
        }
    }

    /**
     * 获得省的数据
     */
    private void getProvinces(final int type) {
        String url = "http://dm.deepinfo.cn/jxf/app.php/Index/provinces/";
        OkGo.get(url)
                .tag("province")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ShengShiquBean result = new Gson().fromJson(s, ShengShiquBean.class);
                        onChoiceAddress(type, result);
                    }
                });
    }

    /**
     * 获得区域信息
     *
     * @param parent_id
     * @param type
     */
    private void getAddressInfo(int parent_id, final int type) {
        String url = "http://dm.deepinfo.cn/jxf/app.php/Index/distinct/";
        OkGo.get(url).tag("addressInfo")
                .params("pid", parent_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        ShengShiquBean result = gson.fromJson(s, ShengShiquBean.class);
                        onChoiceAddress(type, result);//处理回调
                    }
                });
    }

    /**
     *
     * @param type 0 省 1 市 2 区
     * @param result
     */
    private void onChoiceAddress(int type, ShengShiquBean result) {
        switch (type) {
            case 0://省
                List<ShengShiquBean.DataBean> shengBeanList = result.getData();
                if (adapter == null) {
                    adapter = new ShengShiQuAdapter(context, shengBeanList);
                    adapter.setTag(1);
                    listview.setAdapter(adapter);
                } else {
                    adapter.setTag(1);
                    adapter.setDatas(shengBeanList);
                    listview.smoothScrollToPosition(0);
                }
                break;
            case 1://市
                List<ShengShiquBean.DataBean> shiBeanList = result.getData();
                adapter.setTag(2);
                adapter.setDatas(shiBeanList);
                listview.smoothScrollToPosition(0);
                break;
            case 2://区
                List<ShengShiquBean.DataBean> quBeanList = result.getData();
                if (quBeanList == null || quBeanList.size() <= 0) {
                    tv_qu.setText("");
                    String shengshiqu = tv_sheng.getText().toString() + tv_shi.getText().toString() + tv_qu.getText().toString();
                    tvAddrssChoice.setText(shengshiqu);
                    popupWindow.dismiss();
                    fromX = 0;
                } else {
                    to3 = tv_sheng.getMeasuredWidth() + tv_shi.getMeasuredWidth();
                    toX = to3;
                    setLineAnimation(fromX, toX);
                    adapter.setTag(3);
                    adapter.setDatas(quBeanList);
                    listview.smoothScrollToPosition(0);
                }
                break;
        }

    }

    private class ShengShiQuAdapter extends BaseAdapter {
        public int selectionPosition = -1;
        private int tag;
        private Context context;
        private List<ShengShiquBean.DataBean> data = new ArrayList<>();
        private LayoutInflater inflater;

        public ShengShiQuAdapter(Context context, List<ShengShiquBean.DataBean> data) {
            this.context = context;
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShengShiQuAdapter.ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.pop_item_create_address, null);
                holder = new ShengShiQuAdapter.ViewHolder();
                holder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(holder);
            } else {
                holder = (ShengShiQuAdapter.ViewHolder) convertView.getTag();
            }
            final int finalPosition = position;
            final ShengShiquBean.DataBean dataBean = data.get(position);
            holder.tv.setText(dataBean.getRegion_name());
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectionPosition(finalPosition);
                    switch (tag) {
                        case 1:
                            tv_sheng.setText(dataBean.getRegion_name());
                            tv_shi.setText("请选择");
                            tv_qu.setText("");
                            index_sheng = finalPosition;
                            sheng_id = dataBean.getRegion_id();
                            shi_parent_id = dataBean.getRegion_id();
                            tv_shi.setClickable(true);
                            tv_shi.setVisibility(View.VISIBLE);
                            getAddressInfo(shi_parent_id, 1);//请求网络获得市的信息
                            to2 = tv_sheng.getMeasuredWidth();
                            toX = to2;
                            setLineAnimation(fromX, toX);
                            break;
                        case 2:
                            tv_shi.setText(dataBean.getRegion_name());
                            tv_qu.setText("请选择");
                            index_shi = finalPosition;
                            shi_id = dataBean.getRegion_id();
                            qu_parent_id = dataBean.getRegion_id();
                            tv_qu.setClickable(true);
                            tv_qu.setVisibility(View.VISIBLE);
                            getAddressInfo(qu_parent_id, 2);
                            break;
                        case 3:
                            index_qu = finalPosition;
                            tv_qu.setText(dataBean.getRegion_name());
                            tv_qu.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                                @Override
                                public boolean onPreDraw() {
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tv_qu.getMeasuredWidth() - 60, 6);
                                    lp.leftMargin = 30;
                                    lp.rightMargin = 30;
                                    line.setLayoutParams(lp);
                                    return true;
                                }
                            });
                            String shengshiqu = tv_sheng.getText().toString() + "-" + tv_shi.getText().toString() + "-" + tv_qu.getText().toString();
                            tvAddrssChoice.setText(shengshiqu);
                            qu_id = dataBean.getRegion_id();
                            popupWindow.dismiss();
                            fromX = 0;
                            break;
                    }
                }
            });
            if (selectionPosition == position) {
                holder.tv.setTextColor(getResources().getColor(R.color.red));
                holder.iv.setImageResource(R.mipmap.icon_gou);//选择的图标
                holder.iv.setVisibility(View.VISIBLE);
            } else {
                holder.tv.setTextColor(getResources().getColor(R.color.text_color_black));
                holder.iv.setImageResource(R.color.transparent);
            }
            return convertView;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public void setSelectionPosition(int selectionPosition) {
            this.selectionPosition = selectionPosition;
            notifyDataSetChanged();
        }

        public void setDatas(List<ShengShiquBean.DataBean> newDatas) {
            this.data = newDatas;
            selectionPosition = -1;
            notifyDataSetChanged();
        }

        class ViewHolder {
            public RelativeLayout rl_item;
            public TextView tv;
            public ImageView iv;
        }
    }
}
