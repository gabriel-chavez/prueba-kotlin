package com.beardedhen.androidbootstrap.api.defaults;

/**
 * Bootstrap provides 5 sizes - XS, SM, MD, LG, and XL. In the Android implementation the scale
 * factors used are 0.70, 0.85, 1.00, 1.30, and 1.60 respectively.
 */
public enum DefaultBootstrapSize {

    XSS(),
    XS(),
    SM(),
    MD(),
    LG(),
    XL(),
    XXL();

    public static DefaultBootstrapSize fromAttributeValue(int attrValue) {
        switch (attrValue) {
            case 0:
                return XS;
            case 1:
                return SM;
            case 2:
                return MD;
            case 3:
                return LG;
            case 4:
                return XL;
            case 5:
                return XXL;
            case 6:
                return XSS;
            default:
                return MD;
        }
    }

    public float scaleFactor() {
        switch (this) {
            case XSS:
                return 0.40f;
            case XS:
                return 0.70f;
            case SM:
                return 0.85f;
            case MD:
                return 1.00f;
            case LG:
                return 1.30f;
            case XL:
                return 1.60f;
            case XXL:
                return 1.90f;
            default:
                return 1.00f;
        }
    }

}
