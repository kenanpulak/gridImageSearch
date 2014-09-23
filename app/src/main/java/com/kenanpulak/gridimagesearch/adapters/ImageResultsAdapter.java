package com.kenanpulak.gridimagesearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.kenanpulak.gridimagesearch.R;
import com.kenanpulak.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kenanpulak on 9/21/14.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult>{

    public ImageResultsAdapter(Context context, List<ImageResult> images){
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ImageResult imageInfo = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        //TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //clear out image from last time
        ivImage.setImageResource(0);
        //populate title and remote download the image url
        //tvTitle.setText(Html.fromHtml(imageInfo.title));
        // Remotely download the image data in the background (with Picasso)
        Picasso.with(getContext()).load(imageInfo.thumbURL).into(ivImage);
        // Return the complete view to be displayed
        return convertView;
    }
}
