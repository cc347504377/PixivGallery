package com.luoye.whr.gallery.view.base

import android.support.v4.app.Fragment

interface ControlFragment {
    fun changeStyle()
}

abstract class BaseControlFragment() : Fragment(), ControlFragment