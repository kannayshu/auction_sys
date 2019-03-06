package com.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

/**
 * Created By liuda on 2018/7/19
 */
public class AlipayClientHolder {


    private static class RealHolder{
        private static AlipayClient client = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do","2016091800541098",
                "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDJNejXz/FWUFgljtUW5VnpOrGbVRDFwgTKrXHCMVhiQtWtccQx5vXBnZYqtK/ycK78/jBbkD1DxEL/vCQrT4ChMoglCxSTZrH06Rx8ACQAN7RN9H0GQpw6//d7eFvSZ6kvmfTX+d2PqiICRVidxC2SR2FNz8W4x/lww/UN9SgzEYiZEcE7wCkQ3UbQ7OXrdKMKCm5/t3xxuMvPZpP/hlrDVR5E9292ADCsdSvUSGtuKIseMKp2fQA58ns2VyVE1FqwRy7wo1iNkHe5sPZDEASw/tCzM9eiJq+kTCqfC88CLuOhyDRh7tTdyUKiKwngGit+kxBl3jMmMXvCjuMKAJF1AgMBAAECggEBAJ/pFV13KPGVNwjaZGYm7vcaK48s4wCYOuCtScyxFPI69cE7ulaCnOw8htz37yG6BoiQp/QNRXN3JlxCtWgqbisukt8F2FtxcZsYLQgrwHDf1I9j8H0qMSn+xjw9opQsyFxJejqvnTORXif53vy7rhAkZKPHrfc2Dvtc8YQfQ8yZCPRSlCq6Hxn4dT7kffIxG83dH2KSzUJIH2OWe1Zq5s0cYtpnDxJrJTQq7o5AlvHGH5M2+RGfxue1fksWX2cn+NpoDgpj7gKKJNRfikRTkXq1+7V6nr+YeMT6d++vPPIOPIP9WY12emqFup3yoyRUeapHLABfeNE3yprzz+OgRwECgYEA5ciuhKQleMDPRd3iBu+dVfUl3NbqKFRMpo4+c8b2+XiTwJZKEuXgqORWGEjwNz6QU+lipaVwcnzL5VGNpjqNcJnfaycpPMIf1PvZwrJkwibY2v8gpSV8hyxFpKIPmD/ILb+4KEqxOXJLMx9UtDbXeLmCmU0dhBjIL66V0vmYcLUCgYEA4CqvJuuXUV5OOj4IVe3pWWJUZ5aOd0vgpz+96YMYdGCHLo2TJvjQAH+g9m5+FoleFW7v69/uu0O3vI63bRe8yrVBSuT6D1ZxCXQeOwCSEP1eje9MtP66+b6Gk06nF5OwEI8/8lJ3ff5X5kaEwluFbMpl318ed3/KjA2wfjA51cECgYEAvvXIEvNXMMMLfkSnUpxPZOAWVzFD58Am80PDIvR2y1wpbiN367yet70Kz6PN/htAVnveP0r96CdOS4U68qo2YR1/Ts18t8GcMqQalHMsySz/iv/7YUOF+dAREICd9SkMcvihUtq/7b3OQO4Itrvc1bdVkZFEtkLiGr48RNiolwECgYEAisWB1qtD6qhh7yZVzLRSn/YCYHpQycNcWM1aCQQSEIFtTkFwllCzm4tnHwd54QO7DbO2rSPcy2r5hK4pGUz3n9mjiPTFZNdTdNHAoTASPJhNf7pjlyyWnWL4NpsbT7oq5aV4pc+tj37z9Wc0aolzyMxnUme1Ga8vuZID76p1M0ECgYEAzfXI7v7w5+TT92+JXdF5Q4n/uzx+xopPSkqN5i+fZKPlUA07sOSzu7/mwwekPqpzP+Ng7ZafbueKiRs+fFzKrWSS7XU8yz/5u9a05TCp7uJHZauy1WWN3XMxEIQ1iZPivx1WaPnmvHeHduRUbjAibzQ/EGar6Npa1Ud01lJXmFY=",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5JLsK1ElNRrAj3lZxFkOXieDHKkcaSLQuj9yTwdP0Pe77hKTWTwk38Kn1hCB08+M/bFrJ1OW8HrBZ2OEk5I2bKE6VtIO5P0fCrjmumBm90iXT+HQTvwsRB3QDpLoNhGu6e+nZQqy2aLUaK6ko7lxXoTJ4bGbw3/Q2yj0sRsvSRjsER4ZnPB04frP5HJ9rKYX+DhGFRfF7pecFwTbYhhBS4nc0gsEbF6p/MIbG2DfWMQu7ZwnliMPRTNgvyBJQr6n3pIWSYMq2UsQQj0MNQe1PuJrxJQO91OWL4R6QngNKluBDVehnKiDPMXWU4C00FO9FUztfbIIdJtnNtKLUirnIwIDAQAB",                "RSA2");

    }
    public static AlipayClient getInstance(){
        return RealHolder.client;
    }
}
