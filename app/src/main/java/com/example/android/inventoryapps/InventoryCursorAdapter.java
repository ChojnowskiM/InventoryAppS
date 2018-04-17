package com.example.android.inventoryapps;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapps.data.InventoryContract;
import com.example.android.inventoryapps.data.InventoryContract.*;


public class InventoryCursorAdapter extends CursorAdapter {


    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        Button saleButton = view.findViewById(R.id.sale);


        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);


        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);

        final int prodQuant = Integer.parseInt(productQuantity);

        if (TextUtils.isEmpty(productQuantity)) {
            productQuantity = context.getString(R.string.default_quantity);
        }


        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
        String currentId = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        final Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Long.parseLong(currentId));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prodQuant > 0) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, (prodQuant - 1));

                    int newUpdate = context.getContentResolver().update(currentUri, values, null, null);
                    if (newUpdate == 0)
                        Toast.makeText(context, R.string.error_update, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.quantity_below_zero, Toast.LENGTH_SHORT).show();
            }
        });

    }
}