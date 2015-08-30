package example.tiny.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import example.tiny.backetball.R;
import example.tiny.data.Comment;
import example.tiny.data.CommentGroup;
import example.tiny.utils.DateFormat;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by tiny on 15-8-27.
 */
public class CommentListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    ArrayList<CommentGroup> commentData = null;
    private LayoutInflater inflater;
    DisplayImageOptions options;

    public CommentListAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
        commentData = new ArrayList<>();


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

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.layout_comment_header, viewGroup, false);
            holder.text = (TextView) convertView.findViewById(R.id.tv_comment_headertext);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //具体操作
        if (commentData.get(position).isRecent())
            holder.text.setText("最新吐槽");
        else
            holder.text.setText("热门吐槽");

        return convertView;
    }

    //当此处返回值一致时,将会被放置在同一个StickyHeader下,并且其按从小到大的方式排序
    @Override
    public long getHeaderId(int position) {
        if (commentData.get(position).isRecent())
            return 1;
        else
            return 0;
    }

    public boolean add(CommentGroup data) {
        if (commentData == null)
            commentData = new ArrayList<>();
        //如果添加的数据没有objectID --> 说明该条数据是新添加
        Comment commment = data.getComment();
        Log.e("Comment", commment + "comment");
        if (data.getComment().getObjectId() == null || "".equals(data.getComment().getObjectId())) {
            return commentData.add(data);
        }

        for (int i = 0; i < commentData.size(); i++) {
            if (data.getComment().getObjectId().equals(commentData.get(i).getComment().getObjectId())
                    && !data.getComment().getObjectId().equals("")) {
                //如果出现：新加入的数据id与之前存在的数据id一致，则删除旧的数据
                commentData.remove(i);
            }
        }
        return commentData.add(data);
    }

    public ArrayList getDataList() {
        return commentData;
    }

    @Override
    public int getCount() {
        return commentData.size();
    }

    @Override
    public Object getItem(int position) {
        if (commentData.size() > position)
            return commentData.get(position);
        else
            return null;
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
            convertView = inflater.inflate(R.layout.layout_comment_item, parent, false);
            holder.mTvCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
            holder.mImgCommentAvator = (CircleImageView) convertView.findViewById(R.id.img_comment_avator);
            holder.mTvCommentName = (TextView) convertView.findViewById(R.id.tv_comment_name);
            holder.mTvCommentLikeNumber = (TextView) convertView.findViewById(R.id.tv_comment_upvote_number);
            holder.mTvCommentTime = (TextView) convertView.findViewById(R.id.tv_comment_time);
            holder.mChkCommentLike = (CheckBox) convertView.findViewById(R.id.chk_comment_upvote);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentGroup curViewData = commentData.get(position);

        holder.mTvCommentContent.setText(Html.fromHtml(LoadTextFromData(curViewData)), TextView.BufferType.SPANNABLE);


        holder.mTvCommentTime.setText(DateFormat.TimeElapse(curViewData.getComment().getCreatedAt()));
        String nickName = curViewData.getUser().getNickname();

        holder.mTvCommentName.setText(nickName == null ? "Null" : nickName);
        holder.mChkCommentLike.setChecked(curViewData.isLikes());

        holder.mChkCommentLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int number = Integer.parseInt((String) holder.mTvCommentLikeNumber.getText());
                if (isChecked)
                    ++number;
                else
                    --number;
                holder.mTvCommentLikeNumber.setText(number + "");
            }
        });
        holder.mTvCommentLikeNumber.setText(curViewData.getComment().getLikes() + "");

        ImageLoader.getInstance().displayImage(curViewData.getUser().getAvatorUrl(), holder.mImgCommentAvator, options);

        return convertView;
    }

    @Override
    public boolean isEmpty() {
        return (commentData.size() == 0);
    }

    public String LoadTextFromData(CommentGroup data) {
        if (data.getAtUser() == null)
            return data.getComment().getContent();
        else {
            String text = "回复 " + "<font color='#404040'>@" + data.getAtUser().getNickname() + "</font>";
            text += (" : " + data.getComment().getContent());
            return text;
        }
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView mTvCommentContent;
        CircleImageView mImgCommentAvator;
        TextView mTvCommentName;
        TextView mTvCommentTime;
        CheckBox mChkCommentLike;
        TextView mTvCommentLikeNumber;

    }

}
