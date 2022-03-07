package com.saf.mpesa.interfaces;

import com.saf.mpesa.utils.Pair;

/**
 * Created by miles on 19/11/2017.
 */

public interface MpesaListener {
    public void onMpesaError(Pair<Integer, String> result);
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage);
}
