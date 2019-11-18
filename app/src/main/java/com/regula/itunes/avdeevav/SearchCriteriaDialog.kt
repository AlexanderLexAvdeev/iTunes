package com.regula.itunes.avdeevav

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class SearchCriteriaDialog : DialogFragment(), DialogInterface.OnClickListener {

    companion object {
        private const val TAG = "SearchCriteriaDialog"

        @JvmStatic
        fun getInstance(fragmentManager: FragmentManager): SearchCriteriaDialog {

            var searchCriteriaDialog: SearchCriteriaDialog? = fragmentManager.findFragmentByTag(TAG) as SearchCriteriaDialog?

            if (searchCriteriaDialog == null) {
                searchCriteriaDialog = SearchCriteriaDialog()
            }

            return searchCriteriaDialog
        }

        fun getTag(): String {

            return TAG
        }
    }

    private lateinit var iSearchCriteriaDialog: ISearchCriteriaDialog
    private var checkedItem: Int = 0


    override fun onAttach(context: Context) {

        super.onAttach(context)

        iSearchCriteriaDialog = context as ISearchCriteriaDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, 0)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder = AlertDialog.Builder(context!!)

        alertDialogBuilder.setTitle(R.string.dialogTitle)
        alertDialogBuilder.setSingleChoiceItems(
                Array(SearchCriteria.values().size) {
                    SearchCriteria.values()[it].criteria
                },
                checkedItem,
                this@SearchCriteriaDialog
        )

        return alertDialogBuilder.create()
    }


    override fun onClick(dialog: DialogInterface?, which: Int) {

        iSearchCriteriaDialog.onSearchCriteriaSelected(SearchCriteria.values()[which])
        dismiss()
    }
}
