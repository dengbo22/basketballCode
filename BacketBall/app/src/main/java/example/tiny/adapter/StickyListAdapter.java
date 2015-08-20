package example.tiny.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.tiny.backetball.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by tiny on 15-8-19.
 */
public class StickyListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context mContext ;
    private String[] dates;
    private LayoutInflater inflater;

    public StickyListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        dates = context.getResources().getStringArray(R.array.my_string_array);
        mContext = context;
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public Object getItem(int position) {
        return dates[position];
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
            convertView = inflater.inflate(R.layout.layout_listitem, parent, false);
            holder.mTvCampus = (TextView) convertView.findViewById(R.id.tv_list_campus);
            holder.mTvCompetitionType = (TextView) convertView.findViewById(R.id.tv_list_competition_type);
            holder.mImgCompetitionTypeIcon = (ImageView) convertView.findViewById(R.id.img_list_competition_typeicon);
            LinearLayout left = (LinearLayout) convertView.findViewById(R.id.layout_team_left);
            holder.mImgTeamAIcon = ( ImageView )left.findViewById(R.id.img_list_team_icon);
            holder.mTvTeamAName = (TextView) left.findViewById(R.id.tv_list_team_name);
            LinearLayout right = (LinearLayout) convertView.findViewById(R.id.layout_team_right);
            holder.mImgTeamBIcon = (ImageView) right.findViewById(R.id.img_list_team_icon);
            holder.mTvTeamBName = (TextView) right.findViewById(R.id.tv_list_team_name);
            holder.mTvGameName = (TextView) convertView.findViewById(R.id.tv_list_game_name);
            holder.mTvTeamAScore = (TextView) convertView.findViewById(R.id.tv_list_teamA_score);
            holder.mTvTeamBScore = (TextView) convertView.findViewById(R.id.tv_list_teamB_score);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvantGardeBkBT.ttf");
            holder.mTvTeamAScore.setTypeface(type);
            holder.mTvTeamBScore.setTypeface(type);
            ((TextView)convertView.findViewById(R.id.tv_list_score_center)).setTypeface(type);
            holder.mTvCompetitionStatus = (TextView)convertView.findViewById(R.id.tv_list_competition_status);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //具体操作
        holder.mImgTeamAIcon.setImageResource(R.drawable.troop_a);
        holder.mTvTeamAName.setText("11级A班");
        holder.mImgTeamBIcon.setImageResource(R.drawable.troop_b);
        holder.mTvTeamBName.setText("11级B班");

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.layout_listheader, viewGroup, false);
            holder.mHeaderText = (TextView) convertView.findViewById(R.id.tv_listheader_date);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //具体操作

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return position / 2;
    }

    class ViewHolder {
        TextView mTvCampus;
        TextView mTvCompetitionType;
        ImageView mImgCompetitionTypeIcon;
        ImageView mImgTeamAIcon;
        TextView mTvTeamAName;
        ImageView mImgTeamBIcon;
        TextView mTvTeamBName;
        TextView mTvGameName;
        TextView mTvTeamAScore;
        TextView mTvTeamBScore;
        TextView mTvCompetitionStatus;
    }

    class HeaderViewHolder {
        TextView mHeaderText;
    }
}
