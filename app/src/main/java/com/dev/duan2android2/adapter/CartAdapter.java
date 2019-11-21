package com.dev.duan2android2.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.duan2android2.R;
import com.dev.duan2android2.fragment.Fragment_Cart;
import com.dev.duan2android2.user.User;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private Fragment_Cart context;
    private ArrayList<User.Product> products;
    private ArrayList<User.cartsp> cartsps;
    private ArrayList<Integer> integers=new ArrayList<>();

    public CartAdapter(Fragment_Cart context, ArrayList<User.Product> products, ArrayList<User.cartsp> cartsps) {
        this.context = context;
        this.products = products;
        this.cartsps = cartsps;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cart,parent,false);
       return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {
         final User.Product product=products.get(position);
         holder.txtColor.setText(product.getColorproduct());
         holder.txtgia.setText(Double.parseDouble(product.getPriceproduct())* Double.parseDouble(cartsps.get(position).getSoluong())+"đ");
         holder.txtnameproduct.setText(product.getNameproduct());
         holder.txtnameshop.setText(product.getNameshop());
         holder.txtsoluong.setText(cartsps.get(position).getSoluong());
         Picasso.get().load(product.getUri()).into(holder.imgnameproduct);
         holder.themsl.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int i= Integer.parseInt(cartsps.get(position).getSoluong())+1;
                 if (i<= Integer.parseInt(product.getSoluong())){
                     context.themcart(cartsps.get(position),position);
                     cartsps.set(position,new User.cartsp(cartsps.get(position).getIdsp(),(String.valueOf(i))));
                 }else {
                     Toast.makeText(context.getActivity(), "Không đủ số lượng", Toast.LENGTH_SHORT).show();
                 }


             }
         });
         integers.clear();

         holder.cbok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked){
                     double tongtien = 0;
                      integers.add(position);
                      for (Integer i:integers){
                         tongtien+= Double.parseDouble(products.get(i).getPriceproduct())* Double.parseDouble(cartsps.get(i).getSoluong());
                      }
                      context.tongtien(tongtien+"đ");

                     }else {
                     double tongtien = 0;
                     for (int i=0;i<integers.size();i++){
                         if (position==integers.get(i)){
                             integers.remove(i);
                         }
                     }
                     for (Integer i:integers){
                         tongtien+= Double.parseDouble(products.get(i).getPriceproduct())* Double.parseDouble(cartsps.get(i).getSoluong());
                     }
                     context.tongtien(tongtien+"đ");
                 }
             }
         });

        holder.trusl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i= Integer.parseInt(cartsps.get(position).getSoluong());
                if (i==1){
                    Toast.makeText(context.getActivity(), "Sô lượng ít nhất là 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (i>1){
                    context.trucart(cartsps.get(position),position);
                    int b= Integer.parseInt(cartsps.get(position).getSoluong())-1;
                    cartsps.set(position,new User.cartsp(cartsps.get(position).getIdsp(),(String.valueOf(b))));
                }

            }
        });

        holder.btndeletecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.deletesp(position,product.getNameproduct(),products.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        public CheckBox cbok;
        public TextView txtnameshop;
        public ImageView btndeletecart;
        public ImageView imgnameproduct;
        public TextView txtnameproduct;
        public TextView txtColor;
        public ImageView trusl;
        public TextView txtsoluong;
        public ImageView themsl;
        public TextView txtgia;



        public CartHolder(View itemView) {
            super(itemView);
            cbok = itemView.findViewById(R.id.cbok);
            txtnameshop =  itemView.findViewById(R.id.txtnameshop);
            btndeletecart =  itemView.findViewById(R.id.btndeletecart);
            imgnameproduct =  itemView.findViewById(R.id.imgnameproduct);
            txtnameproduct =  itemView.findViewById(R.id.txtnameproduct);
            txtColor =  itemView.findViewById(R.id.txtColor);
            trusl =  itemView.findViewById(R.id.trusl);
            txtsoluong = itemView.findViewById(R.id.txtsoluong);
            themsl =  itemView.findViewById(R.id.themsl);
            txtgia = itemView.findViewById(R.id.txtgia);
        }
    }


}
