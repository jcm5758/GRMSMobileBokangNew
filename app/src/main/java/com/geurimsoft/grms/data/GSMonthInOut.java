package com.geurimsoft.grms.data;

import java.util.ArrayList;

public class GSMonthInOut
{

    // list 데이터의 수
    public int recordCount;

    // 헤더의 수
    public int headerCount;

    // 헤더
    public String[] header;

    // 리스트
    public ArrayList<GSMonthInOutDetail> list = new ArrayList<GSMonthInOutDetail>();

    public GSMonthInOut(){}

    public void add(GSMonthInOutDetail detail)
    {
        this.list.add(detail);
    }

}
