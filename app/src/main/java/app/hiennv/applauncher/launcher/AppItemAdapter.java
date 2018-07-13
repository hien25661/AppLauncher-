package app.hiennv.applauncher.launcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.hiennv.applauncher.R;

public class AppItemAdapter extends RecyclerView.Adapter<AppItemAdapter.ViewHolder>{
    private ArrayList<AppItem> mAppItems;
    private Context mContext;
    private OpenAppItemListener mListener;

    public AppItemAdapter(ArrayList<AppItem> mAppItems, OpenAppItemListener listener) {
        this.mAppItems = mAppItems;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AppItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        ViewHolder vh = new ViewHolder(v);
        mContext = v.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AppItemAdapter.ViewHolder holder, int position) {
        if(position >= 0 && position < mAppItems.size()){
            final AppItem appItem = mAppItems.get(position);
            if(appItem!=null){
                //todo
                holder.imvIcon.setImageDrawable(appItem.getIcon());
                holder.tvName.setText(appItem.getLabel());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener!=null){
                            mListener.openAppItem(appItem);
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.mAppItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imvIcon;
        TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);
            imvIcon = (ImageView)itemView.findViewById(R.id.imv_appicon);
            tvName = (TextView)itemView.findViewById(R.id.tv_appname);
        }
    }

    public interface OpenAppItemListener{
        void openAppItem(AppItem appItem);
    }
}
