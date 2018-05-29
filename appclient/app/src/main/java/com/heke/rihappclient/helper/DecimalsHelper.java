package com.heke.rihappclient.helper;

import java.text.DecimalFormat;

/**
 * Created by wgmhx on 2017/4/27.
 */

public class DecimalsHelper
{
    public static Boolean NumBerStringIsFormat(String paramString)
    {
        if (!paramString.isEmpty())
        {
            int i = -1 + paramString.length();
            int j = paramString.toString().indexOf(".");
            if ((i - j > 8) && (j != -1))
                return Boolean.valueOf(false);
            if ((j == -1) && (paramString.toString().length() > 10))
            {
                char[] arrayOfChar = paramString.toString().toCharArray();
                if ((arrayOfChar.length > 10) && (!String.valueOf(arrayOfChar[10]).equals(".")))
                    return Boolean.valueOf(false);
            }
        }
        return Boolean.valueOf(true);
    }

    public static String Transfloat(double paramDouble, int paramInt)
    {
        if (Math.round(paramDouble) - paramDouble == 0.0D)
            return String.valueOf(paramDouble);
        DecimalFormat localDecimalFormat;
        if (paramInt >= 8)
        {
            localDecimalFormat = new DecimalFormat("0000000000.00000000");
            return String.valueOf(Double.parseDouble(localDecimalFormat.format(paramDouble)));
        }
        StringBuilder localStringBuilder = new StringBuilder("0000000000.");
        for (int i = 1; ; i++)
        {
            if (i > paramInt)
            {
                localDecimalFormat = new DecimalFormat(localStringBuilder.toString());
                break;
            }
            localStringBuilder.append("0");
        }
        return localStringBuilder.toString();
    }
}

