package romanmoran.com.mappingyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.data.User;


/**
 * Created by roman on 08.08.2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<User>users;

    public interface OnItemClickListener{
        void onViewHolderClick(View itemView,int position);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }


    public ContactsAdapter(List<User> users){
        this.users = users;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contactImg)
        RoundedImageView contactImg;
        @BindView(R.id.tvContactName)
        TextView tvContactName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener!=null){
                    onItemClickListener.onViewHolderClick(itemView,getAdapterPosition());
                }
            });
        }

        public void bind(User user){
            /*Glide.with(itemView.getContext())
                    .load(user.getUri())
                    .asBitmap()
                    .placeholder(R.drawable.stub_image_user)
                    .error(R.drawable.stub_image_user)
                    .into(contactImg);*/
            Picasso.with(itemView.getContext())
                    .load(user.getUri())
                    .placeholder(R.drawable.stub_image_user)
                    .error(R.drawable.stub_image_user)
                    .into(contactImg);
            tvContactName.setText(user.getName());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contacts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(users.get(position));
    }


    @Override
    public int getItemCount() {
        return users!=null?users.size():0;
    }

}
