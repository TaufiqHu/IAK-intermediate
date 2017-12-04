package id.educo.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.educo.popularmovies.R;
import id.educo.popularmovies.model.Review;
import id.educo.popularmovies.model.Trailer;

/**
 * Created by Tito on 04/12/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.review = reviewList.get(position);
        Log.d("Data Author", holder.review.getAuthor());
        Log.d("Data Content", holder.review.getContent());
        holder.author.setText(holder.review.getAuthor());
        holder.content.setText(holder.review.getContent());
    }

    @Override
    public int getItemCount() { return reviewList.size(); }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author) TextView author;
        @BindView(R.id.tv_content) TextView content;
        Review review;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
