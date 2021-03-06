package example.tiny.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import example.tiny.backetball.BasketBallApp;
import example.tiny.backetball.R;
import example.tiny.data.Team;

/**
 * Created by tiny on 15-9-9.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Team> mTeamList;


    public TeamRecyclerAdapter(Context context) {
        super();
        mContext = context;
        if(mTeamList == null)
            mTeamList = new ArrayList<Team>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_teams_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).mTvItemTeamSeq.setText((position+1) + ".");
        ((ItemViewHolder)holder).mTvItemTeamName.setText(mTeamList.get(position).getName());
        ImageLoader.getInstance().displayImage(mTeamList.get(position).getLogoUrl(),((ItemViewHolder)holder).mImgItemTeamIcon, BasketBallApp.getTeamIconOptions());
        if(position % 2 == 0)
        holder.itemView.setBackgroundColor(Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }


    public boolean add(Team data) {
        return mTeamList.add(data);
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
