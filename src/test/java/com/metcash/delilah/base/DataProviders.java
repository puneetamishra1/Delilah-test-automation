package com.metcash.delilah.base;

import com.metcash.delilah.utils.Utils;
import com.metcash.delilah.utils.Xls_Reader;

public class DataProviders {

    Xls_Reader datatable = new Xls_Reader(Utils.getPropertyValue("ALM_TestAutomation"));

}
