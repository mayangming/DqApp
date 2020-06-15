package com.da.library.controls.custombuttom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.da.library.R;
import com.da.library.controls.custombuttom.bean.CustomButtom;

import java.util.List;

/**
 * Created by Kind on 2019/3/22.
 */
public class CustomButtomDialogAdapter extends BaseAdapter {

    private Context context;
    private int gravity;
    private List<CustomButtom> datas;

    public CustomButtomDialogAdapter(Context context, List<CustomButtom> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setGravity(int gravity){
        this.gravity = gravity;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public CustomButtom getItem(int position) {
        if (datas == null) {
            return null;
        }

        if (position < 0 || position >= datas.size()) {
            return null;
        }

        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_buttom_dialog_item, null);
            vh.name = (TextView) convertView.findViewById(R.id.custom_buttom_name);
            if(gravity != -1){
                vh.name.setGravity(gravity);
            }
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        CustomButtom buttom = getItem(position);
        vh.name.setText(buttom.text);


        vh.name.setTextColor(context.getResources().getColor(R.color.text_blue_pressed));
        if (buttom.text_color != 0) {
            vh.name.setTextColor(buttom.text_color);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
    }
}
