package romanmoran.com.mappingyou.cluster_renderer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.makeramen.roundedimageview.RoundedImageView;

import romanmoran.com.mappingyou.R;
import romanmoran.com.mappingyou.data.Event;
import romanmoran.com.mappingyou.data.Item;
import romanmoran.com.mappingyou.data.User;
import romanmoran.com.mappingyou.enums.GENDER;
import romanmoran.com.mappingyou.utility.Utility;

import static romanmoran.com.mappingyou.enums.GENDER.FEMALE;
import static romanmoran.com.mappingyou.enums.GENDER.UNKNOWN;

/**
 * Created by roman on 03.08.2017.
 */

public class PersonRenderer extends DefaultClusterRenderer<Item> {
    private ImageView mImageViewFrame;
    private RoundedImageView mImageViewProfile;
    private View customMarkerView;
    private int  mSizeX;
    private int  mSizeH;
    private Activity activity;


    public PersonRenderer(Activity activity,GoogleMap mMap,ClusterManager<Item> mClusterManager) {
        super(activity, mMap, mClusterManager);
        this.activity = activity;
    }

    @Override
    protected void onBeforeClusterItemRendered(Item item, MarkerOptions markerOptions) {
        customMarkerView = activity.getLayoutInflater().inflate(item instanceof User ? R.layout.profile:R.layout.event, null);
        mImageViewFrame = (ImageView) customMarkerView.findViewById(R.id.background_marker);
        mImageViewProfile = (RoundedImageView) customMarkerView.findViewById(R.id.main_marker);
        mSizeX = (int)mImageViewProfile.getLayoutParams().width;
        mSizeH = (int)mImageViewProfile.getLayoutParams().height;
        if (item instanceof User){
            initUser((User) item);
        }else{
            initEvent((Event)item);
        }
        markerOptions.icon(Utility.getBitmap(customMarkerView)).title(item.getName());
    }

    @Override
    protected void onClusterItemRendered(final Item item, final Marker marker) {
        Glide.with(activity)
                .load(item.getPicUrl())
                .asBitmap().fitCenter().centerCrop()
                .into(new SimpleTarget<Bitmap>(mSizeX,mSizeH) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            mImageViewProfile.setImageBitmap(resource);
                            marker.setIcon(Utility.getBitmap(customMarkerView));
                            marker.setTitle(item.getName());
                            Glide.clear(this);
                        } catch (Exception exception) {
                        }

                    }
                });
    }


    private void initEvent(Event item) {
        mImageViewProfile.setImageResource(R.drawable.stub_location);
    }


    private void initUser(User item){
        GENDER gender = GENDER.fromValue(item.getGener());

        if (!gender.equals(UNKNOWN)) {
            mImageViewFrame.setImageResource(gender.equals(FEMALE)?
                    R.drawable.ic_pin_woman:R.drawable.ic_pin_man);
            mImageViewProfile.setImageResource(gender.equals(FEMALE)?
                    R.drawable.ic_woman_user_image_stub:R.drawable.ic_man_user_image_stub);
        } else {
            mImageViewFrame.setImageResource(R.drawable.ic_pin_man);
            mImageViewProfile.setImageResource(R.drawable.stub_image_user);
        }
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Item> cluster) {
        return cluster.getSize() > 1;
    }
}
