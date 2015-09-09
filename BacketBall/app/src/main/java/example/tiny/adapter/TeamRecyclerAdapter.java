package example.tiny.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import example.tiny.backetball.BasketBallApp;
import example.tiny.backetball.R;

/**
 * Created by tiny on 15-9-9.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;

    public TeamRecyclerAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_teams_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).mTvItemTeamSeq.setText((position+1) + ".");
        ((ItemViewHolder)holder).mTvItemTeamName.setText("11级A班");
//        ImageLoader.getInstance().displayImage("url",((ItemViewHolder)holder).mImgItemTeamIcon, BasketBallApp.getTeamIconOptions());
        if(position % 2 == 0)
        holder.itemView.setBackgroundColor(Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return 20;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTvItemTeamSeq;
        ImageView mImgItemTeamIcon;
        TextView mTvItemTeamName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTvItemTeamSeq = (TextView) itemView.findViewById(R.id.tv_teams_sequence);
            mImgItemTeamIcon = (ImageView) itemView.findViewById(R.id.img_teams_icon);
            mTvItemTeamName = (TextView) itemView.findViewById(R.id.tv_teams_name);
        }
    }
}
