package com.cc5c.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class ExcelListener<T> extends AnalysisEventListener {
    private List<T> data = new ArrayList<>();

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        T t = (T) o;
        data.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


}
