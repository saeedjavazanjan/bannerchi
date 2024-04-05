package com.burhanrashid52.photoediting;

import java.util.List;

import ir.cafebazaar.poolakey.entity.PurchaseInfo;

public interface DataFilledCallback {
    void onDataFilled(List<PurchaseInfo> dataList);

}
