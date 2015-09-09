package example.tiny.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import example.tiny.backetball.BasketBallApp;
import example.tiny.backetball.R;

/**
 * Created by tiny on 15-9-9.
 */
public class ReportRecyclerAdapter extends RecyclerView.Adapter {
    Context mContext ;

    public ReportRecyclerAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_game_report_item, parent, false);
        return new ReportItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReportItemViewHolder)holder).mTvReportTopic.setText("东哥最后三秒神奇绝杀，11级A班夺取总冠军，然而并没有什么卵用！");
        ((ReportItemViewHolder)holder).mTvReportAuthor.setText("中东篮协");
        ((ReportItemViewHolder)holder).mTvReportTime.setText("2小时前");
        ImageLoader.getInstance().displayImage("url", ((ReportItemViewHolder) holder).mImgReportHeader, BasketBallApp.getBackGroundOptions());
    }

    @Override
    public int getItemCount() {
        return 20;
    }


    class ReportItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgReportHeader ;
        TextView mTvReportTopic;
        TextView mTvReportTime;
        TextView mTvReportAuthor;

        public ReportItemViewHolder(View itemView) {
            super(itemView);
            mImgReportHeader = (ImageView) itemView.findViewById(R.id.img_game_report_img);
            mTvReportTopic = (TextView) itemView.findViewById(R.id.tv_game_report_topic);
            LinearLayout time = (LinearLayout) itemView.findViewById(R.id.ictxt_game_report_time);
            LinearLayout author = (LinearLayout) itemView.findViewById(R.id.ictxt_game_report_author);
            ((ImageView)time.findViewById(R.id.the_img)).setImageResource(R.drawable.icon_clock);
            ((ImageView)author.findViewById(R.id.the_img)).setImageResource(R.drawable.icon_author);
            mTvReportTime = (TextView) time.findViewById(R.id.the_text);
            mTvReportAuthor = (TextView) author.findViewById(R.id.the_text);

        }
    }
}
