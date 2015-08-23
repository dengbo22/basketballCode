package example.tiny.adapter;


import android.content.Context;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import example.tiny.data.LiveItemData;
import example.tiny.backetball.R;
import example.tiny.pulltorefreshstickylistview.StickyListHeadersAdapter;

/**
 * Created by tiny on 15-8-19.
 */
public class StickyListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private static final String LOG_TAG = "StickyListAdapter";
    private Context mContext ;
    private ImageLoader imageLoader;
    DisplayImageOptions options ;
    public ArrayList<LiveItemData> getCompetitionData() {
        return mCompetitionData;
    }

    private ArrayList<LiveItemData> mCompetitionData = null;
    private LayoutInflater inflater;

    public StickyListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mCompetitionData = new ArrayList<LiveItemData>();
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_team_logo)
                .showImageForEmptyUri(R.drawable.default_team_logo)
                .showImageOnFail(R.drawable.default_team_logo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return mCompetitionData.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(LOG_TAG, "getItem:" +position);
        return mCompetitionData.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d(LOG_TAG, "getItemId:" + position);
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
        LiveItemData positionData = mCompetitionData.get(position);

        holder.mTvCampus.setText(positionData.getCampusData());
        String competitionType = positionData.getCompetitionTypeData();
        holder.mTvCompetitionType.setText(competitionType);
        if(competitionType.equals("半决赛"))
            holder.mImgCompetitionTypeIcon.setImageResource(R.drawable.listitem_competitiontype_semifinal);
        else if(competitionType.equals("小组赛"))
            holder.mImgCompetitionTypeIcon.setImageResource(R.drawable.listitem_competitiontype_group);

        holder.mTvTeamAName.setText(positionData.getTeamANameData());
        holder.mTvTeamAScore.setText(positionData.getTeamAScoreData() + "");
        holder.mTvTeamBName.setText(positionData.getTeamBNameData());
        holder.mTvTeamBScore.setText(positionData.getTeamBScoreData() + "");

        imageLoader.displayImage(positionData.getTeamAIconData(), holder.mImgTeamAIcon, options);
        imageLoader.displayImage(positionData.getTeamBIconData(), holder.mImgTeamBIcon, options);



        holder.mTvGameName.setText(positionData.getGameNameData());


        String competitionState = positionData.getCompetitionStatusData();
        if(competitionState.equals("未开始") ){
            String time = new SimpleDateFormat("HH:mm 开始").format(positionData.getBeginTime());
//            String time = (String) android.text.format.DateFormat.format("HH:mm", positionData.getBeginTime());
            holder.mTvCompetitionStatus.setText(time);
            holder.mTvCompetitionStatus.setTextColor(Color.rgb(246,177,57));
        }else {
            holder.mTvCompetitionStatus.setText(positionData.getCompetitionStatusData());
            holder.mTvCompetitionStatus.setTextColor(Color.rgb(191, 191, 191));
        }

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
        Date date = mCompetitionData.get(position).getBeginTime();

        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EE", date);
        String month = (String) android.text.format.DateFormat.format("M", date);
        String day = (String) android.text.format.DateFormat.format("d", date);

        String whole = (String)android.text.format.DateFormat.format("M月d日 EE", date);
        holder.mHeaderText.setText(whole);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        Date date = mCompetitionData.get(position).getBeginTime();

        return date.getMonth() * 31 + date.getDay();


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
