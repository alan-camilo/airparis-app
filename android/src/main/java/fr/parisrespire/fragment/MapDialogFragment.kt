/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any 
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package fr.parisrespire.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import fr.parisrespire.R
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.dialog_image.view.*

class MapDialogFragment : DialogFragment() {

    private var url: String? = null
    private var imageView: ImageView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        url = arguments?.getString("url")
        val message = arguments?.getString("message")
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setNegativeButton(
                    R.string.close
                ) { _, _ -> }
            val view = it.layoutInflater.inflate(R.layout.dialog_image, layout)
            imageView = view.map_iv
            progressBar = view.progress
            builder.setView(view)
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MapDialogFragment", "url=$url imageView=$imageView")
        Picasso.get().load(url)
            .error(R.drawable.baseline_highlight_off_24).into(this.imageView, object : Callback {
                override fun onSuccess() {
                    progressBar?.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    progressBar?.visibility = View.GONE
                }
            })
    }
}