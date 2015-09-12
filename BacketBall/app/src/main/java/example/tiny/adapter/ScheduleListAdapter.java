package example.tiny.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import example.tiny.backetball.BasketBallApp;
import example.tiny.backetball.R;
import example.tiny.data.LiveItemData;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by tiny on 15-9-12.
 */
public class ScheduleListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    ArrayList<LiveItemData> mScheduleData = null;
    Context mContext ;

    public ScheduleListAdapter(Context context) {
        super();
        if(mScheduleData == null)
            mScheduleData = new ArrayList<LiveItemData>();
        mContext = context;

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if(convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_schedule_header, viewGroup, false);
            holder.mHeaderScheduleText = (TextView)convertView.findViewById(R.id.tv_schedule_header);
            convertView.setTag(holder);
        }else
            holder = (HeaderViewHolder) convertView.getTag();

        //Set Text
        holder.mHeaderScheduleText.setText(mScheduleData.get(position).getCompetitionTypeData());

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        Log.e("ScheduleListAdapter", "getHeaderId()执行");
        LiveItemData data = mScheduleData.get(position);
        if("总决赛".equals(data.getCompetitionTypeData()))
            return 0;
        else if("半决赛".equals(data.getCompetitionTypeData()))
            return 1;
        else if("小组赛".equals(data.getCompetitionTypeData()))
            return 2;
        else
            return 3;
    }

    @Override
    public int getCount() {
        return mScheduleData.size();
    }

    @Override
    public Object getItem(int position) {
        return mScheduleData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_listitem, parent, false);
            holder.mTvScheduleCampus = (TextView) convertView.findViewById(R.id.tv_list_campus);
            holder.mTvScheduleCompetitionType = (TextView) convertView.findViewById(R.id.tv_list_competition_type);
            holder.mImgScheduleCompetitionTypeIcon = (ImageView) convertView.findViewById(R.id.img_list_competition_typeicon);
            LinearLayout left = (LinearLayout) convertView.findViewById(R.id.layout_team_left);
            holder.mImgScheduleTeamAIcon = ( ImageView )left.findViewById(R.id.img_list_team_icon);
            holder.mTvScheduleTeamAName = (TextView) left.findViewById(R.id.tv_list_team_name);
            LinearLayout right = (LinearLayout) convertView.findViewById(R.id.layout_team_right);
            holder.mImgScheduleTeamBIcon = (ImageView) right.findViewById(R.id.img_list_team_icon);
            holder.mTvScheduleTeamBName = (TextView) right.findViewById(R.id.tv_list_team_name);
            holder.mTvScheduleGameName = (TextView) convertView.findViewById(R.id.tv_list_game_name);
            holder.mTvScheduleTeamAScore = (TextView) convertView.findViewById(R.id.tv_list_teamA_score);
            holder.mTvScheduleTeamBScore = (TextView) convertView.findViewById(R.id.tv_list_teamB_score);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvantGardeBkBT.ttf");
            holder.mTvScheduleTeamAScore.setTypeface(type);
            holder.mTvScheduleTeamBScore.setTypeface(type);
            ((TextView)convertView.findViewById(R.id.tv_list_score_center)).setTypeface(type);
            holder.mTvScheduleCompetitionStatus = (TextView)convertView.findViewById(R.id.tv_list_competition_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        //具体操作
        LiveItemData positionData = mScheduleData.get(position);

        holder.mTvScheduleCampus.setText(positionData.getCampusData());
        String competitionType = positionData.getCompetitionTypeData();
        holder.mTvScheduleCompetitionType.setText(competitionType);
        if(competitionType.equals("半决赛"))
            holder.mImgScheduleCompetitionTypeIcon.setImageResource(R.drawable.listitem_competitiontype_semifinal);
        else if(competitionType.equals("小组赛"))
            holder.mImgScheduleCompetitionTypeIcon.setImageResource(R.drawable.listitem_competitiontype_group);

        holder.mTvScheduleTeamAName.setText(positionData.getTeamANameData());
        holder.mTvScheduleTeamAScore.setText(positionData.getTeamAScoreData() + "");
        holder.mTvScheduleTeamBName.setText(positionData.getTeamBNameData());
        holder.mTvScheduleTeamBScore.setText(positionData.getTeamBScoreData() + "");

        ImageLoader.getInstance().displayImage(positionData.getTeamAIconData(), holder.mImgScheduleTeamAIcon, BasketBallApp.getTeamIconOptions());
        ImageLoader.getInstance().displayImage(positionData.getTeamBIconData(), holder.mImgScheduleTeamBIcon, BasketBallApp.getTeamIconOptions());


        holder.mTvScheduleGameName.setText(positionData.getGameNameData());


        String competitionState = positionData.getCompetitionStatusData();
        if(competitionState.equals("未开始") ){
            String time = new SimpleDateFormat("HH:mm 开始").format(positionData.getBeginTime());
            holder.mTvScheduleCompetitionStatus.setText(time);
            holder.mTvScheduleCompetitionStatus.setTextColor(Color.rgb(246, 177, 57));
        }else {
            holder.mTvScheduleCompetitionStatus.setText(positionData.getCompetitionStatusData());
            holder.mTvScheduleCompetitionStatus.setTextColor(Color.rgb(191, 191, 191));
        }
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;

    }

    class ViewHolder {
        TextView mTvScheduleCampus;
        TextView mTvScheduleCompetitionType;
        ImageView mImgScheduleCompetitionTypeIcon;
        ImageView mImgScheduleTeamAIcon;
        TextView mTvScheduleTeamAName;
        ImageView mImgScheduleTeamBIcon;
        TextView mTvScheduleTeamBName;
        TextView mTvScheduleGameName;
        TextView mTvScheduleTeamAScore;
        TextView mTvScheduleTeamBScore;
        TextView mTvScheduleCompetitionStatus;
    }

    class HeaderViewHolder {
        TextView mHeaderScheduleText;
    }

    public boolean add (LiveItemData data) {
        if(mScheduleData == null)
            mScheduleData = new ArrayList<LiveItemData>();
        return mScheduleData.add(data);
    }
}
