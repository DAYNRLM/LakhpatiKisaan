package com.nrlm.lakhpatikisaan.utils

class AppConstant {
    companion object ConstantObject {
        const val wantToShow: Boolean = true
        const val dummyImei: String = "dummy_123456789"
        const val noInternetCode: String = "12163"
        const val unsyncStatus: String = "0"
        const val syncStatus: String = "1"
        const val beforeNrlmStatus: String = "B"
        const val entryCompleted: String = "1"
        const val entryNotCompleted: String = "0"

        const val afterNrlmStatus: String = "A"
        const val geoSelection: String = "gp_village"
        const val vo_clfSelection: String = "vo_clf"
        val items = arrayOf("Income (Before coming into NRLM Fold)", "Income at present")
        val confirmation = arrayOf("Do you confirm all this entry", "Do You want still add more activity")
        val status = arrayOf("1","2");

    }
}

//using minimum buil version N because for data base operation using Stream API......