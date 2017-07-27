package test.bwie.com.shejiaoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.bean.DataBean;
import test.bwie.com.shejiaoapp.bean.IndexBean;
import test.bwie.com.shejiaoapp.utils.AMapUtils;
import test.bwie.com.shejiaoapp.utils.DeviceUtils;
import test.bwie.com.shejiaoapp.utils.DisanceUtils;
import test.bwie.com.shejiaoapp.utils.PreferencesUtils;

/**
 * date: 2017/7/11
 * author:陈茹
 * 类的用途:   第一个fragment的适配器
 */

public class IndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<DataBean> list;

    private Context context;
    private int tag = 1; // 1 先行布局 2 瀑布流
    private int itemWidth ;

    public IndexAdapter(Context context,List<DataBean> list) {
        this.context = context;
       this.list=list;

        //当前屏幕 的宽度 除以3
        itemWidth = DeviceUtils.getDisplayInfomation(context).x / 3 ;
    }

    public void setData(IndexBean bean) {
        if (list == null) {
            list = new ArrayList<>();
        }

        list.addAll(bean.getData());
        notifyDataSetChanged();
    }

    /**
     * 2 瀑布流
     * 1 线性布局
     *
     * @param type
     */
    public void dataChange(int type) {
        this.tag = type;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {   //当前显示第一个的界面（线性布局）
            View view = LayoutInflater.from(context).inflate(R.layout.indexfragment_verticaladapter, parent, false);
            VerticalViewHolder verticalViewHolder = new VerticalViewHolder(view);
            view.setOnClickListener(this);

            return verticalViewHolder;

        } else {   //点击切换模式，换一个布局显示界面（瀑布流布局）
            View view = LayoutInflater.from(context).inflate(R.layout.indexfragment_pinterest, parent, false);
            PinterestViewHolder pinterestViewHolder = new PinterestViewHolder(view);
            view.setOnClickListener(this);
            return pinterestViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VerticalViewHolder) {

            //列表的形式展示
            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;

            verticalViewHolder.indexfragmentNickname.setText(list.get(position).getNickname());


            verticalViewHolder.indexfragmentDes.setText(list.get(position).getIntroduce());

            Glide.with(context).load(list.get(position).getImagePath()).error(R.drawable.c).into(verticalViewHolder.indexfragmentFace);

            String lat =  PreferencesUtils.getValueByKey(context, AMapUtils.LAT,"");
            String lng = PreferencesUtils.getValueByKey(context,AMapUtils.LNG,"");

            double olat = list.get(position).getLat();
            double olng = list.get(position).getLng();


            if(!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng) && olat != 0.0 && olng != 0.0){

                double dlat = Double.valueOf(lat);
                double dlng = Double.valueOf(lng);
                DPoint dPoint = new DPoint(dlat,dlng);
                DPoint oPoint = new DPoint(olat,olng);

                //计算两点之间的距离
                float dis =  CoordinateConverter.calculateLineDistance(dPoint,oPoint);

                verticalViewHolder.indexfragmentAgesex.setText(list.get(position).getAge() + "岁 , " + list.get(position).getGender() + " , " + DisanceUtils.standedDistance(dis));
               verticalViewHolder.itemView.setTag(list.get(position).getUserId());
            } else {
                verticalViewHolder.indexfragmentAgesex.setText(list.get(position).getAge() + "岁 , " + list.get(position).getGender());
               verticalViewHolder.itemView.setTag(list.get(position).getUserId());
            }
        } else {
            PinterestViewHolder staggeredViewHolder = (PinterestViewHolder) holder;

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) staggeredViewHolder.indexfragmentStagger.getLayoutParams() ;

            float scale =  (float) itemWidth / (float) list.get(position).getPicWidth()  ;
            params.width = itemWidth;
            params.height = (int)( (float)scale * (float)list.get(position).getPicHeight()) ;

            System.out.println("params.scale = " + scale);
            System.out.println("params.width = " + params.width + " " + list.get(position).getPicWidth());
            System.out.println("params.height = " + params.height + "  " + list.get(position).getPicHeight());

            staggeredViewHolder.indexfragmentStagger.setLayoutParams(params);
            staggeredViewHolder.textzhuanzhi.setText(list.get(position).getNickname());
            Glide.with(context).load(list.get(position).getImagePath()).error(R.drawable.c).into(staggeredViewHolder.indexfragmentStagger);

           staggeredViewHolder.itemView.setTag(list.get(position).getUserId());
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (tag == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(v, (int) v.getTag());
    }

    static class VerticalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.indexfragment_nickname)
        TextView indexfragmentNickname;
        @BindView(R.id.indexfragment_agesex)
        TextView indexfragmentAgesex;
        @BindView(R.id.indexfragment_des)
        TextView indexfragmentDes;
        @BindView(R.id.indexfragment_face)
        ImageView indexfragmentFace;

        public VerticalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class PinterestViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.indexfragment_stagger)
        ImageView indexfragmentStagger;
        @BindView(R.id.text_zhuanzhi)
        TextView textzhuanzhi;
        public PinterestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //点击的接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //监听的方法
    private OnItemClickListener  mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
