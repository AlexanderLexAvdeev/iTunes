package com.regula.itunes.avdeevav

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class SearchOptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    companion object {
        private const val TAG = "SearchOptionsDialog"

        @JvmStatic
        fun getInstance(fragmentManager: FragmentManager): SearchOptionsDialog {

            var searchOptionsDialog: SearchOptionsDialog? = fragmentManager.findFragmentByTag(TAG) as SearchOptionsDialog?

            if (searchOptionsDialog == null) {
                searchOptionsDialog = SearchOptionsDialog()
            }

            return searchOptionsDialog
        }

        fun getTag(): String {

            return TAG
        }
    }

    private lateinit var iSearchOptionsDialog: ISearchOptionsDialog


    override fun onAttach(context: Context) {

        super.onAttach(context)

        iSearchOptionsDialog = context as ISearchOptionsDialog
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
                Array(SearchMediaTypes.values().size) {
                    SearchMediaTypes.values()[it].mediaType
                },
                LastSearchRequest.getMediaTypeIndex(context!!),
                this@SearchOptionsDialog
        )

        return alertDialogBuilder.create()
    }


    override fun onClick(dialog: DialogInterface?, mediaTypeIndex: Int) {

        iSearchOptionsDialog.onSearchMediaTypeSelected(mediaTypeIndex)
        dismiss()
    }
}
