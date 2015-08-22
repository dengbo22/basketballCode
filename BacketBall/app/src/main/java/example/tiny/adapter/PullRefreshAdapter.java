package example.tiny.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import example.tiny.backetball.CompetitionItemData;
import example.tiny.backetball.R;
import example.tiny.widget.ForegroundImageView;

/**
 * Created by tiny on 15-8-22.
 */
public class PullRefreshAdapter extends BaseAdapter {

    private static final String LOG_TAG = "PullRefreshAdapter";
    private Context mContext ;
    private DisplayImageOptions options ;

    private ArrayList<CompetitionItemData> mGameData;

    private LayoutInflater inflater;
    public PullRefreshAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mGameData = new ArrayList<CompetitionItemData>();
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.troop_a)
                .showImageOnFail(R.drawable.troop_a)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    public ArrayList<CompetitionItemData> getGameData() {
        return mGameData;
    }




    @Override
    public int getCount() {
        return mGameData.size();
    }

    @Override
    public Object getItem(int position) {
        return mGameData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_secondpage_listitem, parent, false);

            holder.mImgGameCover = (ForegroundImageView)convertView.findViewById(R.id.img_sndlist_cover);
            holder.mImgGameType = (ImageView)convertView.findViewById(R.id.img_sndlist_type);
            holder.mTvGameName = (TextView)convertView.findViewById(R.id.tv_sndlist_gamename);
            LinearLayout linear = (LinearLayout)convertView.findViewById(R.id.layout_sndlist_locationwithfollow);
            holder.mTvGameLocation = (TextView)linear.findViewById(R.id.tv_sndlist_location_text);
            holder.mImgGameLocationIcon = (ImageView)linear.findViewById(R.id.img_sndlist_location_icon);
            holder.mTvGameFollow = (TextView)linear.findViewById(R.id.tv_sndlist_follow_text);
            holder.mImgGameFollowIcon = (ImageView)linear.findViewById(R.id.img_sndlist_location_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Holder操作
        CompetitionItemData currentData = mGameData.get(position);
        holder.mTvGameLocation.setText(currentData.getCampus());
        holder.mTvGameFollow.setText(currentData.getFollowNumber() +"");
        holder.mTvGameName.setText(currentData.getCollege() + "-" + currentData.getName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(currentData.getCoverUrl(), holder.mImgGameCover, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                holder.mImgGameCover.setForegroundResource(R.drawable.nba_mask);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        if( currentData.getType().equals("总决赛") ){
            holder.mImgGameType.setImageResource(R.drawable.sndlist_stage_final_icon);
        } else if( currentData.getType().equals("半决赛")) {
            holder.mImgGameType.setImageResource(R.drawable.sndlist_stage_half_icon);
        } else if ( currentData.getType().equals("小组赛")) {
            holder.mImgGameType.setImageResource(R.drawable.sndlist_stage_group_icon);
        } else {
            holder.mImgGameType.setImageResource(R.drawable.sndlist_stage_konckout_icon);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTvGameLocation;
        ImageView mImgGameLocationIcon;
        TextView mTvGameFollow;
        ImageView mImgGameFollowIcon;
        TextView mTvGameName;
        ImageView mImgGameType;
        ForegroundImageView mImgGameCover;
    }


}
