package test.bwie.com.shejiaoapp.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.utils.EaseSmileUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import test.bwie.com.shejiaoapp.R;
import test.bwie.com.shejiaoapp.speex.SpeexPlayer;

//聊天的使用的recyclerview，，这个类是recyclerview的适配器


public class ChatMeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    Context context;
    List<EMMessage> list;
    private View view;
    private View view1;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    public ChatMeAdapter(Context context, List<EMMessage> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //文本的布局
        if (viewType == 1) {
            View view1 = LayoutInflater.from(context).inflate(R.layout.text_left, parent, false);
            LefltHolder viewHolderLeftText = new LefltHolder(view1);

            return viewHolderLeftText;

        } else  if (viewType == 2){
            View view2 = LayoutInflater.from(context).inflate(R.layout.text_right, parent, false);
            RightHolder viewHolderRightText = new RightHolder(view2);

            return viewHolderRightText;
        }
        //语音的布局  layout
        else if (viewType == 3) {
            view = LayoutInflater.from(context).inflate(R.layout.text_left_voice, parent, false);
            LefltHoldervoic viewHolderLeftText = new LefltHoldervoic(view);
            view.setOnClickListener(this);
            return viewHolderLeftText;

        } else {
            view1 = LayoutInflater.from(context).inflate(R.layout.text_right_voice, parent, false);
            RightHoldervoic  viewHolderRightText = new RightHoldervoic(view1);
            view1.setOnClickListener(this);
            return viewHolderRightText;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //别人发过来的文本
        if (holder instanceof LefltHolder) {
            LefltHolder leftholder = (LefltHolder) holder;
            String txt = list.get(position).getBody().toString();
            String string = txt.substring(5, txt.length() - 1);
            leftholder.textLeftImageText.setText(EaseSmileUtils.getSmiledText(context, string));

            //自己发的文本
        } else if (holder instanceof RightHolder) {
            RightHolder rightholder = (RightHolder) holder;
            String Msg = list.get(position).getBody().toString();
            String sendMsg = Msg.substring(5, Msg.length() - 1);
            rightholder.textRightImageText.setText(  EaseSmileUtils.getSmiledText(context, sendMsg));
  //TODO -------数据绑定
        } else {
            if (holder instanceof LefltHoldervoic) {
                final LefltHoldervoic leftholdervice = (LefltHoldervoic) holder;
                //听别人发过来的语音监听
                leftholdervice.textLeftImageTextvoic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EMVoiceMessageBody emb =(EMVoiceMessageBody) list.get(position).getBody();
                        SpeexPlayer player = new SpeexPlayer(emb.getLocalUrl(),handler);
                        player.startPlay();


                        leftholdervice.textLeftImageTextvoic.setImageResource(R.drawable.animation1);
                        AnimationDrawable animationDrawable = (AnimationDrawable)  leftholdervice.textLeftImageTextvoic.getDrawable();
                        animationDrawable.start();
                    }

                });


            } else {
                final RightHoldervoic rightholdervice = (RightHoldervoic) holder;

                //听自己发送的语音监听
                rightholdervice.textRightImageTextvoic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EMVoiceMessageBody emb =(EMVoiceMessageBody) list.get(position).getBody();
                        SpeexPlayer player = new SpeexPlayer(emb.getLocalUrl(),handler);
                        player.startPlay();

                        rightholdervice.textRightImageTextvoic.setImageResource(R.drawable.animation2);
                        AnimationDrawable animationDrawable = (AnimationDrawable)  rightholdervice.textRightImageTextvoic.getDrawable();
                        animationDrawable.start();
                    }

                });

            }
        }

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemViewType(int position) {

        //文本
        if (list.get(position).getType() == EMMessage.Type.TXT) {

            if (list.get(position).direct() == EMMessage.Direct.RECEIVE) {
                return 2;//发送文本
            } else {
                return 1;  //接收文本
            }
            //如果是语音
        }
        else if (list.get(position).getType() == EMMessage.Type.VOICE){

            if (list.get(position).direct() == EMMessage.Direct.RECEIVE){
                return 4;  //发送语音
            }else {
                return 3;   //接收语音
            }
     }
        return  -1;
    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
    }

    //接收文本viewholder
    class LefltHolder extends RecyclerView.ViewHolder {


        private final TextView textLeftImageText;
        private final CircleImageView textLeftImage;

        public LefltHolder(View itemView) {
            super(itemView);
            textLeftImageText = (TextView) itemView.findViewById(R.id.text_left_image_text);
            textLeftImage = (CircleImageView) itemView.findViewById(R.id.text_left_image);
        }
    }


    //发送文本viewholder
    class RightHolder extends RecyclerView.ViewHolder {

        private final TextView textRightImageText;
        private final CircleImageView textRightImage;

        public RightHolder(View itemView) {
            super(itemView);

            textRightImage = (CircleImageView) itemView.findViewById(R.id.text_right_image);
            textRightImageText = (TextView) itemView.findViewById(R.id.text_right_image_text);


        }


    }

    //接收语音的viewholder
    class LefltHoldervoic extends RecyclerView.ViewHolder {
        private final ImageView textLeftImageTextvoic;
        private final CircleImageView textLeftImage;

        public LefltHoldervoic(View itemView) {
            super(itemView);

            textLeftImageTextvoic = (ImageView) itemView.findViewById(R.id.text_left_image_text);
            textLeftImage = (CircleImageView) itemView.findViewById(R.id.text_left_imagevoic);
        }
    }
    //发送语音viewholder
    class RightHoldervoic extends RecyclerView.ViewHolder {
        private final CircleImageView textRightImage;
        private final ImageView textRightImageTextvoic;

        public RightHoldervoic(View itemView) {
            super(itemView);

            textRightImage = (CircleImageView) itemView.findViewById(R.id.text_right_imagevoic);
            textRightImageTextvoic = (ImageView) itemView.findViewById(R.id.text_right_image_text);

        }
    }



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //监听的方法
    private OnItemClickListener  mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }




}
