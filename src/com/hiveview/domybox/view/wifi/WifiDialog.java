/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hiveview.domybox.view.wifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

class WifiDialog extends AlertDialog implements View.OnClickListener,
        TextWatcher, AdapterView.OnItemSelectedListener {
    private static final String KEYSTORE_SPACE = "keystore://";

    static final int BUTTON_SUBMIT = DialogInterface.BUTTON_POSITIVE;
    static final int BUTTON_FORGET = DialogInterface.BUTTON_NEUTRAL;

    final boolean edit;
    private final DialogInterface.OnClickListener mListener;
    private AccessPoint mAccessPoint;

    private TextView mSsid;
    private int mSecurity;
//    private TextView mPassword;


//    static boolean requireKeyStore(WifiConfiguration config) {
//        String values[] = {config.ca_cert.value(), config.client_cert.value(),
//                config.private_key.value()};
//        for (String value : values) {
//            if (value != null && value.startsWith(KEYSTORE_SPACE)) {
//                return true;
//            }
//        }
//        return false;
//    }

    WifiDialog(Context context, DialogInterface.OnClickListener listener,
            AccessPoint accessPoint, boolean edit) {
        super(context);
        this.edit = edit;
        mListener = listener;
        mAccessPoint = accessPoint;
        mSecurity = (accessPoint == null) ? AccessPoint.SECURITY_NONE : accessPoint.security;
    }
    
    public void setAccessPoint(AccessPoint accessPoint){
    	mAccessPoint = accessPoint;
    }

    WifiConfiguration getConfig() {
//        if (mAccessPoint != null && mAccessPoint.networkId != -1 && !edit) {
//            return null;
//        }

        WifiConfiguration config = new WifiConfiguration();

        if (mAccessPoint == null) {
            config.SSID = AccessPoint.convertToQuotedString(
                    mSsid.getText().toString());
            // If the user adds a network manually, assume that it is hidden.
            config.hiddenSSID = true;
        } else if (mAccessPoint.networkId == -1) {
            config.SSID = AccessPoint.convertToQuotedString(mAccessPoint.ssid);
        } else {
            config.networkId = mAccessPoint.networkId;
        }

        switch (mSecurity) {
            case AccessPoint.SECURITY_NONE:
                config.allowedKeyManagement.set(KeyMgmt.NONE);
                return config;

            case AccessPoint.SECURITY_WEP:
                config.allowedKeyManagement.set(KeyMgmt.NONE);
                config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
                config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
                if (mAccessPoint.mPassWord.length() != 0) {
                    int length = mAccessPoint.mPassWord.length();
                    String password = mAccessPoint.mPassWord.toString();
                    // WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
                    if ((length == 10 || length == 26 || length == 58) &&
                            password.matches("[0-9A-Fa-f]*")) {
                        config.wepKeys[0] = password;
                    } else {
                        config.wepKeys[0] = '"' + password + '"';
                    }
                }
                return config;

            case AccessPoint.SECURITY_PSK:
                config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
                if (mAccessPoint.mPassWord.length() != 0) {
                    String password = mAccessPoint.mPassWord.toString();
                    if (password.matches("[0-9A-Fa-f]{64}")) {
                        config.preSharedKey = password;
                    } else {
                        config.preSharedKey = '"' + password + '"';
                    }
                }
                return config;

            case AccessPoint.SECURITY_EAP:
                config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
                config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
//                config.eap.setValue((String) mEapMethod.getSelectedItem());
//
//                config.phase2.setValue((mPhase2.getSelectedItemPosition() == 0) ? "" :
//                        "auth=" + mPhase2.getSelectedItem());
//                config.ca_cert.setValue((mEapCaCert.getSelectedItemPosition() == 0) ? "" :
//                        KEYSTORE_SPACE + Credentials.CA_CERTIFICATE +
//                        (String) mEapCaCert.getSelectedItem());
//                config.client_cert.setValue((mEapUserCert.getSelectedItemPosition() == 0) ? "" :
//                        KEYSTORE_SPACE + Credentials.USER_CERTIFICATE +
//                        (String) mEapUserCert.getSelectedItem());
//                config.private_key.setValue((mEapUserCert.getSelectedItemPosition() == 0) ? "" :
//                        KEYSTORE_SPACE + Credentials.USER_PRIVATE_KEY +
//                        (String) mEapUserCert.getSelectedItem());
//                config.identity.setValue((mEapIdentity.length() == 0) ? "" :
//                        mEapIdentity.getText().toString());
//                config.anonymous_identity.setValue((mEapAnonymous.length() == 0) ? "" :
//                        mEapAnonymous.getText().toString());
//                if (mPassword.length() != 0) {
//                    config.password.setValue(mPassword.getText().toString());
//                }
                return config;
        }
        return null;
    }


    private void validate() {
        // TODO: make sure this is complete.
        if ((mSsid != null && mSsid.length() == 0) ||
                ((mAccessPoint == null || mAccessPoint.networkId == -1) &&
                ((mSecurity == AccessPoint.SECURITY_WEP && mAccessPoint.mPassWord.length() == 0) ||
                (mSecurity == AccessPoint.SECURITY_PSK && mAccessPoint.mPassWord.length() < 8)))) {
            getButton(BUTTON_SUBMIT).setEnabled(false);
        } else {
            getButton(BUTTON_SUBMIT).setEnabled(true);
        }
    }

    public void onClick(View view) {
//    	mAccessPoint.mPassWord.setInputType(
//                InputType.TYPE_CLASS_TEXT | (((CheckBox) view).isChecked() ?
//                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
//                InputType.TYPE_TEXT_VARIATION_PASSWORD));
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    
    public void afterTextChanged(Editable editable) {
        validate();
    }

    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        mSecurity = position;
        validate();
    }

    public void onNothingSelected(AdapterView parent) {
    }


    private void setCertificate(Spinner spinner, String prefix, String cert) {
        prefix = KEYSTORE_SPACE + prefix;
        if (cert != null && cert.startsWith(prefix)) {
            setSelection(spinner, cert.substring(prefix.length()));
        }
    }

    private void setSelection(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            for (int i = adapter.getCount() - 1; i >= 0; --i) {
                if (value.equals(adapter.getItem(i))) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }
}
