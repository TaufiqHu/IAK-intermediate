package id.educo.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.educo.popularmovies.R;
import id.educo.popularmovies.model.Trailer;
import id.educo.popularmovies.utils.NetworkUtils;
import id.educo.popularmovies.utils.RecyclerViewItemClickListener;

/**
 * Created by Tito on 03/12/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context context;
    private List<Trailer> trailerList;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public TrailerAdapter(Context context, List<Trailer> trailerList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = context;
        this.trailerList = trailerList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.trailer = trailerList.get(position);
        Glide.with(context)
                .load(NetworkUtils.getYoutubeThumbnail(holder.trailer.getKey()))
                .into(holder.trailerThumbnail);
        Log.i("Thumbnail: ", NetworkUtils.getYoutubeThumbnail(holder.trailer.getKey()).toString());

        holder.itemView.setOnClickListener(view -> {
            int itemPosition = holder.getAdapterPosition();
            recyclerViewItemClickListener.onItemClicked(itemPosition);
        });

    }

    @Override
    public int getItemCount() { return trailerList.size(); }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_thumbnail)
        ImageView trailerThumbnail;

        Trailer trailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
