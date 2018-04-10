package com.atc.vuyaninxele.sight3;

import android.view.View;

import java.util.List;

/**
 * Created by Vuyani.Nxele on 2/23/2018.
 */

public interface ClickListener {
    void itemClicked(final List<Sites> sitesList, View view, int position);
}
